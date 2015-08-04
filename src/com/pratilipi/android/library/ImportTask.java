/*
 * Copyright (C) 2013 Alex Kuiper
 *
 * This file is part of PageTurner
 *
 * PageTurner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PageTurner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PageTurner.  If not, see <http://www.gnu.org/licenses/>.*
 */

package com.pratilipi.android.library;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import jedi.functional.Command;
import jedi.option.None;
import jedi.option.Option;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.service.MediatypeService;

import com.pratilipi.android.R;
import com.pratilipi.android.scheduling.QueueableAsyncTask;
import com.pratilipi.android.util.Configuration;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

public class ImportTask extends QueueableAsyncTask<File, Integer, Void>
		implements OnCancelListener {

	private Context context;
	private LibraryService libraryService;
	private ImportCallback callBack;
	private Configuration config;

	private boolean copyToLibrary;

	private List<String> errors = new ArrayList<>();

	private static final int UPDATE_FOLDER = 1;
	private static final int UPDATE_IMPORT = 2;

	private int foldersScanned = 0;
	private int booksImported = 0;

	private boolean emptyLibrary;
	private boolean silent;

	private String importFailed = null;

	public ImportTask(Context context, LibraryService libraryService,
			ImportCallback callBack, Configuration config,
			boolean copyToLibrary, boolean silent) {

		this.context = context;
		this.libraryService = libraryService;
		this.callBack = callBack;
		this.copyToLibrary = copyToLibrary;
		this.config = config;
		this.silent = silent;
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		requestCancellation();
	}

	public boolean isSilent() {
		return this.silent;
	}

	public void setCallBack(ImportCallback callBack) {
		this.callBack = callBack;
	}

	@Override
	public Option<Void> doInBackground(File... params) {

		doInBackground(params[0]);

		return new None();
	}

	private void doInBackground(File parent) {

		/*
		 * Hack: don't run automated import on an empty database, since we
		 * explicitly ask the user to import.
		 */
		if (silent && libraryService.findAllByTitle(null).getSize() == 0) {
			return;
		}

		if (!parent.exists()) {
			importFailed = String.format(
					context.getString(R.string.no_such_folder),
					parent.getPath());
			return;
		}

		this.emptyLibrary = this.libraryService.findAllByTitle(null).getSize() == 0;

		List<File> books = new ArrayList<>();
		findEpubsInFolder(parent, books);

		int total = books.size();
		int i = 0;

		while (i < books.size() && !isCancelled()) {

			File book = books.get(i);
			try {
				if (importBook(book)) {
					booksImported++;
				}
			} catch (OutOfMemoryError oom) {
				errors.add(book.getName() + ": Out of memory.");
				return;
			}

			i++;
			publishProgress(UPDATE_IMPORT, i, total);
		}

	}

	private void findEpubsInFolder(File folder, List<File> items) {

		if (folder == null || !folder.exists()) {
			return;
		}

		// If we got a single file, just import that.
		if (!folder.isDirectory()) {
			items.add(folder);
			return;
		}

		Queue<File> dirs = new LinkedList<>();
		dirs.add(folder);

		while (!isCancelled() && !dirs.isEmpty()) {

			File[] fileList = dirs.poll().listFiles();

			if (fileList != null) {
				for (File f : fileList) {
					if (f.isDirectory()) {
						foldersScanned++;
						publishProgress(UPDATE_FOLDER, foldersScanned);

						// Check if a recursive structure with symlinks
						if (!dirs.contains(f)) {
							dirs.add(f);
						}

					} else if (f.isFile()) {
						processFile(f, items);
					}
				}
			}
		}

	}

	private void processFile(final File file, final List<File> items) {

		final String fileName = file.getAbsolutePath();

		// Scan items
		if (fileName.endsWith(".epub")) {
			items.add(file);
		} else {

			// Command<File> add = f -> {
			// if ( f.getName().indexOf(".") == -1 ) {
			// //Older versions downloaded files without an extension
			// items.add(f);
			// }
			// };
			final Command<File> add = new Command<File>() {

				@Override
				public void execute(File f) {
					if (f.getName().indexOf(".") == -1) {
						// Older versions downloaded files without an extension
						items.add(f);
					}
				}
			};

			// config.getLibraryFolder().forEach( libraryFolder -> {
			// if ( fileName.startsWith(libraryFolder.getAbsolutePath() ) ) {
			// add.execute( file );
			// }
			// });
			config.getLibraryFolder().forEach(new Command<File>() {

				@Override
				public void execute(File libraryFolder) {
					if (fileName.startsWith(libraryFolder.getAbsolutePath())) {
						add.execute(file);
					}
				}
			});

			// config.getDownloadsFolder().forEach( downloadsFolder -> {
			// if ( fileName.startsWith( downloadsFolder.getAbsolutePath() )) {
			// add.execute( file );
			// }
			// });
			config.getDownloadsFolder().forEach(new Command<File>() {

				@Override
				public void execute(File downloadsFolder) {
					if (fileName.startsWith(downloadsFolder.getAbsolutePath())) {
						add.execute(file);
					}
				}
			});
		}

	}

	private boolean importBook(File file) {

		if (!libraryService.hasBook(file.getName())) {
			try {

				String fileName = file.getAbsolutePath();

				// read epub file
				EpubReader epubReader = new EpubReader();

				Book importedBook = epubReader.readEpubLazy(fileName, "UTF-8",
						Arrays.asList(MediatypeService.mediatypes));

				libraryService.storeBook(fileName, importedBook, false,
						this.copyToLibrary);

				return true;

			} catch (Exception io) {
				if (!isCancelled()) {
					errors.add(file + ": " + io.getMessage());
				} else {
				}
			}
		}

		return false;
	}

	@Override
	public void doOnProgressUpdate(Integer... values) {

		String message;

		if (values[0] == UPDATE_IMPORT) {
			message = String.format(context.getString(R.string.importing),
					values[1], values[2]);
		} else {
			message = String.format(context.getString(R.string.scan_folders),
					values[1]);
		}

		callBack.importStatusUpdate(message, silent);
	}

	@Override
	public void doOnCancelled(Option<Void> none) {
		this.callBack.importCancelled(booksImported, errors, emptyLibrary,
				silent);
	}

	@Override
	public void doOnPostExecute(Option<Void> none) {

		if (importFailed != null) {
			callBack.importFailed(importFailed, silent);
		} else {
			this.callBack.importComplete(booksImported, errors, emptyLibrary,
					silent);
		}
	}
}

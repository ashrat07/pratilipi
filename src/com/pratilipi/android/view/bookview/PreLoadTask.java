package com.pratilipi.android.view.bookview;

import com.pratilipi.android.epub.PageTurnerSpine;
import com.pratilipi.android.htmlspanner.HtmlSpanner;
import com.pratilipi.android.scheduling.QueueableAsyncTask;

import android.text.Spannable;
import jedi.functional.Command;
import jedi.option.None;
import jedi.option.Option;
import nl.siegmann.epublib.domain.Resource;

import static jedi.functional.FunctionalPrimitives.isEmpty;
import static jedi.option.Options.none;

/**
 * Created by alex on 10/14/14.
 */
public class PreLoadTask extends QueueableAsyncTask<Void, Void, Void> {

	private PageTurnerSpine spine;
	private TextLoader textLoader;

	public PreLoadTask(PageTurnerSpine spine, TextLoader textLoader) {
		this.spine = spine;
		this.textLoader = textLoader;
	}

	@Override
	public Option<Void> doInBackground(Void... voids) {
		doInBackground();

		return none();
	}

	private void doInBackground() {
		if (spine == null) {
			return;
		}

		Option<Resource> resource = spine.getNextResource();

		// resource.forEach( res -> {
		// Option<Spannable> cachedText = textLoader.getCachedTextForResource(
		// res );
		//
		// if ( isEmpty(cachedText) ) {
		// try {
		// textLoader.getText( res, PreLoadTask.this::isCancelled );
		// } catch ( Exception | OutOfMemoryError e ) {
		// //Ignore
		// }
		// }
		// });
		resource.forEach(new Command<Resource>() {

			public void execute(Resource res) {
				Option<Spannable> cachedText = textLoader
						.getCachedTextForResource(res);

				if (isEmpty(cachedText)) {
					try {
						textLoader.getText(res,
								new HtmlSpanner.CancellationCallback() {

									@Override
									public boolean isCancelled() {
										return false;
									}
								});
					} catch (Exception | OutOfMemoryError e) {
						// Ignore
					}
				}
			};
		});
	}
}

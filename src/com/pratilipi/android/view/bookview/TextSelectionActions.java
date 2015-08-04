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

package com.pratilipi.android.view.bookview;

import com.pratilipi.android.R;
import com.pratilipi.android.util.PlatformUtil;
import com.pratilipi.android.util.UiUtils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import jedi.functional.Command;
import jedi.option.Option;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TextSelectionActions implements ActionMode.Callback {

	private TextSelectionCallback callBack;
	private SelectedTextProvider selectedTextProvider;

	private Context context;

	public static interface SelectedTextProvider {
		Option<String> getSelectedText();

		int getSelectionStart();

		int getSelectionEnd();
	}

	public TextSelectionActions(Context context,
			TextSelectionCallback callBack,
			SelectedTextProvider selectedTextProvider) {
		this.callBack = callBack;
		this.context = context;
		this.selectedTextProvider = selectedTextProvider;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

		mode.finish();

		return true;
	}

	private static OnMenuItemClickListener react(final ActionMode mode,
			final UiUtils.Action action) {
		// return item -> {
		// action.perform();
		// mode.finish();
		// return true;
		// };
		return new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				action.perform();
				mode.finish();
				return true;
			}
		};
	}

	@Override
	public boolean onCreateActionMode(final ActionMode mode, Menu menu) {

		menu.removeItem(android.R.id.selectAll);

		MenuItem copyItem = menu.findItem(android.R.id.copy);

		if (copyItem != null) {
			// copyItem.setOnMenuItemClickListener(
			// react(mode, () ->
			// selectedTextProvider.getSelectedText().forEach(t ->
			// PlatformUtil.copyTextToClipboard(context, t))));
			copyItem.setOnMenuItemClickListener(react(mode,
					new UiUtils.Action() {

						@Override
						public void perform() {
							selectedTextProvider.getSelectedText().forEach(
									new Command<String>() {

										@Override
										public void execute(String t) {
											PlatformUtil.copyTextToClipboard(
													context, t);
										}
									});
						}
					}));
		}

		// menu.add( R.string.abs__shareactionprovider_share_with )
		// .setOnMenuItemClickListener(
		// react(mode, () -> selectedTextProvider.getSelectedText().forEach(t ->
		// callBack.share(
		// selectedTextProvider.getSelectionStart(),
		// selectedTextProvider.getSelectionEnd(),
		// t
		// )
		// )
		// )
		// ).setIcon(R.drawable.abs__ic_menu_share_holo_dark);
		menu.add(R.string.share_with)
				.setOnMenuItemClickListener(react(mode, new UiUtils.Action() {

					@Override
					public void perform() {
						selectedTextProvider.getSelectedText().forEach(
								new Command<String>() {

									@Override
									public void execute(String t) {
										callBack.share(selectedTextProvider
												.getSelectionStart(),
												selectedTextProvider
														.getSelectionEnd(), t);
									}
								});
					}
				})).setIcon(R.drawable.shelf);

		// menu.add(R.string.highlight)
		// .setOnMenuItemClickListener(
		// react( mode, () -> selectedTextProvider.getSelectedText().forEach( t
		// ->
		// callBack.highLight(selectedTextProvider.getSelectionStart(),
		// selectedTextProvider.getSelectionEnd(), t)
		// )));
		menu.add(R.string.highlight).setOnMenuItemClickListener(
				react(mode, new UiUtils.Action() {

					@Override
					public void perform() {
						selectedTextProvider.getSelectedText().forEach(
								new Command<String>() {

									@Override
									public void execute(String t) {
										callBack.highLight(selectedTextProvider
												.getSelectionStart(),
												selectedTextProvider
														.getSelectionEnd(), t);
									}
								});
					}
				}));

		if (callBack.isDictionaryAvailable()) {
			// menu.add(R.string.dictionary_lookup)
			// .setOnMenuItemClickListener( react( mode, () ->
			// selectedTextProvider.getSelectedText().forEach(
			// callBack::lookupDictionary )));
			menu.add(R.string.dictionary_lookup).setOnMenuItemClickListener(
					react(mode, new UiUtils.Action() {

						@Override
						public void perform() {
							selectedTextProvider.getSelectedText().forEach(
									new Command<String>() {

										@Override
										public void execute(String text) {
											callBack.lookupDictionary(text);
										}
									});
						}
					}));
		}

		// menu.add(R.string.lookup_wiktionary)
		// .setOnMenuItemClickListener( react(mode, () ->
		// selectedTextProvider.getSelectedText().forEach(callBack::lookupWiktionary)));
		menu.add(R.string.lookup_wiktionary).setOnMenuItemClickListener(
				react(mode, new UiUtils.Action() {

					@Override
					public void perform() {
						selectedTextProvider.getSelectedText().forEach(
								new Command<String>() {

									@Override
									public void execute(String text) {
										callBack.lookupWiktionary(text);
									}
								});
					}
				}));

		// menu.add(R.string.wikipedia_lookup)
		// .setOnMenuItemClickListener( react( mode, () ->
		// selectedTextProvider.getSelectedText().forEach(callBack::lookupWikipedia)));
		menu.add(R.string.wikipedia_lookup).setOnMenuItemClickListener(
				react(mode, new UiUtils.Action() {

					@Override
					public void perform() {
						selectedTextProvider.getSelectedText().forEach(
								new Command<String>() {

									@Override
									public void execute(String text) {
										callBack.lookupWikipedia(text);
									}
								});
					}
				}));

		// menu.add(R.string.google_lookup)
		// .setOnMenuItemClickListener( react( mode, () ->
		// selectedTextProvider.getSelectedText().forEach(callBack::lookupGoogle)));
		menu.add(R.string.google_lookup).setOnMenuItemClickListener(
				react(mode, new UiUtils.Action() {

					@Override
					public void perform() {
						selectedTextProvider.getSelectedText().forEach(
								new Command<String>() {

									@Override
									public void execute(String text) {
										callBack.lookupGoogle(text);
									}
								});
					}
				}));

		return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {

	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

		return true;
	}

}
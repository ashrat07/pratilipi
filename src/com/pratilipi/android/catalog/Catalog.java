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

package com.pratilipi.android.catalog;

import static java.util.Arrays.asList;
import static jedi.functional.FunctionalPrimitives.firstOption;

import java.util.List;

import jedi.functional.Filter;
import jedi.option.Option;
import android.view.View;
import android.widget.TextView;

import com.pratilipi.android.R;
import com.pratilipi.android.htmlspanner.HtmlSpanner;
import com.pratilipi.android.nucular.atom.Entry;
import com.pratilipi.android.nucular.atom.Feed;
import com.pratilipi.android.nucular.atom.Link;

public class Catalog {

	/**
	 * Reserved ID to identify the feed entry where custom sites are added.
	 */
	public static final String CUSTOM_SITES_ID = "IdCustomSites";

	private static final int ABBREV_TEXT_LEN = 150;

	private Catalog() {
	}

	/**
	 * Selects the right image link for an entry, based on preference.
	 * 
	 * @param feed
	 * @param entry
	 * @return
	 */
	public static Option<Link> getImageLink(Feed feed, Entry entry) {

		List<Link> items;

		if (feed.isDetailFeed()) {
			items = asList(entry.getImageLink().unsafeGet(), entry
					.getThumbnailLink().unsafeGet());
		} else {
			items = asList(entry.getThumbnailLink().unsafeGet(), entry
					.getImageLink().unsafeGet());
		}

		// return firstOption( items, l -> l != null );
		return firstOption(items, new Filter<Link>() {

			@Override
			public Boolean execute(Link l) {
				return l != null;
			}
		});
	}

	/**
	 * Loads the details for the given entry into the given layout.
	 * 
	 * @param layout
	 * @param entry
	 * @param abbreviateText
	 */
	public static void loadBookDetails(View layout, Entry entry,
			boolean abbreviateText) {

		HtmlSpanner spanner = new HtmlSpanner();

		// We don't want to load images here
		spanner.unregisterHandler("img");

		TextView title = (TextView) layout.findViewById(R.id.itemTitle);
		TextView desc = (TextView) layout.findViewById(R.id.itemDescription);

		title.setText(entry.getTitle());

		CharSequence text;

		if (entry.getContent() != null) {
			text = spanner.fromHtml(entry.getContent().getText());
		} else if (entry.getSummary() != null) {
			text = spanner.fromHtml(entry.getSummary());
		} else {
			text = "";
		}

		if (abbreviateText && text.length() > ABBREV_TEXT_LEN) {
			text = text.subSequence(0, ABBREV_TEXT_LEN) + "���";
		}

		desc.setText(text);
	}

}

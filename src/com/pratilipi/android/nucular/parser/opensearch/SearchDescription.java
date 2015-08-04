/*
 * Copyright (C) 2012 Alex Kuiper
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

package com.pratilipi.android.nucular.parser.opensearch;

import jedi.functional.Filter;
import jedi.option.Option;

import java.util.ArrayList;
import java.util.List;

import com.pratilipi.android.nucular.atom.AtomConstants;
import com.pratilipi.android.nucular.atom.Link;

import static jedi.functional.FunctionalPrimitives.firstOption;

public class SearchDescription {

	private List<Link> links = new ArrayList<Link>();

	public void addLink(Link link) {
		this.links.add(link);
	}

	public Option<Link> getSearchLink() {
		// return firstOption( this.links, l -> AtomConstants.TYPE_ATOM.equals(
		// l.getType() ) );
		return firstOption(links, new Filter<Link>() {

			@Override
			public Boolean execute(Link l) {
				return AtomConstants.TYPE_ATOM.equals(l.getType());
			}
		});
	}
}

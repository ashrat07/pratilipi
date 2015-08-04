package com.pratilipi.android.util;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import jedi.option.Option;

import static jedi.option.Options.option;

/**
 * Created by alex on 10/11/14.
 */
public class UiUtils {

	public static interface Operation<A> {
		void thenDo(A arg);
	}

	public static interface Action {
		void perform();
	}

	public static Operation<Action> onMenuPress(Menu menu, int elementName) {
		return onMenuPress(menu.findItem(elementName));
	}

	public static Operation<Action> onMenuPress(final MenuItem menuItem) {
		// return action -> menuItem.setOnMenuItemClickListener(item -> {
		// action.perform();
		// return true;
		// });
		return new Operation<Action>() {

			@Override
			public void thenDo(final Action action) {

				menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						action.perform();
						return true;
					}
				});
			}
		};
	}

	// public static Operation<Action> onMenuPress(
	// final android.view.MenuItem menuItem) {
	// // return action -> menuItem.setOnMenuItemClickListener( item -> {
	// // action.perform();
	// // return true;
	// // });
	//
	// return new Operation<Action>() {
	//
	// @Override
	// public void thenDo(final Action action) {
	// menuItem.setOnMenuItemClickListener(new
	// android.view.MenuItem.OnMenuItemClickListener() {
	//
	// @Override
	// public boolean onMenuItemClick(android.view.MenuItem item) {
	// action.perform();
	// return true;
	// }
	// });
	// }
	// };
	// }

	public static SearchView.OnQueryTextListener onQuery(
			final Operation<String> op) {

		return new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				op.thenDo(query);
				return true;
			}

			@Override
			public boolean onQueryTextChange(String query) {
				return false;
			}
		};
	}

	public static MenuItem.OnActionExpandListener onCollapse(
			final Action onCollapse) {
		return new MenuItem.OnActionExpandListener() {
			@Override
			public boolean onMenuItemActionExpand(MenuItem menuItem) {
				return true;
			}

			@Override
			public boolean onMenuItemActionCollapse(MenuItem menuItem) {
				onCollapse.perform();
				return true;
			}
		};
	}

	public static Option<TextView> getTextView(View parent, int id) {
		return getView(parent, id, TextView.class);
	}

	public static Option<ImageView> getImageView(View parent, int id) {
		return getView(parent, id, ImageView.class);
	}

	public static <T extends View> Option<T> getView(View parent, int id,
			Class<T> viewType) {
		return option((T) parent.findViewById(id));
	}

}

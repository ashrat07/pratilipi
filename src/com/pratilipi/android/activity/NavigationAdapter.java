package com.pratilipi.android.activity;

import static java.util.Arrays.asList;
import static jedi.functional.FunctionalPrimitives.flatten;
import static jedi.functional.FunctionalPrimitives.headOption;
import static com.pratilipi.android.util.CollectionUtil.listElement;
import static com.pratilipi.android.util.UiUtils.getImageView;
import static com.pratilipi.android.util.UiUtils.getTextView;

import java.util.ArrayList;
import java.util.List;

import jedi.functional.Command;
import jedi.functional.FunctionalPrimitives;
import jedi.functional.Functor;
import jedi.functional.Functor0;
import jedi.functional.Functor2;
import jedi.option.Option;
import jedi.option.OptionMatcher;
import com.pratilipi.android.util.PlatformUtil;
import com.pratilipi.android.R;
import com.pratilipi.android.view.NavigationCallback;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA. User: alex Date: 8/12/13 Time: 11:10 AM To change
 * this template use File | Settings | File Templates.
 */
public class NavigationAdapter extends BaseExpandableListAdapter {

	private List<NavigationCallback> items;
	private Context context;
	private final int level;

	private Functor2<List<NavigationCallback>, Integer, ExpandableListView> subListProvider;

	private static final int INDENT = 12;

	public NavigationAdapter(
			Context context,
			List<NavigationCallback> items,
			Functor2<List<NavigationCallback>, Integer, ExpandableListView> subListProvider,
			int level) {
		this.context = context;
		this.subListProvider = subListProvider;

		this.items = new ArrayList<>(items);
		this.level = level;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean b,
			final View view, ViewGroup viewGroup) {

		Option<NavigationCallback> childItem = findChild(groupPosition,
				childPosition);

		if (FunctionalPrimitives.isEmpty(childItem)) {
			// Could not find childView with index
		}

		// View childView = childItem.map( c -> c.hasChildren() ?
		// getChildNodeView(c,view): getChildLeafView(c, view ) )
		// .getOrElse(view);
		View childView = childItem.map(new Functor<NavigationCallback, View>() {

			public View execute(NavigationCallback c) {
				return c.hasChildren() ? getChildNodeView(c, view)
						: getChildLeafView(c, view);
			};
		}).getOrElse(new Functor0<View>() {

			@Override
			public View execute() {
				return view;
			}
		});

		int paddingValue = (int) getDipValue(INDENT) * (level + 2);

		childView.setPadding(paddingValue, childView.getPaddingTop(),
				childView.getPaddingRight(), childView.getPaddingBottom());

		return childView;
	}

	private float getDipValue(int input) {

		Resources r = context.getResources();

		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, input,
				r.getDisplayMetrics());
	}

	private View getChildNodeView(NavigationCallback childItem, View view) {

		ExpandableListView layout;

		if (view instanceof ExpandableListView) {
			layout = (ExpandableListView) view;
		} else {
			layout = subListProvider.execute(asList(childItem), level + 1);
		}

		return layout;
	}

	private View getChildLeafView(final NavigationCallback childItem, View view) {

		View layout = view;

		if (layout == null) {
			layout = PlatformUtil.getLayoutInflater(context).inflate(
					R.layout.drawer_list_subitem, null);
		}

		// getTextView( layout, R.id.itemText ).forEach( t ->
		// t.setText(childItem.getTitle() ) );
		getTextView(layout, R.id.itemText).forEach(new Command<TextView>() {

			public void execute(TextView t) {
				t.setText(childItem.getTitle());
			};
		});
		// getTextView( layout, R.id.subtitleText ).forEach( t ->
		// t.setText(childItem.getSubtitle() ));
		getTextView(layout, R.id.subtitleText).forEach(new Command<TextView>() {

			@Override
			public void execute(TextView t) {
				t.setText(childItem.getSubtitle());
			}
		});

		return layout;
	}

	@Override
	public int getGroupCount() {
		return items.size();
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int i, int i2) {
		return true;
	}

	@Override
	public View getGroupView(final int i, final boolean isExpanded, View view,
			ViewGroup viewGroup) {

		View layout;

		if (view != null) {
			layout = view;
		} else {
			layout = PlatformUtil.getLayoutInflater(context).inflate(
					R.layout.drawer_list_item, null);
		}

		Option<TextView> textView = getTextView(layout, R.id.groupName);
		Option<ImageView> indicator = getImageView(layout,
				R.id.explist_indicator);

		// indicator.forEach( ind -> {
		// if ( getChildrenCount( i ) == 0 ) {
		// ind.setVisibility( View.INVISIBLE );
		// } else {
		// ind.setVisibility( View.VISIBLE );
		// ind.setImageResource( isExpanded ? R.drawable.arrowhead_up :
		// R.drawable.arrowhead_down );
		// }
		// });

		indicator.forEach(new Command<ImageView>() {

			@Override
			public void execute(ImageView ind) {
				if (getChildrenCount(i) == 0) {
					ind.setVisibility(View.INVISIBLE);
				} else {
					ind.setVisibility(View.VISIBLE);
					ind.setImageResource(isExpanded ? R.drawable.arrowhead_up
							: R.drawable.arrowhead_down);
				}
			}
		});

		final NavigationCallback item = items.get(i);

		// textView.match(
		// t -> t.setText(item.getTitle()),
		// () -> LOG.error("View for title not found!")
		// );
		textView.match(new OptionMatcher<TextView>() {

			@Override
			public void caseNone() {
				// View for title not found!
			}

			@Override
			public void caseSome(TextView t) {
				t.setText(item.getTitle());
			}
		});

		return layout;
	}

	@Override
	public int getChildrenCount(int i) {
		// Option<Integer> count = listElement( items, i
		// ).map(NavigationCallback::getChildCount);
		Option<Integer> count = listElement(items, i).map(
				new Functor<NavigationCallback, Integer>() {

					public Integer execute(NavigationCallback c) {
						return c.getChildCount();
					};
				});

		return count.getOrElse(0);
	}

	public Option<NavigationCallback> findGroup(int i) {
		return listElement(items, i);
	}

	public Option<NavigationCallback> findChild(int groupId, final int childId) {

		// Option<Option<NavigationCallback>> childItem =
		// listElement(items, groupId).map(g -> g.getChild(childId));
		Option<Option<NavigationCallback>> childItem = listElement(items,
				groupId).map(
				new Functor<NavigationCallback, Option<NavigationCallback>>() {

					public Option<NavigationCallback> execute(
							NavigationCallback c) {
						return c.getChild(childId);
					};
				});

		return headOption(flatten(childItem));
	}

	@Override
	public Object getChild(int groupId, int childId) {
		return findChild(groupId, childId).unsafeGet();
	}

	@Override
	public NavigationCallback getGroup(int groupId) {
		return findGroup(groupId).unsafeGet();
	}

	@Override
	public long getGroupId(int i) {
		return i;
	}

	@Override
	public long getChildId(int i, int i2) {
		return i * 100 + i2;
	}

	public int getIndexForChildId(int groupIndex, int childId) {
		return childId - groupIndex * 100;
	}

	public int getLevel() {
		return level;
	}
}

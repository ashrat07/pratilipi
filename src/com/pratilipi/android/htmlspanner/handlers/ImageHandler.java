package com.pratilipi.android.htmlspanner.handlers;

import java.io.IOException;
import java.net.URL;

import org.htmlcleaner.TagNode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import com.pratilipi.android.htmlspanner.SpanStack;
import com.pratilipi.android.htmlspanner.TagNodeHandler;

public class ImageHandler extends TagNodeHandler {

	@Override
	public void handleTagNode(TagNode node, SpannableStringBuilder builder,
			int start, int end, SpanStack stack) {
		String src = node.getAttributeByName("src");

		builder.append("\uFFFC");

		Bitmap bitmap = loadBitmap(src);

		if (bitmap != null) {
			Drawable drawable = new BitmapDrawable(bitmap);
			drawable.setBounds(0, 0, bitmap.getWidth() - 1,
					bitmap.getHeight() - 1);

			stack.pushSpan(new ImageSpan(drawable), start, builder.length());
		}
	}

	/**
	 * Loads a Bitmap from the given url.
	 * 
	 * @param url
	 * @return a Bitmap, or null if it could not be loaded.
	 */
	protected Bitmap loadBitmap(String url) {
		try {
			return BitmapFactory.decodeStream(new URL(url).openStream());
		} catch (IOException io) {
			return null;
		}
	}

}

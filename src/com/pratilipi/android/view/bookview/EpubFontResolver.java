package com.pratilipi.android.view.bookview;

import android.content.Context;
import android.graphics.Typeface;
import com.google.inject.Inject;
import com.pratilipi.android.htmlspanner.FontFamily;
import com.pratilipi.android.htmlspanner.SystemFontResolver;

import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.util.IOUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA. User: alex Date: 6/23/13 Time: 9:56 AM To change
 * this template use File | Settings | File Templates.
 */
public class EpubFontResolver extends SystemFontResolver {

	private Map<String, FontFamily> loadedTypeFaces = new HashMap<String, FontFamily>();
	private TextLoader textLoader;
	private Context context;

	@Inject
	public EpubFontResolver(TextLoader loader, Context context) {

		this.textLoader = loader;
		loader.setFontResolver(this);

		this.context = context;
	}

	@Override
	protected FontFamily resolveFont(String name) {

		if (loadedTypeFaces.containsKey(name)) {
			return loadedTypeFaces.get(name);
		}

		return super.resolveFont(name);
	}

	public void loadEmbeddedFont(String name, String resourceHRef) {

		if (loadedTypeFaces.containsKey(name)) {
			return;
		}

		// Resource res =
		// textLoader.getCurrentBook().getResources().getByFileName(
		// resourceHRef );
		Resource res = textLoader.getCurrentBook().getResources()
				.getByHref(resourceHRef);

		if (res == null) {
			// No resource found for href
			return;
		}

		File tempFile = new File(context.getCacheDir(), UUID.randomUUID()
				.toString());

		try {
			IOUtil.copy(res.getInputStream(), new FileOutputStream(tempFile));
			res.close();

			Typeface typeface = Typeface.createFromFile(tempFile);

			FontFamily fontFamily = new FontFamily(name, typeface);

			loadedTypeFaces.put(name, fontFamily);

		} catch (IOException io) {
			// Could not load embedded font
		} finally {
			tempFile.delete();
		}
	}
}

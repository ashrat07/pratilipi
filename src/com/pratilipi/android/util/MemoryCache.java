package com.pratilipi.android.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;

public class MemoryCache {

	private Map<String, Bitmap> cache = Collections
			.synchronizedMap(new HashMap<String, Bitmap>());

	public Bitmap get(String id) {
		if (!cache.containsKey(id))
			return null;
		Bitmap ref = cache.get(id);
		return ref;
	}

	public void put(String id, Bitmap bitmap) {
		cache.put(id, bitmap);
	}

	public void clear() {
		cache.clear();
	}
}

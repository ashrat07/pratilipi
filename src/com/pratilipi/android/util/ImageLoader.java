package com.pratilipi.android.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.pratilipi.android.R;

public class ImageLoader {

	MemoryCache memoryCache = new MemoryCache();
	FileCache fileCache;
	private Map<View, String> imageViews = Collections
			.synchronizedMap(new HashMap<View, String>());
	ExecutorService executorService;
	Context context;
	int stub_id = R.drawable.noimage;

	private boolean isFullImage;

	public ImageLoader(Context context) {
		this.context = context;
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(3);
	}

	public void displayFullImage(String url, View view) {
		isFullImage = true;
		displayImage(url, view);

	}

	public void displayImage(String url, int loader, View view) {
		stub_id = loader;
		imageViews.put(view, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			if (view instanceof ImageView) {

				((ImageView) view).setImageBitmap(bitmap);

			} else {
				BitmapDrawable bdrawable = new BitmapDrawable(
						context.getResources(), bitmap);
				view.setBackground(bdrawable);
			}

		} else {
			queuePhoto(url, view);
			if (!isFullImage) {
				if (view instanceof ImageView) {
					((ImageView) view).setImageResource(loader);
				} else {
					view.setBackgroundResource(loader);
				}
			}
		}
	}

	public void displayImage(String url, View view) {
		imageViews.put(view, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			if (view instanceof ImageView) {
				TransitionDrawable td = new TransitionDrawable(new Drawable[] {
						new ColorDrawable(android.R.color.transparent),
						new BitmapDrawable(context.getResources(), bitmap) });

				((ImageView) view).setImageDrawable(td);

				td.startTransition(300);

			} else {
				BitmapDrawable bdrawable = new BitmapDrawable(
						context.getResources(), bitmap);
				view.setBackground(bdrawable);
			}

		} else {
			queuePhoto(url, view);
		}
	}

	private void queuePhoto(String url, View imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	public ExecutorService getDownloadService() {
		return executorService;
	}

	private Bitmap getBitmap(String url) {

		File f = fileCache.getFile(url);
		// precheck file length
		if (f.exists() && f.length() > 0) {
			// from SD cache
			Bitmap b = decodeFile(f);
			if (b != null) {
				return b;
			}
		} else {
			// from web
			try {
				Bitmap bitmap;
				URL imageUrl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) imageUrl
						.openConnection();
				conn.setConnectTimeout(30000);
				conn.setReadTimeout(30000);
				conn.setInstanceFollowRedirects(true);
				InputStream is = conn.getInputStream();
				OutputStream os = new FileOutputStream(f);
				PUtils.copyStream(is, os);
				os.close();
				bitmap = decodeFile(f);
				return bitmap;
			} catch (Exception e) {
				LoggerUtils.logWarn("ImageLoader", Log.getStackTraceString(e));
				return null;
			}
		}
		return null;
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
			if (isFullImage) {
				BitmapFactory.Options o2 = new BitmapFactory.Options();
				o2.inTempStorage = new byte[64 * 1024];
				o2.inPurgeable = true;
				isFullImage = false;
				return BitmapFactory.decodeStream(new FileInputStream(f), null,
						o2);
			} else {
				// decode image size
				BitmapFactory.Options o = new BitmapFactory.Options();
				o.inJustDecodeBounds = true;
				BitmapFactory.decodeStream(new FileInputStream(f), null, o);

				// Find the correct scale value. It should be the power of 2.
				final int REQUIRED_SIZE = 70;
				int width_tmp = o.outWidth, height_tmp = o.outHeight;
				int scale = 1;
				while (true) {
					if (width_tmp / 2 < REQUIRED_SIZE
							|| height_tmp / 2 < REQUIRED_SIZE)
						break;
					width_tmp /= 2;
					height_tmp /= 2;
					scale *= 2;
				}

				// decode with inSampleSize
				BitmapFactory.Options o2 = new BitmapFactory.Options();
				o2.inSampleSize = scale;
				return BitmapFactory.decodeStream(new FileInputStream(f), null,
						o2);
			}
		} catch (FileNotFoundException e) {
			LoggerUtils.logWarn("ImgLoader FileNotFoundException",
					Log.getStackTraceString(e));
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public View view;

		public PhotoToLoad(String u, View i) {
			url = u;
			view = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {

			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = getBitmap(photoToLoad.url);
			memoryCache.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			Activity a = (Activity) photoToLoad.view.getContext();
			a.runOnUiThread(bd);
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.view);
		return tag == null || !tag.equals(photoToLoad.url);
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {

			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null) {
				if (photoToLoad.view instanceof ImageView) {
					TransitionDrawable td = new TransitionDrawable(
							new Drawable[] {
									new ColorDrawable(
											android.R.color.transparent),
									new BitmapDrawable(context.getResources(),
											bitmap) });

					((ImageView) photoToLoad.view).setImageDrawable(td);

					td.startTransition(1000);

				} else {
					BitmapDrawable bdrawable = new BitmapDrawable(
							context.getResources(), bitmap);
					photoToLoad.view.setBackground(bdrawable);
				}
			} else {
				if (photoToLoad.view instanceof ImageView) {
					if (!isFullImage) {
						((ImageView) photoToLoad.view)
								.setImageResource(stub_id);
					}
				} else {
					if (!isFullImage) {
						photoToLoad.view.setBackground(context.getResources()
								.getDrawable(stub_id));
					}
				}
			}
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

}
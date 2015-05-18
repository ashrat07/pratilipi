package com.pratilipi.android.util;

import java.io.File;
import java.io.IOException;

import android.content.Context;

public class FileCache {

	private File cacheDir;
	private static File appFile;

	public FileCache(Context context) {
		// Find the dir to save cached images
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
					"TempImages");
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	public File getFile(String url) {
		String filename = String.valueOf(url.hashCode());
		File f = new File(cacheDir, filename);
		return f;

	}

	public void clear() {
		File[] files = cacheDir.listFiles();
		if (files == null)
			return;
		for (File f : files)
			f.delete();
	}

	public static void createAppFile() {
		String filename = ".fc_" + PUtils.getAppVersionCode();
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			appFile = new File(
					android.os.Environment.getExternalStorageDirectory(),
					filename);
			try {
				if (!appFile.exists())
					appFile.createNewFile();
			} catch (IOException e) {
			}
		}
	}

	public static boolean isAppFileExists() {
		String filename = ".fc_" + PUtils.getAppVersionCode();
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			appFile = new File(
					android.os.Environment.getExternalStorageDirectory(),
					filename);
			return appFile.exists();
		}
		return false;
	}

}
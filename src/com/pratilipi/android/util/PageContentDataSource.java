package com.pratilipi.android.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.pratilipi.android.model.PageContent;

public class PageContentDataSource {

	private SQLiteDatabase database;
	private PageContentSQLiteHelper dbHelper;
	private String[] allColumns = { PageContentSQLiteHelper.COLUMN_ID,
			PageContentSQLiteHelper.COLUMN_PRATILIPI_ID,
			PageContentSQLiteHelper.COLUMN_PAGE_NO,
			PageContentSQLiteHelper.COLUMN_CONTENT,
			PageContentSQLiteHelper.COLUMN_LANGUAGE };

	public PageContentDataSource(Context context) {
		dbHelper = PageContentSQLiteHelper.getInstance(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public PageContent createPageContent(PageContent pageContent) {
		PageContent newPageContent = null;
		ContentValues values = new ContentValues();
		values.put(PageContentSQLiteHelper.COLUMN_PRATILIPI_ID,
				pageContent.pratilipiId);
		values.put(PageContentSQLiteHelper.COLUMN_PAGE_NO, pageContent.pageNo);
		values.put(PageContentSQLiteHelper.COLUMN_CONTENT, pageContent.content);
		values.put(PageContentSQLiteHelper.COLUMN_LANGUAGE,
				pageContent.language);
		long insertId = database.insert(
				PageContentSQLiteHelper.TABLE_PAGE_CONENT, null, values);
		Cursor cursor = database.query(
				PageContentSQLiteHelper.TABLE_PAGE_CONENT, allColumns,
				PageContentSQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		if (cursor.moveToFirst()) {
			newPageContent = cursorToPageContent(cursor);
		}
		cursor.close();
		return newPageContent;
	}

	public void deletePageContent(long pratilipiId) {
		database.delete(PageContentSQLiteHelper.TABLE_PAGE_CONENT,
				PageContentSQLiteHelper.COLUMN_PRATILIPI_ID + " = ?",
				new String[] { String.valueOf(pratilipiId) });
	}

	public PageContent getPageContent(long pratilipiId, int pageNo) {
		Cursor cursor = database.query(
				PageContentSQLiteHelper.TABLE_PAGE_CONENT,
				allColumns,
				PageContentSQLiteHelper.COLUMN_PRATILIPI_ID + " = ? and "
						+ PageContentSQLiteHelper.COLUMN_PAGE_NO + " = ?",
				new String[] { String.valueOf(pratilipiId),
						String.valueOf(pageNo) }, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			PageContent pageContent = cursorToPageContent(cursor);
			return pageContent;
		}
		// make sure to close the cursor
		cursor.close();
		return null;
	}

	private PageContent cursorToPageContent(Cursor cursor) {
		PageContent pageContent = new PageContent(cursor.getLong(0),
				cursor.getLong(1), cursor.getInt(2), cursor.getString(3),
				cursor.getString(4));
		return pageContent;
	}

}

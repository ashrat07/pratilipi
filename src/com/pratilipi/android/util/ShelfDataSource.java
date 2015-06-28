package com.pratilipi.android.util;

import java.util.ArrayList;
import java.util.List;

import com.pratilipi.android.model.Shelf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ShelfDataSource {

	private SQLiteDatabase database;
	private ShelfSQLiteHelper dbHelper;
	private String[] allColumns = { ShelfSQLiteHelper.COLUMN_ID,
			ShelfSQLiteHelper.COLUMN_PRATILIPI_ID,
			ShelfSQLiteHelper.COLUMN_CONTENT, ShelfSQLiteHelper.COLUMN_LANGUAGE };

	public ShelfDataSource(Context context) {
		dbHelper = new ShelfSQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Shelf createShelfContent(Shelf shelf) {
		Shelf newShelf = null;
		ContentValues values = new ContentValues();
		values.put(ShelfSQLiteHelper.COLUMN_PRATILIPI_ID, shelf.pratilipiId);
		values.put(ShelfSQLiteHelper.COLUMN_CONTENT, shelf.content);
		values.put(ShelfSQLiteHelper.COLUMN_LANGUAGE, shelf.language);
		long insertId = database.insert(ShelfSQLiteHelper.TABLE_SHELF, null,
				values);
		Cursor cursor = database.query(ShelfSQLiteHelper.TABLE_SHELF,
				allColumns, ShelfSQLiteHelper.COLUMN_ID + " = " + insertId,
				null, null, null, null);
		if (cursor.moveToFirst()) {
			newShelf = cursorToShelf(cursor);
		}
		cursor.close();
		return newShelf;
	}

	public void deleteContent(long pratilipiId) {
		database.delete(ShelfSQLiteHelper.TABLE_SHELF,
				ShelfSQLiteHelper.COLUMN_PRATILIPI_ID + " = " + pratilipiId,
				null);
	}

	public List<Shelf> getAllContent() {
		List<Shelf> shelfList = new ArrayList<>();
		Cursor cursor = database.query(ShelfSQLiteHelper.TABLE_SHELF,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Shelf shelf = cursorToShelf(cursor);
			shelfList.add(shelf);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return shelfList;
	}

	private Shelf cursorToShelf(Cursor cursor) {
		Shelf shelf = new Shelf(cursor.getLong(0), cursor.getLong(1),
				cursor.getString(2), cursor.getString(3));
		return shelf;
	}

}

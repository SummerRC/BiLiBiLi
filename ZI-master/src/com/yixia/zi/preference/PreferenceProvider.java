/*
 * Copyright (C) 2011 Cedric Fung (wolfplanet@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yixia.zi.preference;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * When using, must to change the value ot AUTHORITY 
 *
 */
public class PreferenceProvider extends ContentProvider {
	public static final String AUTHORITY = "me.abitno.zi.provider.preference";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/preferences");
	public static final String COL_ID = "_id";
	public static final String COL_KEY = "key";
	public static final String COL_VALUE = "value";

	@Override
	public boolean onCreate() {
		mDbHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (URI_MATCHER.match(uri)) {
		case PREFERENCES:
			qb.setTables(TB_NAME);
			break;
		case PREFERENCE_KEY:
			qb.setTables(TB_NAME);
			qb.appendWhere(COL_KEY + "= '" + uri.getPathSegments().get(1) + "'");
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		return qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
	}

	@Override
	public String getType(Uri uri) {
		return TYPE;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		boolean isPrefrences = URI_MATCHER.match(uri) == PREFERENCES;
		if (!isPrefrences)
			throw new IllegalArgumentException("Unknown URI " + uri);

		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		long rowId = db.insert(TB_NAME, null, values);
		if (rowId > 0) {
			Uri noteUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(noteUri, null);
			return noteUri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int count;
		switch (URI_MATCHER.match(uri)) {
		case PREFERENCES:
			count = db.delete(TB_NAME, selection, selectionArgs);
			break;
		case PREFERENCE_KEY:
			count = db.delete(TB_NAME, COL_KEY + "= '" + uri.getPathSegments().get(1) + "'" + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int count;
		switch (URI_MATCHER.match(uri)) {
		case PREFERENCES:
			count = db.update(TB_NAME, values, selection, selectionArgs);
			break;
		case PREFERENCE_KEY:
			count = db.update(TB_NAME, values, COL_KEY + "= '" + uri.getPathSegments().get(1)  + "'" + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SQL_CREATE_TABLE);
			db.execSQL(SQL_ADD_INDEX);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
			onCreate(db);
		}
	}

	private DatabaseHelper mDbHelper;
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "preferences.db";
	private static final String TB_NAME = "preferences";
	private static final String SQL_CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY, %s TEXT UNIQUE NOT NULL, %s TEXT);", TB_NAME, COL_ID, COL_KEY, COL_VALUE);
	private static final String SQL_ADD_INDEX = "CREATE UNIQUE INDEX index_key ON " + TB_NAME + "(" + COL_KEY + ");";
	private static final int PREFERENCES = 10;
	private static final int PREFERENCE_KEY = 11;
	private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		URI_MATCHER.addURI(AUTHORITY, "preferences", PREFERENCES);
		URI_MATCHER.addURI(AUTHORITY, "preferences/*", PREFERENCE_KEY);
	}
	private static final String TYPE = "me.abitno.zi.provider/preference";
}

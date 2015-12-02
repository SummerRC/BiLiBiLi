package com.yixia.zi.provider;


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
 * 
 *  When using, must to change the value of AUTHORITY
 * 
 */
public class SessionProvider extends ContentProvider {
	public static String AUTHORITY = "com.yixia.zi.provider.session";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/sessions");
	public static final String CONTENT_TYPE_SESSIONS = "vnd.android.cursor.dir/vnd.zi.sessions";
	public static final String CONTENT_ITEM_TYPE_SESSION = "vnd.android.cursor.item/vnd.zi.session";
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
		case SESSIONS:
			qb.setTables(TB_NAME);
			break;
		case SESSION_ID:
			qb.setTables(TB_NAME);
			qb.appendWhere(COL_ID + "=" + uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		return qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
	}

	@Override
	public String getType(Uri uri) {
		
		switch (URI_MATCHER.match(uri)) {
		case SESSIONS:
			return CONTENT_TYPE_SESSIONS;
		case SESSION_ID:
			return CONTENT_ITEM_TYPE_SESSION;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		boolean isPrefrences = URI_MATCHER.match(uri) == SESSIONS;
		if (!isPrefrences)
			throw new IllegalArgumentException("Unknown URI " + uri);

		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		long rowId = db.insert(TB_NAME, COL_VALUE, values);
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
		case SESSIONS:
			count = db.delete(TB_NAME, selection, selectionArgs);
			break;
		case SESSION_ID:
			count = db.delete(TB_NAME, COL_ID + "=" + uri.getPathSegments().get(1) + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"), selectionArgs);
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
		case SESSIONS:
			count = db.update(TB_NAME, values, selection, selectionArgs);
			break;
		case SESSION_ID:
			count = db.update(TB_NAME, values, COL_ID + "=" + uri.getPathSegments().get(1) + (TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"), selectionArgs);
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
	private static final String DB_NAME = "sessions.db";
	private static final String TB_NAME = "sessions";
	private static final String SQL_CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY, %s TEXT UNIQUE NOT NULL, %s TEXT);", TB_NAME, COL_ID, COL_KEY, COL_VALUE);
	private static final String SQL_ADD_INDEX = "CREATE UNIQUE INDEX index_key ON " + TB_NAME + "(" + COL_KEY + ");";
	private static final int SESSIONS = 10;
	private static final int SESSION_ID = 11;
	private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
	
	static {
		URI_MATCHER.addURI(AUTHORITY, "sessions", SESSIONS);
		URI_MATCHER.addURI(AUTHORITY, "sessions/#", SESSION_ID);
	}
}

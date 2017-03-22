package com.example.whetherdata.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.example.whetherdata.dbhelper.WeatherDataHandler;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by pallavshah on 5/29/15.
 */
public class WeatherProvider extends ContentProvider {

    // database 
    private WeatherDataHandler database;

    // used for the UriMacher
    private static final int WEATHERS = 10;
    private static final int WEATHER_ID = 20;

    private static final String AUTHORITY = "com.example.whetherdata.provider";

    private static final String BASE_PATH = "weathers";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/weathers";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/weather";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, WEATHERS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", WEATHER_ID);
    }
    @Override
    public boolean onCreate() {
        database = new WeatherDataHandler(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(WeatherDataHandler.WEATHER_TABLE_NAME);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case WEATHERS:
                break;
            case  WEATHER_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(WeatherDataHandler.KEY_WEATHER_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    private void checkColumns(String[] projection) {
        String[] available = WeatherDataHandler.WEATHER_COLUMNS;
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}

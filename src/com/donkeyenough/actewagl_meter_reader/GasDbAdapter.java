/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.donkeyenough.actewagl_meter_reader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple readings database access helper class. Defines the basic CRUD operations
 * for the ACTEWAGL reader, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 */
public class GasDbAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_DOE = "doe";
    public static final String KEY_GAS = "gas";

    private static final String TAG = "GasDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
        "create table table_gas (_id integer primary key autoincrement, "
        + "doe text not null, gas text not null);";

    private static final String DATABASE_NAME = "db_gas";
    private static final String DATABASE_TABLE = "table_gas";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public GasDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the readings database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public GasDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    /**
     * Create a new reading using the gas and electricity values provided. If the reading is
     * successfully created return the new rowId for that reading, otherwise return
     * a -1 to indicate failure.
     * 
     * @param doe the date of entry of the reading
     * @param gas the gas value for that reading
     * @return rowId or -1 if failed
     */
    public long createReading(String doe, String gas) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DOE, doe);
        initialValues.put(KEY_GAS, gas);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the reading with the given rowId
     * 
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteReading(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all readings in the database
     * 
     * @return Cursor over all readings
     */
    public Cursor fetchAllReadings() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_DOE, KEY_GAS}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the reading that matches the given rowId
     * 
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching reading, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchReading(long rowId) throws SQLException {

        Cursor mCursor =

            mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_DOE, KEY_GAS}, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the reading using the details provided. The reading to be updated is
     * specified using the rowId, and it is altered to use the doe, gas and elec
     * values passed in
     * 
     * @param rowId id of note to update
     * @param doe the date of entry to set the reading to
     * @param gas the gas value to set the reading to
     * @return true if the reading was successfully updated, false otherwise
     */
    public boolean updateReading(long rowId, String doe, String gas) {
        ContentValues args = new ContentValues();       
        args.put(KEY_DOE, doe);
        args.put(KEY_GAS, gas);        

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

	public void deleteNote(long id) {
		// TODO Auto-generated method stub
		
	}
}

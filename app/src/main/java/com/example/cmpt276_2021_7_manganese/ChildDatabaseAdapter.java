package com.example.cmpt276_2021_7_manganese;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChildDatabaseAdapter{
    private static final String TAG = "ChildDBAdapter";

    public static final String KEY_ROWID = "id";

    public static final String KEY_NAME = "name";
    public static final int COL_NAME = 1;
    public static final String[] ALL_KEYS = new String[] {KEY_NAME};
    public static final String DATABASE_TABLE = "ChildTable";
    public static final int DATABASE_VERSION = 2;
    private static final String DATABASE_CREATE_SQL =
            "create table if not exists " + DATABASE_TABLE
                    + " (" + KEY_NAME + " text not null "
                    + ");";

    private Context context;

    SQLiteOpenHelper myDBHelper;
    private SQLiteDatabase db;

    public ChildDatabaseAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new SQLiteOpenHelper(context, "ChildTable", null, DATABASE_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(DATABASE_CREATE_SQL);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
                onCreate(db);
            }
        };
    }

    // Open the database connection.
    public ChildDatabaseAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        myDBHelper.close();
    }


    public long insertRow(String name) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);

        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }

    public void deleteAll() {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    // Return all data in the database.
    public Cursor getAllRows() {
        String where = null;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    public Cursor getRow(String name) {
        String where = KEY_NAME + "=" + name;
        Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    public boolean updateRow(long rowId, String name) {
        String where = KEY_ROWID + "=" + rowId;
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_NAME, name);

        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }


}

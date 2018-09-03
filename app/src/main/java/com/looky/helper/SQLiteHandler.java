package com.looky.helper;

/**
 * Created by kangjaeyun on 2017. 6. 8..
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "looky";

    // Login table name
    private static final String TABLE_USER = "user";

    private static final String TABLE_TUTORIAL = "tutorial";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_AGE = "age";

    private static final String KEY_MAIN_TUTORIAL = "main_tutorial";
    private static final String KEY_LIKE_TUTORIAL = "like_tutorial";
    private static final String KEY_CHART_TUTORIAL = "chart_tutorial";


    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT," + KEY_GENDER + " TEXT," + KEY_AGE + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        String CREATE_TUTORIAL_TABLE = "CREATE TABLE " + TABLE_TUTORIAL + "("
                + KEY_MAIN_TUTORIAL + " INTEGER,"
                + KEY_LIKE_TUTORIAL + " INTEGER,"
                + KEY_CHART_TUTORIAL + " INTEGER" + ")";

        db.execSQL(CREATE_TUTORIAL_TABLE);

        ContentValues values = new ContentValues();
        values.put(KEY_MAIN_TUTORIAL, "0");
        values.put(KEY_LIKE_TUTORIAL, "0");
        values.put(KEY_CHART_TUTORIAL, "0");

        db.insert(TABLE_TUTORIAL, null, values);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String email, String id, String uid, String created_at, String gender, String age) {
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_ID, id); // Id
        values.put(KEY_UID, uid); // Uid
        values.put(KEY_CREATED_AT, created_at); // Created At
        values.put(KEY_GENDER, gender);
        values.put(KEY_AGE, age);

        // Inserting Row
        db.delete(TABLE_USER, null, null);
        long insertId = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + insertId);
    }

    public void updateUserGender(String gender) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_GENDER, gender);

        db.update(TABLE_USER, values, null, null);
        db.close(); // Closing database connection

    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("id", cursor.getString(0));
            user.put("uid", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
            user.put("gender", cursor.getString(5));
            user.put("age", cursor.getString(6));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    public HashMap<String, String> getTutorialDetails() {

        HashMap<String, String> tutorial = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_TUTORIAL;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            tutorial.put("tutorial_main", cursor.getString(0));
            tutorial.put("tutorial_like", cursor.getString(1));
            tutorial.put("tutorial_chart", cursor.getString(2));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + tutorial.toString());

        return tutorial;
    }

    public void setTutorialMainCompleted() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_MAIN_TUTORIAL, "1");

        db.update(TABLE_TUTORIAL, values, null, null);
        db.close(); // Closing database connection
    }

    public void setTutorialLikeCompleted() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LIKE_TUTORIAL, "1");

        db.update(TABLE_TUTORIAL, values, null, null);
        db.close(); // Closing database connection
    }

    public void setTutorialChartCompleted() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CHART_TUTORIAL, "1");

        db.update(TABLE_TUTORIAL, values, null, null);
        db.close(); // Closing database connection
    }


    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}
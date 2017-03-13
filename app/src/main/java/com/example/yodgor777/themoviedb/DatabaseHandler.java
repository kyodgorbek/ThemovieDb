package com.example.yodgor777.themoviedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.R.attr.id;
import static android.R.attr.name;

/**
 * Created by yodgor777 on 2017-03-12.
 */


//I have created this database class and table you understand right now I have to pass sqlite data to button
 public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String  FavoriteMovie = "favoritemovie";

    // Contacts table name
    private static final String FavoritesMovie = "favoritesmovie";

    // Contacts Table Columns names
    private static final String movieID = "id";
    private static final  String movieName = "name";


    public DatabaseHandler(Context context) {
        super(context, FavoriteMovie, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + FavoritesMovie + "("
                + movieID + " INTEGER PRIMARY KEY," + movieName + " TEXT,"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + FavoritesMovie);

        // Create tables again
        onCreate(db);
    }
}
package com.example.user.async_task_jsonreader_test02;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
    Klasse zur Datenbankbehandlung auf Basis von SQLite

    Enthält Datenbankstruktur sowie Methoden zur Erstellung und Aktualisierung der DB
    sowie zum Einfügen neuer Datensätze

 */

public class AlbumDataOpenHandler extends SQLiteOpenHelper {

    private static final String TAG = AlbumDataOpenHandler.class.getSimpleName();

    private static final String DATABASE_NAME = "myalbums.db";
    private static final int DATABASE_VERSION = 1;

    private static final String _ID = "_id";
    private static final String TABLE_NAME_ALBUMS = "albums";


    private static final String ALBUM_NAME = "albumname";
    private static final String ARTIST_NAME = "artistname";


    private static final String TRACK_TITLE = "tracktitle";
    private static final String TABLE_NAME_ALBUMTRACKS = "albumtracks";



    private static final String TABLE_ALBUMS_CREATE = "CREATE TABLE "
            + TABLE_NAME_ALBUMS + " (" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ALBUM_NAME
            + " TEXT unique, "
            + ARTIST_NAME
            + " TEXT);";

    private String TABLE_ALBUMTRACKS_CREATE = "CREATE TABLE "
            + TABLE_NAME_ALBUMTRACKS + " (" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ALBUM_NAME
            + " TEXT, "
            + TRACK_TITLE
            + " TEXT unique);";

    AlbumDataOpenHandler(Context context){

        super(context,DATABASE_NAME,null,DATABASE_VERSION,null);

    }


    public void onCreate(SQLiteDatabase db){

        db.execSQL(TABLE_ALBUMS_CREATE);
        db.execSQL(TABLE_ALBUMTRACKS_CREATE);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){


    }

    public void insertData(String albumname, String artistname, String[] tracks)throws SQLiteConstraintException {



        SQLiteDatabase db = getWritableDatabase();

        ContentValues albumTableValues = new ContentValues();
        albumTableValues.put(ALBUM_NAME, albumname);
        albumTableValues.put(ARTIST_NAME, artistname);

        db.insert(TABLE_NAME_ALBUMS,null,albumTableValues);

        ContentValues trackTableValues = new ContentValues();


        for(int i = 0; i<tracks.length; i++){

            trackTableValues.put(ALBUM_NAME, albumname);
            trackTableValues.put(TRACK_TITLE,tracks[i]);
            db.insert(TABLE_NAME_ALBUMTRACKS, null,trackTableValues);
        }






    }


}
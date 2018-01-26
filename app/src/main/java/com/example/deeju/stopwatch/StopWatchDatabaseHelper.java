package com.example.deeju.stopwatch;

import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Deeju on 9/18/2017.
 */

public class StopWatchDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "stopwatch";
    private static final int DB_VERSION = 1;
    private Context contexts;


    StopWatchDatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
        contexts=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL("CREATE TABLE TIME (_id INTEGER PRIMARY KEY AUTOINCREMENT,VALUE TEXT,DATES STRING,CHECKER INTEGER);");
    }
    public void removeRows()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM TIME");
    }
    public void insertTime(ArrayList<String> list ) {
        long check=-1;
        Collections.reverse(list);
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        if (db.isOpen())
            Toast.makeText(contexts, "Database exists", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(contexts, "Database doesn't exist", Toast.LENGTH_SHORT).show();
       for ( int i=0 ; i<list.size() ; i++ ) {
            contentValues.put("VALUE",list.get(i));
            contentValues.put("DATES",date);
            contentValues.put("CHECKER",i);
            check=db.insert("TIME", null, contentValues);
           if(check != -1)
               Toast.makeText(contexts, "Data Saved" + check, Toast.LENGTH_SHORT).show();
           else
               Toast.makeText(contexts, "Something wrong, Data is not Saved", Toast.LENGTH_SHORT).show();
        }
    }
    public Cursor showRows()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from TIME",null);
        return res;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS TIME");
        onCreate(db);
    }
}

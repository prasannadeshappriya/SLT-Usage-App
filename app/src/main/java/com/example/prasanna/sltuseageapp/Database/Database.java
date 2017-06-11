package com.example.prasanna.sltuseageapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.prasanna.sltuseageapp.Utilities.PrintLog;

/**
 * Created by prasanna on 6/11/17.
 */

public class Database extends SQLiteOpenHelper {
    private String name;
    private Context context;
    private String command;

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.name = name;
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create tables
        command = "CREATE TABLE IF NOT EXISTS user (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_index VARCHAR(50), " +
                "user_status VARCHAR(10), " +
                "password VARCHAR(255));";
        PrintLog.print("Create User Table [" + command + "]", "DATABASE");
        db.execSQL(command);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        command = "DROP TABLE IF EXISTS user;";
        PrintLog.print("Drop User Table [" + command + "]", "DATABASE");
        sqLiteDatabase.execSQL(command);
        onCreate(sqLiteDatabase);
    }

    public String getDatabaseName(){return name;}
    public void setContext(Context context){
        this.context = context;
    }

    public SQLiteDatabase getDatabase(){
        return getWritableDatabase();
    }
}

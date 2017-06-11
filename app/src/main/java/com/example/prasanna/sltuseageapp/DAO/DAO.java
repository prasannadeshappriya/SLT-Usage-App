package com.example.prasanna.sltuseageapp.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.prasanna.sltuseageapp.Constants.Constants;
import com.example.prasanna.sltuseageapp.Database.Database;

/**
 * Created by prasanna on 6/11/17.
 */

public class DAO {
    public Database database;
    public SQLiteDatabase sqldb;

    public DAO(Context context){
        database = new Database(
                context,
                Constants.DATABASE_NAME,
                null,
                Constants.DATABASE_VERSION
        );
        sqldb = database.getDatabase();
    }
}

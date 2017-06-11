package com.example.prasanna.sltuseageapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.prasanna.sltuseageapp.Models.User;
import com.example.prasanna.sltuseageapp.Utilities.EncryptDecrypt;
import com.example.prasanna.sltuseageapp.Utilities.PrintLog;

import java.util.ArrayList;

/**
 * Created by prasanna on 6/11/17.
 */

public class UserDAO extends DAO {
    private Context context;
    private String tableName = "user";
    private String command;

    public UserDAO(Context context) {
        super(context);
        this.context = context;
    }

    public void addUser(User user){
        ContentValues cv = new ContentValues();
        cv.put("user_index",user.getUser_id());
        cv.put("user_status",user.getUser_status());
        cv.put("password", user.getPassword());
        PrintLog.print("User: " + user.getUser_id() + ", successfully added to the database","UserDAO");
        sqldb.insert(tableName,null,cv);
    }

    public Boolean isUserExist(String user_index){
        command = "SELECT * FROM "+tableName+" WHERE user_index=\""+ user_index + "\";";
        PrintLog.print("Check for user availability", "UserDAO");
        Cursor c = sqldb.rawQuery(command,null);
        if(c.getCount()>0){
            c.close();
            return true;
        }else{
            c.close();
            return false;
        }
    }

    public boolean isDBEmpty(){
        command = "SELECT * FROM "+tableName+" WHERE 1;";
        PrintLog.print("Check for user availability", "UserDAO");
        Cursor c = sqldb.rawQuery(command,null);
        if(c.getCount()>0){
            c.close();
            return false;
        }else{
            c.close();
            return true;
        }
    }

    public void updateStatus(String user_index){
        command = "UPDATE " + tableName + " SET user_status=\"0\";";
        PrintLog.print("Update status of all users to zero", "UserDAO");
        sqldb.execSQL(command);
        command = "UPDATE " + tableName + " SET user_status=\"1\" WHERE user_index=\"" + user_index + "\";";
        PrintLog.print("Update status of users [" + user_index + "] to one", "UserDAO");
        sqldb.execSQL(command);
    }

    public ArrayList<User> getUserArray(){
        command = "SELECT * FROM "+tableName+" WHERE 1;";
        PrintLog.print("Get all users to array ["+ command + "]", "UserDAO");
        Cursor c = sqldb.rawQuery(command,null);
        PrintLog.print("Cursor count :" + String.valueOf(c.getCount()), "UserDAO");
        ArrayList<User> arrUser = new ArrayList<>();
        User user;
        if(c.moveToFirst()) {
            do {
                user = new User(
                        c.getString(c.getColumnIndex("user_index")),
                        EncryptDecrypt.decrypt(c.getString(c.getColumnIndex("password"))),
                        c.getString(c.getColumnIndex("user_status")));
                arrUser.add(user);
            } while (c.moveToNext());
        }
        return arrUser;
    }

    public User getUser(String user_index){
        command = "SELECT * FROM "+tableName+" WHERE user_index=\"" + user_index + "\";";
        PrintLog.print("get user ["+ command + "]", "UserDAO");
        Cursor c = sqldb.rawQuery(command,null);
        PrintLog.print("Cursor count :" + String.valueOf(c.getCount()), "UserDAO");
        User user;
        if(c.moveToFirst()) {
            do {
                user = new User(
                        c.getString(c.getColumnIndex("user_index")),
                        EncryptDecrypt.decrypt(c.getString(c.getColumnIndex("password"))),
                        c.getString(c.getColumnIndex("user_status")));
                return user;
            } while (c.moveToNext());
        }
        return null;
    }
}

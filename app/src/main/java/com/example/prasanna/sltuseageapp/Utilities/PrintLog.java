package com.example.prasanna.sltuseageapp.Utilities;

import android.util.Log;

import com.example.prasanna.sltuseageapp.Constants.Constants;

/**
 * Created by prasanna on 6/10/17.
 */

public abstract class PrintLog {
    public static void print(String message, String sender){
        Log.i(Constants.TAG,"[" + sender + "] " + message);
    }
}

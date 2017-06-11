package com.example.prasanna.sltuseageapp.Interfaces;

import android.graphics.Bitmap;
import android.support.annotation.StringRes;

/**
 * Created by prasanna on 6/10/17.
 */

public interface LoginListener {
    //Called every time a new captcha is loaded
    void onLoadCaptcha(Bitmap bitmap);

    //Called when the usage page HTML is finished phasing
    void onReceiveHtml(String html);

    //Returns when server specific login errors
    void onLoginError(String error);

    //Called when login state changes.
    void onChangeState(int state);

    //Called to hide progressDialog
    void onHideShowProgressDialog(boolean state, String message);
}

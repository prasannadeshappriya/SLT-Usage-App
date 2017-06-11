package com.example.prasanna.sltuseageapp.Constants;

/**
 * Created by prasanna on 6/10/17.
 */

public abstract class Constants {
    //Tag for debugging purposes
    public final static String TAG = "tag";

    //Urls
    public final static String LOGIN_URL = "https://www.internetvas.slt.lk/SLTVasPortal-war/application/login.nable";
    public final static String HOME_URL = "https://www.internetvas.slt.lk/SLTVasPortal-war/application/home.nable";
    public final static String USEAGE_URL = "https://www.internetvas.slt.lk/SLTVasPortal-war/application/usage.nable";
    public final static String LOGIN_REDIRCT_URL = "https://www.internetvas.slt.lk/SLTVasPortal-war/application/index.jsp?page=usage";
    public final static String BASE_URL = "https://www.internetvas.slt.lk";
    public final static String LOGOUT_URL = "https://www.internetvas.slt.lk/SLTVasPortal-war/application/logout.nable";

    //Scrapping
    public final static Double MAX_HEIGHT = 171.428571429;
    public final static int SCREEN_FACTOR = 110;

    //Database
    public final static String DATABASE_NAME = "slt_usage";
    public final static int DATABASE_VERSION = 1;

}

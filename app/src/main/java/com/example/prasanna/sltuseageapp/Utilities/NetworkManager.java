package com.example.prasanna.sltuseageapp.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by prasanna on 6/10/17.
 */

public class NetworkManager {
    public static String getCookies() {
        android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
        return cookieManager.getCookie("https://www.internetvas.slt.lk/SLTVasPortal-war/");
    }
}

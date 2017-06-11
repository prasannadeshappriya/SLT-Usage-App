package com.example.prasanna.sltuseageapp.Utilities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.prasanna.sltuseageapp.Constants.Constants;
import com.example.prasanna.sltuseageapp.Interfaces.LoginListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by prasanna on 6/10/17.
 */

public class WebService {
    private WebView WebViewer;
    private Context context;
    private LoginListener loginListener;
    private String captcha_url = null;

    public WebService(Context context, WebView webViewer, LoginListener loginListener) {
        this.context = context;
        WebViewer = webViewer;
        this.loginListener = loginListener;

        //Initialize WebViewer
        initWebViewer();

        //Initialize Information
        init();
    }

    private void init() {
        PrintLog.print("Initializing..","WebService");
        //Check Internet Connectivity
        WebViewer.loadUrl(Constants.LOGOUT_URL);
        //Send message back saying initializing
        loginListener.onChangeState(3);
    }

    public void logout() {
        WebViewer.loadUrl(Constants.LOGOUT_URL);
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initWebViewer() {
        //Initial Settings for WebViewer
        WebViewer.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        WebViewer.getSettings().setSaveFormData(false);
        WebViewer.getSettings().setLoadsImagesAutomatically(false);
        WebViewer.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT <= 18)
            WebViewer.getSettings().setSavePassword(false);

        WebViewer.addJavascriptInterface(new HtmlHandler(), "HtmlHandler");

        WebViewer.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                PrintLog.print("Url: " + url + " is loaded","initWebViewer");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                PrintLog.print("Url: " + url + " is finished loading","initWebViewer");

                switch (url){
                    case Constants.LOGOUT_URL:
                        WebViewer.loadUrl(Constants.LOGIN_REDIRCT_URL);
                        break;
                    case Constants.LOGIN_URL:
                        //Calling HtmlHandler Functions
                        WebViewer.loadUrl("javascript:HtmlHandler.checkStatus(document.getElementsByClassName('login_inv_message_text')[0].innerHTML);");
                        WebViewer.loadUrl("javascript:HtmlHandler.receivedLoginHtml(document.documentElement.outerHTML);");
                        break;
                    case Constants.HOME_URL:
                        WebViewer.loadUrl(Constants.USEAGE_URL);
                        break;
                    case Constants.USEAGE_URL:
                        //Call HtmlHandler function to scrap the page
                        WebViewer.loadUrl("javascript:HtmlHandler.receivedUsageHtml(document.documentElement.outerHTML);");
                        break;
                }
            }

            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                final boolean[] con = {false};
                class SSLAlertBuilder extends AlertDialog.Builder {
                    public SSLAlertBuilder(Context context) {
                        super(context);
                        setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                handler.proceed();
                            }
                        });
                        setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                handler.cancel();
                            }
                        });
                    }
                }

                if (!con[0]) {
                    //Handling SSL
                    loginListener.onChangeState(2);
                    handler.proceed();
                } else {
                    new SSLAlertBuilder(context).show();
                }
            }
        });
    }

    /**
     * Class to hold javascript interfaces
     * JavaScript runs in another thread and not in the thread in which it was constructed.
     */
    public class HtmlHandler {
        @JavascriptInterface
        public void receivedUsageHtml(String html) {
            loginListener.onReceiveHtml(html);
        }
        @JavascriptInterface
        public void receivedLoginHtml(String html) {
            getCaptcha(getCaptchaUrl(html));
        }
        /**
         * The object that is bound to your JavaScript runs in another thread
         * and not in the thread in which it was constructed.
         */
        @JavascriptInterface
        public void checkStatus(final String loginStatusMsg) {
            if (!loginStatusMsg.equals("")) {
                new Handler(context.getMainLooper()).post((new Runnable() {
                    @Override
                    public void run() {
                        loginListener.onLoginError(loginStatusMsg);
                    }
                }));
            }
        }
    }

    public void getCaptcha(String url) {
        class LoadCaptchaTask extends AsyncTask<String, Void, Bitmap> {
            @Override
            protected Bitmap doInBackground(String... urls) {
                try {
                    URL url = new URL(urls[0]);
                    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                    urlConnection.setRequestProperty("Cookie", NetworkManager.getCookies());
                    urlConnection.setSSLSocketFactory(SSLTrustManager.setup(context));
                    return BitmapFactory.decodeStream(urlConnection.getInputStream());
                } catch (Exception e) {
                    PrintLog.print(e.toString(),"getCaptcha");
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                // send the loaded bitmap to the listeners
                loginListener.onLoadCaptcha(bitmap);
            }
        }
        new LoadCaptchaTask().execute(url);
    }

    public String getCaptchaUrl(String html) {
        Document doc = Jsoup.parse(html);
        doc.setBaseUri(Constants.BASE_URL);
        captcha_url = doc.select("img[src*=/SLTVasPortal-war/javax.faces.resource/dynamiccontent]").attr("abs:src");
        return captcha_url;
    }

    public void submit(String username, String password, String captcha) {
        if (Constants.LOGIN_URL.equals(WebViewer.getUrl())) {
            // if all fields are filled
            if (username.equals("") || password.equals("") || captcha.equals("")) {
                loginListener.onLoginError("All Field Are required");
            } else {
                WebViewer.loadUrl("javascript:document.getElementsByName('loginfrm:chngusername')[0].value='" + username + "';void(0);");
                WebViewer.loadUrl("javascript:document.getElementsByName('loginfrm:chngpassword')[0].value='" + password + "';void(0);");
                WebViewer.loadUrl("javascript:document.getElementsByName('loginfrm:chngcaptchs')[0].value='" + captcha + "';void(0);");
                WebViewer.loadUrl("javascript:document.getElementById('loginfrm:logbtn').click();");

                loginListener.onChangeState(3);
            }
        } else {
            init();
        }
    }
}

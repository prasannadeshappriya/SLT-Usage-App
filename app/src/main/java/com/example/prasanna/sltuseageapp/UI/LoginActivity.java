package com.example.prasanna.sltuseageapp.UI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.prasanna.sltuseageapp.DAO.UserDAO;
import com.example.prasanna.sltuseageapp.Interfaces.LoginListener;
import com.example.prasanna.sltuseageapp.Models.User;
import com.example.prasanna.sltuseageapp.R;
import com.example.prasanna.sltuseageapp.Utilities.EncryptDecrypt;
import com.example.prasanna.sltuseageapp.Utilities.PrintLog;
import com.example.prasanna.sltuseageapp.Utilities.WebService;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private AutoCompleteTextView etUsername;
    private EditText etPassword;
    private EditText etCaptcha;
    private ImageButton btnSignIn;
    private ImageView imgCaptcha;
    private WebView webViewer;
    private ProgressDialog pd;
    private boolean startup = false;
    private ArrayList<User> arrUser;
    private UserDAO user_dao;
    private RelativeLayout relLayoutLogin;
    private WebService service;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialize Everything
        init();

        //Initialize Autocomplete texts and user array
        initDatabaseUsers();

        //Create Web Service Instance [First check the internet connection]
        CheckInternetAccess internetAccess = new CheckInternetAccess(0);
        internetAccess.execute();

        etUsername.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String name = parent.getAdapter().getItem(position).toString();
                        boolean con = true;
                        for(User user: arrUser){
                            if(user.getUser_id().equals(name)){
                                con = false;
                                etPassword.setText(user.getPassword());
                                etCaptcha.requestFocus();
                                break;
                            }
                        }
                        if(con){etPassword.requestFocus();}
                    }
                }
        );

        btnSignIn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        password = EncryptDecrypt.encrypt(etPassword.getText().toString());
                        CheckInternetAccess internetAccess = new CheckInternetAccess(1);
                        internetAccess.execute();
                    }
                }
        );

        imgCaptcha.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckInternetAccess internetAccess = new CheckInternetAccess(0);
                        internetAccess.execute();
                    }
                }
        );

        relLayoutLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeKeyboard();
                    }
                }
        );

        etUsername.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void afterTextChanged(Editable s) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(s.toString().replace(" ","").equals("")){
                            etPassword.setText("");
                        }else {
                            if (!user_dao.isDBEmpty()) {
                                if (user_dao.isUserExist(s.toString())) {
                                    User user = user_dao.getUser(s.toString());
                                    if (user == null) {
                                        etPassword.setText("");
                                    } else {
                                        etPassword.setText(user.getPassword());
                                    }
                                }else{
                                    etPassword.setText("");
                                }
                            }
                        }
                    }
                }
        );
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void initializeWebService(){
         service = new WebService(
                this, webViewer,
                new LoginListener() {
                    @Override
                    public void onLoadCaptcha(Bitmap bitmap) {
                        PrintLog.print("onLoadCaptcha method triggered","WebServerClass");
                        imgCaptcha.setImageBitmap(bitmap);
                        etCaptcha.setHint("Captcha Code");
                        startup = false;
                        pd.dismiss();
                    }

                    @Override
                    public void onReceiveHtml(String html) {
                        PrintLog.print("onReceiveHtml method triggered","WebServerClass");
                        if(user_dao.isUserExist(etUsername.getText().toString().toLowerCase())) {
                            user_dao.updateStatus(etUsername.getText().toString().toLowerCase());
                        }else{
                            user_dao.addUser(new User(
                                    etUsername.getText().toString().toLowerCase(),
                                    password,
                                    "0"
                            ));
                            user_dao.updateStatus(etUsername.getText().toString().toLowerCase());
                        }
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        i.putExtra("HTML_CODE", html);
                        i.putExtra("USER_ID", etUsername.getText().toString().toLowerCase());
                        pd.dismiss();
                        startActivity(i);
                    }

                    @Override
                    public void onLoginError(String error) {
                        PrintLog.print("onLoginError method triggered","WebServerClass");
                        if(!startup) {
                            Toast.makeText(getApplicationContext(), error.replace("!", "").replace(".", ""), Toast.LENGTH_LONG).show();
                        }
                        pd.dismiss();
                    }

                    @Override
                    public void onChangeState(int state) {
                        PrintLog.print("onChangeState method triggered","WebServerClass");
                    }

                    @Override
                    public void onHideShowProgressDialog(boolean state, String message) {
                        if(state){
                            pd.dismiss();
                        }else{
                            showProcessDialog(message);
                        }
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    private void initDatabaseUsers() {
        ArrayList<String> arrUsername = new ArrayList<>();
        if(!user_dao.isDBEmpty()){
            arrUser = user_dao.getUserArray();
            for(User user : arrUser){
                arrUsername.add(user.getUser_id());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrUsername);
            etUsername.setAdapter(adapter);

            boolean con = true;
            for(int i=0; i<arrUser.size(); i++){
                if(arrUser.get(i).getUser_status().equals("1")){
                    con = false;
                    etUsername.setText(arrUser.get(i).getUser_id());
                    etPassword.setText(arrUser.get(i).getPassword());
                    etCaptcha.requestFocus();
                    break;
                }
            }
            if(con){
                if(arrUser.size()>0){
                    etUsername.setText(arrUser.get(0).getUser_id());
                    etPassword.setText(arrUser.get(0).getPassword());
                    etCaptcha.requestFocus();
                }
            }

        }
    }

    private void showProcessDialog(String message){
        pd.setIndeterminate(false);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage(message);
        pd.show();
    }

    private void init() {
        startup = true;
        pd = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        arrUser = new ArrayList<>();
        user_dao = new UserDAO(this);
        etUsername = (AutoCompleteTextView) findViewById(R.id.username);
        etCaptcha = (EditText)findViewById(R.id.captcha);
        etPassword = (EditText)findViewById(R.id.password);
        webViewer = (WebView) findViewById(R.id.web_view);
        imgCaptcha = (ImageView) findViewById(R.id.captcha_view);
        btnSignIn = (ImageButton) findViewById(R.id.login_button);
        relLayoutLogin = (RelativeLayout) findViewById(R.id.relLayoutLogin);
    }

    private class CheckInternetAccess extends AsyncTask<Void,Void,Void> {
        private boolean con;
        private int Method;
        public CheckInternetAccess(int Method){
            this.Method = Method;
        }
        @Override
        protected void onPreExecute() {
            con = false;
            showProcessDialog("Connecting ..");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Ping is not working for emulator
            //Check weather the app is running on emulator or not
            if(Build.PRODUCT.matches(".*_?sdk_?.*")){
                con = true;
            }else {
                Runtime runtime = Runtime.getRuntime();
                try {
                    Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                    int exitValue = ipProcess.waitFor();
                    con = (exitValue == 0);
                } catch (Exception e) {
                    PrintLog.print("Error: " + e.toString(), "Internet Connection Verify");
                    con = false;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(con) {
                if(Method==0) {
                    PrintLog.print("Internet Connection Available", "Internet Connection Verify");
                    pd.setMessage("Initializing ...");
                    initializeWebService();
                }else if(Method==1){
                    pd.setMessage("Authenticating ...");
                    service.submit(etUsername.getText().toString().toLowerCase(),
                            etPassword.getText().toString(),
                            etCaptcha.getText().toString());
                }
            }else{
                PrintLog.print("Internet Connection Available", "Internet Connection Verify");
                pd.dismiss();
                Toast.makeText(getApplicationContext(),"No internet connection !", Toast.LENGTH_LONG).show();
            }
        }
    }
}

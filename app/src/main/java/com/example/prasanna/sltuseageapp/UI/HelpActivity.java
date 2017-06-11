package com.example.prasanna.sltuseageapp.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.prasanna.sltuseageapp.R;
import com.example.prasanna.sltuseageapp.UI.HomeActivity;

public class HelpActivity extends AppCompatActivity {
    private String HTML;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        final Bundle params = getIntent().getExtras();
        if(params!=null){
            HTML = params.getString("HTML_CODE");
            userID = params.getString("USER_ID");
        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        i.putExtra("HTML_CODE", HTML);
        i.putExtra("USER_ID", userID);
        startActivity(i);
    }
}

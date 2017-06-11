package com.example.prasanna.sltuseageapp.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.prasanna.sltuseageapp.Adapter.UsageAdapter;
import com.example.prasanna.sltuseageapp.Constants.Constants;
import com.example.prasanna.sltuseageapp.Models.UsageStatistics;
import com.example.prasanna.sltuseageapp.R;
import com.example.prasanna.sltuseageapp.Utilities.PrintLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private String HTML;
    private String userID;

    private String remain_total;
    private String remain_peak;
    private String total_height;
    private String peak_height;

    private ListView lstUsageStat;
    private TextView btnHelp;
    private ImageButton btnSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        final Bundle params = getIntent().getExtras();
        if(params!=null){
            HTML = params.getString("HTML_CODE");
            userID = params.getString("USER_ID");
        }

        //Initialize
        lstUsageStat = (ListView) findViewById(R.id.lstUsageStat);
        btnHelp = (TextView)findViewById(R.id.btnHelp);
        btnSignOut = (ImageButton) findViewById(R.id.log_out_button);
        initializeVeriables();

        ArrayList<UsageStatistics> arrayList = initListArray();
        UsageAdapter adapter = new UsageAdapter(this,arrayList);
        lstUsageStat.setAdapter(adapter);

        btnHelp.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), HelpActivity.class);
                        i.putExtra("HTML_CODE", HTML);
                        i.putExtra("USER_ID", userID);
                        startActivity(i);
                    }
                }
        );

        btnSignOut.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                    }
                }
        );
    }

    private ArrayList<UsageStatistics> initListArray(){
        ArrayList<UsageStatistics> arrUsage = new ArrayList<>();

        Double remainPeak = Double.parseDouble(remain_peak);
        Double remainTotal = Double.parseDouble(remain_total);
        Double peakHeight = Double.parseDouble(peak_height);
        Double totalHeight = Double.parseDouble(total_height);

        Double peak_available_percentage = (peakHeight/Constants.MAX_HEIGHT)*100;
        Double total_peak_calc = (remainPeak/peak_available_percentage)*100;
        Double used_peak_calc = (total_peak_calc - remainPeak);
        arrUsage.add(new UsageStatistics(
                used_peak_calc,
                total_peak_calc,
                "PEAK USAGE",
                (total_peak_calc-used_peak_calc),
                total_peak_calc));

        Double total_available_percentage = (totalHeight/Constants.MAX_HEIGHT)*100;
        Double total_calc = (remainTotal/total_available_percentage)*100;
        Double total_used_calc = total_calc - remainTotal;

        Double off_peak_total_calc = total_calc - total_peak_calc;
        Double off_peak_used_calc = total_used_calc - used_peak_calc;
        arrUsage.add(new UsageStatistics(
                off_peak_used_calc,
                off_peak_total_calc,
                "OFF-PEAK USAGE",
                (off_peak_total_calc-off_peak_used_calc),
                off_peak_total_calc));
        arrUsage.add(new UsageStatistics(
                total_used_calc,
                total_calc,
                "TOTAL USAGE",
                (total_calc-total_used_calc),
                total_calc));

        return arrUsage;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
    }

    private void initializeVeriables() {
        Document doc = Jsoup.parse(HTML);
        Elements elements; String text;
        elements = doc.getElementsByClass("remain_label");
        for(Element e : elements){
            text = e.ownText().replace("GB","").replace("MB","").replace(" ","");
            if(!text.equals("")){
                remain_total = text;
                PrintLog.print(e.ownText(),"remain_label");
                break;
            }
        }

        elements = doc.getElementsByClass("remain_label_peak");
        for(Element e : elements){
            text = e.ownText().replace("GB","").replace("MB","").replace(" ","");
            if(!text.equals("")) {
                remain_peak = text;
                PrintLog.print(e.ownText(), "remain_label_peak");
                break;
            }
        }

        elements = doc.getElementsByClass("bfill_peak");
        for(Element e : elements){
            text = getHeight(e.toString());
            if(!text.equals("")) {
                peak_height = text;
                PrintLog.print(e.toString(), "bfill_peak");
                break;
            }
        }

        elements = doc.getElementsByClass("bfill");
        for(Element e : elements){
            text = getHeight(e.toString());
            if(!text.equals("")) {
                total_height = text;
                PrintLog.print(e.toString(),"bfill");
                break;
            }
        }
    }

    private String getHeight(String html){
        String htmlArr[] = html.split(" ");
        for(int i=0; i<htmlArr.length; i++){
            if(htmlArr[i].equals("height:")){
                return htmlArr[i+1].replace("px","").replace(";","");
            }
        }
        return "";
    }
}

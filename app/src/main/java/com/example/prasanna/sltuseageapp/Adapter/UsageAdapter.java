package com.example.prasanna.sltuseageapp.Adapter;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.prasanna.sltuseageapp.Constants.Constants;
import com.example.prasanna.sltuseageapp.Models.UsageStatistics;
import com.example.prasanna.sltuseageapp.R;

import java.util.ArrayList;

/**
 * Created by prasanna on 6/11/17.
 */

public class UsageAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<UsageStatistics> arrUsage;

    public UsageAdapter(Context context, ArrayList<UsageStatistics> arrUsage) {
        this.context = context;
        this.arrUsage = arrUsage;
    }

    @Override
    public int getCount() {
        return arrUsage.size();
    }

    @Override
    public Object getItem(int position) {
        return arrUsage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return arrUsage.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.adapter_usage, null);

        EditText etProgressbar = (EditText)v.findViewById(R.id.etProgressbar);
        EditText etProgressbarUsed = (EditText)v.findViewById(R.id.etProgressbarUsed);
        TextView tvAvailable = (TextView) v.findViewById(R.id.adapterAvailable);
        TextView tvUsed = (TextView) v.findViewById(R.id.adapterUsed);
        TextView tvPeriod = (TextView) v.findViewById(R.id.adapterPeriod);
        TextView tvAvailableData = (TextView) v.findViewById(R.id.adapterAvailableData);
        TextView tvTotalData = (TextView) v.findViewById(R.id.adapterTotalData);

        UsageStatistics statistics = arrUsage.get(position);
        Double max_value = statistics.getMaximumValue();
        Double used_value = statistics.getUsedValue();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x - Constants.SCREEN_FACTOR;
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) etProgressbar.getLayoutParams();
        lp.width = width;
        etProgressbar.setLayoutParams(lp);

        Double maxWidth = Double.parseDouble(String.valueOf(width));
        Double usedWidth = ((max_value-used_value)/max_value)*maxWidth;
        lp = (RelativeLayout.LayoutParams) etProgressbarUsed.getLayoutParams();
        lp.width = usedWidth.intValue();
        etProgressbarUsed.setLayoutParams(lp);

        Double available = ((max_value-used_value)/max_value)*100;
        int used = 100 - available.intValue();

        int TotalData = (int) Math.round(statistics.getTotalData());
        int AvailableData = (int) Math.round(statistics.getAvailableData());
        int usedData = TotalData - AvailableData;
        tvAvailableData.setText("Available Data: " + AvailableData + " GB [" + usedData + " GB Used]");
        tvTotalData.setText("Total Data: " + TotalData + " GB");
        tvAvailable.setText(String.valueOf(available.intValue()) + "% Available");
        tvUsed.setText(String.valueOf(used) + "% Used");
        tvPeriod.setText(statistics.getPeriod());

        return v;
    }
}

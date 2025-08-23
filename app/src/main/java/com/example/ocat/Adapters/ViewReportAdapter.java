package com.example.ocat.Adapters;

import static android.view.View.GONE;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ocat.GraphPage;
import com.example.ocat.R;

import java.util.ArrayList;

public class ViewReportAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ArrayList<String>> parentList;
    private ArrayList<String> fileName;

    ArrayList<String> values = new ArrayList<>();


    public ViewReportAdapter(Context context, ArrayList<ArrayList<String>> parentList, ArrayList<String> fileName) {
        this.context = context;
        this.parentList = parentList;
        this.fileName = fileName;

    }

    @Override
    public int getCount() {
        return fileName.size();
    }

    @Override
    public Object getItem(int position) {
        return fileName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_report, parent, false);
        }

        LinearLayout layout = convertView.findViewById(R.id.layout);
        TextView txt_filename = convertView.findViewById(R.id.txtFilename);
        TextView txt_date = convertView.findViewById(R.id.txtDate);

        txt_filename.setText("OCAT_Report_"+fileName.get(i)+".pdf");
        txt_date.setText(fileName.get(i));

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                values = parentList.get(i);
                String category = fileName.get(i);
                Intent intent = new Intent(context, GraphPage.class);
                intent.putStringArrayListExtra("values", values);
                intent.putExtra("category", category);
                context.startActivity(intent);
            }
        });


        return convertView;
    }
}

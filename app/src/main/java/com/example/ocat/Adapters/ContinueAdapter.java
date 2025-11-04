package com.example.ocat.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ocat.AssessmentPage;
import com.example.ocat.AssessmentPage2;
import com.example.ocat.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class ContinueAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> category_id;
    private ArrayList<String> category_name;
    private ArrayList<String> category_question;
    private ArrayList<String> user_response;

    public ContinueAdapter(Context context, ArrayList<String> category_id, ArrayList<String> category_name, ArrayList<String> category_question, ArrayList<String> user_response) {
        this.context = context;
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_question = category_question;
        this.user_response = user_response;
    }

    @Override
    public int getCount() {
        return category_id.size();
    }

    @Override
    public Object getItem(int position) {
        return category_id.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_paused_assessment, parent, false);
        }


        LinearLayout layout = convertView.findViewById(R.id.layout);
        TextView txtFileName = convertView.findViewById(R.id.txtFilename);
        TextView txtTotalQue = convertView.findViewById(R.id.txtTotalQue);
        TextView txtAnsQue = convertView.findViewById(R.id.txtAnsQue);
        TextView txtRemQue = convertView.findViewById(R.id.txtRemQue);

        txtFileName.setText(category_name.get(position));

        // remove [ ] then split
        String category__quest = category_question.get(position).substring(1, category_question.get(position).length() - 1).trim();
        // Regex: split on comma + space only if followed by uppercase letter
        String[] items1 = category__quest.split(", (?=[A-Z])");
        ArrayList<String> cat_que = new ArrayList<>(Arrays.asList(items1));
        txtTotalQue.setText(String.valueOf(cat_que.size()));

        String user__resp = user_response.get(position).replace("[", "").replace("]", "");
        ArrayList<String> ans_que = new ArrayList<>(Arrays.asList(user__resp.split(",")));
        txtAnsQue.setText(String.valueOf(ans_que.size()));

        txtRemQue.setText(String.valueOf(cat_que.size() - ans_que.size()));

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //continue assessment
                //get options first
                Intent intent = new Intent(context, AssessmentPage2.class);
                intent.putExtra("category_id", category_id.get(position));
                intent.putExtra("category_name", category_name.get(position));
                intent.putStringArrayListExtra("category_question", cat_que);
                intent.putStringArrayListExtra("answered_question", ans_que);
                context.startActivity(intent);
            }
        });

        return convertView;
    }


}

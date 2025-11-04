package com.example.ocat;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ocat.Adapters.CategoryAdapter;
import com.example.ocat.Adapters.ViewReportAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ViewReports extends AppCompatActivity {

    ImageView back;
    GridView gridView;
    LinearLayout loader;
    LinearLayout noreport;
    // Declare globally or in enclosing method
    int totalRequests = 8;
    int completedRequests = 0;
    ArrayList<ArrayList<String>> parentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reports);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ChooseLanguage.loadLanguage(this);

        //get the user id from shared preference
        SharedPreferences sharedPreferences = getSharedPreferences("Login Pref", Context.MODE_PRIVATE);
        String got_user_id = sharedPreferences.getString("id", null);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewReports.this, Dashboard.class));
            }
        });
        gridView = findViewById(R.id.gridview);
        loader = findViewById(R.id.loader);
        noreport = findViewById(R.id.noreport);

        // before the loop
        parentList = new ArrayList<>(Collections.nCopies(8, null));  // 8 empty slots

        for (int i = 0; i < 8; i++) {
            final int index = i;

            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://kwamea19.sg-host.com/WACSI_OCAT/get_report.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                String message = json.getString("message");

                                JSONArray jsonArray = new JSONArray(message);
                                ArrayList<String> slicedValues = new ArrayList<>();

                                if (jsonArray.length() > 0) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                                    // extract values
                                    ArrayList<Object> values = new ArrayList<>();
                                    Iterator<String> iterator = jsonObject.keys();
                                    while (iterator.hasNext()) {
                                        String key = iterator.next();
                                        values.add(jsonObject.get(key));
                                    }

                                    for (int j = 2; j <= values.size() - 2; j++) {
                                        slicedValues.add((String) values.get(j));
                                    }
                                } else {
                                    slicedValues.add("");
                                }

                                // ✅ Insert into correct index
                                parentList.set(index, slicedValues);

                                completedRequests++;

                                // ✅ When all are done → continue process and populate GridView
                                if (completedRequests == totalRequests) {
                                    populateGridView(parentList);
                                }
//                                System.out.println("Response for index " + index + " → Parent List: " + parentList);

                            } catch (Exception e) {
                                Toast.makeText(ViewReports.this, R.string.report_loading_failed, Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.e(TAG, "Network Error " + volleyError);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("category_id", String.valueOf(index + 1));
                    params.put("user_id", got_user_id);
                    return params;
                }
            };

            Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
        }

        System.out.println("Parent List: " + parentList);
    }


    private void populateGridView(ArrayList<ArrayList<String>> parentList) {
        loader.setVisibility(GONE);
        //get the categories
        ArrayList<String> catName = new ArrayList<>();

        //get all the categories in an array with their pictures
        Dialog myDialog = new Dialog(ViewReports.this);
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText(R.string.checking_reports_available);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://kwamea19.sg-host.com/WACSI_OCAT/categories.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();
                        System.out.println("Categories response: " + response);

                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("status");
                            String message = json.getString("message");

                            JSONArray jsonArray = new JSONArray(message);
                            int len = jsonArray.length();
                            for (int i=0; i<len; i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String categoryId = jsonObject.getString("id");
                                String categoryName = jsonObject.getString("category_name");
                                String categoryImage = jsonObject.getString("image_url");
                                String newString = categoryImage.replace("localhost", "kwamea19.sg-host.com");

                                catName.add(categoryName);
                            }

                            //filter parentList to remove empty slots
                            ArrayList<ArrayList<String>> filteredParentList = new ArrayList<>();
                            ArrayList<String> filteredFileName = new ArrayList<>();
                            for (int i = 0; i < parentList.size(); i++) {
                                if (!(parentList.get(i).size() == 1)) {
                                    filteredParentList.add(parentList.get(i));
                                    filteredFileName.add(catName.get(i));
                                }
                            }

                            System.out.println("Filtered Parent List: " + filteredParentList+"\nFiltered File Name: "+filteredFileName);

                            if (filteredParentList.isEmpty()){
                                //there is no report to show
                                gridView.setVisibility(GONE);
                                noreport.setVisibility(VISIBLE);
                            }else{
                                //report(s) is available
                                ViewReportAdapter viewReportAdapter = new ViewReportAdapter(ViewReports.this, filteredParentList, filteredFileName);
                                gridView.setAdapter(viewReportAdapter);
                                gridView.setVisibility(VISIBLE);
                            }

                        }catch(Exception e) {
                            Toast.makeText(ViewReports.this, R.string.categories_loading_failed, Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        myDialog.dismiss();
                        if(volleyError == null){
                            return;
                        }
                        Log.e(TAG, volleyError.toString());
                        System.out.println("Network Error "+volleyError);
                        Toast.makeText(ViewReports.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        //do nothing
    }
}
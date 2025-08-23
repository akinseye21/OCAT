package com.example.ocat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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
import android.widget.RelativeLayout;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Completion extends AppCompatActivity {

    TextView categoryName;
    String category;
    String categoryId;

    RelativeLayout btn_view_assessment, btn_view_recommendations, btn_dashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completion);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //get the user id from shared preference
        SharedPreferences sharedPreferences = getSharedPreferences("Login Pref", Context.MODE_PRIVATE);
        String got_user_id = sharedPreferences.getString("id", null);

        Intent i = getIntent();
        category = i.getStringExtra("category");
        categoryId = i.getStringExtra("categoryId");

        categoryName = findViewById(R.id.categoryName);
        categoryName.setText("'"+category+"'");

        btn_dashboard = findViewById(R.id.btn_dashboard);
        btn_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Completion.this, Dashboard.class));
            }
        });

        btn_view_assessment = findViewById(R.id.btn_view_assessment);
        btn_view_assessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog myDialog = new Dialog(Completion.this);
                myDialog.setContentView(R.layout.custom_popup_loading);
                TextView text = myDialog.findViewById(R.id.text);
                text.setText("Loading report... Please wait");
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(false);
                myDialog.show();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://10.144.181.184/WACSI_OCAT/get_report.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                myDialog.dismiss();
                                System.out.println("Report response: " + response);

                                try {
                                    JSONObject json = new JSONObject(response);
                                    String status = json.getString("status");
                                    String message = json.getString("message");

                                    JSONArray jsonArray = new JSONArray(message);
                                    int len = jsonArray.length();
                                    if (len>0){
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        //count number of items
                                        int count = jsonObject.length();
                                        // Get all values into a list
                                        ArrayList<Object> values = new ArrayList<>();
                                        Iterator<String> iterator = jsonObject.keys();
                                        while (iterator.hasNext()) {
                                            String key = iterator.next();
                                            values.add(jsonObject.get(key));
                                        }

                                        // Get values from index 2 to count-1
                                        ArrayList<String> slicedValues = new ArrayList<>();
                                        for (int i = 2; i <= count-2 && i < values.size(); i++) {
                                            slicedValues.add((String) values.get(i));
                                        }
                                        System.out.println("Values from index 2 to count-1: " + slicedValues);

                                        //work with sliced value
                                        Intent j = new Intent(Completion.this, GraphPage.class);
                                        j.putStringArrayListExtra("values", slicedValues);
                                        j.putExtra("category", category);
                                        startActivity(j);
                                    }



                                }catch(Exception e) {
                                    Toast.makeText(Completion.this, "Report loading failed", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(Completion.this, "Network Error!", Toast.LENGTH_SHORT).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams(){
                        Map<String, String> params = new HashMap<>();
                        params.put("category_id", categoryId);
                        params.put("user_id", got_user_id);
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
        });

    }
}
package com.example.ocat;

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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StartAssessment extends AppCompatActivity {

    ImageView back;
    ListView listviewCategory;
    ArrayList<ArrayList<String>> parentList;
    String got_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_assessment);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ChooseLanguage.loadLanguage(this);

        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "en");
        // Print to log or console
//        System.out.println("Selected Language: " + language);

        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("Login Pref", Context.MODE_PRIVATE);
        got_user_id = sharedPreferences.getString("id", null);

        listviewCategory = findViewById(R.id.listviewCategory);

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            startActivity(new Intent(StartAssessment.this, Dashboard.class));
        });


        ArrayList<String> catId = new ArrayList<>();
        ArrayList<String> catName = new ArrayList<>();
        ArrayList<String> catImage = new ArrayList<>();

        catId.clear();
        catName.clear();
        catImage.clear();

        //get all the categories in an array with their pictures
        Dialog myDialog = new Dialog(StartAssessment.this);
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText(R.string.loading_categories);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://kwamea19.sg-host.com/WACSI_OCAT/categories.php",
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

                                catId.add(categoryId);
                                catName.add(categoryName);
                                catImage.add(newString);

                            }

                            checker();

//                            System.out.println("Category Id = "+catId);
                            CategoryAdapter categoryAdapter = new CategoryAdapter(StartAssessment.this, catId, catName, catImage);
                            listviewCategory.setAdapter(categoryAdapter);

                        }catch(Exception e) {
                            Toast.makeText(StartAssessment.this, R.string.categories_loading_failed, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(StartAssessment.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("language", language);
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

    public void checker(){

        ArrayList<String> checkerList = new ArrayList<>();

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


                            } catch (Exception e) {
                                Toast.makeText(StartAssessment.this, R.string.report_loading_failed, Toast.LENGTH_SHORT).show();
                            }


                            System.out.println("Parent List: " + parentList);


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

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        //do nothing
    }
}
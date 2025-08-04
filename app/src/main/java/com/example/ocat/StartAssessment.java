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
import java.util.HashMap;
import java.util.Map;

public class StartAssessment extends AppCompatActivity {

    ImageView back;
    ListView listviewCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_assessment);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        listviewCategory = findViewById(R.id.listviewCategory);

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            finish();
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

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://192.168.0.192/WACSI_OCAT/categories.php",
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
                                String newString = categoryImage.replace("localhost", "192.168.0.192");

                                catId.add(categoryId);
                                catName.add(categoryName);
                                catImage.add(newString);

                            }

                            CategoryAdapter categoryAdapter = new CategoryAdapter(StartAssessment.this, catId, catName, catImage);
                            listviewCategory.setAdapter(categoryAdapter);

                        }catch(Exception e) {
                            Toast.makeText(StartAssessment.this, "Categories loading failed", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(StartAssessment.this, "Network Error!", Toast.LENGTH_SHORT).show();
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
}
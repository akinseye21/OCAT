package com.example.ocat.Adapters;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.ocat.AssessmentPage;
import com.example.ocat.Dashboard;
import com.example.ocat.Login;
import com.example.ocat.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoryAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> categoryId;
    private ArrayList<String> categoryName;
    private ArrayList<String> categoryImage;

    public CategoryAdapter(Context context, ArrayList<String> categoryId, ArrayList<String> categoryName, ArrayList<String> categoryImage) {
        this.context = context;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
    }



    @Override
    public int getCount() {
        return categoryName.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_module, parent, false);
        }

        LinearLayout lin_module = convertView.findViewById(R.id.module);
        TextView txt_categoryName = convertView.findViewById(R.id.txt);
        ImageView img_categoryImage = convertView.findViewById(R.id.img);

        txt_categoryName.setText(categoryName.get(position));
        Glide.with(context)
                .load(categoryImage.get(position))
                .into(img_categoryImage);
        lin_module.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the category id and pass it to the next activity
                String selected_id = categoryId.get(position);
                String selected_category = categoryName.get(position);

                //send to the question DB
                getQuestions(selected_id, selected_category);
            }
        });

        return convertView;
    }

    private void getQuestions(String selectedId, String selectedCategory) {

        ArrayList<String> arr_questionId = new ArrayList<>();
        ArrayList<String> arr_questionCategory = new ArrayList<>();
        ArrayList<String> arr_questionText = new ArrayList<>();

        arr_questionId.clear();
        arr_questionCategory.clear();
        arr_questionText.clear();

        Dialog myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText(R.string.getting_questions);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://10.19.71.184/WACSI_OCAT/cat_questions.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();
                        System.out.println("Question response: " + response);

                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("status");
                            String message = json.getString("message");

                            JSONArray jsonArray = new JSONArray(message);
                            int len = jsonArray.length();
                            for (int i=0; i<len; i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String questionId = jsonObject.getString("id");
                                String questionCategory = jsonObject.getString("category");
                                String questionText = jsonObject.getString("question");

                                arr_questionId.add(questionId);
                                arr_questionCategory.add(questionCategory);
                                arr_questionText.add(questionText);
                            }

                            //send to the next activity
                            Intent i = new Intent(context, AssessmentPage.class);
                            i.putStringArrayListExtra("arr_questionId", arr_questionId);
                            i.putStringArrayListExtra("arr_questionCategory", arr_questionCategory);
                            i.putStringArrayListExtra("arr_questionText", arr_questionText);
                            i.putExtra("category", selectedCategory);
                            context.startActivity(i);

                        }catch(Exception e) {
                            Toast.makeText(context, "Question Loading failed", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("id", selectedId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
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

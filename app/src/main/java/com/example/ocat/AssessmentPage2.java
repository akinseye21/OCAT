package com.example.ocat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.Map;

public class AssessmentPage2 extends AppCompatActivity {

    ImageView back;
    TextView txt_category;
    TextView currentquestion, totalquestions;
    TextView question;
    RelativeLayout btn1, btn2, btn3, btn4, btn5;
    TextView option1, option2, option3, option4, option5;
    TextView btn_next;
    String category_id, category_name;
    ArrayList<String> category_question, answered_question;
    Drawable selectedDrawable, unselectedDrawable;
    String uID;

    Integer selected_point = 0;
    String selected_answer = "";

    ArrayList<String> arr_option = new ArrayList<>();
    ArrayList<Integer> arr_point = new ArrayList<>();

    MyDatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_page2);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ChooseLanguage.loadLanguage(this);

        Intent intent = getIntent();
        category_id = intent.getStringExtra("category_id");
        category_name = intent.getStringExtra("category_name");
        category_question = intent.getStringArrayListExtra("category_question");
        answered_question = intent.getStringArrayListExtra("answered_question");

        //get the user id from shared preference
        SharedPreferences sharedPreferences_db = getSharedPreferences("Login Pref", Context.MODE_PRIVATE);
        uID = sharedPreferences_db.getString("id", null);

        myDB = new MyDatabaseHelper(this);

        //get all options
        getOptions();

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        txt_category = findViewById(R.id.category);
        txt_category.setText(category_name);

        currentquestion = findViewById(R.id.currentquestion);
        totalquestions = findViewById(R.id.totalquestions);
        totalquestions.setText(String.valueOf(category_question.size()));

        question = findViewById(R.id.question);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        option5 = findViewById(R.id.option5);
        btn_next = findViewById(R.id.btn_next);

        selectedDrawable = getResources().getDrawable(R.drawable.selected_option);
        unselectedDrawable = getResources().getDrawable(R.drawable.button_grey);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn1.getBackground().getConstantState().equals(unselectedDrawable.getConstantState())) {
                    // Not selected, make it selected
                    btn1.setBackground(selectedDrawable);
                    selected_point = arr_point.get(0);
                    selected_answer = arr_option.get(0);
                    System.out.println("Array answer = "+selected_answer);
                    System.out.println("Array point = "+selected_point);
                    // Unselect others
                    btn2.setBackground(unselectedDrawable);
                    btn3.setBackground(unselectedDrawable);
                    btn4.setBackground(unselectedDrawable);
                    btn5.setBackground(unselectedDrawable);
                } else {
                    // Selected, make it unselected
                    btn1.setBackground(unselectedDrawable);
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn2.getBackground().getConstantState().equals(unselectedDrawable.getConstantState())) {
                    // Not selected, make it selected
                    btn2.setBackground(selectedDrawable);
                    selected_point = arr_point.get(1);
                    selected_answer = arr_option.get(1);
                    System.out.println("Array answer = "+selected_answer);
                    System.out.println("Array point = "+selected_point);
                    // Unselect others
                    btn1.setBackground(unselectedDrawable);
                    btn3.setBackground(unselectedDrawable);
                    btn4.setBackground(unselectedDrawable);
                    btn5.setBackground(unselectedDrawable);
                } else {
                    // Selected, make it unselected
                    btn2.setBackground(unselectedDrawable);
                }
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn3.getBackground().getConstantState().equals(unselectedDrawable.getConstantState())) {
                    // Not selected, make it selected
                    btn3.setBackground(selectedDrawable);
                    selected_point = arr_point.get(2);
                    selected_answer = arr_option.get(2);
                    System.out.println("Array answer = "+selected_answer);
                    System.out.println("Array point = "+selected_point);
                    // Unselect others
                    btn2.setBackground(unselectedDrawable);
                    btn1.setBackground(unselectedDrawable);
                    btn4.setBackground(unselectedDrawable);
                    btn5.setBackground(unselectedDrawable);
                } else {
                    // Selected, make it unselected
                    btn3.setBackground(unselectedDrawable);
                }
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn4.getBackground().getConstantState().equals(unselectedDrawable.getConstantState())) {
                    // Not selected, make it selected
                    btn4.setBackground(selectedDrawable);
                    selected_point = arr_point.get(3);
                    selected_answer = arr_option.get(3);
                    System.out.println("Array answer = "+selected_answer);
                    System.out.println("Array point = "+selected_point);
                    // Unselect others
                    btn2.setBackground(unselectedDrawable);
                    btn3.setBackground(unselectedDrawable);
                    btn1.setBackground(unselectedDrawable);
                    btn5.setBackground(unselectedDrawable);
                } else {
                    // Selected, make it unselected
                    btn4.setBackground(unselectedDrawable);
                }
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn5.getBackground().getConstantState().equals(unselectedDrawable.getConstantState())) {
                    // Not selected, make it selected
                    btn5.setBackground(selectedDrawable);
                    selected_point = arr_point.get(4);
                    selected_answer = arr_option.get(4);
                    System.out.println("Array answer = "+selected_answer);
                    System.out.println("Array point = "+selected_point);
                    // Unselect others
                    btn2.setBackground(unselectedDrawable);
                    btn3.setBackground(unselectedDrawable);
                    btn4.setBackground(unselectedDrawable);
                    btn1.setBackground(unselectedDrawable);
                } else {
                    // Selected, make it unselected
                    btn5.setBackground(unselectedDrawable);
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selected_point == 0){
                    Toast.makeText(AssessmentPage2.this, "Please select an option", Toast.LENGTH_SHORT).show();
                }else{
                    // the selected point, add to an array of points
                    answered_question.add(String.valueOf(selected_point));
                    selected_point = 0;
                    System.out.println("Array final point = "+answered_question);
                    
                    //clear any highlighted button
                    btn1.setBackground(unselectedDrawable);
                    btn2.setBackground(unselectedDrawable);
                    btn3.setBackground(unselectedDrawable);
                    btn4.setBackground(unselectedDrawable);
                    btn5.setBackground(unselectedDrawable);
                    
                    // increase current question number
                    int currentQuestionIncrease = Integer.parseInt(currentquestion.getText().toString()) + 1;
                    currentquestion.setText(String.valueOf(currentQuestionIncrease));
                    // set the next question
                    int currentQuestionText = Integer.parseInt(currentquestion.getText().toString()) - 1;
                    question.setText(category_question.get(currentQuestionText));
                    //change the button next to submit for the final question
                    if (currentquestion.getText().toString().equals(totalquestions.getText().toString())){
                        btn_next.setText(R.string.submit);
                        btn_next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                answered_question.add(String.valueOf(selected_point));
//                                arr_finalAnswer.add(selected_answer);

                                System.out.println("Array final final point = "+answered_question);
//                                System.out.println("Array final final answer = "+arr_finalAnswer);

                                //check the category of questions
                                if (category_name.equals("Governance and Leadership")){
                                    saveGovernance();
                                }else if (category_name.equals("Human Resources")){
                                    saveHR();
                                }else if (category_name.equals("Finance")){
                                    saveFinance();
                                }else if (category_name.equals("Working Practice")){
                                    saveWorkingPractice();
                                }else if (category_name.equals("Community Engagement")){
                                    saveCommunityEngagement();
                                }else if (category_name.equals("Partnerships")){
                                    savePartnership();
                                }else if (category_name.equals("Information Technology")){
                                    saveTechnology();
                                }else if (category_name.equals("Sustainability")){
                                    saveSustainability();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public void getOptions() {

        Dialog myDialog = new Dialog(AssessmentPage2.this);
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText(R.string.get_ready);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://kwamea19.sg-host.com/WACSI_OCAT/options.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();
                        System.out.println("Options response: " + response);

                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("status");
                            String message = json.getString("message");

                            JSONArray jsonArray = new JSONArray(message);
                            int len = jsonArray.length();
                            for (int i=0; i<len; i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String options = jsonObject.getString("options");
                                String points = jsonObject.getString("points");
                                Integer point = Integer.parseInt(points);

                                arr_option.add(options);
                                arr_point.add(point);

                            }

                            setValues();

                        }catch(Exception e) {
                            Toast.makeText(AssessmentPage2.this, "Options loading failed", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AssessmentPage2.this, R.string.network_error, Toast.LENGTH_SHORT).show();
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

    private void setValues() {

        int start_point = answered_question.size();

        //set the question and options
        currentquestion.setText(String.valueOf(start_point+1));
        question.setText(category_question.get(start_point));
        option1.setText(arr_option.get(0));
        option2.setText(arr_option.get(1));
        option3.setText(arr_option.get(2));
        option4.setText(arr_option.get(3));
        option5.setText(arr_option.get(4));
    }

    private void saveGovernance() {

        //get the user id from shared preference
        SharedPreferences sharedPreferences = getSharedPreferences("Login Pref", Context.MODE_PRIVATE);
        String got_user_id = sharedPreferences.getString("id", null);

        Dialog myDialog = new Dialog(AssessmentPage2.this);
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Saving your responses");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://kwamea19.sg-host.com/WACSI_OCAT/save_governance.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();
                        System.out.println("Governance saving response: " + response);

                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("status");
                            String message = json.getString("message");

                            if (status.equals("success")){

                                myDB.deleteRow(category_id);

                                Toast.makeText(AssessmentPage2.this, message, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(AssessmentPage2.this, Completion.class);
                                i.putExtra("category", category_name);
                                i.putExtra("categoryId", category_id);
                                startActivity(i);
                            }else {
                                Toast.makeText(AssessmentPage2.this, message, Toast.LENGTH_SHORT).show();
                            }

                        }catch(Exception e) {
                            Toast.makeText(AssessmentPage2.this, "Failed to save", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AssessmentPage2.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("user_id", got_user_id);
                params.put("q1", String.valueOf(answered_question.get(0)));
                params.put("q2", String.valueOf(answered_question.get(1)));
                params.put("q3", String.valueOf(answered_question.get(2)));
                params.put("q4", String.valueOf(answered_question.get(3)));
                params.put("q5", String.valueOf(answered_question.get(4)));
                params.put("q6", String.valueOf(answered_question.get(5)));
                params.put("q7", String.valueOf(answered_question.get(6)));
                params.put("q8", String.valueOf(answered_question.get(7)));
                params.put("q9", String.valueOf(answered_question.get(8)));
                params.put("q10", String.valueOf(answered_question.get(9)));
                params.put("q11", String.valueOf(answered_question.get(10)));
                params.put("q12", String.valueOf(answered_question.get(11)));
                params.put("q13", String.valueOf(answered_question.get(12)));
                params.put("q14", String.valueOf(answered_question.get(13)));
                params.put("q15", String.valueOf(answered_question.get(14)));
                params.put("q16", String.valueOf(answered_question.get(15)));
                params.put("q17", String.valueOf(answered_question.get(16)));
                params.put("q18", String.valueOf(answered_question.get(17)));
                params.put("q19", String.valueOf(answered_question.get(18)));
                params.put("q20", String.valueOf(answered_question.get(19)));
                params.put("q21", String.valueOf(answered_question.get(20)));
                params.put("q22", String.valueOf(answered_question.get(21)));
                params.put("que_status", "completed");
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
    
    private void saveHR() {

        //get the user id from shared preference
        SharedPreferences sharedPreferences = getSharedPreferences("Login Pref", Context.MODE_PRIVATE);
        String got_user_id = sharedPreferences.getString("id", null);

        Dialog myDialog = new Dialog(AssessmentPage2.this);
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Saving your responses");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://kwamea19.sg-host.com/WACSI_OCAT/save_hr.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();
                        System.out.println("HR saving response: " + response);

                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("status");
                            String message = json.getString("message");

                            if (status.equals("success")){

                                myDB.deleteRow(category_id);

                                Toast.makeText(AssessmentPage2.this, message, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(AssessmentPage2.this, Completion.class);
                                i.putExtra("category", category_name);
                                i.putExtra("categoryId", category_id);
                                startActivity(i);
                            }else {
                                Toast.makeText(AssessmentPage2.this, message, Toast.LENGTH_SHORT).show();
                            }


                        }catch(Exception e) {
                            Toast.makeText(AssessmentPage2.this, "Failed to save", Toast.LENGTH_SHORT).show();
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
                        System.out.println("Network Error "+volleyError.getMessage());
                        Toast.makeText(AssessmentPage2.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("user_id", got_user_id);
                params.put("q1", String.valueOf(answered_question.get(0)));
                params.put("q2", String.valueOf(answered_question.get(1)));
                params.put("q3", String.valueOf(answered_question.get(2)));
                params.put("q4", String.valueOf(answered_question.get(3)));
                params.put("q5", String.valueOf(answered_question.get(4)));
                params.put("q6", String.valueOf(answered_question.get(5)));
                params.put("q7", String.valueOf(answered_question.get(6)));
                params.put("q8", String.valueOf(answered_question.get(7)));
                params.put("q9", String.valueOf(answered_question.get(8)));
                params.put("q10", String.valueOf(answered_question.get(9)));
                params.put("q11", String.valueOf(answered_question.get(10)));
                params.put("q12", String.valueOf(answered_question.get(11)));
                params.put("q13", String.valueOf(answered_question.get(12)));
                params.put("q14", String.valueOf(answered_question.get(13)));
                params.put("que_status", "completed");
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

    private void saveFinance() {

        //get the user id from shared preference
        SharedPreferences sharedPreferences = getSharedPreferences("Login Pref", Context.MODE_PRIVATE);
        String got_user_id = sharedPreferences.getString("id", null);

        Dialog myDialog = new Dialog(AssessmentPage2.this);
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Saving your responses");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://kwamea19.sg-host.com/WACSI_OCAT/save_finance.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();
                        System.out.println("Finance saving response: " + response);

                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("status");
                            String message = json.getString("message");

                            if (status.equals("success")){

                                myDB.deleteRow(category_id);

                                Toast.makeText(AssessmentPage2.this, message, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(AssessmentPage2.this, Completion.class);
                                i.putExtra("category", category_name);
                                i.putExtra("categoryId", category_id);
                                startActivity(i);
                            }else {
                                Toast.makeText(AssessmentPage2.this, message, Toast.LENGTH_SHORT).show();
                            }


                        }catch(Exception e) {
                            Toast.makeText(AssessmentPage2.this, "Failed to save", Toast.LENGTH_SHORT).show();
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
                        System.out.println("Network Error "+volleyError.getMessage());
                        Toast.makeText(AssessmentPage2.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("user_id", got_user_id);
                params.put("q1", String.valueOf(answered_question.get(0)));
                params.put("q2", String.valueOf(answered_question.get(1)));
                params.put("q3", String.valueOf(answered_question.get(2)));
                params.put("q4", String.valueOf(answered_question.get(3)));
                params.put("q5", String.valueOf(answered_question.get(4)));
                params.put("q6", String.valueOf(answered_question.get(5)));
                params.put("q7", String.valueOf(answered_question.get(6)));
                params.put("q8", String.valueOf(answered_question.get(7)));
                params.put("q9", String.valueOf(answered_question.get(8)));
                params.put("q10", String.valueOf(answered_question.get(9)));
                params.put("que_status", "completed");
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

    private void saveWorkingPractice() {

        //get the user id from shared preference
        SharedPreferences sharedPreferences = getSharedPreferences("Login Pref", Context.MODE_PRIVATE);
        String got_user_id = sharedPreferences.getString("id", null);

        Dialog myDialog = new Dialog(AssessmentPage2.this);
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Saving your responses");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://kwamea19.sg-host.com/WACSI_OCAT/save_working_practice.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();
                        System.out.println("Working Practice saving response: " + response);

                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("status");
                            String message = json.getString("message");

                            if (status.equals("success")){

                                myDB.deleteRow(category_id);

                                Toast.makeText(AssessmentPage2.this, message, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(AssessmentPage2.this, Completion.class);
                                i.putExtra("category", category_name);
                                i.putExtra("categoryId", category_id);
                                startActivity(i);
                            }else {
                                Toast.makeText(AssessmentPage2.this, message, Toast.LENGTH_SHORT).show();
                            }


                        }catch(Exception e) {
                            Toast.makeText(AssessmentPage2.this, "Failed to save", Toast.LENGTH_SHORT).show();
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
                        System.out.println("Network Error "+volleyError.getMessage());
                        Toast.makeText(AssessmentPage2.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("user_id", got_user_id);
                params.put("q1", String.valueOf(answered_question.get(0)));
                params.put("q2", String.valueOf(answered_question.get(1)));
                params.put("q3", String.valueOf(answered_question.get(2)));
                params.put("q4", String.valueOf(answered_question.get(3)));
                params.put("q5", String.valueOf(answered_question.get(4)));
                params.put("q6", String.valueOf(answered_question.get(5)));
                params.put("q7", String.valueOf(answered_question.get(6)));
                params.put("q8", String.valueOf(answered_question.get(7)));
                params.put("q9", String.valueOf(answered_question.get(8)));
                params.put("q10", String.valueOf(answered_question.get(9)));
                params.put("q11", String.valueOf(answered_question.get(10)));
                params.put("q12", String.valueOf(answered_question.get(11)));
                params.put("q13", String.valueOf(answered_question.get(12)));
                params.put("q14", String.valueOf(answered_question.get(13)));
                params.put("q15", String.valueOf(answered_question.get(14)));
                params.put("q16", String.valueOf(answered_question.get(15)));
                params.put("q17", String.valueOf(answered_question.get(16)));
                params.put("q18", String.valueOf(answered_question.get(17)));
                params.put("q19", String.valueOf(answered_question.get(18)));
                params.put("q20", String.valueOf(answered_question.get(19)));
                params.put("q21", String.valueOf(answered_question.get(20)));
                params.put("que_status", "completed");
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

    private void saveCommunityEngagement() {

        //get the user id from shared preference
        SharedPreferences sharedPreferences = getSharedPreferences("Login Pref", Context.MODE_PRIVATE);
        String got_user_id = sharedPreferences.getString("id", null);

        Dialog myDialog = new Dialog(AssessmentPage2.this);
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Saving your responses");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://kwamea19.sg-host.com/WACSI_OCAT/save_community_engagement.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();
                        System.out.println("Community Engagement saving response: " + response);

                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("status");
                            String message = json.getString("message");

                            if (status.equals("success")){

                                myDB.deleteRow(category_id);

                                Toast.makeText(AssessmentPage2.this, message, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(AssessmentPage2.this, Completion.class);
                                i.putExtra("category", category_name);
                                i.putExtra("categoryId", category_id);
                                startActivity(i);
                            }else {
                                Toast.makeText(AssessmentPage2.this, message, Toast.LENGTH_SHORT).show();
                            }


                        }catch(Exception e) {
                            Toast.makeText(AssessmentPage2.this, "Failed to save", Toast.LENGTH_SHORT).show();
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
                        System.out.println("Network Error "+volleyError.getMessage());
                        Toast.makeText(AssessmentPage2.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("user_id", got_user_id);
                params.put("q1", String.valueOf(answered_question.get(0)));
                params.put("q2", String.valueOf(answered_question.get(1)));
                params.put("q3", String.valueOf(answered_question.get(2)));
                params.put("q4", String.valueOf(answered_question.get(3)));
                params.put("q5", String.valueOf(answered_question.get(4)));
                params.put("q6", String.valueOf(answered_question.get(5)));
                params.put("q7", String.valueOf(answered_question.get(6)));
                params.put("q8", String.valueOf(answered_question.get(7)));
                params.put("q9", String.valueOf(answered_question.get(8)));
                params.put("q10", String.valueOf(answered_question.get(9)));
                params.put("q11", String.valueOf(answered_question.get(10)));
                params.put("q12", String.valueOf(answered_question.get(11)));
                params.put("que_status", "completed");
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

    private void savePartnership() {

        //get the user id from shared preference
        SharedPreferences sharedPreferences = getSharedPreferences("Login Pref", Context.MODE_PRIVATE);
        String got_user_id = sharedPreferences.getString("id", null);

        Dialog myDialog = new Dialog(AssessmentPage2.this);
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText(R.string.saving_your_responses);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://kwamea19.sg-host.com/WACSI_OCAT/save_partnership.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();
                        System.out.println("Partnership saving response: " + response);

                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("status");
                            String message = json.getString("message");

                            if (status.equals("success")){

                                myDB.deleteRow(category_id);

                                Toast.makeText(AssessmentPage2.this, message, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(AssessmentPage2.this, Completion.class);
                                i.putExtra("category", category_name);
                                i.putExtra("categoryId", category_id);
                                startActivity(i);
                            }else {
                                Toast.makeText(AssessmentPage2.this, message, Toast.LENGTH_SHORT).show();
                            }


                        }catch(Exception e) {
                            Toast.makeText(AssessmentPage2.this, response, Toast.LENGTH_SHORT).show();
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
                        System.out.println("Network Error "+volleyError.getMessage());
                        Toast.makeText(AssessmentPage2.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("user_id", got_user_id);
                params.put("q1", String.valueOf(answered_question.get(0)));
                params.put("q2", String.valueOf(answered_question.get(1)));
                params.put("q3", String.valueOf(answered_question.get(2)));
                params.put("q4", String.valueOf(answered_question.get(3)));
                params.put("q5", String.valueOf(answered_question.get(4)));
                params.put("q6", String.valueOf(answered_question.get(5)));
                params.put("q7", String.valueOf(answered_question.get(6)));
                params.put("q8", String.valueOf(answered_question.get(7)));
                params.put("q9", String.valueOf(answered_question.get(8)));
                params.put("q10", String.valueOf(answered_question.get(9)));
                params.put("q11", String.valueOf(answered_question.get(10)));
                params.put("q12", String.valueOf(answered_question.get(11)));
                params.put("q13", String.valueOf(answered_question.get(12)));
                params.put("q14", String.valueOf(answered_question.get(13)));
                params.put("q15", String.valueOf(answered_question.get(14)));
                params.put("q16", String.valueOf(answered_question.get(15)));
                params.put("q17", String.valueOf(answered_question.get(16)));
                params.put("q18", String.valueOf(answered_question.get(17)));
                params.put("que_status", "completed");
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

    private void saveTechnology() {

        //get the user id from shared preference
        SharedPreferences sharedPreferences = getSharedPreferences("Login Pref", Context.MODE_PRIVATE);
        String got_user_id = sharedPreferences.getString("id", null);

        Dialog myDialog = new Dialog(AssessmentPage2.this);
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Saving your responses");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://kwamea19.sg-host.com/WACSI_OCAT/save_technology.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();
                        System.out.println("Technology saving response: " + response);

                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("status");
                            String message = json.getString("message");

                            if (status.equals("success")){

                                myDB.deleteRow(category_id);

                                Toast.makeText(AssessmentPage2.this, message, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(AssessmentPage2.this, Completion.class);
                                i.putExtra("category", category_name);
                                i.putExtra("categoryId", category_id);
                                startActivity(i);
                            }else {
                                Toast.makeText(AssessmentPage2.this, message, Toast.LENGTH_SHORT).show();
                            }

                        }catch(Exception e) {
                            Toast.makeText(AssessmentPage2.this, "Failed to save", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AssessmentPage2.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("user_id", got_user_id);
                params.put("q1", String.valueOf(answered_question.get(0)));
                params.put("q2", String.valueOf(answered_question.get(1)));
                params.put("q3", String.valueOf(answered_question.get(2)));
                params.put("q4", String.valueOf(answered_question.get(3)));
                params.put("q5", String.valueOf(answered_question.get(4)));
                params.put("q6", String.valueOf(answered_question.get(5)));
                params.put("q7", String.valueOf(answered_question.get(6)));
                params.put("q8", String.valueOf(answered_question.get(7)));
                params.put("q9", String.valueOf(answered_question.get(8)));
                params.put("q10", String.valueOf(answered_question.get(9)));
                params.put("q11", String.valueOf(answered_question.get(10)));
                params.put("q12", String.valueOf(answered_question.get(11)));
                params.put("q13", String.valueOf(answered_question.get(12)));
                params.put("q14", String.valueOf(answered_question.get(13)));
                params.put("q15", String.valueOf(answered_question.get(14)));
                params.put("q16", String.valueOf(answered_question.get(15)));
                params.put("q17", String.valueOf(answered_question.get(16)));
                params.put("q18", String.valueOf(answered_question.get(17)));
                params.put("q19", String.valueOf(answered_question.get(18)));
                params.put("q20", String.valueOf(answered_question.get(19)));
                params.put("q21", String.valueOf(answered_question.get(20)));
                params.put("que_status", "completed");
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

    private void saveSustainability() {

        //get the user id from shared preference
        SharedPreferences sharedPreferences = getSharedPreferences("Login Pref", Context.MODE_PRIVATE);
        String got_user_id = sharedPreferences.getString("id", null);

        Dialog myDialog = new Dialog(AssessmentPage2.this);
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Saving your responses");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://kwamea19.sg-host.com/WACSI_OCAT/save_sustainability.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();
                        System.out.println("Sustainability saving response: " + response);

                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("status");
                            String message = json.getString("message");

                            if (status.equals("success")){

                                myDB.deleteRow(category_id);

                                Toast.makeText(AssessmentPage2.this, message, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(AssessmentPage2.this, Completion.class);
                                i.putExtra("category", category_name);
                                i.putExtra("categoryId", category_id);
                                startActivity(i);
                            }else {
                                Toast.makeText(AssessmentPage2.this, message, Toast.LENGTH_SHORT).show();
                            }

                        }catch(Exception e) {
                            Toast.makeText(AssessmentPage2.this, "Failed to save", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AssessmentPage2.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("user_id", got_user_id);
                params.put("q1", String.valueOf(answered_question.get(0)));
                params.put("q2", String.valueOf(answered_question.get(1)));
                params.put("q3", String.valueOf(answered_question.get(2)));
                params.put("q4", String.valueOf(answered_question.get(3)));
                params.put("q5", String.valueOf(answered_question.get(4)));
                params.put("q6", String.valueOf(answered_question.get(5)));
                params.put("q7", String.valueOf(answered_question.get(6)));
                params.put("q8", String.valueOf(answered_question.get(7)));
                params.put("q9", String.valueOf(answered_question.get(8)));
                params.put("q10", String.valueOf(answered_question.get(9)));
                params.put("q11", String.valueOf(answered_question.get(10)));
                params.put("q12", String.valueOf(answered_question.get(11)));
                params.put("q13", String.valueOf(answered_question.get(12)));
                params.put("q14", String.valueOf(answered_question.get(13)));
                params.put("q15", String.valueOf(answered_question.get(14)));
                params.put("que_status", "completed");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //do nothing
    }
}
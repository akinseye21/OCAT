package com.example.ocat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.example.ocat.Adapters.CategoryAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AssessmentPage extends AppCompatActivity {

    ImageView back;
    TextView txt_category;
    TextView currentquestion, totalquestions;
    TextView question;
    RelativeLayout btn1, btn2, btn3, btn4, btn5;
    TextView option1, option2, option3, option4, option5;
    TextView btn_next;

    String que_cat;
    ArrayList<String> arr_questionId, arr_questionCategory, arr_questionText;

    ArrayList<String> arr_option;
    ArrayList<Integer> arr_point;
    int j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_page);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        que_cat = i.getStringExtra("category");
        arr_questionId = i.getStringArrayListExtra("arr_questionId");
        arr_questionCategory = i.getStringArrayListExtra("arr_questionCategory");
        arr_questionText = i.getStringArrayListExtra("arr_questionText");


        arr_option = new ArrayList<>();
        arr_point = new ArrayList<>();

        arr_option.clear();
        arr_point.clear();


        //get all options
        getOptions();


        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ask to pause the assessment using a dialog
            }
        });
        txt_category = findViewById(R.id.category);
        txt_category.setText(que_cat);

        currentquestion = findViewById(R.id.currentquestion);
        totalquestions = findViewById(R.id.totalquestions);
        totalquestions.setText(String.valueOf(arr_questionText.size()));

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

        Drawable selectedDrawable = getResources().getDrawable(R.drawable.selected_option);
        Drawable unselectedDrawable = getResources().getDrawable(R.drawable.button_grey);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn1.getBackground().getConstantState().equals(unselectedDrawable.getConstantState())) {
                    // Not selected, make it selected
                    btn1.setBackground(selectedDrawable);
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


    }

    private void getOptions() {

        Dialog myDialog = new Dialog(AssessmentPage.this);
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Get ready!");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://10.19.71.184/WACSI_OCAT/options.php",
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
                            Toast.makeText(AssessmentPage.this, "Options loading failed", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AssessmentPage.this, "Network Error!", Toast.LENGTH_SHORT).show();
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

        for (j=0; j<arr_questionId.size(); j++){
            //set the question and options
            question.setText(arr_questionText.get(j));
            option1.setText(arr_option.get(0));
            option2.setText(arr_option.get(1));
            option3.setText(arr_option.get(2));
            option4.setText(arr_option.get(3));
            option5.setText(arr_option.get(4));

            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//
//                    //check the answer
//
//                    j = j+1;
//
//                    if (j<arr_questionId.size()){
//                        //set the next question
//
//                    }
                }
            });

        }
    }

}
package com.example.ocat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    ImageView back;
    LinearLayout txtlogin;
    Button signup;
    EditText edt_fullname, edt_email, edt_password, edt_confirmpassword;
    Boolean fullnameBool = false, emailBool = false, passwordBool = false, confirmpasswordBool = false, policyBool = false;
    String fullname, email, password, confirmpassword;
    CheckBox policycheck;
    LinearLayout txt_policy;
    String got_tos, got_privacy_policy, got_how_to_use;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtlogin = findViewById(R.id.txtlogin);
        txtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this, Login.class));
            }
        });

        signup = findViewById(R.id.signup);
        policycheck = findViewById(R.id.policycheck);
        policycheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Checkbox just checked
                // Checkbox just unchecked
                policyBool = isChecked;
            }
        });

        edt_fullname = findViewById(R.id.edt_fullname);
        edt_fullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edt_fullname.getText().toString().trim().length() < 4){
                    edt_fullname.setError("Full name is too short");
                    fullnameBool = false;
                }else{
                    fullname = edt_fullname.getText().toString().trim();
                    fullnameBool = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_email = findViewById(R.id.edt_email);
        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Patterns.EMAIL_ADDRESS.matcher(edt_email.getText().toString().trim()).matches()){
                    email = edt_email.getText().toString().trim();
                    emailBool = true;
                }else{
                    edt_email.setError("Wrong input");
                    emailBool = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_password = findViewById(R.id.edt_password);
        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edt_password.getText().toString().trim().length() < 5) {
                    edt_password.setError("Password is too short");
                    passwordBool = false;
                } else {
                    password = edt_password.getText().toString().trim();
                    passwordBool = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_confirmpassword = findViewById(R.id.edt_confirmpassword);
        edt_confirmpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!edt_confirmpassword.getText().toString().trim().equals(edt_password.getText().toString().trim())) {
                    edt_confirmpassword.setError("Password do not match");
                    confirmpasswordBool = false;
                } else {
                    confirmpassword = edt_confirmpassword.getText().toString().trim();
                    confirmpasswordBool = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txt_policy = findViewById(R.id.txt_policy);
        txt_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                policyView();
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check(fullname, email, password);
            }
        });
    }

    @Override
    protected void onStart() {
        //get policies from DB
        super.onStart();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://10.19.71.184/WACSI_OCAT/policies.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Policy response: " + response);

                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("status");

                            if (status.equals("success")){
                                String message = json.getString("message");

                                JSONArray jsonArray = new JSONArray(message);
                                int len = jsonArray.length();
                                for (int i=0; i<len; i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    got_privacy_policy = jsonObject.getString("privacy_policy");
                                    got_tos = jsonObject.getString("terms_of_service");
                                    got_how_to_use = jsonObject.getString("how_to_use");
                                }
                            }else{
                                String message = json.getString("message");
                                Toast.makeText(Signup.this, "Error! "+message, Toast.LENGTH_SHORT).show();
                            }

                        }catch(Exception e) {
                            Toast.makeText(Signup.this, "Signup failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if(volleyError == null){
                            return;
                        }
                        Log.e(TAG, volleyError.toString());
                        System.out.println("Network Error "+volleyError);
                        Toast.makeText(Signup.this, "Network Error!", Toast.LENGTH_SHORT).show();
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

    private void policyView() {


        //set dialog text with policies
        Dialog myDialog = new Dialog(Signup.this);
        myDialog.setContentView(R.layout.custom_popup_policy);

        ImageView close = myDialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        TextView tos = myDialog.findViewById(R.id.tos);
        tos.setText(got_tos);
        TextView privacy_policy = myDialog.findViewById(R.id.privacy_policy);
        privacy_policy.setText(got_privacy_policy);

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

    }

    private void check(String fullname, String email, String password) {
        if (fullnameBool && emailBool && passwordBool && confirmpasswordBool && policyBool){
            sendToDb(fullname, email, password);
        }else{
            if (!fullnameBool){
                edt_fullname.setError("Wrong input");
            }
            if (!emailBool){
                edt_email.setError("Wrong input");
            }
            if (!passwordBool){
                edt_password.setError("Wrong input");
            }
            if (!confirmpasswordBool) {
                edt_confirmpassword.setError("Wrong input");
            }
            if (!policyBool){
                Toast.makeText(Signup.this, "Please read and agree to the policy", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendToDb(String fullname, String email, String password) {
        Dialog myDialog = new Dialog(Signup.this);
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText(R.string.signing_up);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://10.19.71.184/WACSI_OCAT/register.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();
                        System.out.println("Register response: " + response);

                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("status");

                            if (status.equals("success")){
                                String message = json.getString("message");
                                String user = json.getString("user");

                                JSONObject jsonObject = new JSONObject(user);
                                String id = jsonObject.getString("id");
                                String login_fullname = jsonObject.getString("fullname");
                                String login_email = jsonObject.getString("email");

                                SharedPreferences sharedPreferences = getSharedPreferences("Login Pref", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                // Store the JSON string in SharedPreferences
                                editor.putString("id", id);
                                editor.putString("email", login_email);
                                editor.putString("fullname", login_fullname);
                                // Commit the changes
                                editor.apply();


                                Toast.makeText(Signup.this, message+" - "+fullname, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Signup.this, OrgProfile.class);
                                i.putExtra("id", id);
                                startActivity(i);
                            }else{
                                String message = json.getString("message");
                                Toast.makeText(Signup.this, "Error! "+message, Toast.LENGTH_SHORT).show();
                            }

                        }catch(Exception e) {
                            Toast.makeText(Signup.this, "Signup failed", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Signup.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                params.put("fullname", fullname);
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
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

public class Login extends AppCompatActivity {

    ImageView back;
    LinearLayout txtsignup;
    EditText edt_email, edt_password;
    Button btn_login;
    Boolean emailBool = false, passwordBool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtsignup = findViewById(R.id.txtsignup);
        txtsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Signup.class));
            }
        });

        btn_login = findViewById(R.id.login);

        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);

        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Patterns.EMAIL_ADDRESS.matcher(edt_email.getText().toString().trim()).matches()){
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
        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edt_password.getText().toString().trim().length() < 2){
                    edt_password.setError("Password is too short");
                    passwordBool = false;
                }else{
                    passwordBool = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check(edt_email.getText().toString().trim(), edt_password.getText().toString().trim());
            }
        });

    }

    private void check(String email, String password) {

        if (emailBool && passwordBool){
            sendToDb(email, password);
        }else{
            edt_email.setError("Wrong input");
            edt_password.setError("Wrong input");
        }

    }

    private void sendToDb(String email, String password) {

        Dialog myDialog = new Dialog(Login.this);
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText(R.string.logging_in);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://10.19.71.184/WACSI_OCAT/login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();
                        System.out.println("Login response: " + response);

                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("status");

                            if (status.equals("success")){
                                String message = json.getString("message");
                                String data = json.getString("data");

                                JSONObject jsonObject = new JSONObject(data);
                                String id = jsonObject.getString("id");
                                String login_fullname = jsonObject.getString("fullname");
                                String login_email = jsonObject.getString("email");
                                String created_at = jsonObject.getString("created_at");

                                SharedPreferences sharedPreferences = getSharedPreferences("Login Pref", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                // Store the JSON string in SharedPreferences
                                editor.putString("id", id);
                                editor.putString("email", login_email);
                                editor.putString("fullname", login_fullname);
                                // Commit the changes
                                editor.apply();

                                Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Login.this, Dashboard.class);
                                startActivity(i);
                            }else{
                                String message = json.getString("message");
                                Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                            }

                        }catch(Exception e) {
                            Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Login.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
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
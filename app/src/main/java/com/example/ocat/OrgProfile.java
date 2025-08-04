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
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrgProfile extends AppCompatActivity {

    ImageView back;
    String user_id;
    EditText edt_orgname;
    Button btn_continue;
    Spinner spinner_countries, spinner_thematic_areas, spinner_org_type;
    String selectedCountry, selectedThematicArea, selectedOrgType;
    Boolean orgnameBool = false, countryBool = false, thematicAreaBool = false, orgTypeBool = false;
    // List of West African countries
    String[] westAfricanCountries = {
            "Select a country",
            "Benin",
            "Burkina Faso",
            "Cape Verde",
            "Cote d'Ivoire",
            "Gambia",
            "Ghana",
            "Guinea",
            "Guinea-Bissau",
            "Liberia",
            "Mali",
            "Mauritania",
            "Niger",
            "Nigeria",
            "Senegal",
            "Sierra Leone",
            "Togo"
    };

    String[] thematicAreas = {
            "Select a thematic area",
            "Democracy and Governance",
            "Human Rights",
            "Environmental Protection and Climate Action",
            "Education",
            "Health",
            "Poverty Alleviation and Livelihoods",
            "Gender Equality and Women's Empowerment",
            "Child Protection and Youth Development",
            "Disability Inclusion",
            "Peace and Conflict Resolution",
            "Humanitarian Aid and Disaster Relief",
            "Economic Development",
            "Agriculture and Food Security",
            "Water, Sanitation, and Hygiene (WASH)",
            "Social Justice and Advocacy",
            "Technology and Innovation",
            "Cultural Heritage and Arts",
            "Animal Welfare"
    };

    String[] orgTypes = {
            "Select an organisation type",
            "NGO",
            "CSO",
            "CBO",
            "INGO",
            "Foundation",
            "Trust",
            "Association",
            "Federation",
            "Coalition",
            "Network",
            "Social Enterprise",
            "Cooperative",
            "Religious Organization",
            "Labor Union",
            "Academic Institution",
            "Community Fund",
            "Other"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_profile);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        user_id = i.getStringExtra("id");

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        edt_orgname = findViewById(R.id.orgname);
        edt_orgname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edt_orgname.getText().toString().trim().length() < 4){
                    edt_orgname.setError("Enter organisation fullname");
                    orgnameBool = false;
                }else{
                    orgnameBool = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        spinner_countries = findViewById(R.id.spinnercountries);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                westAfricanCountries
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_countries.setAdapter(adapter);

        spinner_thematic_areas = findViewById(R.id.thematicarea);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                thematicAreas
        );
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_thematic_areas.setAdapter(adapter2);

        spinner_org_type = findViewById(R.id.orgtype);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                orgTypes
        );
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_org_type.setAdapter(adapter3);

        btn_continue = findViewById(R.id.continu);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orgnameBool){
                    selectedCountry = spinner_countries.getSelectedItem().toString();
                    selectedThematicArea = spinner_thematic_areas.getSelectedItem().toString();
                    selectedOrgType = spinner_org_type.getSelectedItem().toString();

                    countryBool = !selectedCountry.equals("Select a country");

                    thematicAreaBool = !selectedThematicArea.equals("Select a thematic area");

                    orgTypeBool = !selectedOrgType.equals("Select an organisation type");

                    checkSpinners(selectedCountry, selectedThematicArea, selectedOrgType);
                }else{
                    edt_orgname.setError("Enter organisation fullname");
                }
            }
        });

    }

    private void checkSpinners(String selectedCountry, String selectedThematicArea, String selectedOrgType) {
        if (countryBool && thematicAreaBool && orgTypeBool){
            //sendToDb
            Dialog myDialog = new Dialog(OrgProfile.this);
            myDialog.setContentView(R.layout.custom_popup_loading);
            TextView text = myDialog.findViewById(R.id.text);
            text.setText(R.string.registering_your_organisation);
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.setCanceledOnTouchOutside(false);
            myDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.0.192/WACSI_OCAT/add_org.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            myDialog.dismiss();
                            System.out.println("Add organisation response: " + response);

                            try {
                                JSONObject json = new JSONObject(response);
                                String status = json.getString("status");
                                String message = json.getString("message");

                                if (status.equals("success")){
                                    Toast.makeText(OrgProfile.this, message, Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(OrgProfile.this, Dashboard.class);
                                    startActivity(i);
                                }else{
                                    Toast.makeText(OrgProfile.this, message, Toast.LENGTH_SHORT).show();
                                }

                            }catch(Exception e) {
                                Toast.makeText(OrgProfile.this, "Update failed. Please try again", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(OrgProfile.this, "Network Error!", Toast.LENGTH_SHORT).show();
                        }
                    }){
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", user_id);
                    params.put("organisation_name", edt_orgname.getText().toString().trim());
                    params.put("country", selectedCountry);
                    params.put("thematic_area", selectedThematicArea);
                    params.put("organisation_type", selectedOrgType);
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



        }else{
            if (!countryBool){
                spinner_countries.setBackgroundResource(R.drawable.error_shade);
            }
            if (!thematicAreaBool) {
                spinner_thematic_areas.setBackgroundResource(R.drawable.error_shade);
            }
            if (!orgTypeBool) {
                spinner_org_type.setBackgroundResource(R.drawable.error_shade);
            }
        }
    }
}
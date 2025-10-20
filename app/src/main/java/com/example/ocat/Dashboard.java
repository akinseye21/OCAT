package com.example.ocat;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Dashboard extends AppCompatActivity {

    ImageView logout;
    String id, fullname, email;
    TextView txt_fullname;
    String displayName;

    RelativeLayout startassessment, continueassessment, viewreport, faq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ChooseLanguage.loadLanguage(this);

        // Get the SharedPreferences instance
        SharedPreferences sharedPreferences = getSharedPreferences("Login Pref", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("id", "");
        fullname = sharedPreferences.getString("fullname", "");
        email = sharedPreferences.getString("email", "");

        displayName = fullname;

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txt_fullname = findViewById(R.id.fullname);
        if (fullname.contains(" ")) {
            String[] parts = fullname.split(" ");
            String firstName = parts[0];
            String lastNameInitial = parts[1].substring(0, 1).toUpperCase(); // Get first letter

            displayName = firstName + " " + lastNameInitial + ".";
        }
        txt_fullname.setText(displayName);

        startassessment = findViewById(R.id.startassessment);
        continueassessment = findViewById(R.id.continueassessment);
        viewreport = findViewById(R.id.viewreport);
        faq = findViewById(R.id.faq);

        startassessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, StartAssessment.class));
            }
        });
        viewreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, ViewReports.class));
            }
        });
        continueassessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, ContinueAssessment.class));
            }
        });
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, FAQ.class));
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

        Dialog myDialog = new Dialog(Dashboard.this);
        myDialog.setContentView(R.layout.custom_popup_quit2);

        Button yes = myDialog.findViewById(R.id.btn_yes_quit);
        Button no = myDialog.findViewById(R.id.btn_no_quit);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("Login Pref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                // Clear all data
                editor.clear();
                editor.apply(); // or editor.commit();
                // Optionally, navigate to login screen
                Intent intent = new Intent(Dashboard.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();
    }
}
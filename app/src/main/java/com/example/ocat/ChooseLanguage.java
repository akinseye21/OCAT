package com.example.ocat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class ChooseLanguage extends AppCompatActivity {
    
    Button btn_english, btn_french;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_language);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btn_english = findViewById(R.id.btnenglish);
        btn_french = findViewById(R.id.btnfrench);

        btn_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale(ChooseLanguage.this, "en");
                saveLanguage("en");
                startActivity(new Intent(ChooseLanguage.this, MainActivity.class));
            }
        });

        btn_french.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(ChooseLanguage.this, MainActivity.class));
                setLocale(ChooseLanguage.this, "fr");
                saveLanguage("fr");
                startActivity(new Intent(ChooseLanguage.this, MainActivity.class));
            }
        });
    }

    public static void setLocale(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

        // restart activity to apply language change
//        Intent intent = new Intent(context, ChooseLanguage.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
    }

    private void saveLanguage(String lang) {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    public static void loadLanguage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("Settings", MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "en"); // default is English
        setLocale(context, language);
    }
}
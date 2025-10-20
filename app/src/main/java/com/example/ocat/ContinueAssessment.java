package com.example.ocat;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ocat.Adapters.ContinueAdapter;

import java.util.ArrayList;

public class ContinueAssessment extends AppCompatActivity {

    ImageView back;
    GridView gridView;
    LinearLayout norecord;
    LinearLayout loader;
    MyDatabaseHelper myDB;
    ArrayList<String> id, user_id, category_id, category_name, category_que, user_response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_assessment);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ChooseLanguage.loadLanguage(this);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        gridView = findViewById(R.id.gridview);
        loader = findViewById(R.id.loader);
        norecord = findViewById(R.id.norecord);

        myDB = new MyDatabaseHelper(ContinueAssessment.this);
        id = new ArrayList<>();
        user_id = new ArrayList<>();
        category_id = new ArrayList<>();
        category_name = new ArrayList<>();
        category_que = new ArrayList<>();
        user_response = new ArrayList<>();

        storeDataInArrays();
        ContinueAdapter continueAdapter = new ContinueAdapter(ContinueAssessment.this, category_id, category_name, category_que, user_response);
        gridView.setAdapter(continueAdapter);
        gridView.setVisibility(VISIBLE);


    }

    private void storeDataInArrays() {

        //get the number of row in the DB
        if (myDB.getRecordCount()==0){
            norecord.setVisibility(VISIBLE);
            loader.setVisibility(GONE);
        }else{
            norecord.setVisibility(GONE);
            loader.setVisibility(GONE);
            //get all the records
            Cursor cursor = myDB.readAllData();
            if (cursor != null && cursor.moveToFirst()) {
                id.clear();
                user_id.clear();
                category_id.clear();
                category_name.clear();
                category_que.clear();
                user_response.clear();

                do {
                    id.add(cursor.getString(0));
                    user_id.add(cursor.getString(1));
                    category_id.add(cursor.getString(2));
                    category_name.add(cursor.getString(3));
                    category_que.add(cursor.getString(4));
                    user_response.add(cursor.getString(5));
                }while (cursor.moveToNext());
            }
        }

    }


}
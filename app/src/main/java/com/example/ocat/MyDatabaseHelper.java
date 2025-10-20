package com.example.ocat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "OCAT.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "saved_assessments";
    private static final String COLUMN_ID = "_id";
    private static final String USER_ID = "user_id";
    private static final String COLUMN_CATEGORY_ID = "category_id";
    private static final String COLUMN_CATEGORY_NAME = "category_name";
    private static final String COLUMN_CATEGORY_QUE = "category_que";
    private static final String COLUMN_USER_RESPONSES = "user_responses";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" +COLUMN_ID + " INTEGER PRIMARY KEY, "+
                USER_ID + " TEXT, "+
                COLUMN_CATEGORY_ID + " TEXT, "+
                COLUMN_CATEGORY_NAME + " TEXT, "+
                COLUMN_CATEGORY_QUE + " TEXT, "+
                COLUMN_USER_RESPONSES + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

    void addTable(String user_id, String category_id, String category_name, String category_que, String user_responses){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(USER_ID, user_id);
        cv.put(COLUMN_CATEGORY_ID, category_id);
        cv.put(COLUMN_CATEGORY_NAME, category_name);
        cv.put(COLUMN_CATEGORY_QUE, category_que);
        cv.put(COLUMN_USER_RESPONSES, user_responses);
        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1){
            Toast.makeText(context, R.string.failed, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, R.string.assessment_saved, Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAllData(){
        String query = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public int getRecordCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;

        try {
            // Execute a query to get the count
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0); // The count is at index 0
            }

            cursor.close();
        } finally {
            db.close();
        }

        return count;
    }

    void deleteRow(String category_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "category_id=?", new String[]{category_id});
        if (result == -1){
//            Toast.makeText(context, "Failed to Delete", Toast.LENGTH_SHORT).show();
        }else {
//            Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();
        }
    }
}

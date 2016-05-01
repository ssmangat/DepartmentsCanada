package com.example.sukhbeer.departmentscanada;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sukhbeer on 2016-05-01.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    final static String DB_NAME = "departments.db";
    final static int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS Departments;");
        db.execSQL("CREATE TABLE Departments(_id integer PRIMARY KEY AUTOINCREMENT ,department TEXT,commodity_family TEXT,commodity_group TEXT, commodity_cat TEXT , commodity_sub TEXT, fiscal_year TEXT, quarter TEXT, period TEXT, amount TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void addToDatabase(String department, String commodity_family, String commodity_group, String commodity_cat, String commodity_subCat, String year, String quarter, String period, String amount)
    {
        ContentValues values = new ContentValues();
        values.put("department", department);
        values.put("commodity_family", commodity_family);
        values.put("commodity_group", commodity_group);
        values.put("commodity_cat", commodity_cat);
        values.put("commodity_sub", commodity_subCat);
        values.put("fiscal_year", year);
        values.put("quarter", quarter);
        values.put("period", period);
        values.put("amount", amount);

        SQLiteDatabase db = getWritableDatabase();
        db.insert("Departments", null, values);
        db.close();

    }

    public String[] readDatabase(String string){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select Distinct " + string + " from Departments", null);
        String list[] = new String[150];
        cursor.moveToFirst();
        int i =0;
        while(!cursor.isAfterLast()) {
            list[i] = cursor.getString(cursor.getColumnIndex(string));
            //departments[i+1] = list[i];
            i++;
            cursor.moveToNext();
            //Log.d("Data", "entered" + departments[i - 1]);
        }
        return list;
    }
}

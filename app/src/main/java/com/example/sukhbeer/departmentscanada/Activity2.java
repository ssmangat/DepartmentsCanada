package com.example.sukhbeer.departmentscanada;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Activity2 extends ActionBarActivity {
    private DatabaseHelper dBhelper;
    private Spinner spinner;
    private String[] departments = {"select department"};
    private String departmentString = "department";
    public String department_val;
    private String[] commodity_fam = {"Please select one"};
    private String commodityString = "commodity_family";
    public String commodity_val;
    private String[] commodity_group = {"select group"};
    private String commodityGroupString = "commodity_group";
    public String commodity_group_val;
    private String[] commodity_cat = {"select category"};
    private String categoryString = "commodity_cat";
    public String category_val;
    private String[] commodity_sub = {"select sub-category"};
    private String subCatString = "commodity_sub";
    public String sub_cat_val;
    private String[] fiscal_year = {"select fiscal-year"};
    private String yearString = "fiscal_year";
    public String year_val;
    private String[] quarter = {"Select Quarter"};
    private String quaterString = "quarter";
    public String quater_val;
    private String[] period = {"Select Period"};
    private String periodString = "period";
    public String period_val;
    public static String query;
    public static String Answer;
    //public static Departments departmentsStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_2);
        dBhelper = new DatabaseHelper(this);
        updateDepartments(departmentString);
        spinner = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,departments);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                department_val = departments[position];
                Log.d("Spin value", department_val);
                if (department_val.equals("Please select one department")) {
                    Log.d("Nothing","selected");
                } else {
                    selectCommodityFamily(department_val);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("Nothing","selected");
            }

        });
    }

    private void selectCommodityFamily(final String department_val1) {
        updateCommodityFam(department_val1, commodityString);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, commodity_fam);
        spinner2.setAdapter(arrayAdapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                commodity_val = commodity_fam[position];
                Log.d("Spin value", commodity_val);
                if (commodity_val.equals("Please select one commodity family")) {
                    Log.d("Nothing","selected");
                } else {
                    department_val = department_val.replace("\"","");
                    selectCommodityGroup(department_val.replace("\"", ""), commodity_val);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    private void selectCommodityGroup(final String department_val, final String commodity_val) {
        updateCommodityGroup(commodity_val, department_val);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner4);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, commodity_group);
        spinner2.setAdapter(arrayAdapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                commodity_group_val = commodity_group[position];
                Log.d("Spin value", commodity_group_val);
                if (commodity_group_val.equals("Please select one commodity group")) {
                    Log.d("Nothing","selected");
                } else {
                    selectCommodityCategory(department_val, commodity_val, commodity_group_val);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    private void selectCommodityCategory(final String department_val, final String commodity_val, final String commodity_group_val) {
        updateCommodityCategory(department_val, commodity_val, commodity_group_val);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner5);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, commodity_cat);
        spinner2.setAdapter(arrayAdapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category_val = commodity_cat[position];
                Log.d("Spin value", category_val);
                if (category_val == null) {
                    category_val = "not available";
                }else if(category_val.equals("Please select one category value")) {
                    Log.d("Nothing","selected");
                } else {
                    selectCommoditySubCategory(department_val, commodity_val, commodity_group_val.replace("\"",""), category_val);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    private void selectCommoditySubCategory(final String department_val, final String commodity_val, final String commodity_group_val, final String category_val) {
        updateCommoditySubCategory(department_val, commodity_val, commodity_group_val, category_val);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner6);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, commodity_sub);
        spinner2.setAdapter(arrayAdapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sub_cat_val = commodity_sub[position];
                Log.d("Spin value", sub_cat_val);
                if (sub_cat_val.equals("Please select one commodity sub-category")) {
                    Log.d("Nothing","selected");
                } else {
                    selectFiscalYear(department_val, commodity_val, commodity_group_val, category_val, sub_cat_val);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void selectFiscalYear(final String department_val, final String commodity_val, final String commodity_group_val, final String category_val, final String sub_cat_val) {
        updateFiscalYear(department_val, commodity_val, commodity_group_val, category_val, sub_cat_val);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner7);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, fiscal_year);
        spinner2.setAdapter(arrayAdapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year_val = fiscal_year[position];
                Log.d("Spin value", year_val);
                if (year_val.equals("Please select one Fiscal Year")) {
                    Log.d("Nothing","selected");
                } else {
                    selectQuater(department_val, commodity_val, commodity_group_val, category_val, sub_cat_val, year_val);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateFiscalYear(String department_val, String commodity_val, String commodity_group_val, String category_val, String sub_cat_val) {
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        Cursor cursor = db.query(true, "Departments", new String[]{yearString}, departmentString + " =  \"" + department_val + "\" AND " + commodityString + "= \"" + commodity_val + "\" AND " + commodityGroupString + " = \"" + commodity_group_val + "\"" + " AND " + categoryString + " = \"" + category_val + "\"" + " AND " + subCatString + " = \"" + sub_cat_val + "\"", null, null, null, null, null);
        String data = "";
        fiscal_year = new String[cursor.getCount()+1];
        cursor.moveToFirst();
        int i =0;
        int rowID = cursor.getColumnIndex(yearString);
        fiscal_year[i] = "Please select one Fiscal Year";

        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            data = cursor.getString(rowID);
            fiscal_year[i+1] = data;
            i++;
            Log.d("Data","entered" + fiscal_year[i]);
        }
        cursor.close();
    }

    private void selectQuater(final String department_val, final String commodity_val, final String commodity_group_val, final String category_val, final String sub_cat_val, final String year_val) {
        updateQuarter(department_val, commodity_val, commodity_group_val, category_val, sub_cat_val, year_val);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner8);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, quarter);
        spinner2.setAdapter(arrayAdapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                quater_val = quarter[position];
                Log.d("Spin value", quater_val);
                if (quater_val.equals("Please select quater")) {
                    Log.d("Nothing", "selected");
                } else {
                    selectPeriod(department_val, commodity_val, commodity_group_val, category_val, sub_cat_val, year_val, quater_val);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateQuarter(String department_val, String commodity_val, String commodity_group_val, String category_val, String sub_cat_val, String year_val) {
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        Cursor cursor = db.query(true, "Departments", new String[]{quaterString}, departmentString + " =  \"" + department_val + "\" AND " + commodityString + "= \"" + commodity_val + "\" AND " + commodityGroupString + " = \"" + commodity_group_val + "\"" + " AND " + categoryString + " = \"" + category_val + "\"" + " AND " + subCatString + " = \"" + sub_cat_val + "\"" + " AND " + yearString + " = \"" + year_val + "\"" , null, null, null, null, null);
        String data = "";
        quarter = new String[cursor.getCount()+1];
        cursor.moveToFirst();
        int i =0;
        int rowID = cursor.getColumnIndex(quaterString);
        quarter[i] = "Please select quater";

        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            data = cursor.getString(rowID);
            quarter[i+1] = data;
            i++;
            Log.d("Data","entered" + quarter[i]);
        }
        cursor.close();
    }

    private void selectPeriod(String department_val, String commodity_val, String commodity_group_val, String category_val, String sub_cat_val, String year_val, String quater_val) {
        updatePeriod(department_val, commodity_val, commodity_group_val, category_val, sub_cat_val, year_val, quater_val);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner9);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, period);
        spinner2.setAdapter(arrayAdapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                period_val = period[position];
                Log.d("Spin value", period_val);
                if (period_val.equals("Please select Period")) {
                    Log.d("Nothing", "selected");
                } else {
                    //startFinalActivity();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updatePeriod(String department_val, String commodity_val, String commodity_group_val, String category_val, String sub_cat_val, String year_val, String quater_val) {
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        Cursor cursor = db.query(true, "Departments", new String[]{periodString}, departmentString + " =  \"" + department_val + "\" AND " + commodityString + "= \"" + commodity_val + "\" AND " + commodityGroupString + " = \"" + commodity_group_val + "\"" + " AND " + categoryString + " = \"" + category_val + "\"" + " AND " + subCatString + " = \"" + sub_cat_val + "\"" + " AND " + yearString + " = \"" + year_val + "\"" + " AND " + quaterString + " = \"" + quater_val + "\"" , null, null, null, null, null);
        String data = "";
        period = new String[cursor.getCount()+1];
        cursor.moveToFirst();
        int i =0;
        int rowID = cursor.getColumnIndex(periodString);
        period[i] = "Please select Period";

        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            data = cursor.getString(rowID);
            period[i+1] = data;
            i++;
            Log.d("Data","entered" + period[i]);
        }
        cursor.close();
    }

    private void updateCommoditySubCategory(String department_val, String commodity_val, String commodity_group_val, String category_val) {
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        Cursor cursor = db.query(true, "Departments", new String[]{subCatString}, departmentString + " =  \"" + department_val + "\" AND " + commodityString + "= \"" + commodity_val + "\" AND " + commodityGroupString + " = \"" + commodity_group_val.replace("\"", "") + "\" AND " + categoryString + " = \"" + category_val.replace("\"", "") + "\"", null, null, null, null, null);
        String data = "";
        commodity_sub = new String[cursor.getCount()+1];
        cursor.moveToFirst();
        int i =0;
        int rowID = cursor.getColumnIndex(subCatString);
        commodity_sub[i] = "Please select one commodity sub-category";

        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            data = cursor.getString(rowID);
            commodity_sub[i+1] = data;
            i++;
            Log.d("Data","entered" + commodity_sub[i]);
        }
        cursor.close();
    }

    private void updateCommodityCategory(String department_val, String commodity_val, String commodity_group_val) {
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        Cursor cursor = db.query(true, "Departments", new String[]{categoryString}, departmentString + " =  \"" + department_val + "\" AND " + commodityString + "= \"" + commodity_val + "\" AND " + commodityGroupString + " = \"" + commodity_group_val.replace("\"", "") + "\"", null, null, null, null, null);
        String data = "";
        commodity_cat = new String[cursor.getCount()+1];
        cursor.moveToFirst();
        int i =0;
        int rowID = cursor.getColumnIndex(categoryString);
        commodity_cat[i] = "Please select one category value";

        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            data = cursor.getString(rowID);
            commodity_cat[i+1] = data;
            i++;
            Log.d("Data","entered" + commodity_cat[i]);
        }
        cursor.close();
    }

    private void updateCommodityGroup(String commodity_val, String department_val) {
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        Cursor cursor = db.query(true, "Departments", new String[]{commodityGroupString}, departmentString + " =  \"" + department_val + "\" AND " + commodityString + "= \"" + commodity_val + "\"", null, null, null, null, null);
        String data = "";
        commodity_group = new String[cursor.getCount()+1];
        cursor.moveToFirst();
        int i =0;
        int rowID = cursor.getColumnIndex(commodityGroupString);
        commodity_group[i] = "Please select one commodity group";

        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            data = cursor.getString(rowID);
            commodity_group[i+1] = data;
            i++;
            Log.d("Data","entered" + commodity_group[i]);
        }
        cursor.close();
    }

    private void updateCommodityFam(String department_val, String commodityString) {
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        Cursor cursor = db.query(true, "Departments", new String[]{commodityString},departmentString + " =  \""+department_val.replace("\"","")+"\"", null, null, null, null, null);
        String data = "";
        commodity_fam = new String[cursor.getCount()+1];
        cursor.moveToFirst();
        int i =0;
        int rowID = cursor.getColumnIndex(commodityString);
        commodity_fam[i] = "Please select one commodity family";

        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            data = cursor.getString(rowID);
            commodity_fam[i+1] = data;
            i++;
            Log.d("Data","entered" + commodity_fam[i]);
        }
        cursor.close();
    }

    public void updateDepartments(String string){
        SQLiteDatabase db = dBhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select Distinct " + string + " from Departments", null);
        String list[] = new String[150];
        departments = new String[cursor.getCount()+1];
        cursor.moveToFirst();
        int i =0;
        departments[i] = "Please select one department";
        while(!cursor.isAfterLast()) {
            list[i] = cursor.getString(cursor.getColumnIndex(string));
            departments[i+1] = list[i];
            i++;
            cursor.moveToNext();
            Log.d("Data","entered" + departments[i]);
        }
        cursor.close();
    }
}

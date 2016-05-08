package com.example.sukhbeer.departmentscanada;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class MainActivity extends AppCompatActivity {
    private String url = null;    //private string to store url
    private ProgressDialog dialog1;  //progress bar for downloading
    public static final int progress1 = 0;
    private Spinner spinner;
    private String spin_val;
    private String[] years = { "select a year","2011","2012","2013","2014" };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private ProgressDialog dialog2;
    private static final int progress2 = 1;
    private ProgressDialog dialog3;
    private static final int progress3 = 2;
    public DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(MainActivity.this);
        setContentView(R.layout.activity_main);
        File databaseFile = new File(Environment.getDataDirectory() + File.separator + "/data/com.example.sukhbeer.departmentscanada/databases/departments.db");
        Log.d("address", String.valueOf(databaseFile));
        if(databaseFile.exists()){
            deleteDatabase(String.valueOf(databaseFile));
            Log.d("Database","Deleted");
        } else {
            Log.d("Database deletion","failed");
        }
        databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,years);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spin_val = years[position];

                switch (spin_val) {
                    case "2011":
                        url = "https://buyandsell.gc.ca/cds/public/spends/tpsgc-pwgsc_depenses-pm-spend-bd-EF-FY10-11.zip";
                        new DownloadFile().execute(url);//calls the download function
                        break;
                    case "2012":
                        url = "https://buyandsell.gc.ca/cds/public/spends/tpsgc-pwgsc_depenses-pm-spend-bd-EF-FY11-12.zip";
                        new DownloadFile().execute(url);//calls the download function
                        break;
                    case "2013":
                        url = "https://buyandsell.gc.ca/cds/public/spends/tpsgc-pwgsc_depenses-pm-spend-bd-EF-FY12-13.zip";
                        new DownloadFile().execute(url);//calls the download function
                        break;
                    case "2014":
                        url = "https://buyandsell.gc.ca/cds/public/spends/tpsgc-pwgsc_depenses-pm-spend-bd-EF-FY13-14.zip";
                        new DownloadFile().execute(url);//calls the download function
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress1: // we set this to 0
                dialog1 = new ProgressDialog(this);
                dialog1.setMessage("Downloading file. Please wait...");
                dialog1.setIndeterminate(false);
                dialog1.setMax(100);
                dialog1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog1.setCancelable(true);
                dialog1.show();
                return dialog1;
            case progress2: // we set this to 2
                dialog2 = new ProgressDialog(this);
                dialog2.setMessage("unzipping file. Please wait...");
                dialog2.setIndeterminate(false);
                dialog2.setMax(100);
                dialog2.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog2.setCancelable(true);
                dialog2.show();
                return dialog2;
            case progress3: // we set this to 3
                dialog3 = new ProgressDialog(this);
                dialog3.setMessage("Reading the downloaded file. Please wait for 10-15 minutes as because of the large size reading it costs some time. thanks for your patience...");
                dialog3.setIndeterminate(false);
                dialog3.setMax(100);
                dialog3.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                dialog3.setCancelable(true);
                dialog3.show();
                return dialog3;
            default:
                return null;
        }
    }

    public class DownloadFile extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            showDialog(progress1);
        }


        @Override
        protected String doInBackground(String... params) {
            int count;
            try {
                URL url = new URL(params[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                int fileSize = connection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(),10000);
                FileOutputStream out = openFileOutput("data1",MODE_WORLD_READABLE);

                byte data[] = new byte[1024];

                long total = 0;
                while ((count = input.read(data)) != -1){
                    total+= count;
                    publishProgress(""+(int)((total*100)/fileSize));

                    out.write(data, 0, count);
                }
                out.flush();

                out.close();

            } catch (java.io.IOException e) {
                Log.e("Error::", e.getMessage());
            }
            return null;
        }
        protected void onProgressUpdate(String... params) {
            // setting progress percentage
            dialog1.setProgress(Integer.parseInt(params[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress1);
            new unzip().execute();
        }
    }

    private class unzip extends AsyncTask<String,String,String>{
        InputStream is;

        ZipInputStream zis;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            showDialog(progress2);
        }



        @Override
        protected String doInBackground(String... params) {
            try{
                is = new FileInputStream(Environment.getDataDirectory() + File.separator + "/data/com.example.sukhbeer.departmentscanada/files/data1");
                zis = new ZipInputStream(new BufferedInputStream(is));
                ZipEntry ze;
                while ((ze = zis.getNextEntry())!= null){
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[12000];
                    int count1;
                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + "unzipped.csv");
                    int fileSize = (int) ze.getSize();
                    OutputStream fileOutputStream = new FileOutputStream(file);
                    long total = 0;
                    while ((count1 = zis.read(buffer))!= -1){
                        baos.write(buffer,0,count1);
                        total += count1;
                        publishProgress("" + (int) ((total *100)/fileSize));
                        byte[] bytes = baos.toByteArray();
                        fileOutputStream.write(bytes);
                        baos.reset();
                    }
                    fileOutputStream.close();
                    zis.closeEntry();

                }
                zis.close();
            } catch (IOException e){
                Log.e("Decompress", "Unzip", e);
            }
            return null;
        }
        protected void onProgressUpdate(String... params) {
            // setting progress percentage
            dialog2.setProgress(Integer.parseInt(params[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress2);
            new readCSV().execute();
        }
    }

    private class readCSV extends AsyncTask<String,String,String>{
        File fr = null;
        FileReader fileReader = null;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            showDialog(progress3);
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d("ReadCsv","Reading");
            try{
                fr = new File(Environment.getExternalStorageDirectory() + File.separator + "unzipped.csv");
                fileReader = new FileReader(fr);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Log.d("ReadCsv", "file detected");

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String data = "";
            long count = 0;
            try{
                Log.d("ReadCsv","started reading");
                String garbage = bufferedReader.readLine();
                while ((data = bufferedReader.readLine()) != null){
                    String[] stringArray = data.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                   // Departments departments = new Departments(stringArray[0].replace("\"", ""),stringArray[1].replace("\"", ""),stringArray[2].replace("\"", ""),stringArray[3].replace("\"", ""),stringArray[4].replace("\"", ""),stringArray[5].replace("\"", ""),stringArray[6].replace("\"", ""),stringArray[7].replace("\"", ""),stringArray[16]);
                    databaseHelper.addToDatabase(stringArray[0].replace("\"", ""), stringArray[1].replace("\"", ""), stringArray[2].replace("\"", ""), stringArray[3].replace("\"", ""), stringArray[4].replace("\"", ""), stringArray[5].replace("\"", ""), stringArray[6].replace("\"", ""), stringArray[7].replace("\"", ""), stringArray[16]);
                    count = count+1;
                    publishProgress("" + (int) ((count * 100) / 80000));

                }
                Log.d("ReadCsv", "Done Entering data");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onProgressUpdate(String... params) {
            // setting progress percentage
            dialog3.setProgress(Integer.parseInt(params[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was read
            dismissDialog(progress3);
            startNewActivity();
        }
    }
    private void startNewActivity() {
        Intent i = new Intent(this,Activity2.class);
        startActivity(i);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

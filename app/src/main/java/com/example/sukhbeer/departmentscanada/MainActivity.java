package com.example.sukhbeer.departmentscanada;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    private String url = null;    //private string to store url
    private ProgressDialog dialog1;  //progress bar for downloading
    public static final int progress1 = 0;
    private Spinner spinner;
    private String spin_val;
    private String[] years = { "select a year","2011","2012","2013","2014" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,years);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spin_val = years[position];

                switch (spin_val){
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
                File data1 = new File(Environment.getExternalStorageDirectory() + "/App_data");

                OutputStream out = new FileOutputStream(data1);

                byte data[] = new byte[1024];

                long total = 0;
                while ((count = input.read(data)) != -1){
                    total+= count;
                    publishProgress(""+(int)((total*100)/fileSize));

                    out.write(data,0,count);
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
        }
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

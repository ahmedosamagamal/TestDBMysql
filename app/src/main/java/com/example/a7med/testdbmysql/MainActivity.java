package com.example.a7med.testdbmysql;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends Activity {
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onAdd(View view) throws Exception{
        try {
            Toast.makeText(this, "Add Button is Clicked", Toast.LENGTH_SHORT).show();
            new DbWrite().execute(40);
        }catch (Exception e){
            Toast.makeText(this, "Add Button is Clicked"+e, Toast.LENGTH_SHORT).show();
        }
    }

    public void onView(View view) throws Exception{
        try {

            Toast.makeText(this, "View Button is Clicked", Toast.LENGTH_SHORT).show();
            new DbRead().execute(40);
            Toast.makeText(this, "View Button2 is Clicked", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, ""+e , Toast.LENGTH_SHORT ).show();
        }

    }

    class DbRead extends AsyncTask<Integer,Integer,Integer>{
        EditText email,pass;
        String password,pd;


        @Override
        protected void onPreExecute() {
            email= (EditText)findViewById(R.id.editText);
            pass = findViewById(R.id.editText2);
            password = "";
            pd = "";
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            try {

                HttpClient hc = new DefaultHttpClient();
                //HttpPost hp = new HttpPost("http://192.168.1.4/demo/select.php");
                HttpPost hp = new HttpPost("https://ahmedosama25.000webhostapp.com/select.php");
                ArrayList<NameValuePair> ar = new ArrayList<NameValuePair>();
                ar.add(new BasicNameValuePair("email",email.getText().toString()));
                ar.add(new BasicNameValuePair("password",pass.getText().toString()));
                hp.setEntity(new UrlEncodedFormEntity(ar));
                HttpResponse hr = hc.execute(hp);
                InputStream is = hr.getEntity().getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line=br.readLine())!=null){
                    sb.append(line+"\n");
                }
                is.close();
                String result = sb.toString();
                if (result.trim().equals("No Result")){
                    password = "email not founded";
                }else {
                    JSONObject jo = new JSONObject(result);
                    password = jo.getString("password");//the name is the key from DB table
                    pd = jo.getString("email");//the name is the key from DB table

                }
                /*
                *if result is an array we use JSONArray
                * JSONArray ja = new JSONArray(result);
                * for(int i=0 ; i<ja.length ; i++){
                * JSONObject jo = new JSONObject(result);
                * password = jo.getString("password");
                * }
                 ***/

            }catch (Exception e){
                Toast.makeText(MainActivity.this, "Error"+e, Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            //pass.setText(password);
            Toast.makeText(MainActivity.this, password, Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivity.this, pd, Toast.LENGTH_SHORT).show();
            super.onPostExecute(result);

        }

    }

    class DbWrite extends AsyncTask<Integer,Integer,Integer>{
        EditText email,pass;

        @Override
        protected void onPreExecute() {
            email= (EditText)findViewById(R.id.editText);
            pass = findViewById(R.id.editText2);

            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            try {

                HttpClient hc = new DefaultHttpClient();
                HttpPost hp = new HttpPost("https://ahmedosama25.000webhostapp.com/insert.php");
                ArrayList<NameValuePair> ar = new ArrayList<NameValuePair>();
                ar.add(new BasicNameValuePair("email",email.getText().toString()));
                ar.add(new BasicNameValuePair("password",pass.getText().toString()));
                hp.setEntity(new UrlEncodedFormEntity(ar));
                HttpResponse hr = hc.execute(hp);
                InputStream is = hr.getEntity().getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line=br.readLine())!=null){
                    sb.append(line+"\n");
                }
                is.close();

            }catch (Exception e){
                Toast.makeText(MainActivity.this, "Error"+e, Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            //pass.setText(password);
            Toast.makeText(MainActivity.this, "Add .....Success", Toast.LENGTH_SHORT).show();
            super.onPostExecute(result);

        }

    }
}

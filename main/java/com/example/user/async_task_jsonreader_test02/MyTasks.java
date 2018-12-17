package com.example.user.async_task_jsonreader_test02;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class MyTasks {


}

/*
    Diese Klasse stellt benötigte AsyncTasks zur Vefügung, über welche zunächst eine Internetverbindung zu übgergebener URL hergestellt wird

    InternetTask --> liest die zurückegegebene Zeichenkette(JSON) der Ziel-URL in doInBackground() aus
    und gibt diese bei Erfolg in onPostExecute() über das Interface OnEventListenener() an die Anwednnung in Form eines Strings zurück.

    GetIconTask --> liest die zurückgegebene Bilddatei der Ziel-URL in doInBackground() aus
    und gibt diese bei Erfolg in onPostExecute() über das Interface OnEventListener() an die Anwendung in Form einer Bitmap zurück.


 */

class InternetTask extends AsyncTask<String,Void,String> {

    private OnEventListener<String> mCallBack;
    private Context mContext;
    public Exception mException;

    public InternetTask(Context context, OnEventListener callback){

        mCallBack = callback;
        mContext = context;

    }

    @Override
    protected String doInBackground(String... strings) {


        StringBuilder sb = new StringBuilder();

        try {
            URL url = new URL(strings[0]);
            HttpURLConnection urlconnection = (HttpURLConnection)url.openConnection();
            urlconnection.setRequestMethod("GET");

            InputStreamReader isr = new InputStreamReader(urlconnection.getInputStream());
            BufferedReader br = new BufferedReader(isr);

            String line;

            while((line = br.readLine()) != null){

                sb.append(line);
            }

            br.close();
            urlconnection.disconnect();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString() ;
    }

    @Override
    protected void onPostExecute(String s) {

        if(mCallBack != null){
            if(mException == null){
                try {
                    mCallBack.OnSuccess(s);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else{
                mCallBack.OnFailure(mException);
            }

        }

    }


}

class getIconTask extends AsyncTask<String,Void,Bitmap> {

    private OnEventListener<Bitmap> mCallBack;
    private Context mContext;
    public Exception mException;

    public getIconTask(Context context, OnEventListener callback){

        mCallBack = callback;
        mContext = context;

    }

    protected Bitmap doInBackground(String... strings) {

        Bitmap bmp = null;
        URL url = null;
        try {
            url = new URL(strings[0]);
            HttpURLConnection urlconnection = (HttpURLConnection)url.openConnection();
            urlconnection.setRequestMethod("GET");

            bmp = BitmapFactory.decodeStream(urlconnection.getInputStream());
            urlconnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return bmp;
    }

    protected void onPostExecute(Bitmap bmp){

        if(mCallBack != null){
            if(mException == null){
                try {
                    mCallBack.OnSuccess(bmp);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else{
                mCallBack.OnFailure(mException);
            }

        }

    }

}
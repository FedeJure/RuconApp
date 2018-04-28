package com.ruccon.clases.rucconapp;

import android.database.CharArrayBuffer;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.ResourceBundle;

import static com.ruccon.clases.rucconapp.ManejadorBaseDeDatos.url;

/**
 * Created by Fede on 11/4/2018.
 */

public class ControladorDescargas {

    public ControladorDescargas(){

    }
    public static void download(String url, File file) throws MalformedURLException, IOException {
        URLConnection ucon = new URL(url).openConnection();
        HttpURLConnection httpConnection = (HttpURLConnection) ucon;
        if (file.exists() && file.lastModified() == ucon.getLastModified() ){
            return;
        }
        int responseCode = httpConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedInputStream bis = new BufferedInputStream(ucon.getInputStream());
            ByteArrayOutputStream baf = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int current = 0;
            while ((current = bis.read(data,0,data.length)) != -1) {
                baf.write(data,0,current);
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.close();
            bis.close();
        }
    }
    public static boolean cambioUltimaModificacion(final Long ultima, final String url){
        LastModificationHelper lamh = new LastModificationHelper();
        lamh.execute(ultima);
        while (lamh.result == -1){

        }
        Log.d("DEBUG", String.valueOf(lamh.result ==  1));
        return lamh.result ==  1;

    }

    static class LastModificationHelper extends AsyncTask<Long, Integer, Boolean> {
        int result = -1;
        @Override
        protected Boolean doInBackground(Long... params) {
            Long ultima = params[0];
            URLConnection ucon = null;
            try {
                ucon = new URL(url).openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            HttpURLConnection httpConnection = (HttpURLConnection) ucon;
            if ((ultima != httpConnection.getLastModified())){
                result = 1;
            }
            else{
                result = 0;
            }

            return (ultima != httpConnection.getLastModified());
        }


    }
}

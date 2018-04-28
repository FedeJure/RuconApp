package com.ruccon.clases.rucconapp;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Fede on 30/11/2017.
 */

public class ManejadorBaseDeDatos extends SQLiteOpenHelper {
    public static String DB_PATH = MainActivity.getContext().getFilesDir().getAbsolutePath() +"/";
    ProgressDialog mProgressDialog;
    public static String url = "https://rucon14.000webhostapp.com/base_de_datos_ruccon.db";
    public static String DB_NAME = "base_de_datos_ruccon.db";
    private static final int DB_VERSION = 1;
    private static SQLiteDatabase db;
    private static ManejadorBaseDeDatos miInstancia;
    public boolean pudoDescargar = false;

    private static long ultimaModificacionDeArchivo;
    public ManejadorBaseDeDatos() {
        super(MainActivity.getContext(), DB_NAME,null,DB_VERSION );
        try{
            actualizarBaseDeDatos();

        } catch (SQLException e){
            e.printStackTrace();
        }



    }

    public void actualizarBaseDeDatos(){
        File archivoDB = new File(DB_PATH+DB_NAME);
        crearBaseDeDatos();

        try {
            final DownloaderTask downloaderTask = new DownloaderTask();
            downloaderTask.execute();
            while (!pudoDescargar) {
            }
            AppCompatActivity activity = MainActivity.getInstance();
            activity.finish();
            activity.overridePendingTransition(0, 0);
            activity.startActivity(activity.getIntent());
            activity.overridePendingTransition(0, 0);

        }
        catch (android.os.NetworkOnMainThreadException e){
        }
        abrirBaseDeDatos();



        /*
        String url = "https://rucon14.000webhostapp.com/hola.txt";
        File file = new File(MainActivity.getContext().getFilesDir(), "test.txt");
        try {
            ControladorDescargas.download(url, file);
        } catch (MalformedURLException e) {
            // TODO handle error
        } catch (IOException e) {
            // TODO handle error
        }
        */
    }
    class DownloaderTask extends AsyncTask<Void, Integer, Void> {
        private PowerManager.WakeLock mWakeLock;

        @Override
        protected Void doInBackground(Void... params) {

            File file = new File(DB_PATH, DB_NAME);
            try {
                ControladorDescargas.download(url, file);

            } catch (MalformedURLException e) {
                // TODO handle error
            } catch (IOException e) {
                // TODO handle error
            }
            pudoDescargar = true;
            return null;
        }


    }


    private void abrirBaseDeDatos() throws SQLException {
        String path = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
    }

    private void crearBaseDeDatos() {
        SQLiteDatabase db_read = null;
        if (!existeBaseDeDatos()) {
            db_read = this.getReadableDatabase();
            db_read.close();
            try {
                copiarBaseDeDatos();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void copiarBaseDeDatos() throws IOException {
        //InputStream miInput = MainActivity.getContext().openFileInput(DB_NAME);
        InputStream miInput = MainActivity.getContext().getAssets().open(DB_NAME);
        String nombreArchivo = DB_PATH + DB_NAME;
        OutputStream output = new FileOutputStream(nombreArchivo);
        byte[] buffer = new byte[1024];
        int tamanio;
        while ((tamanio = miInput.read(buffer)) > 0){
            output.write(buffer,0,tamanio);
        }
        ultimaModificacionDeArchivo=new File(DB_PATH+DB_NAME).lastModified();
        output.flush();
        output.close();
        miInput.close();
    }

    private boolean existeBaseDeDatos() {
        File dbFile = new File(DB_PATH+DB_NAME);
        return !(dbFile.lastModified() != ultimaModificacionDeArchivo || !dbFile.exists());

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public static ManejadorBaseDeDatos instancia(){
        if (miInstancia == null){
            miInstancia = new ManejadorBaseDeDatos();
        }
        return miInstancia;
    }

    public SQLiteDatabase getBaseDeDatos() throws IOException {
        if (db == null){
            throw new IOException();
        }
        return db;
    }
}


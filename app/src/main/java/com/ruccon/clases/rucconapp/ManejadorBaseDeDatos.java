package com.ruccon.clases.rucconapp;


import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Fede on 30/11/2017.
 */

public class ManejadorBaseDeDatos extends SQLiteOpenHelper {
    private static String DB_PATH = "/data/user/0/com.ruccon.clases.rucconapp/files/";


    private static String DB_NAME = "base_de_datos_ruccon.db";
    private static final int DB_VERSION = 1;
    private static SQLiteDatabase db;
    private static ManejadorBaseDeDatos miInstancia;

    private static long ultimaModificacionDeArchivo;
    public ManejadorBaseDeDatos() {
        super(MainActivity.getContext(), DB_NAME,null,DB_VERSION );

        try{
            crearBaseDeDatos();
            abrirBaseDeDatos();
        } catch (SQLException e){
            MainActivity.mostrarMensaje("no se puedo abrir la base de datos");
            e.printStackTrace();
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



        if (dbFile.lastModified() != ultimaModificacionDeArchivo || !dbFile.exists()) {
            return false;
        }

        return true;
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

    public SQLiteDatabase getBaseDeDatos() {
        return db;
    }
}

package com.ruccon.clases.rucconapp;









import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;



/**
 * Created by Fede on 27/10/2017.
 */

public class ProcesadorDeDatos {
    private static final String NOMBRE_TABLA = "materiales";

    private static final String CODIGO= "CE";
    private static final String NOMBRE ="NOMBRE_MATERIAL";
    private static final String MATERIA = "MATERIA";
    private static final String TIPO_MATERIAL = "TIPO_MATERIAL";
    private static final String TIPO_IMPRESION = "TIPO_IMPRESION";
    private static final String TEMAS = "TEMAS_PAGINAS_PALABRASCLAVE";
    private static final String NIVEL = "NIVEL";
    private String[] campos = new String[]{CODIGO,NOMBRE,MATERIA,TIPO_MATERIAL,TIPO_IMPRESION,TEMAS,NIVEL};
    SQLiteDatabase baseDeDatos;

    public ProcesadorDeDatos(SQLiteDatabase baseDeDatos) {

        this.baseDeDatos = baseDeDatos;

    }


    public ArrayList<Material> obtenerMateriales()  {
        ArrayList<Material> materiales = new ArrayList<>();



        Cursor c = baseDeDatos.query(NOMBRE_TABLA,campos,null,null,null,null,null);


        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            Material material = new Material(c.getString(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),
                    c.getString(5),c.getString(6));
            materiales.add(material);
        }

        c.close();


        return materiales;
    }

    public ArrayList<String> getNombreMaterialesConPalabrasClave(String[] claves) {
        String whereQuery = "";
        for (String clave : claves){
            if (whereQuery.endsWith("'")){
                whereQuery += " OR ";
            }
            whereQuery += TEMAS + " LIKE '%" + clave + "%'";
        }
        Cursor c = baseDeDatos.query(NOMBRE_TABLA,campos,whereQuery,null,null,null,null);
        ArrayList<String> materiales = new ArrayList<>();
        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            materiales.add(c.getString(1));
        }
        return materiales;
    }

}

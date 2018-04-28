package com.ruccon.clases.rucconapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by Fede on 27/10/2017.
 */

public class Material {
    private String codigo;
    private String nombre;
    private String materia;
    private String nivel;
    private ArrayList<String> tipos;
    private String tipoImpresion;
    private HashMap<String,ArrayList<String>> palabrasClave; //clave: nombre del tema, valor: lista con palabras clave
    private ArrayList<String> temas;
    private HashMap<String,String> paginas; // nombre tema, pagina
    private int puntuacion = 0;


    public Material(String codigo,String nombre,String materia,String tipo,String tipoImpresion,String stringsTemas,String nivel){
        this.codigo = codigo;
        this.nombre = nombre;
        this.materia = materia;
        this.nivel = nivel;

        this.tipoImpresion = tipoImpresion;

        tipos= new ArrayList<String>();
        this.palabrasClave = new HashMap<String, ArrayList<String>>();
        this.paginas = new HashMap<String, String>();
        this.temas = new ArrayList<String>();

        crearListaTemas(stringsTemas);
        crearListaTipos(tipo);

    }

    private void crearListaTipos(String tipo) {
        tipos = new ArrayList<String>(Arrays.asList(tipo.split(",")));
    }

    private void crearListaTemas (String strignTemas){
        ArrayList<String> auxiliar = new ArrayList<String>(Arrays.asList(strignTemas.split(";")));

        for (int i=0; i<auxiliar.size(); i++){

            ArrayList<String> unTema = new ArrayList<String>(Arrays.asList(auxiliar.get(i).split(",")));
            String pagina = unTema.remove(1);// es de la forma "NOMBRE TEMA, num pagina,   palabraClave,palabraClave"

            for (int j=0; j<unTema.size();j++) {

                String nombreTema = unTema.remove(0);
                if (!temas.contains(nombreTema)) temas.add(nombreTema);




                palabrasClave.put(nombreTema, unTema);

                paginas.put(nombreTema, pagina);
            }





        }



    }

    public String nombre(){
        return nombre;
    }
    public String codigo(){
        return codigo;
    }
    public String materia(){
        return materia;
    }
    public String nivel(){
        return nivel;
    }
    public ArrayList<String> tipo(){
        return tipos;
    }
    public String tipoImpresion(){
        return tipoImpresion;
    }
    public ArrayList<String> palabrasClave(){
        ArrayList<String> temas = new ArrayList<String>();
        for (String nombreTema : paginas.keySet()){
            temas.add(nombreTema);
        }
        //devolver lista con palabrasClave.
        return temas;
}
    public ArrayList<String> getTemas(){
        return temas;
    }


    public String paginaDe(String palabraClave) {
        return paginas.get(palabraClave);
    }



    public boolean esMayorQue(Material otro) {
        return (this.puntuacion > otro.getPuntuacion());
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public boolean tieneFiltros(ArrayList<String> filtros) {
        for (String filtro : filtros) {
            if (!palabrasClave().contains(filtro)) {
                return false;
            }
        }
        return true;
    }

    public boolean esLibro() {

        return (tipoImpresion.contains("Libro"));
    }
    public boolean esImpresion(){
        return (tipoImpresion.contains("Impresión"));
    }

    public ArrayList<String> getTemasConPalabraClave(String s) {
        ArrayList<String> aux =new ArrayList<>();
        for (String st : palabrasClave.keySet()){
            if (st.contains(s)){
                st = st+" [Página: "+ paginaDe(st)+"]";
                aux.add(st);
            }
        }
        return aux;
    }

    public ArrayList<String> getTemasConPagina() {
        ArrayList<String> aux = new ArrayList<>();

        for (int i= 0; i< temas.size(); i++){
            String s = temas.get(i)+" [Página: " + paginaDe(temas.get(i)) + "]";
            aux.add(s);
        }

        return aux;
    }
}

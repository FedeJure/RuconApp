package com.ruccon.clases.rucconapp;



import android.os.Debug;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Set;


/**
 * Created by Fede on 27/10/2017.
 */

public class MaterialesRuccon {
    HashMap<String, ArrayList<Material>> materialesPorPalabraClave;
    HashMap<String, ArrayList<Material>> materialesPorNivel;
    HashMap<String, ArrayList<Material>> materialesPorMateria;
    HashMap<String, ArrayList<Material>> materialesPorTipo;
    HashMap<String, ArrayList<Material>> materialesPorTipoImpresion;
    HashMap<String, Material> materialesPorNombre;



    ArrayList<Material> materiales;
    ProcesadorDeDatos procesador;

    static MaterialesRuccon miInstancia;




    private MaterialesRuccon() throws IOException {
        materialesPorPalabraClave = new HashMap<String,ArrayList<Material>>();
        materialesPorNivel = new HashMap<String,ArrayList<Material>>();
        materialesPorTipo = new HashMap<String,ArrayList<Material>>();
        materialesPorTipoImpresion = new HashMap<String,ArrayList<Material>>();
        materialesPorMateria = new HashMap<String,ArrayList<Material>>();
        materialesPorNombre = new HashMap<String,Material>();



        materiales = new ArrayList<Material>();
        procesador = new ProcesadorDeDatos(ManejadorBaseDeDatos.instancia().getBaseDeDatos());
        cargarDatos();

    }
    public static MaterialesRuccon getInstance() {
        if (miInstancia == null){

            try {
                miInstancia = new MaterialesRuccon();
            } catch (IOException e) {

                e.printStackTrace();
            }

        }
        return miInstancia;
    }

    public void cargarDatos()  {
        /*implementar metodo que carga los datos de la tabla excel en el programa*/

        materiales = procesador.obtenerMateriales();


        for (Material material : materiales){





            /***********por palabra clave********************/
            for (int i = 0; i<material.palabrasClave().size(); i++){
                String unTema = material.palabrasClave().get(i);
                if (materialesPorPalabraClave.containsKey(unTema)){
                    materialesPorPalabraClave.get(unTema).add(material);
                }
                else {
                    ArrayList<Material> aux = new ArrayList<Material>();
                    aux.add(material);
                    materialesPorPalabraClave.put(unTema,aux);
                }
            }
            /******************por materia*******************/
            if (materialesPorMateria.containsKey(material.materia())){
                materialesPorMateria.get(material.materia()).add(material);
            }
            else {
                ArrayList<Material> aux = new ArrayList<Material>();
                aux.add(material);
                materialesPorMateria.put(material.materia(),aux);
            }
            /******************por nivel**********************/
            if (materialesPorNivel.containsKey(material.nivel())){
                materialesPorNivel.get(material.nivel()).add(material);
            }
            else {
                ArrayList<Material> aux = new ArrayList<Material>();
                aux.add(material);
                materialesPorNivel.put(material.nivel(),aux);
            }
            /*****************por tipo*************************/
            for (int i=0; i<material.tipo().size();i++) {
                String unTipo = material.tipo().get(i);
                if (materialesPorTipo.containsKey(unTipo)) {
                    materialesPorTipo.get(unTipo).add(material);
                } else {
                    ArrayList<Material> aux = new ArrayList<Material>();
                    aux.add(material);
                    materialesPorTipo.put(unTipo, aux);
                }
            }
            /***********por tipo de impresion*******************/
            if (materialesPorTipoImpresion.containsKey(material.tipoImpresion())){
                materialesPorTipoImpresion.get(material.tipoImpresion()).add(material);
            }
            else {
                ArrayList<Material> aux = new ArrayList<Material>();
                aux.add(material);
                materialesPorTipoImpresion.put(material.tipoImpresion(),aux);
            }

            /***********por nombre******************************/
            if (!materialesPorNombre.containsKey(material.nombre())){
                materialesPorNombre.put(material.nombre(),material);
            }


        }








    }

    public ArrayList<String> getListaPalabrasClave(){

        return new ArrayList<String>(materialesPorPalabraClave.keySet());
    }

    public ArrayList<String> getMaterialesConPalabraClave(String palabra){
        ArrayList<String> aux = new ArrayList<String>();

        HashMap<String,String> auxMap = new HashMap<>();

        if (!materialesPorPalabraClave.containsKey(palabra) ) return aux;
        for (Material m : materialesPorPalabraClave.get(palabra)){
            auxMap.put(m.nombre(),"");

        }
        aux.addAll(auxMap.keySet());
        return aux;
    }

    public Material getMaterialConNombre(String nombreMaterialAux) {

        return materialesPorNombre.get(nombreMaterialAux);
    }
    public ArrayList<String> getMaterias(){
        return new ArrayList<String>(materialesPorMateria.keySet());
    }
    public ArrayList<Material> getMatrialPorMateria(String nombreMateria){
        return materialesPorMateria.get(nombreMateria);
    }

    public ArrayList<String> getTemasDe(String nombreMat) {
        ArrayList<String> aux = new ArrayList<>();
        for (Material m : materialesPorMateria.get(nombreMat)){
            for (String tema: m.getTemas()){
                if (!aux.contains(tema)) aux.add(tema);
            }
        }
        return aux;
    }
    public ArrayList<String> getNiveles() {

        return new ArrayList<String>(materialesPorNivel.keySet());

    }

    public ArrayList<String> getTipos() {
        return new ArrayList<String>(materialesPorTipo.keySet());
    }

    public ArrayList<String> getTipoImpresion() {
        return new ArrayList<String>(materialesPorTipoImpresion.keySet());
    }

    public ArrayList<String> getMaterialesConPalabrasClave(ArrayList<String> filtros) {

        ArrayList<String> aux = new ArrayList<String>();

        HashMap<String,String> auxMap = new HashMap<>();


        for (Material m : materiales){

            if (m.tieneFiltros(filtros))
                if (!aux.contains(m.nombre())){
                    aux.add(m.nombre());

                }

        }

        return aux;
    }
}

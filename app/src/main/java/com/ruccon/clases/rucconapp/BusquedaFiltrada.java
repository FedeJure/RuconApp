package com.ruccon.clases.rucconapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class BusquedaFiltrada extends AppCompatActivity {
    private HashMap<String,ArrayList<String>> filtrosAplicados;
    private ArrayList<Material> listaMateriales;
    private ListView lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_filtrada);

        filtrosAplicados = (HashMap<String, ArrayList<String>>) getIntent().getExtras().get("filtrosAplicados");
        try {
            listaMateriales = MaterialesRuccon.getInstance().materiales;
        } catch (IOException e) {
            e.printStackTrace();
        }
        lista = (ListView) findViewById(R.id.listaFiltrados);

        filtrarMateriales();
        AdaptadorListaFiltrada adaptador = new AdaptadorListaFiltrada(this.getApplicationContext(),crearLista());
        lista.setAdapter(adaptador);




    }


    private String[] crearLista() {
        ArrayList<String> aux = new ArrayList<>();
        String[] l = {};
        for (Material m : listaMateriales) {
            String s = m.nombre() + "\n" + m.materia() + "\n" + m.tipoImpresion();
            aux.add(s);
        }
        return aux.toArray(l);
    }

    private void filtrarMateriales() {
        ArrayList<Material> aux = (ArrayList<Material>) listaMateriales.clone();
        for (String s : filtrosAplicados.get("materia")){
            for (Material m : listaMateriales){

                if (!m.materia().equals(s)) {
                    aux.remove(m);
                }
            }

        }
        for (String s : filtrosAplicados.get("tema")){
            Log.d("DEBUG",s);
            for (Material m : listaMateriales){
                if (!m.getTemas().contains(s)) {
                    aux.remove(m);
                }

            }

        }
        for (String s : filtrosAplicados.get("nivel")){
            for (Material m : listaMateriales){

                if (!m.nivel().equals(s)) {
                    aux.remove(m);
                }

            }

        }
        for (String s : filtrosAplicados.get("tipo")){
            for (Material m : listaMateriales){

                if (!m.tipo().contains(s)) {
                    aux.remove(m);
                }

            }

        }
        for (String s : filtrosAplicados.get("tipoImpresion")){
            for (Material m : listaMateriales){

                if (!m.tipoImpresion().equals(s)) {
                    aux.remove(m);
                }

            }

        }
        listaMateriales = aux;
    }



}




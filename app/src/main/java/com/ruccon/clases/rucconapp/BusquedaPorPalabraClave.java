package com.ruccon.clases.rucconapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class BusquedaPorPalabraClave extends AppCompatActivity {
    private ListView lista;
    private ArrayList<String> listaMateriales;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_por_palabra_clave);

        try {
            listaMateriales = MaterialesRuccon.getInstance().getMaterialesConPalabraClave((String)getIntent().getExtras().get("palabraClave")+",,");
        } catch (IOException e) {
            e.printStackTrace();
        }

        lista = (ListView) findViewById(R.id.listaMateriales);
        String[] aux = {};
        AdaptadorListaFiltrada adaptador = new AdaptadorListaFiltrada(this.getApplicationContext(),listaMateriales.toArray(aux));
        lista.setAdapter(adaptador);
    }
}

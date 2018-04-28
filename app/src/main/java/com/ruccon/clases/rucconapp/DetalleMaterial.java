package com.ruccon.clases.rucconapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DetalleMaterial extends AppCompatActivity {
    TextView nombreMaterial;
    TextView codigo;
    TextView materia;
    TextView tipoMaterial;
    TextView palabrasClave;
    String textoIngresado = "";
    ListView listaPalabrasClave;

    MaterialesRuccon materiales;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_material);
        nombreMaterial = (TextView)findViewById(R.id.nombr_material);
        codigo = (TextView) findViewById(R.id.numero_codigo);
        materia = (TextView) findViewById(R.id.materia);
        tipoMaterial = (TextView) findViewById(R.id.tipoMaterial);
        palabrasClave = (TextView) findViewById(R.id.palabra_clave);
        String nombreMaterialAux =getIntent().getExtras().getString("nombreMaterial");
        nombreMaterial.setText(nombreMaterialAux);
        textoIngresado = getIntent().getExtras().getString("textoIngresado");
        listaPalabrasClave = (ListView) findViewById(R.id.listaPalabrasClave);



        try {
            materiales = MaterialesRuccon.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Material miMaterial = materiales.getMaterialConNombre(nombreMaterialAux);
        if (miMaterial == null) {
            Log.d("DEBUG",nombreMaterialAux);
            return;

        }
        codigo.setText(miMaterial.codigo());
        materia.setText(miMaterial.materia());
        tipoMaterial.setText(miMaterial.tipo().toString());

        crearListaPalabrasClaveConPagina();




    }

    private void crearListaPalabrasClaveConPagina() {
        ArrayList<String> palabrasClavePosibles = new ArrayList<>();
        Comparator comparador;
        if (textoIngresado == null || textoIngresado == ""){
            palabrasClavePosibles = materiales.getMaterialConNombre(nombreMaterial.getText().toString()).getTemasConPagina();
            Collections.sort(palabrasClavePosibles,
                    new Comparator<String>()
                    {
                        public int compare(String f1, String f2)
                        {
                            return f1.toString().compareTo(f2.toString());
                        }
                    });
        }
        else {
            String[] textosBuscado = textoIngresado.split(" ");

            for (String s : textosBuscado) {
                palabrasClavePosibles.addAll(materiales.getTiposQueContenganPalabraClaveDeMaterial(s, nombreMaterial.getText().toString()));
            }
            Collections.sort(palabrasClavePosibles, new PalabrasClaveComparator(textoIngresado));
        }
        String[] palabras = new String[palabrasClavePosibles.size()];
        palabrasClavePosibles.toArray(palabras);

        ArrayAdapter adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, palabras);
        listaPalabrasClave.setAdapter(adaptador);

    }
}

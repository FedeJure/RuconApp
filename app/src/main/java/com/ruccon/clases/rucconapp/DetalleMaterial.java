package com.ruccon.clases.rucconapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

public class DetalleMaterial extends AppCompatActivity {
    TextView nombreMaterial;
    TextView codigo;
    TextView materia;
    TextView tipoMaterial;
    TextView palabrasClave;
    TextView pagina;

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
        //pagina = (TextView) findViewById(R.id.pagina);
        String nombreMaterialAux = (String)getIntent().getExtras().getString("nombreMaterial");
        //String palabraClave = (String)getIntent().getExtras().getString("palabraClave");
        nombreMaterial.setText(nombreMaterialAux);


        materiales = MaterialesRuccon.getInstance();
        Material miMaterial = materiales.getMaterialConNombre(nombreMaterialAux);
        if (miMaterial == null) {
            Log.d("DEBUG",nombreMaterialAux);
            return;

        }
        codigo.setText(miMaterial.codigo());
        materia.setText(miMaterial.materia());
        tipoMaterial.setText(miMaterial.tipo().toString());
       /* palabrasClave.setText(palabraClave);
        try {
            if (miMaterial.paginaDe(palabraClave).isEmpty()) {
                pagina.setText("No especificado");
            } else {
                pagina.setText(miMaterial.paginaDe(palabraClave));
            }
        }catch(NullPointerException e){

        }*/



    }
}

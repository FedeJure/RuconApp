package com.ruccon.clases.rucconapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class ElegirTipoMaterial extends AppCompatActivity {
    private String nombreMateria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir_tipo_material);
        nombreMateria = getIntent().getStringExtra("nombreMateria");
        ArrayList<Material> materiales = null;
        try {
            materiales = MaterialesRuccon.getInstance().getMatrialPorMateria(nombreMateria);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> tipos = new ArrayList<>();

        GridLayout grid = (GridLayout) this.findViewById(R.id.grillaBotonesTipos);
        grid.setColumnCount(3);
        int buttonWidth = grid.getLayoutParams().width / grid.getColumnCount();
        for (Material mat : materiales) {
            if (!tipos.contains(mat.tipo())) {
                for (String tipo : mat.tipo()){
                    Button miBoton = new Button(this);
                    miBoton.setHeight(buttonWidth);
                    miBoton.setWidth(buttonWidth); //ancho
                    miBoton.setPadding(0, 0, 0, 0);
                    miBoton.setText( tipo, TextView.BufferType.SPANNABLE);
                    miBoton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });

                    grid.addView(miBoton);

                }

            }
        }
    }

}

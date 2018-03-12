package com.ruccon.clases.rucconapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.Locale;

import static android.R.id.text1;
import static android.R.layout.simple_list_item_1;

/**
 * Created by Fede on 31/1/2018.
 */

public class AdaptadorListaFiltrada extends ArrayAdapter<String> {
    private final String[] lista;
    private final Context context;
    public AdaptadorListaFiltrada(Context context, String[] lista) {

        super(context,-1,lista);
        this.lista = lista;

        this.context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.objetolista, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.titulo);
        TextView textView1 = (TextView) rowView.findViewById(R.id.secondLine);
        final String[] textoCompleto = lista[position].split("\n");

        textView.setText(textoCompleto[0]);
        textView1.setText(textoCompleto[1]+","+textoCompleto[2]);
        // change the icon for Windows and iPhone
        String s = lista[position];

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG",textoCompleto[0]);
                Intent intent = new Intent(context,DetalleMaterial.class);
                intent.putExtra("tipoMaterial",textoCompleto[0]);
                intent.putExtra("nombreMaterial",textoCompleto[0]);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return rowView;
    }
}

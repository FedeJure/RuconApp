package com.ruccon.clases.rucconapp;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Fede on 24/11/2017.
 */

public class ListViewAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<String> lista;
    ArrayList<String> listaAux;
    ArrayList<String> nombresMatUsados;
    ListView vistaLista;
    MaterialesRuccon materiales;



    public ListViewAdapter (Context context, ArrayList<String> lista, ListView vistaLista,MaterialesRuccon materiales){
        this.vistaLista = vistaLista;
        this.context = context;
        this.lista = lista;  // lista con palabras clave
        inflater = LayoutInflater.from(this.context);
        listaAux = new ArrayList<String>();
        listaAux.addAll(lista);
        nombresMatUsados = new ArrayList<String>();
        vistaLista.setVisibility(View.INVISIBLE);
        this.materiales = materiales;
    }
    public static class ViewHolder {
        TextView palabraClave;
        TextView tipoMaterial;

    }

    @Override
    public int getCount() {
        return lista.size();

    }

    @Override
    public String getItem(int position) {
        return lista.get(position);

    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.activity_item_de_lista,null);
            holder.palabraClave = (TextView) convertView.findViewById(R.id.palabra_clave);
            holder.tipoMaterial = (TextView) convertView.findViewById(R.id.tipoMaterial_itemLista) ;
            convertView.setTag(holder);

        }
        else{

            holder =  (ViewHolder) convertView.getTag();
        }


        //holder.palabraClave.setText(lista.get(position));
        holder.palabraClave.setText(lista.get(position));
        Material material = materiales.getMaterialConNombre(lista.get(position));
        String tipoMat = "";
        if (material != null) {
            tipoMat = materiales.getMaterialConNombre(lista.get(position)).tipo().toString();

            ImageView icono = (ImageView) convertView.findViewById(R.id.iconoItem);
            if (material.esLibro()) {
                icono.setImageResource(R.drawable.book);
            }
            else if (material.esImpresion()){
                icono.setImageResource(R.drawable.fotocopia);
            }
        }

        holder.tipoMaterial.setText(tipoMat);
        setListaMateriales(convertView,lista.get(position));
        View.OnClickListener onClick = new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DetalleMaterial.class);
                intent.putExtra("nombreMaterial",lista.get(position));
                context.startActivity(intent);
            }

        };
        convertView.setOnClickListener(onClick);




        return convertView;

    }

    private void setListaMateriales(View view, final String palabraClave) {
        /*final ListView listaMateriales = (ListView) view.findViewById(R.id.listaMateriales);

        //final String[] aux = materiales.getMaterialesConPalabraClave(palabraClave).toArray(new String[]{});
        final String[] aux = {palabraClave};


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), simple_list_item_1,text1,aux);
        listaMateriales.setAdapter(adapter);



        AdapterView.OnItemClickListener onClick = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // MODIFICAR ESTO PARA QUE ENGRESE AL DETALLE DE UN MATERIAL ESPECIFICO+

                String tipoMaterial = aux[position];



                Intent intent = new Intent(context,DetalleMaterial.class);
                intent.putExtra("tipoMaterial",tipoMaterial);
                intent.putExtra("palabraClave",palabraClave);

                context.startActivity(intent);
            }


        };

        listaMateriales.setOnItemClickListener(onClick);*/
    }

    public void filter(String charText){


        vistaLista.setVisibility(View.VISIBLE);
        charText = normalizarTexto(charText);
        String[] filtros = charText.split(" ");
        lista.clear();
        nombresMatUsados.clear();
        for (String unFiltro : filtros) {


            ArrayList<String> listaFiltros = new ArrayList<String>();
            if (unFiltro.length() != 0) {
                for (String s : listaAux) {
                    for (String filtro : parsearString(charText)) {

                        if (normalizarTexto(s).contains(normalizarTexto(filtro))) {
                            if (!listaFiltros.contains(s)) {
                                listaFiltros.add(s);

                            }

                        }

                    }
                }
                if (listaFiltros.isEmpty()) return;

                for (String s1 : materiales.getMaterialesConPalabrasClave(listaFiltros)) {

                    if (!nombresMatUsados.contains(s1)) {
                        nombresMatUsados.add(s1);

                        lista.add(s1);
                    }
                }

            }
        }
       /* Set<String> hs = new HashSet<>();
        hs.addAll(lista);
        lista.clear();
        lista.addAll(hs);
*/

        notifyDataSetChanged();
    }

    public static String normalizarTexto(String cadena){
        String limpio =null;
        if (cadena !=null) {
            String valor = cadena;
            valor = valor.toUpperCase();
            // Normalizar texto para eliminar acentos, dieresis, cedillas y tildes
            limpio = Normalizer.normalize(valor, Normalizer.Form.NFD);
            // Quitar caracteres no ASCII excepto la enie, interrogacion que abre, exclamacion que abre, grados, U con dieresis.
            limpio = limpio.replaceAll("[^\\p{ASCII}(N\u0303)(n\u0303)(\u00A1)(\u00BF)(\u00B0)(U\u0308)(u\u0308)]", "");
            // Regresar a la forma compuesta, para poder comparar la enie con la tabla de valores
            limpio = Normalizer.normalize(limpio, Normalizer.Form.NFC);
            limpio = limpio.toLowerCase(Locale.getDefault());
        }
        return limpio;
    }

    private String[] parsearString(String string){
        String delims = " ";
        String[] aux = string.split(delims);
        for(String s: aux){


        }
        return aux;
    }

}

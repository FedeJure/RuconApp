package com.ruccon.clases.rucconapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Parcelable;
import android.os.Vibrator;
import android.provider.CalendarContract;
import android.support.annotation.RequiresApi;
import android.support.v4.util.CircularArray;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;

import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SearchView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private MaterialesRuccon materiales;
    private SearchView barraDeBusqueda;
    private ListView listView;
    ListViewAdapter listAdapter;
    GridLayout grillaDeBotones;
    ViewFlipper flipper;
    static Vibrator  vibrador;

    private ManejadorBaseDeDatos manejadorDB;

    private static Context appContext;

    private HashMap<String,ArrayList<String>> filtrosAplicados;
    GridLayout filtrosMateria;
    GridLayout filtrosTema ;
    GridLayout filtrosTipo;
    GridLayout filtrosTipoImpresion;
    GridLayout filtrosNivel;
    private Button botonIzquierda;
    private Button botonDerecha;
    private Button botonBuscar;
    TextView tipoDeFiltro;



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContext = this.getApplicationContext();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        manejadorDB = ManejadorBaseDeDatos.instancia();
        filtrosAplicados = new HashMap<>();
        vibrador = ((Vibrator) getSystemService(VIBRATOR_SERVICE));
        vibrador.cancel();


        materiales = MaterialesRuccon.getInstance();


        tipoDeFiltro = (TextView) findViewById(R.id.TipoDeFiltro);

        listView = (ListView) findViewById(R.id.listview);
        listAdapter = new ListViewAdapter(this,materiales.getListaPalabrasClave(),listView,materiales);

        listView.setAdapter(listAdapter);
        listView.setVisibility(View.INVISIBLE);

        barraDeBusqueda = (SearchView) findViewById(R.id.barra_de_busqueda);
        barraDeBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.vibrar(50L);
            }
        });
        barraDeBusqueda.setOnQueryTextListener(this);

        flipper = (ViewFlipper) findViewById(R.id.flipper);
        botonDerecha = (Button) findViewById(R.id.botonDerecha);
        botonIzquierda = (Button) findViewById(R.id.botonIzquierda);
        botonBuscar = (Button) findViewById(R.id.botonBuscar);
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);

        flipper.setInAnimation(in);
        flipper.setOutAnimation(out);





        flipper.setPadding(0,0,0,20);
        flipper.setOnTouchListener(new DeslizarTouchListener(flipper,tipoDeFiltro));
        ((ScrollView)findViewById(R.id.scroll_view_temas)).setOnTouchListener(new DeslizarTouchListener(flipper,tipoDeFiltro));

        botonDerecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.vibrar(50L);
                flipper.showNext();
                tipoDeFiltro.setText((String)flipper.getCurrentView().getTag());


            }
        });

        botonIzquierda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.vibrar(50L);
                flipper.showPrevious();
                tipoDeFiltro.setText((String)flipper.getCurrentView().getTag());
            }
        });
        final Context thiscontext = this;


        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.vibrar(50L);
                Intent intent = new Intent(appContext,BusquedaFiltrada.class);
                intent.putExtra("filtrosAplicados",filtrosAplicados);
                thiscontext.startActivity(intent);




            }
        });
        filtrosMateria = (GridLayout) flipper.findViewById(R.id.filtrosMateria);
        filtrosTema = (GridLayout) flipper.findViewById(R.id.filtrosTema);
        filtrosNivel = (GridLayout) flipper.findViewById(R.id.filtrosNivel);
        filtrosTipo = (GridLayout) flipper.findViewById(R.id.filtrosTipo);
        filtrosTipoImpresion = (GridLayout) flipper.findViewById(R.id.filtrosTipoImpresion);
        filtrosMateria.setColumnCount(2);
        filtrosTema.setColumnCount(1);
        filtrosNivel.setColumnCount(2);
        filtrosTipo.setColumnCount(2);
        filtrosTipoImpresion.setColumnCount(2);

        filtrosAplicados.put("materia",new ArrayList<String>());
        filtrosAplicados.put("tema",new ArrayList<String>());
        filtrosAplicados.put("nivel",new ArrayList<String>());
        filtrosAplicados.put("tipo",new ArrayList<String>());
        filtrosAplicados.put("tipoImpresion",new ArrayList<String>());



        setearFiltrosMateriasYTemas();
        setearFiltrosNivel();
        setearFiltrosTipo();
        setearFiltrosTipoImpresion();





    }

    private void setearFiltrosMateriasYTemas() {
        for (final String nombreMat : materiales.getMaterias()){
            CheckBox b = crearChecBox(nombreMat);
            b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        filtrosAplicados.get("materia").add(nombreMat);
                        //ArrayList<String> aux=materiales.getTemasDe(nombreMat);
                        //filtrosAplicados.get("tema").addAll(aux);
                        ArrayList<String> aux=materiales.getTemasDe(nombreMat);
                        filtrosAplicados.get("tema").add(aux.get(0));


                        agregarFiltroTemaCon(aux);
                    }

                    if (!isChecked){
                        filtrosAplicados.get("materia").remove(nombreMat);
                        //ArrayList<String> aux=materiales.getTemasDe(nombreMat);
                        //filtrosAplicados.get("tema").removeAll(aux);
                        ArrayList<String> aux=materiales.getTemasDe(nombreMat);
                        filtrosAplicados.get("tema").remove(aux.get(0));
                        quitarFiltroTemaCon(aux);
                    }
                }
            });


            filtrosMateria.addView(b);


        }
    }
    private void setearFiltrosTipoImpresion() {
        for (final String tipoImpresion: materiales.getTipoImpresion()){
            CheckBox b = crearChecBox(tipoImpresion);
            b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        filtrosAplicados.get("tipoImpresion").add(tipoImpresion);
                    }
                    if (!isChecked){
                        filtrosAplicados.get("tipoImpresion").remove(tipoImpresion);
                    }
                }
            });
            filtrosTipoImpresion.addView(b);
        }
    }
    private void setearFiltrosNivel(){
        for (final String nivel : materiales.getNiveles()) {
            CheckBox b = crearChecBox(nivel);
            b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        filtrosAplicados.get("nivel").add(nivel);
                    }
                    if (!isChecked){
                        filtrosAplicados.get("nivel").remove(nivel);
                    }
                }
            });
            filtrosNivel.addView(b);
        }
    }
    private void setearFiltrosTipo(){
        for (final String tipo: materiales.getTipos()){
            CheckBox b = crearChecBox(tipo);
            b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        filtrosAplicados.get("tipo").add(tipo);
                    }
                    if (!isChecked){
                        filtrosAplicados.get("tipo").remove(tipo);
                    }
                }
            });
            filtrosTipo.addView(b);
        }
    }
    private void agregarFiltroTemaCon(ArrayList<String> aux) {
        int ancho = filtrosTema.getLayoutParams().width/filtrosTema.getColumnCount();
        for (String nombreMat:aux){
            if (Character.isUpperCase( nombreMat.charAt(0)) || Character.isUpperCase( nombreMat.charAt(1))) {


                CheckBox b = crearChecBox(nombreMat);
                filtrosTema.addView(b);
            }
        }

    }
    private void quitarFiltroTemaCon(ArrayList<String> aux){
        for (String nombreMat:aux){
            for (int i=0;i<filtrosTema.getChildCount();i++){
                CheckBox c = (CheckBox)filtrosTema.getChildAt(i);
                if (c.getText().toString() == nombreMat){
                    filtrosTema.removeView(c);
                }
            }
        }
    }



    @Override
    public boolean onQueryTextSubmit(String query) {
        listView.setVisibility(View.INVISIBLE);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        String text = newText;
        listAdapter.filter(text);
        return false;
    }
    public static Context getContext(){
        return appContext;
    }

    public static void mostrarMensaje(String mensaje){
        CharSequence cs = mensaje;
        Toast mensajeAMostrar =Toast.makeText(appContext.getApplicationContext(),cs,Toast.LENGTH_SHORT);
        mensajeAMostrar.setMargin(mensajeAMostrar.getXOffset(),20);
        mensajeAMostrar.show();
    }
    private CheckBox crearChecBox(String texto) {
        CheckBox b = new CheckBox(appContext);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.vibrar(50L);
            }
        });
        b.setPadding(5,5,5,5);
        b.setBackgroundColor(Color.parseColor("#ff0099cc"));
        b.setText(texto);
        b.setLinkTextColor(000000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            b.setElevation(8);
        }
        return b;
    }

    public static void vibrar(Long milisegundos){
        if (!vibrador.hasVibrator()) {
            return;
        }

        vibrador.vibrate(milisegundos);
    }

}



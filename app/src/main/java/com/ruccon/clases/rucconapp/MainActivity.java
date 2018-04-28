package com.ruccon.clases.rucconapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
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
import android.view.Window;
import android.view.WindowManager;
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

import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SearchView;
import android.widget.ViewFlipper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private MaterialesRuccon materiales;
    private SearchView barraDeBusqueda;
    private ListView listView;
    ListViewAdapter listAdapter;
    ViewFlipper flipper;
    static Vibrator  vibrador;
    private static Context appContext;
    private static AppCompatActivity miInstancia;

    private HashMap<String,ArrayList<String>> filtrosAplicados;
    GridLayout filtrosMateria;
    GridLayout filtrosTema ;
    GridLayout filtrosTipo;
    GridLayout filtrosTipoImpresion;
    GridLayout filtrosNivel;
    private Button botonBuscar;
    private Button botonActualizar;



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContext = this.getApplicationContext();
        miInstancia = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        filtrosAplicados = new HashMap<>();
        vibrador = ((Vibrator) getSystemService(VIBRATOR_SERVICE));
        vibrador.cancel();

        try {
            materiales = MaterialesRuccon.getInstance();
        } catch (IOException e) {
            mostrarMensajeLargo("No se pudo cargar la base de datos");
            return;
        } catch (android.os.NetworkOnMainThreadException e){
            mostrarMensajeLargo("El servidor esta caido, vuelve a intentarlo mas tarde");
        }


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

        botonActualizar = (Button) findViewById(R.id.botonActualizar);
        botonActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManejadorBaseDeDatos.instancia().actualizarBaseDeDatos();
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
            }
        });

        botonBuscar = (Button) findViewById(R.id.botonBuscar);
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);





        flipper.setPadding(0,0,0,20);
        flipper.setOnTouchListener(new DeslizarTouchListener(flipper,this));
        final ScrollView scrollTemas = ((ScrollView)findViewById(R.id.scroll_view_temas));
        scrollTemas.setOnTouchListener(new DeslizarTouchListener(flipper,this,scrollTemas));
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
        filtrosMateria.setColumnCount(1);
        filtrosTema.setColumnCount(1);
        filtrosNivel.setColumnCount(1);
        filtrosTipo.setColumnCount(1);
        filtrosTipoImpresion.setColumnCount(1);

        filtrosAplicados.put("materia",new ArrayList<String>());
        filtrosAplicados.put("tema",new ArrayList<String>());
        filtrosAplicados.put("nivel",new ArrayList<String>());
        filtrosAplicados.put("tipo",new ArrayList<String>());
        filtrosAplicados.put("tipoImpresion",new ArrayList<String>());



        setearFiltrosMateriasYTemas();
        setearFiltrosNivel();
        setearFiltrosTipo();
        setearFiltrosTipoImpresion();
        if(getCurrentFocus() != null){
            getCurrentFocus().clearFocus();
        }

    }


    private void setearFiltrosMateriasYTemas() {
        for (final String nombreMat : materiales.getMaterias()){
            GridLayout b = crearChecBox(nombreMat);
            ((CheckBox)b.getChildAt(0)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        filtrosAplicados.get("materia").add(nombreMat);
                        //ArrayList<String> aux=materiales.getTemasDe(nombreMat);
                        //filtrosAplicados.get("tema").addAll(aux);
                        ArrayList<String> aux=materiales.getTemasDe(nombreMat);
                        agregarFiltroTemaCon(aux);
                    }

                    if (!isChecked){
                        filtrosAplicados.get("materia").remove(nombreMat);
                        //ArrayList<String> aux=materiales.getTemasDe(nombreMat);
                        //filtrosAplicados.get("tema").removeAll(aux);
                        ArrayList<String> aux=materiales.getTemasDe(nombreMat);
                        quitarFiltroTemaCon(aux);
                    }
                }
            });


            filtrosMateria.addView(b);


        }
    }
    private void setearFiltrosTipoImpresion() {
        for (final String tipoImpresion: materiales.getTipoImpresion()){
            GridLayout b = crearChecBox(tipoImpresion);
            ((CheckBox)b.getChildAt(0)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

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
            GridLayout b = crearChecBox(nivel);
            ((CheckBox)b.getChildAt(0)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

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
            GridLayout b = crearChecBox(tipo);
            ((CheckBox)b.getChildAt(0)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

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
        for (final String nombreMat:aux){
            if (Character.isUpperCase( nombreMat.charAt(0)) || Character.isUpperCase( nombreMat.charAt(1))) {
                GridLayout b = crearChecBox(nombreMat);
                ((CheckBox)b.getChildAt(0)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            filtrosAplicados.get("tema").add(nombreMat);
                        }
                        if (!isChecked){
                            filtrosAplicados.get("tema").remove(nombreMat);
                        }
                    }
                });
                filtrosTema.addView(b);
            }
        }
    }
    private void quitarFiltroTemaCon(ArrayList<String> aux){
        for (String nombreMat:aux){
            for (int i=1;i<filtrosTema.getChildCount();i++){
                GridLayout conjuntoChecbox = (GridLayout) filtrosTema.getChildAt(i);
                TextView c = (TextView) conjuntoChecbox.getChildAt(1);
                if (c.getText().toString() == nombreMat){
                    filtrosTema.removeView(conjuntoChecbox);
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
    public static void mostrarMensajeLargo(String mensaje){
        CharSequence cs = mensaje;
        Toast mensajeAMostrar =Toast.makeText(appContext.getApplicationContext(),cs,Toast.LENGTH_LONG);
        mensajeAMostrar.show();
    }


    private GridLayout crearChecBox(String texto) {
        GridLayout grid = new GridLayout(appContext);
        CheckBox b = new CheckBox(appContext);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.vibrar(50L);
            }
        });
        b.setPadding(5,5,5,5);
        b.setTranslationX(10);
        b.setLinkTextColor(000000);

        TextView text = new TextView(appContext);
        text.setText(texto);
        text.setTextColor(Color.BLACK);
        grid.setColumnCount(2);
        grid.addView(b);
        grid.addView(text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            b.setElevation(8);
        }
        return grid;

    }

    public static void vibrar(Long milisegundos){
        if (!vibrador.hasVibrator()) {
            return;
        }

        //vibrador.vibrate(milisegundos);
    }
    public static AppCompatActivity getInstance(){
        return miInstancia;
    }

    @Override
    public void onBackPressed() {
        if (listView.getVisibility() != View.GONE) {
            listView.setVisibility(View.GONE);
            return;
        }
        // Otherwise defer to system default behavior.
        super.onBackPressed();
    }
}



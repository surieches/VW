package com.androidhive.androidsqlite;

import android.app.Activity;
import android.content.Context;
import android.database.SQLException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.*;
import java.io.IOException;
import java.util.List;

public class MyAndroid extends Activity {

    /**
     * Called when the activity is first created.
     */
    private DataBaseAndroid myDbHelper;
    private Context context;
    private FarmaciaBean fb;
    //EditText
    private EditText MedicamentoName;
    //Spinner 
    private Spinner ProductoName;
    private Spinner ProductoPresentacion;
    //RadioButtons
    private RadioButton Medicamento_Receta_No;//Seccion 5_2
    private RadioButton Medicamento_Receta_Si;//Seccion 5_2
    
    //TableRow
    private TableRow tableRow5_3;//Seccion 5_3
    private TableRow tableRow5_4;//Seccion 5_4
    
    private boolean pendienteFarmacia = false; 
    private boolean canasta = false;
    private int idusu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encuesta);
        context = this;
        
        idusu = Integer.parseInt(Login.usu.getIdusu());
        
        if(ActivityMagg.fb != null){             
            this.fb = ActivityMagg.fb;
            /*Toast.makeText(MyAndroid.this,"Usuario: "+idusu+" "+fb.getFarmacia()+" - "+fb.getColonia()+" - "+fb.getTipoCadena()+" - "+
                    fb.getTipo2()+" - "+fb.getCP()+" - "+fb.getAgeb()+" - "+fb.getCiudad(), Toast.LENGTH_SHORT).show();*/
            ActivityMagg.fb = null;
        }
        else if(NuevaFarmacia.fb != null){            
            pendienteFarmacia = true;
            this.fb = NuevaFarmacia.fb;
            /*Toast.makeText(MyAndroid.this,"Usuario: "+idusu+" "+fb.getFarmacia()+" - "+fb.getColonia()+" - "+fb.getTipoCadena()+" - "+
                    " - "+fb.getDireccion()+" - "+fb.getCP()+" - "+fb.getMunicipio()+" - "+fb.getCiudad(), Toast.LENGTH_SHORT).show();*/
            NuevaFarmacia.fb = null;
        }
        else
            Toast.makeText(this, "Error en la aplicación. Favor de Reiniciar.", Toast.LENGTH_SHORT).show();
        /*this.fb = ActivityMagg.fb;
        Toast.makeText(MyAndroid.this,fb.getFarmacia()+" - "+fb.getColonia()+" - "+fb.getTipoCadena()+" - "+
                fb.getTipo2()+" - "+fb.getCP()+" - "+fb.getAgeb()+" - "+fb.getCiudad(), Toast.LENGTH_SHORT).show();*/
        
        myDbHelper = new DataBaseAndroid(this);
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
        }
        fullSpinnerOptions();//Para llenar las opciones de los Spinner.
        addKeyListener();
        addRadioGroupListener();
        myDbHelper.close();
        
    }

    /**
     * Sirve para cuando da enter y se llenen los spinner.
     *
     * @version 1.0 @autor MaximusPegasus
     */
    private void addKeyListener() {
        MedicamentoName = (EditText) findViewById(R.id.MedicamentoName);
        MedicamentoName.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //LLenamos el spinner de producto
                    actualizarSpinnerProductoName();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Agregamos el evento change del Spinner ProductoName Nota es necesario que
     * para que se llame despues de haber llenado la data.
     *
     * @version 1.0 @autor Maximus Pegasus
     */
    private void eventsSpinners() {
        ProductoPresentacion = (Spinner) findViewById(R.id.ProductoPresentacion);//Encontramos el spinner de la presentación
        ProductoName.setOnItemSelectedListener(new OnItemSelectedListener() {

            int count = 0;
            String[] columna = {"presentacion"};

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (count >= 1) {
                    //LLenamos el spinner de producto
                    List productos = myDbHelper.getColumna("productos", columna, "productos.descripcion like '" + ProductoName.getSelectedItem() + "'");//Se obtiene la lista de los resultados.
                    if (productos.get(0).equals("")) {
                        productos.add(0, "No hay presentación");
                        productos.remove(1);
                    } else {
                        productos.add(0, "Seleccione Presentación");//añadimos para que seleccione el usuario
                    }
                    ArrayAdapter<String> data = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, productos);
                    data.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    ProductoPresentacion.setAdapter(data);//Se le pone la información de los resultados.
                }
                count++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /**
     * Para actualizar el Spinner de Nombres de Producto.
     *
     * @version 1.0 @autor MaximusPegasus
     * @see método addkeylistener cuando presionas enter.
     */
    public void actualizarSpinnerProductoName() {
        //si no es nulo
        if (!(MedicamentoName.getText().toString().equals(""))) {
            String[] columna = {"descripcion"};//Vamos a obtener la columna de descripcion
            ProductoName = (Spinner) findViewById(R.id.ProductoName);//Encontramos el spinner de productos
            List productos = myDbHelper.getColumna("productos", columna, "descripcion like '%" + MedicamentoName.getText() + "%'");//Se obtiene la lista de los resultados.
            productos.add(0, "Seleccione Producto");//añadimos para que seleccione el usuario
            ArrayAdapter<String> data = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, productos);
            data.setDropDownViewResource(android.R.layout.simple_spinner_item);
            ProductoName.setAdapter(data);//se llena el spinner con la data obtenida
            eventsSpinners();//Se agrega el evento para que cambie los spinner
        } else {
            Toast.makeText(context, "Escribe alguna inicial del producto.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sirve para llenar los spinner de las opciones que puede poner el usuario.
     * @autor MaximusPegasus
     *
     * @version 1.0
     */
    private void fullSpinnerOptions() {
        Spinner spinneraux = (Spinner) findViewById(R.id.Precio30pcion);//encontramos el spinner de precio+30
        ArrayAdapter<CharSequence> data = ArrayAdapter.createFromResource(context, R.array.Opciones_Tabla_Precios, android.R.layout.simple_spinner_item);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinneraux.setAdapter(data);
        //Spinner +20
        spinneraux = (Spinner) findViewById(R.id.Precio20pcion);//encontramos el spinner de precio+20
        data = ArrayAdapter.createFromResource(context, R.array.Opciones_Tabla_Precios, android.R.layout.simple_spinner_item);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinneraux.setAdapter(data);
        //Spinner +10
        spinneraux = (Spinner) findViewById(R.id.Precio10pcion);//encontramos el spinner de precio+10
        data = ArrayAdapter.createFromResource(context, R.array.Opciones_Tabla_Precios, android.R.layout.simple_spinner_item);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinneraux.setAdapter(data);
        //Spinner 90
        spinneraux = (Spinner) findViewById(R.id.Precio90pcion);//encontramos el spinner de precio+10
        data = ArrayAdapter.createFromResource(context, R.array.Opciones_Tabla_Precios, android.R.layout.simple_spinner_item);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinneraux.setAdapter(data);
        //Spinner 80
        spinneraux = (Spinner) findViewById(R.id.Precio80pcion);//encontramos el spinner de precio+10
        data = ArrayAdapter.createFromResource(context, R.array.Opciones_Tabla_Precios, android.R.layout.simple_spinner_item);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinneraux.setAdapter(data);
        //Spinner 70
        spinneraux = (Spinner) findViewById(R.id.Precio70pcion);//encontramos el spinner de precio+10
        data = ArrayAdapter.createFromResource(context, R.array.Opciones_Tabla_Precios, android.R.layout.simple_spinner_item);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinneraux.setAdapter(data);
    }

    /**
     * Sirve para iniciar los eventos changed de los radiobuttons. @autor
     * MaximusPegasus
     *
     * @version 1.0
     */
    private void addRadioGroupListener() {
        //iniciamos los botones de Medicamento_Receta
        RadioGroup Medicamento_Receta = (RadioGroup) findViewById(R.id.Medicamento_Receta);
        Medicamento_Receta_Si = (RadioButton) findViewById(R.id.Medicamento_Receta_Si);
        Medicamento_Receta_No = (RadioButton) findViewById(R.id.Medicamento_Receta_No);
        tableRow5_3 = (TableRow) findViewById(R.id.tableRow5_3);
        tableRow5_4 = (TableRow) findViewById(R.id.tableRow5_4);
        Medicamento_Receta.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup rg, int checkedId) {
                for (int i = 0; i < rg.getChildCount(); i++) {
                    if (Medicamento_Receta_Si.getId() == checkedId) {
                        tableRow5_3.setVisibility(View.VISIBLE);
                        tableRow5_4.setVisibility(View.VISIBLE);
                        return;
                    } else if (Medicamento_Receta_No.getId() == checkedId) {
                        tableRow5_3.setVisibility(View.GONE);
                        tableRow5_4.setVisibility(View.GONE);
                        return;

                    } else {
                        Toast.makeText(getBaseContext(), "select atleast one", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}

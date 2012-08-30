package com.androidhive.androidsqlite;

import Beans.FarmaciaBean;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
    private EditText Precio_Pagado_Text;
    private EditText No_Encontro_Text;
    private EditText Venia_Originalmente_Text;//seccion 5_16
    //TextView
    private TextView Precio30_Text;
    private TextView Precio20_Text;
    private TextView Precio10_Text;
    private TextView Precio90_Text;
    private TextView Precio80_Text;
    private TextView Precio70_Text;
    //Spinner 
    private Spinner ProductoName;
    private Spinner ProductoPresentacion;
    private Spinner No_Encontro_Spinner;//sección no encontró medicamento 6_5
    private Spinner Venia_Originalmente_Spinner;//sección 5_17
    //RadioButtons
    private RadioButton Ayudo_Compra_Si;//Ayudo Compra 5_4
    private RadioButton Ayudo_Compra_No;//Ayudo Compra 5_4
    private RadioButton Medicamento_Receta_No;//Seccion 5_2
    private RadioButton Medicamento_Receta_Si;//Seccion 5_2
    private RadioButton Sustituyo_Medicamento_Si;//Ayudo Compra 5_12
    private RadioButton Sustituyo_Medicamento_No;//Ayudo Compra 5_12
    private RadioButton Radio_Encontro_Medicamento_Encuestador_Si;//Seccion 2_4
    private RadioButton Radio_Encontro_Medicamento_Encuestador_No;//Seccion 2_4
    //TableRow
    private TableRow tableRow5_3;//Seccion 5_3
    private TableRow tableRow5_4;//Seccion 5_4
    private TableRow tableRow5_5;//Seccion 5_5
    private TableRow tableRow5_6;//Seccion 5_6
    private TableRow tableRow5_7;//Seccion 5_7
    private TableRow tableRow5_8;//Seccion 5_8
    private TableRow tableRow5_9;//Seccion 5_9
    private TableRow tableRow5_10;//Seccion 5_10
    private TableRow tableRow5_13;//Seccion 5_6
    private TableRow tableRow5_14;//Seccion 5_7
    private TableRow tableRow5_15;//Seccion 5_8
    private TableRow tableRow5_16;//Seccion 5_9
    private TableRow tableRow5_17;//Seccion 5_10
    //TableRow
    private TableRow tableRow2_1;//Seccion 2_1
    private TableRow tableRow2_2;//Seccion 2_2
    private TableRow tableRow2_3;//Seccion 2_3
    private TableRow tableRow2_5;//Seccion 2_1
    private TableRow tableRow2_6;//Seccion 2_2
    //Autor MAGG    
    private boolean canasta = false;
    private int idusu;
    //Para el bean de encuesta
    private BeanEncuesta beanEncuesta = null;
    private int ID_General;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encuesta);
        context = this;

        idusu = Integer.parseInt(Login.usu.getIdusu());

        if (ActivityMagg.fb != null) {
            this.fb = ActivityMagg.fb;
            /*
             * Toast.makeText(MyAndroid.this,"Usuario: "+idusu+"
             * "+fb.getFarmacia()+" - "+fb.getColonia()+" -
             * "+fb.getTipoCadena()+" - "+ fb.getTipo2()+" - "+fb.getCP()+" -
             * "+fb.getAgeb()+" - "+fb.getCiudad(), Toast.LENGTH_SHORT).show();
             */
            ActivityMagg.fb = null;
        } else if (NuevaFarmacia.fb != null) {
            this.fb = NuevaFarmacia.fb;
            /*
             * Toast.makeText(MyAndroid.this,"Usuario: "+idusu+"
             * "+fb.getFarmacia()+" - "+fb.getColonia()+" -
             * "+fb.getTipoCadena()+" - "+ " - "+fb.getDireccion()+" -
             * "+fb.getCP()+" - "+fb.getMunicipio()+" - "+fb.getCiudad(),
             * Toast.LENGTH_SHORT).show();
             */
            NuevaFarmacia.fb = null;
        } else {
            Toast.makeText(this, "Error en la aplicación. Favor de Reiniciar.", Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(MyAndroid.this, "Usuario: " + idusu + " Farmacia Pendiente: " + fb.isPendiente(), Toast.LENGTH_SHORT).show();
        /*
         * this.fb = ActivityMagg.fb;
         * Toast.makeText(MyAndroid.this,fb.getFarmacia()+" -
         * "+fb.getColonia()+" - "+fb.getTipoCadena()+" - "+ fb.getTipo2()+" -
         * "+fb.getCP()+" - "+fb.getAgeb()+" - "+fb.getCiudad(),
         * Toast.LENGTH_SHORT).show();
         */

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
        fullSpinnerOptions();//Para llenar las opciones de los Spinner de +30% hasta -30%.
        addKeyListener();//Para agregar los enter
        addRadioGroupListener();//para los radio.
        buttonValidar();//Para validar la encuesta y enviarla
        myDbHelper.close();//cierre de la bd
    }

    /**
     * Sirve para cuando da enter y se llenen los spinner.
     *
     * @version 1.0
     * @autor MaximusPegasus
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

        Precio_Pagado_Text = (EditText) findViewById(R.id.Precio_Pagado_Text);
        Precio30_Text = (TextView) findViewById(R.id.Precio30_Text);
        Precio20_Text = (TextView) findViewById(R.id.Precio20_Text);
        Precio10_Text = (TextView) findViewById(R.id.Precio10_Text);
        Precio90_Text = (TextView) findViewById(R.id.Precio90_Text);
        Precio80_Text = (TextView) findViewById(R.id.Precio80_Text);
        Precio70_Text = (TextView) findViewById(R.id.Precio70_Text);
        Precio_Pagado_Text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {//Si se sale del foco
                    if (!(Precio_Pagado_Text.getText().toString().equals(""))) {
                        try {
                            double precio = Double.parseDouble(Precio_Pagado_Text.getText().toString());
                            Precio30_Text.setText((String.format("%.2f", precio * 1.3)));
                            Precio20_Text.setText((String.format("%.2f", precio * 1.2)));
                            Precio10_Text.setText((String.format("%.2f", precio * 1.1)));
                            Precio90_Text.setText((String.format("%.2f", precio * 0.9)));
                            Precio80_Text.setText((String.format("%.2f", precio * 0.8)));
                            Precio70_Text.setText((String.format("%.2f", precio * 0.7)));
                            Toast.makeText(context, "Precios Actualizados", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(context, "Escribe algun número.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Escribe algun número.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        No_Encontro_Text = (EditText) findViewById(R.id.No_Encontro_Text);
        No_Encontro_Text.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //LLenamos el spinner de producto
                    actualizarSpinnerProductoNoEncontro();
                    return true;
                }
                return false;
            }
        });

        Venia_Originalmente_Text = (EditText) findViewById(R.id.Venia_Originalmente_Text);
        Venia_Originalmente_Text.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //LLenamos el spinner de producto
                    actualizarSpinnerProductoVeniaOriginalmente();
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
     * @version 1.0
     * @autor MaximusPegasus
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
     * Para actualizar el Spinner de Nombres de productos venia originalmente.
     *
     * @version 1.0
     * @autor MaximusPegasus
     * @see método que es llamado cuando da enter.
     */
    public void actualizarSpinnerProductoVeniaOriginalmente() {
        //si no es nulo
        if (!(Venia_Originalmente_Text.getText().toString().equals(""))) {
            String[] columna = {"descripcion"};//Vamos a obtener la columna de descripcion
            Venia_Originalmente_Spinner = (Spinner) findViewById(R.id.Venia_Originalmente_Spinner);//Encontramos el spinner de productos
            List productos = myDbHelper.getColumna("productos", columna, "descripcion like '%" + Venia_Originalmente_Text.getText() + "%'");//Se obtiene la lista de los resultados.
            if (productos == null) {
                productos = new ArrayList<String>();
                productos.add(0, "No se encontró ningún producto.");
            } else {
                productos.add(0, "Seleccione Producto");//añadimos para que seleccione el usuario
            }
            ArrayAdapter<String> data = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, productos);
            data.setDropDownViewResource(android.R.layout.simple_spinner_item);
            Venia_Originalmente_Spinner.setAdapter(data);//se llena el spinner con la data obtenida
        } else {
            Toast.makeText(context, "Escribe alguna inicial del producto.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Para actualizar el Spinner de Nombres de productos no encontrados.
     *
     * @version 1.0
     * @autor MaximusPegasus
     * @see método que es llamado cuando cambia el foco.
     */
    public void actualizarSpinnerProductoNoEncontro() {
        //si no es nulo
        if (!(No_Encontro_Text.getText().toString().equals(""))) {
            String[] columna = {"descripcion"};//Vamos a obtener la columna de descripcion
            No_Encontro_Spinner = (Spinner) findViewById(R.id.No_Encontro_Spinner);//Encontramos el spinner de productos
            List productos = myDbHelper.getColumna("productos", columna, "descripcion like '%" + No_Encontro_Text.getText() + "%'");//Se obtiene la lista de los resultados.
            if (productos == null) {
                productos = new ArrayList<String>();
                productos.add(0, "No se encontró ningún producto.");
            } else {
                productos.add(0, "Seleccione Producto");//añadimos para que seleccione el usuario
            }
            ArrayAdapter<String> data = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, productos);
            data.setDropDownViewResource(android.R.layout.simple_spinner_item);
            No_Encontro_Spinner.setAdapter(data);//se llena el spinner con la data obtenida
        } else {
            Toast.makeText(context, "Escribe alguna inicial del producto.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Para actualizar el Spinner de Nombres de Producto.
     *
     * @version 1.0
     * @autor MaximusPegasus
     * @see método addkeylistener cuando presionas enter.
     */
    public void actualizarSpinnerProductoName() {
        //si no es nulo
        if (!(MedicamentoName.getText().toString().equals(""))) {
            String[] columna = {"descripcion"};//Vamos a obtener la columna de descripcion
            ProductoName = (Spinner) findViewById(R.id.ProductoName);//Encontramos el spinner de productos
            List productos = myDbHelper.getColumna("productos", columna, "descripcion like '%" + MedicamentoName.getText() + "%'");//Se obtiene la lista de los resultados.
            if (productos == null) {
                productos = new ArrayList<String>();
                productos.add(0, "No se encontró ningún producto.");
            } else {
                productos.add(0, "Seleccione Producto");//añadimos para que seleccione el usuario
            }
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
     *
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
     * Sirve para iniciar los eventos changed de los radiobuttons.
     *
     * @autor MaximusPegasus
     *
     * @version 1.0
     */
    private void addRadioGroupListener() {
        //iniciamos los botones de Medicamento_Receta
        RadioGroup Medicamento_Receta = (RadioGroup) findViewById(R.id.Radio_Medicamento_Receta);
        Medicamento_Receta_Si = (RadioButton) findViewById(R.id.Medicamento_Receta_Si);
        Medicamento_Receta_No = (RadioButton) findViewById(R.id.Medicamento_Receta_No);
        tableRow5_3 = (TableRow) findViewById(R.id.tableRow5_3);
        tableRow5_4 = (TableRow) findViewById(R.id.tableRow5_4);
        tableRow5_5 = (TableRow) findViewById(R.id.tableRow5_5);
        tableRow5_6 = (TableRow) findViewById(R.id.tableRow5_6);
        tableRow5_7 = (TableRow) findViewById(R.id.tableRow5_7);
        tableRow5_8 = (TableRow) findViewById(R.id.tableRow5_8);
        tableRow5_9 = (TableRow) findViewById(R.id.tableRow5_9);
        tableRow5_10 = (TableRow) findViewById(R.id.tableRow5_10);
        tableRow5_3.setVisibility(View.GONE);
        tableRow5_4.setVisibility(View.GONE);
        tableRow5_5.setVisibility(View.GONE);
        tableRow5_6.setVisibility(View.GONE);
        tableRow5_7.setVisibility(View.GONE);
        tableRow5_8.setVisibility(View.GONE);
        tableRow5_9.setVisibility(View.GONE);
        tableRow5_10.setVisibility(View.GONE);
        Medicamento_Receta.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup rg, int checkedId) {
                for (int i = 0; i < rg.getChildCount(); i++) {
                    if (Medicamento_Receta_Si.getId() == checkedId) {
                        tableRow5_3.setVisibility(View.GONE);
                        tableRow5_4.setVisibility(View.GONE);
                        tableRow5_5.setVisibility(View.VISIBLE);
                        tableRow5_6.setVisibility(View.VISIBLE);
                        tableRow5_7.setVisibility(View.VISIBLE);
                        tableRow5_8.setVisibility(View.VISIBLE);
                        tableRow5_9.setVisibility(View.VISIBLE);
                        tableRow5_10.setVisibility(View.VISIBLE);
                        return;
                    } else if (Medicamento_Receta_No.getId() == checkedId) {
                        tableRow5_3.setVisibility(View.VISIBLE);
                        tableRow5_4.setVisibility(View.VISIBLE);
                        tableRow5_5.setVisibility(View.GONE);
                        tableRow5_6.setVisibility(View.GONE);
                        tableRow5_7.setVisibility(View.GONE);
                        tableRow5_8.setVisibility(View.GONE);
                        tableRow5_9.setVisibility(View.GONE);
                        tableRow5_10.setVisibility(View.GONE);
                        return;

                    } else {
                        Toast.makeText(getBaseContext(), "select atleast one", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        //ayudo compra si y no
        RadioGroup Radio_Ayudo_Compra = (RadioGroup) findViewById(R.id.Radio_Ayudo_Compra);
        Ayudo_Compra_Si = (RadioButton) findViewById(R.id.Ayudo_Compra_Si);
        Ayudo_Compra_No = (RadioButton) findViewById(R.id.Ayudo_Compra_No);
        Radio_Ayudo_Compra.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup rg, int checkedId) {
                for (int i = 0; i
                        < rg.getChildCount(); i++) {
                    if (Ayudo_Compra_Si.getId() == checkedId) {
                        tableRow5_5.setVisibility(View.VISIBLE);
                        tableRow5_6.setVisibility(View.VISIBLE);
                        tableRow5_7.setVisibility(View.VISIBLE);
                        tableRow5_8.setVisibility(View.VISIBLE);
                        tableRow5_9.setVisibility(View.VISIBLE);
                        tableRow5_10.setVisibility(View.VISIBLE);
                        return;
                    } else if (Ayudo_Compra_No.getId() == checkedId) {
                        tableRow5_5.setVisibility(View.GONE);
                        tableRow5_6.setVisibility(View.GONE);
                        tableRow5_7.setVisibility(View.GONE);
                        tableRow5_8.setVisibility(View.GONE);
                        tableRow5_9.setVisibility(View.GONE);
                        tableRow5_10.setVisibility(View.GONE);
                        return;

                    } else {
                        Toast.makeText(getBaseContext(), "select atleast one",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        //Sustituyo el medicamento
        RadioGroup Radio_Sustituyo_Medicamento = (RadioGroup) findViewById(R.id.Radio_Sustituyo_Medicamento);
        Sustituyo_Medicamento_Si = (RadioButton) findViewById(R.id.Sustituyo_Medicamento_Si);
        Sustituyo_Medicamento_No = (RadioButton) findViewById(R.id.Sustituyo_Medicamento_No);
        tableRow5_13 = (TableRow) findViewById(R.id.tableRow5_13);//Seccion 5_6
        tableRow5_14 = (TableRow) findViewById(R.id.tableRow5_14);//Seccion 5_7
        tableRow5_15 = (TableRow) findViewById(R.id.tableRow5_15);//Seccion 5_8
        tableRow5_16 = (TableRow) findViewById(R.id.tableRow5_16);//Seccion 5_9
        tableRow5_17 = (TableRow) findViewById(R.id.tableRow5_17);//Seccion 5_10
        tableRow5_13.setVisibility(View.GONE);
        tableRow5_14.setVisibility(View.GONE);
        tableRow5_15.setVisibility(View.GONE);
        tableRow5_16.setVisibility(View.GONE);
        tableRow5_17.setVisibility(View.GONE);
        Radio_Sustituyo_Medicamento.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup rg, int checkedId) {
                for (int i = 0; i
                        < rg.getChildCount(); i++) {
                    if (Sustituyo_Medicamento_Si.getId() == checkedId) {
                        tableRow5_13.setVisibility(View.VISIBLE);
                        tableRow5_14.setVisibility(View.VISIBLE);
                        tableRow5_15.setVisibility(View.VISIBLE);
                        tableRow5_16.setVisibility(View.VISIBLE);
                        tableRow5_17.setVisibility(View.VISIBLE);
                        return;
                    } else if (Sustituyo_Medicamento_No.getId() == checkedId) {
                        tableRow5_13.setVisibility(View.GONE);
                        tableRow5_14.setVisibility(View.GONE);
                        tableRow5_15.setVisibility(View.GONE);
                        tableRow5_16.setVisibility(View.GONE);
                        tableRow5_17.setVisibility(View.GONE);
                        return;

                    } else {
                        Toast.makeText(getBaseContext(), "select atleast one",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        //iniciamos los botones de No encontro medicamento
        RadioGroup Medicamento_Encontro_Encuestador = (RadioGroup) findViewById(R.id.Radio_Encontro_Medicamento_Encuestador);
        Radio_Encontro_Medicamento_Encuestador_Si = (RadioButton) findViewById(R.id.Radio_Encontro_Medicamento_Encuestador_Si);
        Radio_Encontro_Medicamento_Encuestador_No = (RadioButton) findViewById(R.id.Radio_Encontro_Medicamento_Encuestador_No);
        tableRow2_1 = (TableRow) findViewById(R.id.tableRow2_1);
        tableRow2_2 = (TableRow) findViewById(R.id.tableRow2_2);
        tableRow2_3 = (TableRow) findViewById(R.id.tableRow2_3);
        tableRow2_5 = (TableRow) findViewById(R.id.tableRow2_5);
        tableRow2_6 = (TableRow) findViewById(R.id.tableRow2_6);
        tableRow2_5.setVisibility(View.GONE);
        tableRow2_6.setVisibility(View.GONE);
        Medicamento_Encontro_Encuestador.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup rg, int checkedId) {
                for (int i = 0; i < rg.getChildCount(); i++) {
                    if (Radio_Encontro_Medicamento_Encuestador_Si.getId() == checkedId) {
                        tableRow2_1.setVisibility(View.VISIBLE);
                        tableRow2_2.setVisibility(View.VISIBLE);
                        tableRow2_3.setVisibility(View.VISIBLE);
                        tableRow2_5.setVisibility(View.GONE);
                        tableRow2_6.setVisibility(View.GONE);
                        return;
                    } else if (Radio_Encontro_Medicamento_Encuestador_No.getId() == checkedId) {
                        tableRow2_1.setVisibility(View.GONE);
                        tableRow2_2.setVisibility(View.GONE);
                        tableRow2_3.setVisibility(View.GONE);
                        tableRow2_5.setVisibility(View.VISIBLE);
                        tableRow2_6.setVisibility(View.VISIBLE);
                        return;

                    } else {
                        Toast.makeText(getBaseContext(), "select atleast one", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    /**
     * Valida la encuesta
     *
     * @return true si es correcta.
     */
    private boolean validarForm() {
        int color = 0xA2C6FB;
        boolean respuesta = true;
        int auxi;
        beanEncuesta = new BeanEncuesta();
        //Validamos el radiobutton de Sexo
        auxi = validartRadioButton((TableRow) findViewById(R.id.tableRow1_1), (RadioGroup) findViewById(R.id.Radio_Sexo), color, "selecciona el sexo");
        if (auxi < 0) {
            respuesta = false;
        } else {
            //para llenar el bean de encuesta con la info del sexo.
            RadioButton rBtn = (RadioButton) findViewById(auxi);
            if (rBtn.getText().toString().equals("Hombre")) {//si es hombre
                beanEncuesta.setSexo("0");
                //Toast.makeText(getBaseContext(), "BeanEncuesta El Sexo es 0 = Hombre", Toast.LENGTH_SHORT).show();
            } else {//si es mujer
                beanEncuesta.setSexo("1");
                //Toast.makeText(getBaseContext(), "BeanEncuesta El Sexo es 1 = Mujer", Toast.LENGTH_SHORT).show();
            }
            //Se termina el llenado del beanEncuesta para el sexo
        }


        //validamos el radio button de Edad y llenamos el bean
        auxi = validartRadioButton((TableRow) findViewById(R.id.tableRow1_2), (RadioGroup) findViewById(R.id.Radio_Edad), color, "selecciona la edad");
        if (auxi < 0) {
            respuesta = false;
        } else {
            //para llenar el bean de encuesta con la info de la edad.
            RadioButton rBtn = (RadioButton) findViewById(auxi);
            if (rBtn.getText().toString().equals("20-30")) {//si es de 20 a 30
                beanEncuesta.setEdad("2030");
                //Toast.makeText(getBaseContext(), "BeanEncuesta La edad es 2030", Toast.LENGTH_SHORT).show();
            } else if (rBtn.getText().toString().equals("31-40")) {//31-40
                beanEncuesta.setSexo("3040");
                //Toast.makeText(getBaseContext(), "BeanEncuesta La edad es 3040", Toast.LENGTH_SHORT).show();
            } else if (rBtn.getText().toString().equals("41-50")) {//41-50
                beanEncuesta.setSexo("4050");
                //Toast.makeText(getBaseContext(), "BeanEncuesta La edad es 4050", Toast.LENGTH_SHORT).show();
            } else if (rBtn.getText().toString().equals("51-60")) {//51-60
                beanEncuesta.setSexo("5060");
                //Toast.makeText(getBaseContext(), "BeanEncuesta La edad es 5060", Toast.LENGTH_SHORT).show();
            } else {
                beanEncuesta.setSexo("61+");//61++
                //Toast.makeText(getBaseContext(), "BeanEncuesta La edad es 60", Toast.LENGTH_SHORT).show();
            }
            //Se termina el llenado del beanEncuesta para la edad
        }

        //Abre si o no de producto
        auxi = validartRadioButton((TableRow) findViewById(R.id.tableRow2_4), (RadioGroup) findViewById(R.id.Radio_Encontro_Medicamento_Encuestador), color, "selecciona si encontraste el producto");
        if (auxi < 0) {
            respuesta = false;
        } else {
            RadioButton rBtn = (RadioButton) findViewById(auxi);
            if (rBtn.getText().toString().equals("Si")) {
                //Producto
                if (validarSpinner((TableRow) findViewById(R.id.tableRow2_1), (Spinner) findViewById(R.id.ProductoName), color, "Verifica el producto", "Seleccione Producto")) {
                    respuesta = false;
                } else {//Presentación
                    if (validarSpinner((TableRow) findViewById(R.id.tableRow2_1), (Spinner) findViewById(R.id.ProductoPresentacion), color, "Verifica la presentación", "Seleccione Presentación")) {
                        respuesta = false;
                    } else {//llenamos el bean de la encuesta 
                        beanEncuesta.setMedicamento_Adquirio(((Spinner) findViewById(R.id.ProductoName)).getSelectedItem().toString());
                        beanEncuesta.setPresentacion((((Spinner) findViewById(R.id.ProductoPresentacion)).getSelectedItem().toString()));
                        //Toast.makeText(getBaseContext(), "Producto " + ((Spinner) findViewById(R.id.ProductoName)).getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getBaseContext(), "Presentación " + (((Spinner) findViewById(R.id.ProductoName)).getSelectedItem().toString()), Toast.LENGTH_SHORT).show();
                    }//Terminamos de llenar el bean de la encuesta 
                }
            } else {
                //Producto 
                if (validarVacioEditText((TableRow) findViewById(R.id.tableRow2_5), (EditText) findViewById(R.id.Medicamento_Adquirio_Name_Provisional_Text), color, "Escribe un producto")) {
                    respuesta = false;
                } else {//llenamos el bean de la encuesta con el producto que puso el encuestador
                    beanEncuesta.setMedicamento_Adquirio(((EditText) findViewById(R.id.Medicamento_Adquirio_Name_Provisional_Text)).getText().toString());
                    //Toast.makeText(getBaseContext(), "Producto Personal" + ((EditText) findViewById(R.id.Medicamento_Adquirio_Name_Provisional_Text)).getText().toString(), Toast.LENGTH_SHORT).show();
                }//Presentación 
                if (validarVacioEditText((TableRow) findViewById(R.id.tableRow2_6), (EditText) findViewById(R.id.Medicamento_Adquirio_Presentacion_Provisional_Text), color, "Escribe la presentación")) {
                    respuesta = false;
                } else {//llenamos el bean de la encuesta
                    beanEncuesta.setPresentacion(((EditText) findViewById(R.id.Medicamento_Adquirio_Presentacion_Provisional_Text)).getText().toString());
                    //Toast.makeText(getBaseContext(), "Presentación Personal" + ((EditText) findViewById(R.id.Medicamento_Adquirio_Presentacion_Provisional_Text)).getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }


        //Precio pagado 
        if (validarPrecios((TableRow) findViewById(R.id.tableRow2_7), (EditText) findViewById(R.id.Precio_Pagado_Text), color, "Verifica el Precio Pagado")) {
            respuesta = false;
        } else {//llenamos el bean con el precio pagado
            beanEncuesta.setPrecio_Pagado(((EditText) findViewById(R.id.Precio_Pagado_Text)).getText().toString());
            //Toast.makeText(getBaseContext(), "Precio Pagado " + ((EditText) findViewById(R.id.Precio_Pagado_Text)).getText().toString(), Toast.LENGTH_SHORT).show();
        }//Precio pagado 
        if (validarPrecios((TableRow) findViewById(R.id.tableRow2_8), (EditText) findViewById(R.id.Precio_Caja_Text), color, "Verifica el Precio de Caja")) {
            respuesta = false;
        } else {//llenamos el precio con el precio pagado
            beanEncuesta.setPrecio_Caja(((EditText) findViewById(R.id.Precio_Caja_Text)).getText().toString());
            //Toast.makeText(getBaseContext(), "Precio Pagado " + ((EditText) findViewById(R.id.Precio_Caja_Text)).getText().toString(), Toast.LENGTH_SHORT).show();
        }//Precio Justo 
        if (validarPrecios((TableRow) findViewById(R.id.tableRow3_1), (EditText) findViewById(R.id.Precio_Justo_Text), color, "Verifica el Precio Justo")) {
            respuesta = false;
        } else {//llenamos el bean con el precio justo que puso
            beanEncuesta.setPrecio_Justo(((EditText) findViewById(R.id.Precio_Justo_Text)).getText().toString());
            //Toast.makeText(getBaseContext(), "Precio Justo " + ((EditText) findViewById(R.id.Precio_Justo_Text)).getText().toString(), Toast.LENGTH_SHORT).show();
        }//Dudar Calidad
        if (validarPrecios((TableRow) findViewById(R.id.tableRow3_3), (EditText) findViewById(R.id.Barato_Dudar_Calidad_Text), color, "Verifica el Dudar Calidad")) {
            respuesta = false;
        } else {//llenamos el bean con el precio que duda la calidad
            beanEncuesta.setPrecio_Barato_Duda_Calidad(((EditText) findViewById(R.id.Barato_Dudar_Calidad_Text)).getText().toString());
            //Toast.makeText(getBaseContext(), "Dudar Calidad " + ((EditText) findViewById(R.id.Barato_Dudar_Calidad_Text)).getText().toString(), Toast.LENGTH_SHORT).show();
        }//Caro Comprar
        if (validarPrecios((TableRow) findViewById(R.id.tableRow3_5), (EditText) findViewById(R.id.Caro_Aun_Compraria_Text), color, "Verifica el Caro Comprar")) {
            respuesta = false;
        } else {//llenamos el bean con el precio que puso que es caro pero aun lo compraria
            beanEncuesta.setPrecio_Caro_Compraria(((EditText) findViewById(R.id.Caro_Aun_Compraria_Text)).getText().toString());
            //Toast.makeText(getBaseContext(), "Precio Caro Compraria " + ((EditText) findViewById(R.id.Caro_Aun_Compraria_Text)).getText().toString(), Toast.LENGTH_SHORT).show();
        }//Caro NO Comprar
        if (validarPrecios((TableRow) findViewById(R.id.tableRow3_7), (EditText) findViewById(R.id.Caro_No_Compraria_Text), color, "Verifica el Caro NO Comprar")) {
            respuesta = false;
        } else {//llenamos el precio con el precio que es tan caro que ya no lo compraria
            beanEncuesta.setPrecio_Caro_No_Compraria(((EditText) findViewById(R.id.Caro_No_Compraria_Text)).getText().toString());
            //Toast.makeText(getBaseContext(), "Precio Caro No Compraria " + ((EditText) findViewById(R.id.Caro_No_Compraria_Text)).getText().toString(), Toast.LENGTH_SHORT).show();
        }

        //Precio30
        if (validarSpinner((TableRow) findViewById(R.id.tableRow4_1), (Spinner) findViewById(R.id.Precio30pcion), color, "Seleccione opción para precio +%30", "Seleccione una opción")) {
            respuesta = false;
        } else {//llenamos el bean con la opcion y el precio de 30%
            beanEncuesta.setPrecio30(((TextView) findViewById(R.id.Precio30_Text)).getText().toString());
            beanEncuesta.setPrecio30_Opcion("" + spinnerOpcionNumber((Spinner) findViewById(R.id.Precio30pcion)));
            //Toast.makeText(getBaseContext(), "Precio +30% " + ((TextView) findViewById(R.id.Precio30_Text)).getText().toString(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getBaseContext(), "Opcion +30% " + spinnerOpcionNumber((Spinner) findViewById(R.id.Precio30pcion)), Toast.LENGTH_SHORT).show();
        }//Precio20
        if (validarSpinner((TableRow) findViewById(R.id.tableRow4_3), (Spinner) findViewById(R.id.Precio20pcion), color, "Seleccione opción para precio +%20", "Seleccione una opción")) {
            respuesta = false;
        } else {
            beanEncuesta.setPrecio20(((TextView) findViewById(R.id.Precio20_Text)).getText().toString());
            beanEncuesta.setPrecio20_Opcion("" + spinnerOpcionNumber((Spinner) findViewById(R.id.Precio20pcion)));
            //Toast.makeText(getBaseContext(), "Precio +20% " + ((TextView) findViewById(R.id.Precio20_Text)).getText().toString(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getBaseContext(), "Opcion +20% " + spinnerOpcionNumber((Spinner) findViewById(R.id.Precio20pcion)), Toast.LENGTH_SHORT).show();
        }//Precio10
        if (validarSpinner((TableRow) findViewById(R.id.tableRow4_5), (Spinner) findViewById(R.id.Precio10pcion), color, "Seleccione opción para precio +%10", "Seleccione una opción")) {
            respuesta = false;
        } else {
            beanEncuesta.setPrecio10(((TextView) findViewById(R.id.Precio10_Text)).getText().toString());
            beanEncuesta.setPrecio10_Opcion("" + spinnerOpcionNumber((Spinner) findViewById(R.id.Precio10pcion)));
            //Toast.makeText(getBaseContext(), "Precio +10% " + ((TextView) findViewById(R.id.Precio10_Text)).getText().toString(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getBaseContext(), "Opcion +10% " + spinnerOpcionNumber((Spinner) findViewById(R.id.Precio10pcion)), Toast.LENGTH_SHORT).show();
        }//Precio90
        if (validarSpinner((TableRow) findViewById(R.id.tableRow4_7), (Spinner) findViewById(R.id.Precio90pcion), color, "Seleccione opción para precio -%10", "Seleccione una opción")) {
            respuesta = false;
        } else {
            beanEncuesta.setPrecio90(((TextView) findViewById(R.id.Precio90_Text)).getText().toString());
            beanEncuesta.setPrecio90_Opcion("" + spinnerOpcionNumber((Spinner) findViewById(R.id.Precio90pcion)));
            //Toast.makeText(getBaseContext(), "Precio 90% " + ((TextView) findViewById(R.id.Precio90_Text)).getText().toString(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getBaseContext(), "Opcion 90% " + spinnerOpcionNumber((Spinner) findViewById(R.id.Precio90pcion)), Toast.LENGTH_SHORT).show();
        }//Precio80
        if (validarSpinner((TableRow) findViewById(R.id.tableRow4_9), (Spinner) findViewById(R.id.Precio80pcion), color, "Seleccione opción para precio -%20", "Seleccione una opción")) {
            respuesta = false;
        } else {
            beanEncuesta.setPrecio80(((TextView) findViewById(R.id.Precio80_Text)).getText().toString());
            beanEncuesta.setPrecio80_Opcion("" + spinnerOpcionNumber((Spinner) findViewById(R.id.Precio80pcion)));
            //Toast.makeText(getBaseContext(), "Precio 80% " + ((TextView) findViewById(R.id.Precio80_Text)).getText().toString(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getBaseContext(), "Opcion 80% " + spinnerOpcionNumber((Spinner) findViewById(R.id.Precio80pcion)), Toast.LENGTH_SHORT).show();
        }//Precio70
        if (validarSpinner((TableRow) findViewById(R.id.tableRow4_11), (Spinner) findViewById(R.id.Precio70pcion), color, "Seleccione opción para precio -%30", "Seleccione una opción")) {
            respuesta = false;
        } else {
            beanEncuesta.setPrecio70(((TextView) findViewById(R.id.Precio70_Text)).getText().toString());
            beanEncuesta.setPrecio70_Opcion("" + spinnerOpcionNumber((Spinner) findViewById(R.id.Precio70pcion)));
            //Toast.makeText(getBaseContext(), "Precio 70% " + ((TextView) findViewById(R.id.Precio70_Text)).getText().toString(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getBaseContext(), "Opcion 70% " + spinnerOpcionNumber((Spinner) findViewById(R.id.Precio70pcion)), Toast.LENGTH_SHORT).show();
        }

        auxi = validartRadioButton((TableRow) findViewById(R.id.tableRow5_1), (RadioGroup) findViewById(R.id.Radio_Medicamento_Receta), color, "Verifique Medicamento con receta");
        if (auxi < 0) {
            respuesta = false;
        } else {
            RadioButton rBtn = (RadioButton) findViewById(auxi);
            if (rBtn.getText().toString().equals("Si")) {//Si selecciono que SI ¿Compró el médicamento con receta?
                //validamos que haya introducido un número valido
                if (validarPrecios((TableRow) findViewById(R.id.tableRow5_5), (EditText) findViewById(R.id.Ultima_Compra_Text), color, "Verifique Ultima Compra")) {
                    respuesta = false;
                } else {//validamos si fue su ultima compra
                    auxi = validartRadioButton((TableRow) findViewById(R.id.tableRow5_5), (RadioGroup) findViewById(R.id.Ultima_Compra), color, "Verifique Ultima Compra");
                    if (auxi < 0) {
                        respuesta = false;
                    } else {//Si esta bien su ultima compra verificamos la ultima visita a su medico que sea un number
                        if (validarPrecios((TableRow) findViewById(R.id.tableRow5_8), (EditText) findViewById(R.id.Ultima_Visita_Text), color, "Verifique Visita Medico")) {
                            respuesta = false;
                        } else {//verificamos que haya seleccionado una unidad
                            auxi = validartRadioButton((TableRow) findViewById(R.id.tableRow5_8), (RadioGroup) findViewById(R.id.Ultima_Visita), color, "Verifique Visita Medico");
                            if (auxi < 0) {
                                respuesta = false;
                            } else {//llenamos el bean con la información que acabamos de obtener
                                beanEncuesta.setMedicamento_Con_Receta("1");
                                beanEncuesta.setUltima_Compra(((EditText) findViewById(R.id.Ultima_Compra_Text)).getText().toString());
                                beanEncuesta.setUltima_Compra_Unidad("" + radioUnidadTiempo((RadioGroup) findViewById(R.id.Ultima_Compra)));
                                beanEncuesta.setUltima_Visita_Medico(((EditText) findViewById(R.id.Ultima_Visita_Text)).getText().toString());
                                beanEncuesta.setUltima_Visita_Medico_Unidad("" + radioUnidadTiempo((RadioGroup) findViewById(R.id.Ultima_Visita)));
                                //Toast.makeText(getBaseContext(), "Medicamento con receta 1", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getBaseContext(), ((EditText) findViewById(R.id.Ultima_Compra_Text)).getText().toString(), Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getBaseContext(), "" + radioUnidadTiempo((RadioGroup) findViewById(R.id.Ultima_Compra)), Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getBaseContext(), ((EditText) findViewById(R.id.Ultima_Visita_Text)).getText().toString(), Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getBaseContext(), "" + radioUnidadTiempo((RadioGroup) findViewById(R.id.Ultima_Visita)), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            } else {//Si selecciono que NO ¿Compró el médicamento con receta?
                //validamos el radiobutton de algún medico le ayudo con su compra
                auxi = validartRadioButton((TableRow) findViewById(R.id.tableRow5_3), (RadioGroup) findViewById(R.id.Radio_Ayudo_Compra), color, "Verifique Medico Ayudo Compra");
                if (auxi < 0) {
                    respuesta = false;
                } else {//Se valida el radiobutton si de algún medico le ayudo con su compra
                    rBtn = (RadioButton) findViewById(auxi);
                    if (rBtn.getText().toString().equals("Si")) {//Si selecciono que SI recibio ayuda de alguien
                        //validamos que haya introducido un número valido
                        if (validarPrecios((TableRow) findViewById(R.id.tableRow5_5), (EditText) findViewById(R.id.Ultima_Compra_Text), color, "Verifique Ultima Compra")) {
                            respuesta = false;
                        } else {//validamos si fue su ultima compra
                            auxi = validartRadioButton((TableRow) findViewById(R.id.tableRow5_5), (RadioGroup) findViewById(R.id.Ultima_Compra), color, "Verifique Ultima Compra");
                            if (auxi < 0) {
                                respuesta = false;
                            } else {//Si esta bien su ultima compra verificamos la ultima visita a su medico que sea un number
                                if (validarPrecios((TableRow) findViewById(R.id.tableRow5_8), (EditText) findViewById(R.id.Ultima_Visita_Text), color, "Verifique Visita Medico")) {
                                    respuesta = false;
                                } else {//verificamos que haya seleccionado una unidad
                                    auxi = validartRadioButton((TableRow) findViewById(R.id.tableRow5_8), (RadioGroup) findViewById(R.id.Ultima_Compra), color, "Verifique Visita Medico");
                                    if (auxi < 0) {
                                        respuesta = false;
                                    } else {//llenamos el bean si todo fue bien
                                        beanEncuesta.setMedicamento_Con_Receta("0");
                                        beanEncuesta.setAyudo("1");
                                        beanEncuesta.setUltima_Compra(((EditText) findViewById(R.id.Ultima_Compra_Text)).getText().toString());
                                        beanEncuesta.setUltima_Compra_Unidad("" + radioUnidadTiempo((RadioGroup) findViewById(R.id.Ultima_Compra)));
                                        beanEncuesta.setUltima_Visita_Medico(((EditText) findViewById(R.id.Ultima_Visita_Text)).getText().toString());
                                        beanEncuesta.setUltima_Visita_Medico_Unidad("" + radioUnidadTiempo((RadioGroup) findViewById(R.id.Ultima_Visita)));
                                        //Toast.makeText(getBaseContext(), "Medicamento con receta 0", Toast.LENGTH_SHORT).show();
                                        //Toast.makeText(getBaseContext(), "Recibi Ayuda 1", Toast.LENGTH_SHORT).show();
                                        //Toast.makeText(getBaseContext(), ((EditText) findViewById(R.id.Ultima_Compra_Text)).getText().toString(), Toast.LENGTH_SHORT).show();
                                        //Toast.makeText(getBaseContext(), "" + radioUnidadTiempo((RadioGroup) findViewById(R.id.Ultima_Compra)), Toast.LENGTH_SHORT).show();
                                        //Toast.makeText(getBaseContext(), ((EditText) findViewById(R.id.Ultima_Visita_Text)).getText().toString(), Toast.LENGTH_SHORT).show();
                                        //Toast.makeText(getBaseContext(), "" + radioUnidadTiempo((RadioGroup) findViewById(R.id.Ultima_Visita)), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    } else {//selecciono que no recibio ayuda de algún médico
                        beanEncuesta.setMedicamento_Con_Receta("0");//llenamos el bean
                        beanEncuesta.setAyudo("0");//llenamos el bean
                        //Toast.makeText(getBaseContext(), "Medicamento con receta 0", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getBaseContext(), "Recibio ayuda 0", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        auxi = validartRadioButton((TableRow) findViewById(R.id.tableRow5_11), (RadioGroup) findViewById(R.id.Radio_Sustituyo_Medicamento), color, "Verifica Sustituyo medicamento");
        if (auxi < 0) {
            respuesta = false;
        } else {
            RadioButton rBtn = (RadioButton) findViewById(auxi);
            if (rBtn.getText().toString().equals("Si")) {
                auxi = validartRadioButton((TableRow) findViewById(R.id.tableRow5_13), (RadioGroup) findViewById(R.id.Radio_Quien_Sustituyo), color, "Verifica Quien Sustituyo");
                if (auxi < 0) {
                    respuesta = false;
                } else {
                    if (validarSpinner((TableRow) findViewById(R.id.tableRow5_13), (Spinner) findViewById(R.id.Venia_Originalmente_Spinner), color, "Selecciona producto que venia originalmente", "Seleccione Producto")) {
                        respuesta = false;
                    } else {//llenamos el bean con la info
                        beanEncuesta.setSustituyo("1");
                        beanEncuesta.setQuien_Genero_Susticion("" + numero_Quien_Sustituyo((RadioGroup) findViewById(R.id.Radio_Quien_Sustituyo)));
                        beanEncuesta.setMedicamento_Venia_Ori("" + ((Spinner) findViewById(R.id.Venia_Originalmente_Spinner)).getSelectedItem());
                        //Toast.makeText(getBaseContext(), "Sustituyo 1", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getBaseContext(), "Quien sustituyo " + numero_Quien_Sustituyo((RadioGroup) findViewById(R.id.Radio_Quien_Sustituyo)), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getBaseContext(), "Venia originalmente " + ((Spinner) findViewById(R.id.Venia_Originalmente_Spinner)).getSelectedItem(), Toast.LENGTH_SHORT).show();
                    }
                }
            } else {//Si no sustituyo el medicamento
                beanEncuesta.setSustituyo("0");
                //Toast.makeText(getBaseContext(), "Sustituyo 0", Toast.LENGTH_SHORT).show();
            }
        }

        auxi = validartRadioButton((TableRow) findViewById(R.id.tableRow6_1), (RadioGroup) findViewById(R.id.Medicamento_Alternativa), color, "Verifique si usa alternativa");
        if (auxi < 0) {
            respuesta = false;
        } else {
            beanEncuesta.setAlternativa("" + numero_Alternativa((RadioGroup) findViewById(R.id.Medicamento_Alternativa)));
            //Toast.makeText(getBaseContext(), "Alternativa " + numero_Alternativa((RadioGroup) findViewById(R.id.Medicamento_Alternativa)), Toast.LENGTH_SHORT).show();
        }

        if (!(((Spinner) findViewById(R.id.No_Encontro_Spinner)).getSelectedItemId() == AdapterView.INVALID_ROW_ID)) {
            if (!(((Spinner) findViewById(R.id.No_Encontro_Spinner)).getSelectedItem().toString().equals("Seleccione Producto"))) {
                beanEncuesta.setMedicamento_No_Encontro(((Spinner) findViewById(R.id.No_Encontro_Spinner)).getSelectedItem().toString());
                //Toast.makeText(getBaseContext(), "No encontro" + ((Spinner) findViewById(R.id.No_Encontro_Spinner)).getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }
        }
        return respuesta;
    }

    /**
     * Saber el numero de la alternativa
     *
     * @param radio el radio que contiene las opciones de las alternativas
     * @return el número a la que corresponde esa opción
     */
    public int numero_Alternativa(RadioGroup radio) {
        int radioBtnChecked = radio.getCheckedRadioButtonId();
        RadioButton rBtn = (RadioButton) findViewById(radioBtnChecked);
        if (rBtn.getText().toString().equals("Siempre uso alternativa")) {
            return 0;
        } else if (rBtn.getText().toString().equals("Uso mas la alternativa que el original")) {
            return 1;
        } else if (rBtn.getText().toString().equals("Uso mas el original que la alternativa")) {
            return 2;
        } else if (rBtn.getText().toString().equals("Siempre uso el original")) {
            return 3;
        } else {
            return -1;
        }
    }

    /**
     * Numero de quien sustituyo
     *
     * @param radio es el que contiene dependiente o el mismo
     * @return regresa el numero de quien sustituyo para ponerlo en el bean y
     * ahce la consulta
     */
    public int numero_Quien_Sustituyo(RadioGroup radio) {
        int radioBtnChecked = radio.getCheckedRadioButtonId();
        RadioButton rBtn = (RadioButton) findViewById(radioBtnChecked);
        if (rBtn.getText().toString().equals("Usted")) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * Saber el número de la unidad a la que corresponde en el tiempo
     *
     * @param radio el radiogroup que contiene los radiobutton que serán
     * analizados
     * @return regresa el número de la unidad para poder ser introducido
     */
    public int radioUnidadTiempo(RadioGroup radio) {
        int radioBtnChecked = radio.getCheckedRadioButtonId();
        RadioButton rBtn = (RadioButton) findViewById(radioBtnChecked);
        if (rBtn.getText().toString().equals("D")) {
            return 0;
        } else if (rBtn.getText().toString().equals("S")) {
            return 1;
        } else if (rBtn.getText().toString().equals("M")) {
            return 2;
        } else {
            return -1;
        }
    }

    /**
     * Compara la cadena y le asigna el número correspondiente segun la
     * respuesta del usuario
     *
     * @param spinner es el objeto que recibe y compara con las opciones de la
     * cadena
     * @return regresa la el numero con la respuesta para que esta sea añadida
     */
    public int spinnerOpcionNumber(Spinner spinner) {
        if (spinner.getSelectedItem().equals("Definitivamente SI lo usaría")) {
            return 5;
        } else if (spinner.getSelectedItem().equals("Probablemente SI lo usaría")) {
            return 4;
        } else if (spinner.getSelectedItem().equals("NO SÉ si lo usaría")) {
            return 3;
        } else if (spinner.getSelectedItem().equals("Probablemente NO lo usaría")) {
            return 2;
        } else if (spinner.getSelectedItem().equals("Definitivamente NO lo usaría")) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Sirve para validar los spinner
     *
     * @param aux la tabla que será coloreada
     * @param spinner el spinner que se va a validar
     * @param color el color que se pone si es correcto
     * @param error el error que manda
     * @param celda0 la cel numero 0 del spinner sino selecciona nada
     * @return true si es que falla y false si no es asi
     */
    private boolean validarSpinner(TableRow aux, Spinner spinner, int color, String error, String celda0) {
        boolean respuesta = false;
        if (spinner.getSelectedItemId() == AdapterView.INVALID_ROW_ID) {
            respuesta = true;
            aux.setBackgroundColor(Color.RED);
            Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
        } else {
            if (spinner.getSelectedItem().toString().equals(celda0)) {
                respuesta = true;
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
                aux.setBackgroundColor(Color.RED);
            } else {
                aux.setBackgroundColor(color);
            }
        }
        return respuesta;
    }

    /**
     * Sirve para validar que no este vacio el editText
     *
     * @param aux el table row que se pondra en rojo o azul dependiendo si hay
     * error
     * @param editText el edittext que se esta validando
     * @param color el color que se pone si todo esta bien
     * @param error el mensaje de error que despliega si hay error
     * @return el numero del item y si es menor a cero hay algun error
     */
    private int validartRadioButton(TableRow aux, RadioGroup Radio_Group, int color, String error) {
        int radioBtnChecked = Radio_Group.getCheckedRadioButtonId();
        //Sexo
        if (radioBtnChecked < 0) {
            Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
            aux.setBackgroundColor(Color.RED);
        } else {
            aux.setBackgroundColor(color);
        }
        return radioBtnChecked;
    }

    /**
     * Sirve para validar que no este vacio el editText
     *
     * @param aux el table row que se pondra en rojo o azul dependiendo si hay
     * error
     * @param editText el edittext que se esta validando
     * @param color el color que se pone si todo esta bien
     * @param error el mensaje de error que despliega si hay error
     * @return true si es que falla y false si no es asi
     */
    private boolean validarVacioEditText(TableRow aux, EditText editText, int color, String error) {
        boolean respuesta = false;
        if (editText.getText().toString().equals("")) {
            respuesta = true;
            aux.setBackgroundColor(Color.RED);
            Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
        } else {
            aux.setBackgroundColor(color);
        }
        return respuesta;
    }

    /**
     * Sirve para validar que sean números
     *
     * @param aux el table row que se pondra en rojo o azul dependiendo si hay
     * error.
     * @param editText el edittext que se esta validando
     * @param color el color que se pone si todo esta bien
     * @param error el mensaje de error que despliega si hay error
     * @return true si es que falla y false si no es asi
     */
    private boolean validarPrecios(TableRow aux, EditText editText, int color, String error) {
        boolean respuesta = false;
        if (editText.getText().toString().equals("")) {
            respuesta = true;
            aux.setBackgroundColor(Color.RED);
            Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
        } else {
            try {
                Double.parseDouble(editText.getText().toString());
                aux.setBackgroundColor(color);
            } catch (Exception e) {
                respuesta = true;
                aux.setBackgroundColor(Color.RED);
                Toast.makeText(getBaseContext(), error, Toast.LENGTH_SHORT).show();
            }
        }
        return respuesta;
    }

    //para que no se pueda regresar
    @Override
    public void onBackPressed(){
    }
    
    /**
     * Se crea el mení graficamente
     * @param menu el menú que se pondrá
     */
    private void CreateMenu(Menu menu)
    {
        menu.setQwertyMode(true);
        MenuItem mnu1 = menu.add(0, 0, 0, "Misma Encuesta");
        {
            mnu1.setAlphabeticShortcut('m');     
        }
        MenuItem mnu2 = menu.add(0, 1, 1, "Salir");
        {
            mnu2.setAlphabeticShortcut('s');                
        }
    }
    
    private boolean MenuChoice(MenuItem item)
    {        
        switch (item.getItemId()) {
        case 0:
            Toast.makeText(this, "Misma Encuesta", 
                Toast.LENGTH_LONG).show();
            return true;
        case 1:
            Toast.makeText(this, "Salir", Toast.LENGTH_LONG).show();
            final Intent intent = new Intent(getBaseContext(), Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            return true;          
        }
        return false;
    }    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        CreateMenu(menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {    
         return MenuChoice(item);    
    }
    
    /**
     * Boton para poner en acción las validaciones y enviar la encuesta.
     */
    private void buttonValidar() {
        Button Boton_Enviar = (Button) findViewById(R.id.Boton_Enviar);
        Boton_Enviar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (validarForm()) {
                    Calendar fecha = Calendar.getInstance();
                    int aux;
                    String cols;
                    String vals;
                    RadioButton rBtn;
                    long d;
                    int ID_Medicamento;
                    //Toast.makeText(getBaseContext(), "Encuesta correcta!", Toast.LENGTH_SHORT).show();
                    //llenar la tabla de general
                    if (!canasta) {
                        cols = "ID_Encuestador,Fecha,Hora,Farmacia,Colonia,Tipo_Farmacia,Tipo_Cadena,CP,Ageb,Ciudad,Sexo,Edad";
                        vals =  idusu + " , '" + fecha.get(java.util.Calendar.YEAR) + "-" + (fecha.get(java.util.Calendar.MONTH) + 1) + "-" + fecha.get(java.util.Calendar.DATE) + "', '"
                                + fecha.get(Calendar.HOUR_OF_DAY) + ":" + fecha.get(Calendar.MINUTE) + ":" + fecha.get(Calendar.SECOND) + "' , '" + fb.getFarmacia() + "' , '" + fb.getColonia() + "' , '"
                                + fb.getTipo2() + "', '" + fb.getTipoCadena() + "' , '" + fb.getCP() + "', '" + fb.getAgeb() + "', '" + fb.getCiudad() + "', " + beanEncuesta.getSexo() + ", " + beanEncuesta.getEdad();
                        if (fb.isPendiente()) {
                            vals += ", 1";
                            cols += ",PENDIENTE";
                        }
                        d = myDbHelper.insertar("General", cols, vals);
                        ID_General = Integer.parseInt("" + d);
                    }
                    //Toast.makeText(getBaseContext(), "la id es " + Integer.parseInt("" + d), Toast.LENGTH_LONG);

                    //se llena el medicamento
                    cols = "ID_General,Nombre,Presentacion,Precio_Pagado,Precio_Caja";
                    vals =  ID_General + ", '" + beanEncuesta.getMedicamento_Adquirio() + "', '" + beanEncuesta.getPresentacion() + "', " + beanEncuesta.getPrecio_Pagado() + ", " + beanEncuesta.getPrecio_Caja();
                    d = myDbHelper.insertar("medicamento", cols, vals);
                    ID_Medicamento = Integer.parseInt("" + d);

                    //vemos si el medicamento es nuevo para ingresarlo en pendientes
                    aux = validartRadioButton((TableRow) findViewById(R.id.tableRow2_4), (RadioGroup) findViewById(R.id.Radio_Encontro_Medicamento_Encuestador), 0xA2C6FB, "Ocurrio un error");
                    rBtn = (RadioButton) findViewById(aux);
                    if (rBtn.getText().toString().equals("No")) {
                        cols = "DESCRIPCION,PRESENTACION,PENDIENTE";
                        vals = "'" + beanEncuesta.getMedicamento_Adquirio() + "', '" + beanEncuesta.getPresentacion() + "', 1";
                        myDbHelper.insertar("productos", cols, vals);
                    }


                    //llenamos la tabla de opinion_medicamento
                    cols = "ID_Medicamento,Precio_Justo,Barato,Caro_Comprar,Caro_No_Comprar";
                    vals = ID_Medicamento + ", " + beanEncuesta.getPrecio_Justo() + ", " + beanEncuesta.getPrecio_Barato_Duda_Calidad() + ", " + beanEncuesta.getPrecio_Caro_Compraria() + ", " + beanEncuesta.getPrecio_Caro_No_Compraria();
                    myDbHelper.insertar("opinion_medicamento", cols, vals);


                    //llenamos la tabla de precios del medicamento
                    cols = "ID_Medicamento,Precio_Treinta,Opcion_Treinta,Precio_Veinte,Opcion_Veinte,Precio_Diez,Opcion_Diez,Precio_Noventa,Opcion_Noventa,Precio_Ochenta,Opcion_Ochenta,Precio_Setenta,Opcion_Setenta";
                    vals = ID_Medicamento + ", " + beanEncuesta.getPrecio30() + ", " + beanEncuesta.getPrecio30_Opcion() + ", " + beanEncuesta.getPrecio20() + ", " + beanEncuesta.getPrecio20_Opcion() + ", " + beanEncuesta.getPrecio10() + ", " + beanEncuesta.getPrecio10_Opcion() + ", " + beanEncuesta.getPrecio90() + ", " + beanEncuesta.getPrecio90_Opcion() + ", " + beanEncuesta.getPrecio80() + ", " + beanEncuesta.getPrecio80_Opcion() + ", " + beanEncuesta.getPrecio70() + ", " + beanEncuesta.getPrecio70_Opcion();
                    myDbHelper.insertar("tabla_precios", cols, vals);

                    //llenamos la tabla de opinion
                    cols = "ID_Medicamento,Con_Receta";
                    vals = ID_Medicamento + ", " + beanEncuesta.getMedicamento_Con_Receta() ;
                    if (beanEncuesta.getMedicamento_Con_Receta().equals("0")) {//sino compro con receta
                        cols += ",AyudoCompra";
                        vals += ", " + beanEncuesta.getAyudo();
                        if (beanEncuesta.getAyudo().equals("1")) {//Si alguien le ayudo
                            cols += ",Ultima_Compra,Ultima_Compra_Unidad,Ultima_Consulta,Ultima_Consulta_Unidad";
                            vals += ", " + beanEncuesta.getUltima_Compra() + ", " + beanEncuesta.getUltima_Compra_Unidad() + ", " + beanEncuesta.getUltima_Visita_Medico() + ", " + beanEncuesta.getUltima_Visita_Medico_Unidad();
                        }
                        cols += ",Sustituido";
                        vals += ", " + beanEncuesta.getSustituyo() + "";
                        if (beanEncuesta.getSustituyo().equals("1")) {//Si sustituyo
                            cols += ",Quien_Sustituido,Medicamento_Original";
                            vals += ", " + beanEncuesta.getQuien_Genero_Susticion() + ", '" + beanEncuesta.getMedicamento_Venia_Ori() + "'";
                        }
                        cols += ",Alternativa";
                        vals += ", " + beanEncuesta.getAlternativa();
                        if (!(beanEncuesta.getOtro_Medicamento().equals(""))) {//Si no encontro algun medicamento
                            cols += "Medicamento_No_Encontro";
                            vals += ", '" + beanEncuesta.getMedicamento_No_Encontro() + "'";
                        }
                        myDbHelper.insertar("tabla_opinion", cols, vals);
                    } else {//Si puso que si compro con receta
                        cols += ",Ultima_Compra,Ultima_Compra_Unidad,Ultima_Consulta,Ultima_Consulta_Unidad";
                        vals += ", " + beanEncuesta.getUltima_Compra() + ", " + beanEncuesta.getUltima_Compra_Unidad() + ", " + beanEncuesta.getUltima_Visita_Medico() + ", " + beanEncuesta.getUltima_Visita_Medico_Unidad();
                        cols += ",Sustituido";
                        vals += ", " + beanEncuesta.getSustituyo();
                        if (beanEncuesta.getSustituyo().equals("1")) {//Si sustituyo
                            cols += "Quien_Sustituido,Medicamento_Original";
                            vals += ", " + beanEncuesta.getQuien_Genero_Susticion() + ", '" + beanEncuesta.getMedicamento_Venia_Ori() + "'";
                        }
                        cols += ",Alternativa";
                        vals += ", " + beanEncuesta.getAlternativa();
                        if (!(beanEncuesta.getOtro_Medicamento().equals(""))) {//Si no encontro algun medicamento
                            cols += ",Medicamento_No_Encontro";
                            vals += ", '" + beanEncuesta.getMedicamento_No_Encontro() + "'";
                        }
                        myDbHelper.insertar("tabla_opinion", cols, vals);  
                        EncuestaCreada();
                    }
                    Toast.makeText(getBaseContext(), "Encuesta registrada exitosamente.", Toast.LENGTH_SHORT).show();                    
                } else {
                    Toast.makeText(getBaseContext(), "Revise la encuesta.", Toast.LENGTH_SHORT).show();                    
                }
            }
        });
    }
    
    public void EncuestaCreada(){
    
    }
}
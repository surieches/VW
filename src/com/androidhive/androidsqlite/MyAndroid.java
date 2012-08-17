package com.androidhive.androidsqlite;

import android.app.Activity;
import android.content.Context;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.*;
import java.io.IOException;
import java.util.ArrayList;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encuesta);
        context = this;

        idusu = Integer.parseInt(Login.usu.getIdusu());
        Toast.makeText(MyAndroid.this,"Usuario: "+idusu,Toast.LENGTH_SHORT).show();

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
            String[] colu = {"Nombre_Farmacia"};
            List enc = myDbHelper.getColumnaCompleta("farmacias", colu, "_id = "+fb.getId());            
            Toast.makeText(this, "Nombre de farmacia: "+enc.get(0), Toast.LENGTH_SHORT).show();
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
                            Precio30_Text.setText("$ " + (String.format("%.2f", precio * 1.3)) + " pesos");
                            Precio20_Text.setText("$" + (String.format("%.2f", precio * 1.2)) + " pesos");
                            Precio10_Text.setText("$" + (String.format("%.2f", precio * 1.1)) + " pesos");
                            Precio90_Text.setText("$" + (String.format("%.2f", precio * 0.9)) + " pesos");
                            Precio80_Text.setText("$" + (String.format("%.2f", precio * 0.8)) + " pesos");
                            Precio70_Text.setText("$" + (String.format("%.2f", precio * 0.7)) + " pesos");
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
     * @version 1.0 @autor MaximusPegasus
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
     * @version 1.0 @autor MaximusPegasus
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
     * @version 1.0 @autor MaximusPegasus
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
     * @version 1.0 @autor MaximusPegasus
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
        Spinner spinner;
        EditText editText;
        TableRow aux = (TableRow) findViewById(R.id.tableRow1_1);
        RadioGroup Radio_Group = (RadioGroup) findViewById(R.id.Radio_Sexo);
        int radioBtnChecked = Radio_Group.getCheckedRadioButtonId();
        //Sexo
        if (radioBtnChecked < 0) {
            respuesta = false;
            Toast.makeText(getBaseContext(), "selecciona el sexo", Toast.LENGTH_SHORT).show();
            aux.setBackgroundColor(Color.RED);
        } else {
            RadioButton rBtn = (RadioButton) findViewById(radioBtnChecked);
            aux.setBackgroundColor(color);
            //Toast.makeText(getBaseContext(), "" + rBtn.getText().toString(), Toast.LENGTH_SHORT).show();
        }
        //Sexo Cierra
        aux = (TableRow) findViewById(R.id.tableRow1_2);
        Radio_Group = (RadioGroup) findViewById(R.id.Radio_Edad);
        radioBtnChecked = Radio_Group.getCheckedRadioButtonId();
        //Edad
        if (radioBtnChecked < 0) {
            respuesta = false;
            Toast.makeText(getBaseContext(), "selecciona la edad", Toast.LENGTH_SHORT).show();
            aux.setBackgroundColor(Color.RED);
        } else {
            RadioButton rBtn = (RadioButton) findViewById(radioBtnChecked);
            aux.setBackgroundColor(color);
        }
        //Edad Cierra

        //Abre si o no de producto
        aux = (TableRow) findViewById(R.id.tableRow2_4);
        Radio_Group = (RadioGroup) findViewById(R.id.Radio_Encontro_Medicamento_Encuestador);
        radioBtnChecked = Radio_Group.getCheckedRadioButtonId();
        //
        if (radioBtnChecked < 0) {
            respuesta = false;
            Toast.makeText(getBaseContext(), "selecciona si encontraste el producto", Toast.LENGTH_SHORT).show();
            aux.setBackgroundColor(Color.RED);
        } else {
            aux.setBackgroundColor(color);
            RadioButton rBtn = (RadioButton) findViewById(radioBtnChecked);
            if (rBtn.getText().toString().equals("Si")) {
                //Producto
                aux = (TableRow) findViewById(R.id.tableRow2_1);
                spinner = (Spinner) findViewById(R.id.ProductoName);

                if (spinner.getSelectedItemId() == AdapterView.INVALID_ROW_ID) {
                    respuesta = false;
                    aux.setBackgroundColor(Color.RED);
                    Toast.makeText(getBaseContext(), "Selecciona un producto", Toast.LENGTH_SHORT).show();
                } else {
                    if (spinner.getSelectedItem().toString().equals("Seleccione Producto")) {
                        Toast.makeText(getBaseContext(), "Selecciona un producto", Toast.LENGTH_SHORT).show();
                        aux.setBackgroundColor(Color.RED);
                    } else {
                        Toast.makeText(getBaseContext(), "" + spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                        aux.setBackgroundColor(color);
                    }
                }
                //Producto cierra

                //Presentación
                aux = (TableRow) findViewById(R.id.tableRow2_1);
                spinner = (Spinner) findViewById(R.id.ProductoPresentacion);

                if (spinner.getSelectedItemId() == AdapterView.INVALID_ROW_ID) {
                    respuesta = false;
                    aux.setBackgroundColor(Color.RED);
                    Toast.makeText(getBaseContext(), "Selecciona un producto", Toast.LENGTH_SHORT).show();
                } else {
                    if (spinner.getSelectedItem().toString().equals("Seleccione Presentación")) {
                        Toast.makeText(getBaseContext(), "Selecciona Presentación", Toast.LENGTH_SHORT).show();
                        aux.setBackgroundColor(Color.RED);
                    } else {
                        Toast.makeText(getBaseContext(), "" + spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                        aux.setBackgroundColor(color);
                    }
                }
                //Presentación cierra
            } else {
                //Producto 
                aux = (TableRow) findViewById(R.id.tableRow2_5);
                editText = (EditText) findViewById(R.id.Medicamento_Adquirio_Name_Provisional_Text);
                if (editText.getText().toString().equals("")) {
                    respuesta = false;
                    aux.setBackgroundColor(Color.RED);
                    Toast.makeText(getBaseContext(), "Escribe un producto", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "" + editText.getText(), Toast.LENGTH_SHORT).show();
                    aux.setBackgroundColor(color);
                }
                //Producto cierra

                //Presentación 
                aux = (TableRow) findViewById(R.id.tableRow2_6);
                editText = (EditText) findViewById(R.id.Medicamento_Adquirio_Presentacion_Provisional_Text);
                if (editText.getText().toString().equals("")) {
                    respuesta = false;
                    aux.setBackgroundColor(Color.RED);
                    Toast.makeText(getBaseContext(), "Escribe la presentación", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "" + editText.getText(), Toast.LENGTH_SHORT).show();
                    aux.setBackgroundColor(color);
                }
                //Presentación cierra
            }
        }
        //Cierra si o no de presentación y producto


        //Precio pagado 
        aux = (TableRow) findViewById(R.id.tableRow2_7);
        editText = (EditText) findViewById(R.id.Precio_Pagado_Text);
        if (editText.getText().toString().equals("")) {
            respuesta = false;
            aux.setBackgroundColor(Color.RED);
            Toast.makeText(getBaseContext(), "Escribe el Precio Pagado", Toast.LENGTH_SHORT).show();
        } else {
            try {
                Double.parseDouble(editText.getText().toString());
                aux.setBackgroundColor(color);
            } catch (Exception e) {
                respuesta = false;
                aux.setBackgroundColor(Color.RED);
                Toast.makeText(getBaseContext(), "Verifica el Precio Pagado", Toast.LENGTH_SHORT).show();
            }
        }
        //Precio Pagado cierra

        //Precio pagado 
        aux = (TableRow) findViewById(R.id.tableRow2_8);
        editText = (EditText) findViewById(R.id.Precio_Caja_Text);
        try {
            Double.parseDouble(editText.getText().toString());
            aux.setBackgroundColor(color);
        } catch (Exception e) {
            respuesta = false;
            aux.setBackgroundColor(Color.RED);
            Toast.makeText(getBaseContext(), "Verifica el Precio de Caja", Toast.LENGTH_SHORT).show();
        }
        //Precio Pagado cierra
        
        //Precio Justo 
        aux = (TableRow) findViewById(R.id.tableRow3_1);
        editText = (EditText) findViewById(R.id.Precio_Justo_Text);
        if (editText.getText().toString().equals("")) {
            respuesta = false;
            aux.setBackgroundColor(Color.RED);
            Toast.makeText(getBaseContext(), "Escribe el Precio Justo", Toast.LENGTH_SHORT).show();
        } else {
            try {
                Double.parseDouble(editText.getText().toString());
                aux.setBackgroundColor(color);
            } catch (Exception e) {
                respuesta = false;
                aux.setBackgroundColor(Color.RED);
                Toast.makeText(getBaseContext(), "Verifica el Precio Justo", Toast.LENGTH_SHORT).show();
            }
        }
        //Precio Justo cierra
        
        //Dudar Calidad
        aux = (TableRow) findViewById(R.id.tableRow3_3);
        editText = (EditText) findViewById(R.id.Barato_Dudar_Calidad_Text);
        if (editText.getText().toString().equals("")) {
            respuesta = false;
            aux.setBackgroundColor(Color.RED);
            Toast.makeText(getBaseContext(), "Escribe el Dudar Calidad", Toast.LENGTH_SHORT).show();
        } else {
            try {
                Double.parseDouble(editText.getText().toString());
                aux.setBackgroundColor(color);
            } catch (Exception e) {
                respuesta = false;
                aux.setBackgroundColor(Color.RED);
                Toast.makeText(getBaseContext(), "Verifica el Dudar Calidad", Toast.LENGTH_SHORT).show();
            }
        }
        //Dudar Calidad cierra
        
        //Caro Comprar
        aux = (TableRow) findViewById(R.id.tableRow3_5);
        editText = (EditText) findViewById(R.id.Barato_Dudar_Calidad_Text);
        if (editText.getText().toString().equals("")) {
            respuesta = false;
            aux.setBackgroundColor(Color.RED);
            Toast.makeText(getBaseContext(), "Escribe el Caro Comprar", Toast.LENGTH_SHORT).show();
        } else {
            try {
                Double.parseDouble(editText.getText().toString());
                aux.setBackgroundColor(color);
            } catch (Exception e) {
                respuesta = false;
                aux.setBackgroundColor(Color.RED);
                Toast.makeText(getBaseContext(), "Verifica el Caro Comprar", Toast.LENGTH_SHORT).show();
            }
        }
        //Caro Comprar cierra
        
        //Caro NO Comprar
        aux = (TableRow) findViewById(R.id.tableRow3_7);
        editText = (EditText) findViewById(R.id.Caro_No_Compraria_Text);
        if (editText.getText().toString().equals("")) {
            respuesta = false;
            aux.setBackgroundColor(Color.RED);
            Toast.makeText(getBaseContext(), "Escribe el Caro NO Comprar", Toast.LENGTH_SHORT).show();
        } else {
            try {
                Double.parseDouble(editText.getText().toString());
                aux.setBackgroundColor(color);
            } catch (Exception e) {
                respuesta = false;
                aux.setBackgroundColor(Color.RED);
                Toast.makeText(getBaseContext(), "Verifica el Caro NO Comprar", Toast.LENGTH_SHORT).show();
            }
        }
        //Caro NO Comprar cierra
        
        return respuesta;
    }
    
    

    /**
     * Boton para poner en acción las validaciones y enviar la encuesta.
     */
    private void buttonValidar() {
        Button Boton_Enviar = (Button) findViewById(R.id.Boton_Enviar);
        Boton_Enviar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (validarForm()) {
                } else {
                    Toast.makeText(getBaseContext(), "Revise la encuesta.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

package com.androidhive.androidsqlite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.*;
import java.io.IOException;
import java.util.List;

/**
 * 
 * @author MAGG
 * @version 1.0
 */
public class ActivityMagg extends Activity{
    /** Called when the activity is first created. */
    
    private AutoCompleteTextView textView;
    private DataBaseAndroid myDbHelper; 
    private Context context;
    /** 
     * The Bean needed for passing the pharmacy's attributes.  
     * It's a static variable.
     */ 
    public static FarmaciaBean fb;   
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main);                                  
            context = this;
            
            fb = new FarmaciaBean();//Instancia del bean
            textView = (AutoCompleteTextView)findViewById(R.id.ACFar);//Get the Autocomplete's View
            
            myDbHelper = new DataBaseAndroid(this); //Instancia de la BD           
            try {
                    myDbHelper.createDataBase();                                                
            } catch (IOException ioe) {
                    throw new Error("Unable to create database");
            }

            try {
                myDbHelper.openDataBase();
                String[] columna = {"password"};
                List password = myDbHelper.getColumna("encuestador", columna, "username like '"+Login.usu.getUsername()+"'");
                if(password!= null && password.get(0).equals(Login.usu.getPassword())){                    
                    cargarFarmacias();
                    addKeyListener(); 
                    addListenerOnButton(); 
                }else{
                    Toast.makeText(ActivityMagg.this,"Error con los datos de usuario", Toast.LENGTH_SHORT).show();
                }
            }catch(SQLException sqle){}
            finally{
                myDbHelper.close();
            }
    }
    /**
     * Obtiene las farmacias de la BD. Las pone disponible para el AutoComplete.
     */
    private void cargarFarmacias(){
        String[] columna = {"nombre_farmacia"};//Nombre de la columna para realizar la consulta
        List pruebas = myDbHelper.getColumna("farmacias",columna,null);//Obtenemos la lista con las tuplas resultantes
        /*Convertimos la lista en un Array y lo asociamos con el AutoComplete*/
        String [] farmacias = (String[]) pruebas.toArray(new String[pruebas.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, farmacias);            
        textView.setAdapter(adapter);
    }
    /**
     * Activa el Listener del Autocomplete. Empieza a Actualizar el spinner de colonia cuando 'enter' es presionado.
     */
    private void addKeyListener() {
            // get textViewAutoComplete component
            textView = (AutoCompleteTextView)findViewById(R.id.ACFar);            
            // add a keylistener to keep track user input
            textView.setOnKeyListener(new OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){                                                  
                        actualizarColonia(); //Actualizar el spinner de colonia                    
                        return true;                            
                    }                    
                    return false;
                }
            });
    }
    /**
     * Actualiza el Spinner de colonia. Llama la funcion de actualizar Tipo.
     */
    private void actualizarColonia(){
        String[] columna = {"col_farmacia"};//Nombre de la columna para realizar la consulta
        if(myDbHelper.getColumna("farmacias",columna, "nombre_farmacia = '"+textView.getText()+"'")!=null){
            List colonia = myDbHelper.getColumna("farmacias",columna, "nombre_farmacia = '"+textView.getText()+"'");//Obtenemos la lista con las tuplas resultantes
            final Spinner s = (Spinner)findViewById(R.id.SPFr);//Obtenemos la vista del Spinner de colonia.
            /*Asociamos la lista colonia con el spinner*/
            ArrayAdapter<String> data = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,colonia);
            data.setDropDownViewResource(android.R.layout.simple_spinner_item);
            s.setAdapter(data);
            
            actualizarTipo();//Actualizar el spinner de tipo 
            /* Listener para cambio de selección. Actualiza el spinner Tipo.*/
            s.setOnItemSelectedListener(new OnItemSelectedListener() {
                int count = 0;
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    if (count >= 1)                                                
                        actualizarTipo(); //Actualizar el spinner de tipo                    
                    count++;
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
        }else{
            Toast.makeText(ActivityMagg.this,"Esta farmacia no está registrada", Toast.LENGTH_SHORT).show();            
        }
    }
    /**
     * Actualiza el Spinner de Tipo. Llama la funcion de actualizar Cadena.
     */
    private void actualizarTipo(){
        String[] columna = {"tipo_farmacia"};//Nombre de la columna para realizar la consulta
        Spinner sFar = (Spinner)findViewById(R.id.SPFr);
        final Spinner s = (Spinner)findViewById(R.id.SPTi);//Obtenemos la vista del Spinner de tipo.
        List Tipo = myDbHelper.getColumna("farmacias", columna, "(nombre_farmacia like '"+textView.getText()+"' and col_farmacia like '"+sFar.getSelectedItem()+"')"); //Obtenemos la lista con las tuplas resultantes    
        /*Asociamos la lista tipo con el spinner*/
        ArrayAdapter<String> data = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,Tipo);
        data.setDropDownViewResource(android.R.layout.simple_spinner_item);
        s.setAdapter(data); 
        
        actualizarCadena();//Actualizar el spinner de cadena 
        /* Listener para cambio de selección. Actualiza el spinner Cadena.*/
        s.setOnItemSelectedListener(new OnItemSelectedListener() {
                int count = 0;
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    if (count >= 1)                        
                        actualizarCadena();   //Actualizar el spinner de cadena                  
                    count++;
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {//No implementada
                }
            });
    }
    /**
     * Actualiza el Spinner de Cadena. Llama la funcion de actualizar Codigo Postal.
     */
    private void actualizarCadena(){
        String[] columna = {"cadenafarmacia_des"};//Nombre de la columna para realizar la consulta
        Spinner sFar = (Spinner)findViewById(R.id.SPFr);
        Spinner sTip = (Spinner)findViewById(R.id.SPTi);
        final Spinner s = (Spinner)findViewById(R.id.SPCa);//Obtenemos la vista del Spinner de Cadena.
        //Obtenemos la lista con las tuplas resultantes
        String where = "(nombre_farmacia like '"+textView.getText()+"' and col_farmacia like '"+sFar.getSelectedItem()+"' and tipo_farmacia like '"+sTip.getSelectedItem()+"')";
        List Tipo = myDbHelper.getColumna("farmacias", columna, where);
        /*Asociamos la lista tipo(cadena) con el spinner*/
        ArrayAdapter<String> data = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,Tipo);
        data.setDropDownViewResource(android.R.layout.simple_spinner_item);
        s.setAdapter(data);       
        
        actualizarCP();//Actualizar el spinner de CP 
        /* Listener para cambio de selección. Actualiza el spinner CP.*/
        s.setOnItemSelectedListener(new OnItemSelectedListener() {
                int count = 0;
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    if (count >= 1)
                        actualizarCP(); //Actualizar el spinner de CP                    
                    count++;
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
    }
    /**
     * Actualiza el Spinner de Código Postal. Llama la funcion de actualizar AGEB.
     */
    private void actualizarCP(){
        String[] columna = {"cp_farmacia"};//Nombre de la columna para realizar la consulta
        Spinner sFar = (Spinner)findViewById(R.id.SPFr);
        Spinner sTip = (Spinner)findViewById(R.id.SPTi);
        Spinner sCad = (Spinner)findViewById(R.id.SPCa);
        final Spinner s = (Spinner)findViewById(R.id.SPCP);//Obtenemos la vista del Spinner de Codigo Postal.
        //Obtenemos la lista con las tuplas resultantes
        String where = "(nombre_farmacia like '"+textView.getText()+"' and col_farmacia like '"+sFar.getSelectedItem()+"' and tipo_farmacia like '"+sTip.getSelectedItem()+"' and cadenafarmacia_des like '"+sCad.getSelectedItem()+"')";
        List Tipo = myDbHelper.getColumna("farmacias", columna, where);      
        /*Asociamos la lista tipo(CP) con el spinner*/
        ArrayAdapter<String> data = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,Tipo);
        data.setDropDownViewResource(android.R.layout.simple_spinner_item);
        s.setAdapter(data);     
        
        actualizarAGEB();//Actualizar el spinner de AGEB 
        /* Listener para cambio de selección. Actualiza el spinner AGEB.*/
        s.setOnItemSelectedListener(new OnItemSelectedListener() {
                int count = 0;
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    if (count >= 1)
                        actualizarAGEB();     //Actualizar el spinner de AGEB                
                    count++;
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
    }
    /**
     * Actualiza el Spinner de AGEB. Llama la funcion de actualizar Ciudad.
     */
    private void actualizarAGEB(){
        String[] columna = {"ageb"};//Nombre de la columna para realizar la consulta
        Spinner sFar = (Spinner)findViewById(R.id.SPFr);
        Spinner sTip = (Spinner)findViewById(R.id.SPTi);
        Spinner sCad = (Spinner)findViewById(R.id.SPCa);
        Spinner sCP = (Spinner)findViewById(R.id.SPCP);
        final Spinner s = (Spinner)findViewById(R.id.SPAg);//Obtenemos la vista del Spinner de AGEB.
        //Obtenemos la lista con las tuplas resultantes
        String where = "(nombre_farmacia like '"+textView.getText()+"' and col_farmacia like '"+sFar.getSelectedItem()+"' and tipo_farmacia like '"+sTip.getSelectedItem()+"' and cadenafarmacia_des like '"+sCad.getSelectedItem()+"' and cp_farmacia like '"+sCP.getSelectedItem()+"')";                
        if(myDbHelper.getColumna("farmacias", columna, where)!=null){
            List Tipo = myDbHelper.getColumna("farmacias", columna, where);
            /*Asociamos la lista tipo(AGEB) con el spinner*/
            ArrayAdapter<String> data = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,Tipo);
            data.setDropDownViewResource(android.R.layout.simple_spinner_item);
            s.setAdapter(data);            
        }
        actualizarCiudad();
        /* Listener para cambio de selección. Actualiza el spinner Ciudad.*/
        s.setOnItemSelectedListener(new OnItemSelectedListener() {
                int count = 0;
                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    if (count >= 1)
                        actualizarCiudad();                    
                    count++;
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
    }
    /**
     * Actualiza el Spinner de Ciudad.
     */
    private void actualizarCiudad(){
        String[] columna = {"edo_farmacia"};//Nombre de la columna para realizar la consulta
        Spinner sFar = (Spinner)findViewById(R.id.SPFr);
        Spinner sTip = (Spinner)findViewById(R.id.SPTi);
        Spinner sCad = (Spinner)findViewById(R.id.SPCa);
        Spinner sCP = (Spinner)findViewById(R.id.SPCP);
        Spinner s = (Spinner)findViewById(R.id.SPCi);//Obtenemos la vista del Spinner de Ciudad.
        //Obtenemos la lista con las tuplas resultantes
        String where = "(nombre_farmacia like '"+textView.getText()+"' and col_farmacia like '"+sFar.getSelectedItem()+"' and tipo_farmacia like '"+sTip.getSelectedItem()+"' and cadenafarmacia_des like '"+sCad.getSelectedItem()+"' and cp_farmacia like '"+sCP.getSelectedItem()+"')";                        
        List Tipo = myDbHelper.getColumna("farmacias", columna, where);     
        /*Asociamos la lista tipo(Ciudad) con el spinner*/
        ArrayAdapter<String> data = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,Tipo);
        data.setDropDownViewResource(android.R.layout.simple_spinner_item);
        s.setAdapter(data); 
    }
    /**
     * Listener para el boton de cambio de página. Verifica que los datos sean correctos, de ser ése el caso, manda a la siguiente Actividad.    
     */
    private void addListenerOnButton() {
		Button button = (Button) findViewById(R.id.cmbEnc);
                /* Listener para clic.*/
		button.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View arg0) {
                            Spinner sCol = (Spinner)findViewById(R.id.SPFr);
                            Spinner sTip = (Spinner)findViewById(R.id.SPTi);
                            Spinner sCad = (Spinner)findViewById(R.id.SPCa);
                            Spinner sCP = (Spinner)findViewById(R.id.SPCP);
                            Spinner sAge = (Spinner)findViewById(R.id.SPAg); 
                            Spinner sEdo = (Spinner)findViewById(R.id.SPCi);
                            //En caso de que haya farmacia escrita y datos en los Spinner de colonia y Tipo                               
                            if(textView.getText().length()>0 && sCol.getCount()>0 && sTip.getCount()>0){ 
                                boolean correcto = false;
                                String[] columna = {"nombre_farmacia"};//Nombre de la columna para realizar la consulta
                                //Obtenemos la lista con las tuplas resultantes
                                List farmacias = myDbHelper.getColumna("farmacias", columna, "col_farmacia like '"+sCol.getSelectedItem()+"' and tipo_farmacia like '"+sTip.getSelectedItem()+"' and edo_farmacia like '"+sEdo.getSelectedItem()+"'");                                
                                for(int i=0;i<farmacias.size();i++){//Revisamos que los datos para ver si son consistentes.
                                    if(farmacias.get(i).toString().equals(textView.getText().toString())){                                        
                                        correcto = true;
                                        break;
                                    }
                                }
                                if(correcto){//En caso de que sea correcto, guardamos datos en el Bean y pasamos a la siguiente actividad.
                                    fb.setFarmacia(textView.getText().toString());
                                    fb.setColonia(sCol.getSelectedItem().toString());
                                    fb.setTipoCadena(sTip.getSelectedItem().toString());
                                    fb.setTipo2(sCad.getSelectedItem().toString());
                                    fb.setCP(sCP.getSelectedItem().toString());
                                    fb.setAgeb(sAge.getSelectedItem().toString());
                                    fb.setCiudad(sEdo.getSelectedItem().toString());
                                    Intent intent = new Intent(context, MyAndroid.class);
                                    startActivity(intent);
                                }else{//En caso de que los datos no sean consistentes.
                                    Toast.makeText(ActivityMagg.this,"El nombre de la farmacia no coincide con los datos.\nAl actualizar el nombre de la farmacia es necesario presionar enter", Toast.LENGTH_SHORT).show();
                                }
                            }else{//En caso de que no haya frmacia.
                                Toast.makeText(ActivityMagg.this,"Necesitas escribir el nombre de la farmacia y presionar enter", Toast.LENGTH_SHORT).show();
                            }
			}
		});
    }
}
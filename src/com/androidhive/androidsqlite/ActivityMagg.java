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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import java.io.IOException;
import java.util.List;


public class ActivityMagg extends Activity{
    /** Called when the activity is first created. */
    
    private AutoCompleteTextView textView;
    private DataBaseAndroid myDbHelper; 
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main);                                  
            context = this;
            
            myDbHelper = new DataBaseAndroid(this);            
            try {
                    myDbHelper.createDataBase();                                                
            } catch (IOException ioe) {
                    throw new Error("Unable to create database");
            }

            try {
                    myDbHelper.openDataBase();                    
            }catch(SQLException sqle){}
            
            String[] columna = {"nombre_farmacia"};
            List pruebas = myDbHelper.getColumna("farmacias",columna,null);
            String [] farmacias = (String[]) pruebas.toArray(new String[pruebas.size()]);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                 android.R.layout.simple_dropdown_item_1line, farmacias);
            textView = (AutoCompleteTextView)findViewById(R.id.ACFar);
            textView.setAdapter(adapter);
            addKeyListener(); 
            addListenerOnButton();
            myDbHelper.close();
    }
    
    public void addKeyListener() {
            // get textViewAutoComplete component
            textView = (AutoCompleteTextView)findViewById(R.id.ACFar);
            // add a keylistener to keep track user input
            textView.setOnKeyListener(new OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                        //Toast.makeText(ActivityMagg.this,textView.getText()+"", Toast.LENGTH_LONG).show();                          
                        String[] columna = {"col_farmacia"};
                        List colonia = myDbHelper.getColumna("farmacias",columna, "nombre_farmacia = '"+textView.getText()+"'");
                        Spinner s = (Spinner)findViewById(R.id.SPFr);
                        ArrayAdapter<String> data = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,colonia);
                        data.setDropDownViewResource(android.R.layout.simple_spinner_item);
                        s.setAdapter(data);
                        actualizarTipo();
                        return true;                            
                    }                    
                    return false;
                }
            });
    }
    
    public void actualizarTipo(){
        String[] columna = {"tipo_farmacia"};
        Spinner sFar = (Spinner)findViewById(R.id.SPFr);
        Spinner s = (Spinner)findViewById(R.id.SPTi);
        List Tipo = myDbHelper.getColumna("farmacias", columna, "(nombre_farmacia like '"+textView.getText()+"' and col_farmacia like '"+sFar.getSelectedItem()+"')");      
        ArrayAdapter<String> data = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,Tipo);
        data.setDropDownViewResource(android.R.layout.simple_spinner_item);
        s.setAdapter(data);        
        actualizarCadena();
    }
    
    public void actualizarCadena(){
        String[] columna = {"cadenafarmacia_des"};
        Spinner sFar = (Spinner)findViewById(R.id.SPFr);
        Spinner sTip = (Spinner)findViewById(R.id.SPTi);
        Spinner s = (Spinner)findViewById(R.id.SPCa);
        String where = "(nombre_farmacia like '"+textView.getText()+"' and col_farmacia like '"+sFar.getSelectedItem()+"' and tipo_farmacia like '"+sTip.getSelectedItem()+"')";
        List Tipo = myDbHelper.getColumna("farmacias", columna, where);      
        ArrayAdapter<String> data = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,Tipo);
        data.setDropDownViewResource(android.R.layout.simple_spinner_item);
        s.setAdapter(data);              
        actualizarCP();
    }
    
    public void actualizarCP(){
        String[] columna = {"cp_farmacia"};
        Spinner sFar = (Spinner)findViewById(R.id.SPFr);
        Spinner sTip = (Spinner)findViewById(R.id.SPTi);
        Spinner sCad = (Spinner)findViewById(R.id.SPCa);
        Spinner s = (Spinner)findViewById(R.id.SPCP);
        String where = "(nombre_farmacia like '"+textView.getText()+"' and col_farmacia like '"+sFar.getSelectedItem()+"' and tipo_farmacia like '"+sTip.getSelectedItem()+"' and cadenafarmacia_des like '"+sCad.getSelectedItem()+"')";
        List Tipo = myDbHelper.getColumna("farmacias", columna, where);      
        ArrayAdapter<String> data = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,Tipo);
        data.setDropDownViewResource(android.R.layout.simple_spinner_item);
        s.setAdapter(data);        
        actualizarAGEB();        
    }
    
    public void actualizarAGEB(){
        String[] columna = {"ageb"};
        Spinner sFar = (Spinner)findViewById(R.id.SPFr);
        Spinner sTip = (Spinner)findViewById(R.id.SPTi);
        Spinner sCad = (Spinner)findViewById(R.id.SPCa);
        Spinner sCP = (Spinner)findViewById(R.id.SPCP);
        Spinner s = (Spinner)findViewById(R.id.SPAg);
        String where = "(nombre_farmacia like '"+textView.getText()+"' and col_farmacia like '"+sFar.getSelectedItem()+"' and tipo_farmacia like '"+sTip.getSelectedItem()+"' and cadenafarmacia_des like '"+sCad.getSelectedItem()+"' and cp_farmacia like '"+sCP.getSelectedItem()+"')";                
        if(myDbHelper.getColumna("farmacias", columna, where)!=null){
            List Tipo = myDbHelper.getColumna("farmacias", columna, where);              
            ArrayAdapter<String> data = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,Tipo);
            data.setDropDownViewResource(android.R.layout.simple_spinner_item);
            s.setAdapter(data);            
        }
        actualizarCiudad();
    }
    
    public void actualizarCiudad(){
        String[] columna = {"edo_farmacia"};
        Spinner sFar = (Spinner)findViewById(R.id.SPFr);
        Spinner sTip = (Spinner)findViewById(R.id.SPTi);
        Spinner sCad = (Spinner)findViewById(R.id.SPCa);
        Spinner sCP = (Spinner)findViewById(R.id.SPCP);
        Spinner s = (Spinner)findViewById(R.id.SPCi);
        String where = "(nombre_farmacia like '"+textView.getText()+"' and col_farmacia like '"+sFar.getSelectedItem()+"' and tipo_farmacia like '"+sTip.getSelectedItem()+"' and cadenafarmacia_des like '"+sCad.getSelectedItem()+"' and cp_farmacia like '"+sCP.getSelectedItem()+"')";                        
        List Tipo = myDbHelper.getColumna("farmacias", columna, where);              
        ArrayAdapter<String> data = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,Tipo);
        data.setDropDownViewResource(android.R.layout.simple_spinner_item);
        s.setAdapter(data);           
    }

    private void addListenerOnButton() {
		Button button = (Button) findViewById(R.id.cmbEnc);
		button.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View arg0) {
 
			    Intent intent = new Intent(context, MyAndroid.class);
                            startActivity(intent);   
 
			}
 
		});
    }
}
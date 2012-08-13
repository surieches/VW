/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.androidhive.androidsqlite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author CPP-lap
 */
public class NuevaFarmacia extends Activity{
    public static FarmaciaBean fb;
    private DataBaseAndroid db; 
    private Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {                
            super.onCreate(savedInstanceState);
            setContentView(R.layout.nuevafar);            
            context = this;
            db = new DataBaseAndroid(context); //Instancia de la BD           
            try {
                    db.createDataBase();                      
            } catch (IOException ioe) {
                    throw new Error("Unable to create database");
            }
            
            try {                
                db.openDataBase(); 
                llenarTipoCadena();
                addButtonListener();
            }catch(SQLException sqle){}
            finally{
                db.close();
            }            
    }
    /**
     * Llena el Spinner con todos los tipos de cadena
     */
    public void llenarTipoCadena(){
        
        try {
            final Spinner s = (Spinner)findViewById(R.id.SPNFTip);//Obtenemos la vista del Spinner de tipo.            
            String[] columna = {"tipo_farmacia"};
            List tipo = db.getColumna("farmacias", columna, null);
            tipo.add(0, "Selecciona un tipo de farmacia");
            /*Asociamos la lista tipo con el spinner*/
            ArrayAdapter<String> data = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tipo);
            data.setDropDownViewResource(android.R.layout.simple_spinner_item);
            s.setAdapter(data); 
        }catch(SQLException sqle){}
        finally{
            db.close();
        }
    }
    /**
     * Accion del boton para pasar a la entrevista
     */
    public void addButtonListener(){
        Button button = (Button) findViewById(R.id.btnNFReg);
        /* Listener para clic.*/
        button.setOnClickListener(new View.OnClickListener() { 
                @Override
                public void onClick(View arg0) {
                    EditText far = (EditText)findViewById(R.id.ETNFFar);
                    EditText col = (EditText)findViewById(R.id.ETNFCol);
                    Spinner tip = (Spinner)findViewById(R.id.SPNFTip);
                    EditText dir = (EditText)findViewById(R.id.ETNFDir);
                    EditText cp = (EditText)findViewById(R.id.ETNFCP);
                    EditText mun = (EditText)findViewById(R.id.ETNFMun);
                    EditText ciu = (EditText)findViewById(R.id.ETNFCiu);
                    if(far.length()>0 && col.length()>0 && dir.length()>0 && cp.length()>0 && mun.length()>0 && ciu.length()>0 && tip.getSelectedItemPosition()>0){
                        fb = new FarmaciaBean();//Instancia del bean
                        fb.setFarmacia(far.getText().toString());
                        fb.setColonia(col.getText().toString());
                        fb.setTipoCadena(tip.getSelectedItem().toString());
                        fb.setDireccion(dir.getText().toString());                        
                        fb.setCP(cp.getText().toString());                        
                        fb.setMunicipio(mun.getText().toString());
                        fb.setCiudad(ciu.getText().toString());
                        fb.setPendiente(true);
                        Intent intent = new Intent(context, MyAndroid.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(NuevaFarmacia.this,"Necesitas llenar todos los campos", Toast.LENGTH_SHORT).show();
                    }
                    /*Intent intent = new Intent(context, MyAndroid.class);
                    startActivity(intent);  */
                }
        });
    }
}

package com.androidhive.androidsqlite;

import Beans.FarmaciaBean;
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
                db.openDataBase();                 
                llenarTipoCadena();
                addButtonListener();
            }catch (IOException ioe) {
                throw new Error("Unable to create database");
            }catch(SQLException e){
                Toast.makeText(NuevaFarmacia.this,"Un error sucedio: "+e.toString()+"\nReinicie la aplicación por favor.", Toast.LENGTH_LONG).show();
            }
            finally{
                db.close();
            }            
    }
    /**
     * Llena el Spinner con todos los tipos de cadena
     */
    public void llenarTipoCadena(){                
        final Spinner s = (Spinner)findViewById(R.id.SPNFTip);//Obtenemos la vista del Spinner de tipo.            
        String[] columna = {"tipo_farmacia"};
        List tipo = db.getColumna("farmacias", columna, null);
        tipo.add(0, "Selecciona un tipo de farmacia");
        /*Asociamos la lista tipo con el spinner*/
        ArrayAdapter<String> data = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,tipo);
        data.setDropDownViewResource(android.R.layout.simple_spinner_item);
        s.setAdapter(data); 
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
                    EditText tel = (EditText)findViewById(R.id.ETNFTel);
                    if(far.length()>0 && col.length()>0 && dir.length()>0 && cp.length()>0 && mun.length()>0 && ciu.length()>0 && tel.length()>0 && tip.getSelectedItemPosition()>0){
                        fb = new FarmaciaBean();//Instancia del bean
                        fb.setFarmacia(far.getText().toString());
                        fb.setColonia(col.getText().toString());
                        fb.setTipoCadena(tip.getSelectedItem().toString());
                        fb.setDireccion(dir.getText().toString());                        
                        fb.setCP(cp.getText().toString());                        
                        fb.setMunicipio(mun.getText().toString());
                        fb.setCiudad(ciu.getText().toString());
                        fb.setTelefono(tel.getText().toString());
                        fb.setAgeb("");
                        fb.setPendiente(true);                        
                        String etiquetas = "NOMBRE_FARMACIA,TIPO_FARMACIA,DIR_FARMACIA,COL_FARMACIA,CP_FARMACIA,AGEB,MUNICIPIO_FARMACIA,EDO_FARMACIA,TELEFONO,PENDIENTE";
                        String valores = fb.getFarmacia()+","+fb.getTipoCadena()+","+fb.getDireccion()+","+fb.getColonia()+","+fb.getCP()+","+
                                fb.getAgeb()+","+fb.getMunicipio()+","+fb.getCiudad()+","+fb.getTelefono()+",1"; 
                        fb.setId(db.insertar("farmacias", etiquetas, valores));//Insertamos la nueva faracia en la bd y agregamos su ID al bean
                        Intent intent = new Intent(context, MyAndroid.class);
                        startActivity(intent);                        
                    }else{
                        Toast.makeText(NuevaFarmacia.this,"Necesitas llenar todos los campos", Toast.LENGTH_SHORT).show();
                    }
                }
        });
    }
}

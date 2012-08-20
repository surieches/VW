package com.androidhive.androidsqlite;

import Beans.Usuario;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author CPP-lap
 */
public class Login extends Activity{
    private DataBaseAndroid myDbHelper;
    private Context context;
    public static Usuario usu; 
    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.login);
            usu = new Usuario();
            context = this;
            myDbHelper = new DataBaseAndroid(this); //Instancia de la BD                       
            try {
                myDbHelper.createDataBase(); 
                myDbHelper.openDataBase(); 
                iniciarSesion();
            }
            catch (IOException ioe) {
                throw new Error("Unable to create database");
            }catch(SQLException e){
                Toast.makeText(Login.this,"Un error sucedio: "+e.toString()+"\nReinicie la aplicación por favor.", Toast.LENGTH_LONG).show();
            }finally{
                myDbHelper.close();
            }
    }

    private void iniciarSesion() {                            
        Button btn = (Button)findViewById(R.id.btIng);
        btn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View arg0) {
                EditText tv = (EditText)findViewById(R.id.ETUsu);                
                String[] columna = {"password"};
                List password = myDbHelper.getColumna("encuestador", columna, "username like '"+tv.getText()+"'");
                if(password!=null){
                    EditText et = (EditText)findViewById(R.id.ETPas);
                    if(password.get(0).toString().equals(et.getText().toString())){
                        columna[0] = "_id";                        
                        password = myDbHelper.getColumna("encuestador", columna, "username like '"+tv.getText()+"'");                        
                        usu.setUsername(tv.getText().toString());
                        usu.setPassword(et.getText().toString());
                        usu.setIdusu(password.get(0).toString());
                        Intent intent = new Intent(context, ActivityMagg.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(Login.this, "Usuario y/o password incorrecto.",Toast.LENGTH_SHORT).show();
                    }                    
                }else{
                    Toast.makeText(Login.this, "Usuario y/o password incorrecto.",Toast.LENGTH_SHORT).show();
                }            
            }
 
        });        
    }    
}

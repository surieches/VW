package com.androidhive.androidsqlite;

import Beans.FarmaciaBean;
import android.app.Activity;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

/**
 *
 * @author CPP-lap
 */
public class Sincronizacion extends Activity {

    private Button button;
    private DataBaseAndroid bd; 

    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.sincronizacion);
            
            bd = new DataBaseAndroid(this); //Instancia de la BD                      
            
            try {
                bd.createDataBase(); 
                bd.openDataBase();
                addListenerOnButton();
            } catch (IOException ex) {
                throw new Error("Unable to create database");
            }catch(SQLException e){
                Toast.makeText(Sincronizacion.this,"Un error sucedio: "+e.toString()+"\nReinicie la aplicación por favor.", Toast.LENGTH_LONG).show();
            }finally{
                bd.close();
            }            
    }
    /**
     * Listener del boton para enviar los datos. Manda a llamar el metodo postData.
     */
    public void addListenerOnButton() {
            button = (Button) findViewById(R.id.btnSNSin);
            button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {			                              
                        postData();
                    }
            });
    }                
    /**
     * Envia los datos a la IP establecida. Los datos enviados son objetos JSON.
     */
    public void postData() {
        // Create a new HttpClient and Post Header
        HttpParams httpParams = new BasicHttpParams();
        HttpClient httpclient = new DefaultHttpClient(httpParams);
        HttpPost httppost = new HttpPost("http://192.168.0.203:8084/Pruebas/Android");

        try {            
            Gson gson = new Gson();
            String json;
            
            FarmaciaBean fb;
            String[] columna = {"*"};
            List farmacias = bd.getTabla("farmacias", columna, "pendiente = 1"); 
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(farmacias.size());
            nameValuePairs.add(new BasicNameValuePair("n", farmacias.size()+""));
            for(int i=0;i<farmacias.size();i++){
                fb = new FarmaciaBean();
                String[] aux = (String[])farmacias.get(i);
                fb.setId(Integer.parseInt(aux[0]));
                fb.setFarmacia(aux[6]);
                fb.setTipoCadena(aux[7]);
                fb.setDireccion(aux[8]);
                fb.setColonia(aux[9]);
                json = gson.toJson(fb);
                // Add your data
                nameValuePairs.add(new BasicNameValuePair("farmacia"+i, json));                
            }            
            //nameValuePairs.add(new BasicNameValuePair("bean", json));                
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); 
            ResponseHandler<String> responseHandler=new BasicResponseHandler();
            // Execute HTTP Post Request               
            String response = httpclient.execute(httppost,responseHandler);
            Toast.makeText(Sincronizacion.this,response, Toast.LENGTH_LONG).show();
        } catch (Exception e) {                
            Toast.makeText(Sincronizacion.this,"Un error sucedio: "+e.toString()+"\nReinicie la aplicación por favor.", Toast.LENGTH_LONG).show();                
        }
    }
}

package com.androidhive.androidsqlite;

import Beans.BeanEncuesta;
import Beans.FarmaciaBean;
import Beans.Producto;
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
import org.apache.http.client.methods.HttpPost;
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
        HttpPost httppost = new HttpPost("http://192.168.0.235:8084/Pruebas/Android");

        try {            
            Gson gson = new Gson();
            String json;
            
            FarmaciaBean fb;
            String[] columna = {"*"};
            List farmacias = bd.getTabla("farmacias", columna, "pendiente = 1");
            List productos = bd.getTabla("productos", columna,"pendiente = 1");            
            List general = bd.getTabla("general", columna, null);
            List medicamento = bd.getTabla("medicamento", columna, null);
            
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(farmacias.size());
            nameValuePairs.add(new BasicNameValuePair("nfar", farmacias.size()+""));
            for(int i=0;i<farmacias.size();i++){
                /*Creamos Bean de farmacia*/
                fb = new FarmaciaBean();
                String[] aux = (String[])farmacias.get(i);
                fb.setId(Integer.parseInt(aux[0]));fb.setFarmacia(aux[6]);fb.setColonia(aux[9]);fb.setTipoCadena(aux[7]);
                fb.setDireccion(aux[8]);fb.setCP(aux[10]);fb.setMunicipio(aux[12]);fb.setCiudad(aux[13]);
                fb.setTelefono(aux[16]);fb.setAgeb(aux[11]);fb.setPendiente(true);
                json = gson.toJson(fb);
                Toast.makeText(Sincronizacion.this,json, Toast.LENGTH_LONG).show();
                nameValuePairs.add(new BasicNameValuePair("farmacia"+i, json));//Add farmacia bean.
            }
            nameValuePairs.add(new BasicNameValuePair("npro", productos.size()+""));
            for(int i=0;i<productos.size();i++){
                String[] aux =(String[]) productos.get(i);
                Producto pr = new Producto(Integer.parseInt(aux[0]),aux[1],aux[2],true);
                json = gson.toJson(pr);
                Toast.makeText(Sincronizacion.this,json, Toast.LENGTH_LONG).show();
            }
            nameValuePairs.add(new BasicNameValuePair("nfar", general.size()+""));
            for(int i=0;i<general.size();i++){
                String[] aux = (String[])general.get(i);
                BeanEncuesta be = new BeanEncuesta();
                be.setID_General(aux[0]);be.setEncuestador(aux[1]);be.setFecha(aux[2]);be.setHora(aux[3]);be.setFarmacia(aux[4]);be.setColonia(aux[5]);be.setTipo1(aux[6]);
                be.setTipo2(aux[7]);be.setCP(aux[8]);be.setAgeb(aux[9]);be.setCiudad(aux[10]);be.setSexo(aux[11]);be.setEdad(aux[12]);
                be.setPendiente(aux[13]);
                json = gson.toJson(be);                
                Toast.makeText(Sincronizacion.this, json, Toast.LENGTH_LONG).show();
            }
            nameValuePairs.add(new BasicNameValuePair("nmed", medicamento.size()+""));
            for(int i=0;i<medicamento.size();i++){
                String[] aux = (String[])medicamento.get(i);
                Producto pr = new Producto(Integer.parseInt(aux[0]),aux[1],aux[2],aux[3],aux[4],aux[5],false);
                json = gson.toJson(pr);
                Toast.makeText(Sincronizacion.this, json, Toast.LENGTH_LONG).show();
            }
            nameValuePairs.add(new BasicNameValuePair("bean", ""));                
            /*httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); 
            ResponseHandler<String> responseHandler=new BasicResponseHandler();
            // Execute HTTP Post Request               
            String response = httpclient.execute(httppost,responseHandler);
            Toast.makeText(Sincronizacion.this,response, Toast.LENGTH_LONG).show();*/
        } catch (Exception e) {                
            Toast.makeText(Sincronizacion.this,"Un error sucedio: "+e.toString()+"\nReinicie la aplicación por favor.", Toast.LENGTH_LONG).show();                
        }
    }
}

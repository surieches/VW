package Cpp.Utilities;

import android.content.ContentValues;

/**
 *
 * @author CPP-lap
 */
public class Utilidades {
    public static ContentValues crearContentValues(String etiquetas, String valores){
        ContentValues cv = new ContentValues();
        String[] nombres = etiquetas.split(",");
        String[] valor = valores.split(",");
        for(int i=0;i<nombres.length;i++){
            cv.put(nombres[i], valor[i]);
        }
        return cv;
    }    
}

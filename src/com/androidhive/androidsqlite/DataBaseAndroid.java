package com.androidhive.androidsqlite;

import Cpp.Utilities.Utilidades;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataBaseAndroid extends SQLiteOpenHelper{
 
    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.androidhive.androidsqlite/databases/";
    private static String DB_NAME = "psmandroid.db"; 
    private SQLiteDatabase myDataBase = null;  
    private final Context myContext;
 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseAndroid(Context context) { 
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    }	
 
  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public boolean createDataBase() throws IOException{
    	boolean dbExist = checkDataBase();
    	if(dbExist){            
    		//do nothing - database already exist
    	}else{            
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            try {
                    copyDataBase();
            } catch (IOException e) {
                    throw new Error("Error copying database");
            }
    	}
        return dbExist; 
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
    	SQLiteDatabase checkDB = null;
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    	}catch(SQLiteException e){
    		//database does't exist yet.
    	}
 
    	if(checkDB != null){
    		checkDB.close();
    	}
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
 
    public void openDataBase() throws SQLException{
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);         
    }
    /**
     * Distinc de la columna especificada.
     * @param tabla Nombre de la tabla a consultar.
     * @param columna Nombre de la columna que se desea obtener.
     * @param where Condicion where para la consulta sin la palabra 'where' (null en caso de no existir condicion).
     * @return Lista con los datos de la columna especificada.
     */    
    public List getColumna(String tabla, String[] columna, String where) {
        List filas = new ArrayList();
        SQLiteDatabase db = this.myDataBase;               
        if(db!=null){            
            Cursor c = this.getReadableDatabase().query(true, tabla, columna, where, null, null, null,null,null);            
            if(c.moveToFirst()){
                do{                    
                    filas.add(c.getString(0));
                }while(c.moveToNext());
                return filas;
            }
            else
                return null;
        }
        else
            return null;
    }
    
    /**
     * No Distinc de la columna especificada.
     * @param tabla Nombre de la tabla a consultar.
     * @param columna Nombre de la columna que se desea obtener.
     * @param where Condicion where para la consulta sin la palabra 'where' (null en caso de no existir condicion).
     * @return Lista con los datos de la columna especificada.
     */    
    public List<String[]> getTabla(String tabla, String[] columna, String where) {
        List<String[]> filas = new ArrayList();//Lista 
        String[] fila;
        SQLiteDatabase db = this.myDataBase;               
        if(db!=null){            
            Cursor c = this.getReadableDatabase().query(false, tabla, columna, where, null, null, null,null,null);            
            if(c.moveToFirst()){                
                do{                    
                    fila = new String[c.getColumnCount()];
                    for(int i=0;i<c.getColumnCount();i++)
                        fila[i] = c.getString(i);
                    filas.add(fila);
                }while(c.moveToNext());
                return filas;
            }
            else
                return new ArrayList();
        }
        else
            return new ArrayList();
    }
    
    public long insertar(String tabla, String atributos, String valores){
        return this.getWritableDatabase().insert(tabla, null, Utilidades.crearContentValues(atributos, valores));
    }
    @Override
    public synchronized void close() {
        if(myDataBase != null)
                myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
        // Add your public helper methods to access and get content from the database.
       // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
       // to you to create adapters for your views.
}
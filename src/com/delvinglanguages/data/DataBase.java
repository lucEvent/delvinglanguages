package com.delvinglanguages.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBase extends SQLiteOpenHelper {

	private static final String DEBUG = "##DataBase##";
	
	private static final String DATABASE_NAME = "delving.db";
	private static final int DATABASE_VERSION = 3;

	// Database SQL CREATES 
    public static final String estadisticas = "db_estadisticas"; 
    public static final String col_estadisticas[] = { "_id", "npint", "npacert1", "npacert2", "npacert3", "npfall"  }; 
      
    private static final String CREATE_ESTADISTICAS = 
        "CREATE TABLE " + estadisticas + " (" + 
        col_estadisticas[0] + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
        col_estadisticas[1] + " INTEGER," + 
        col_estadisticas[2] + " INTEGER," + 
        col_estadisticas[3] + " INTEGER," + 
        col_estadisticas[4] + " INTEGER," + 
        col_estadisticas[5] + " INTEGER" + 
        ");"; 
  
    public static final String idioma = "db_idioma"; 
    public static final String col_idioma[] = { "_id", "nombre", "estadisticas", "settings" }; 
  
    private static final String CREATE_IDIOMA = 
        "CREATE TABLE " + idioma + " (" + 
        col_idioma[0] + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
        col_idioma[1] + " TEXT NOT NULL," + 
        col_idioma[2] + " INTEGER," + 
        col_idioma[3] + " TEXT NOT NULL," + //Esto es lo nuevo 
        "FOREIGN KEY(" + col_idioma[2] + ") REFERENCES " + estadisticas + "(" + col_estadisticas[0] + ")" + 
        ");"; 
  
    public static final String palabra = "db_palabra"; 
    public static final String col_palabra[] = { "_id", "nombre", "traduccion", "idioma", "pronunciacion", "tipo", "enpapelera", "priority" }; 
      
    private static final String CREATE_PALABRA = 
        "CREATE TABLE " + palabra + " (" + 
        col_palabra[0] + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
        col_palabra[1] + " TEXT NOT NULL," + 
        col_palabra[2] + " TEXT NOT NULL," + 
        col_palabra[3] + " INTEGER," + 
        col_palabra[4] + " TEXT NOT NULL," + 
        col_palabra[5] + " INTEGER," + 
        col_palabra[6] + " INTEGER," + 
        col_palabra[7] + " INTEGER," + 
        "FOREIGN KEY(" + col_palabra[3] + ") REFERENCES " + idioma + "(" + col_idioma[0] + ")" + 
        ");"; 
      
    public static final String almacen = "db_almacen"; 
    public static final String col_almacen[] = { "_id", "nombre", "idioma", "tipo" };    
      
    private static final String CREATE_ALMACEN = 
        "CREATE TABLE " + almacen + " (" + 
        col_almacen[0] + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
        col_almacen[1] + " TEXT NOT NULL," + 
        col_almacen[2] + " INTEGER," + 
        col_almacen[3] + " INTEGER," + 
        "FOREIGN KEY(" + col_almacen[2] + ") REFERENCES " + idioma + "(" + col_idioma[0] + ")" + 
        ");"; 
      
    public static final String test = "db_test"; 
    public static final String col_test[] = { "_id", "nombre", "idioma", "contenido" };  
      
    private static final String CREATE_TEST = 
        "CREATE TABLE " + test + " (" + 
        col_test[0] + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
        col_test[1] + " TEXT NOT NULL," + 
        col_test[2] + " INTEGER," + 
        col_test[3] + " TEXT NOT NULL," + 
        "FOREIGN KEY(" + col_test[2] + ") REFERENCES " + idioma + "(" + col_idioma[0] + ")" + 
        ");"; 
	
	public static final String tiempo_verbal = "db_tiempo_verbal";
	public static final String col_tiempo_verbal[] = { "_id", "idioma", "verbo", "formas", "pronunciacion", "opciones" };	

	private static final String CREATE_TIEMPO_VERBAL =
			"CREATE TABLE " + tiempo_verbal + " (" +
			col_tiempo_verbal[0] + " INTEGER PRIMARY KEY AUTOINCREMENT," +
			col_tiempo_verbal[1] + " INTEGER," +
			col_tiempo_verbal[2] + " INTEGER," +
			col_tiempo_verbal[3] + " TEXT NOT NULL," +
			col_tiempo_verbal[4] + " TEXT NOT NULL," +
			col_tiempo_verbal[5] + " INTEGER," +
			"FOREIGN KEY(" + col_tiempo_verbal[1] + ") REFERENCES " + idioma + "(" + col_idioma[0] + ")" +
			"FOREIGN KEY(" + col_tiempo_verbal[2] + ") REFERENCES " + palabra + "(" + col_palabra[0] + ")" + 
	    ");";

	
	public DataBase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(DEBUG, "En onCreate");

		db.execSQL(CREATE_ESTADISTICAS);
		db.execSQL(CREATE_IDIOMA);
		db.execSQL(CREATE_PALABRA);
		db.execSQL(CREATE_ALMACEN);
		db.execSQL(CREATE_TEST);
		db.execSQL(CREATE_TIEMPO_VERBAL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(DEBUG,"Upgrading DB from VERS: " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		
		//Delete all need tables
		//db.execSQL("DROP TABLE "+ palabra);

		//Create all need tables
	//	db.execSQL("DROP TABLE db_conjugacion");
	//	db.execSQL(CREATE_TIEMPO_VERBAL);
	}
}
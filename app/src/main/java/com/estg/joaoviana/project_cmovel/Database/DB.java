package com.estg.joaoviana.project_cmovel.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by PJ on 13/04/2017.
 */

public class DB extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "favorites.db";

    public DB(Context context){super(context,DATABASE_NAME,null,DATABASE_VERSION);}

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Contrato.Place.SQL_CREATE_ENTRIES);

       // db.execSQL("insert into " + Contrato.Place.TABLE_NAME + " values (1,'12345','teste1','icon1',1.1,1.2,'');");
        //db.execSQL("insert into " + Contrato.Place.TABLE_NAME + " values (2,'12346','teste2','icon2',2.2,2.3,'');");

    }
    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion){
        db.execSQL(Contrato.Place.SQL_DROP_ENTRIES);

        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db,int oldVersion, int newVersion){
        onUpgrade(db,oldVersion,newVersion);
    }


}
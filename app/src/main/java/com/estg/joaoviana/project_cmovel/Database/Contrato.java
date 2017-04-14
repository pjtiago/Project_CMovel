package com.estg.joaoviana.project_cmovel.Database;

import android.provider.BaseColumns;

/**
 * Created by PJ on 13/04/2017.
 */

public class Contrato {
    private static final String TEXT_TYPE = " TEXT ";
    private static final String INT_TYPE = " INTEGER ";
    private static final String DOUBLE_TYPE = " DOUBLE ";

    public Contrato() {
    }



    public static abstract class Place implements BaseColumns {
        public static final String TABLE_NAME = "place";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ICON = "icon";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";


        public static final String[] PROJECTION = {Place._ID,Place.COLUMN_ID,Place.COLUMN_NAME,
                Place.COLUMN_ICON,Place.COLUMN_LATITUDE,Place.COLUMN_LONGITUDE};

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + Place.TABLE_NAME + "(" +
                        Place._ID + INT_TYPE + " PRIMARY KEY," +
                        Place.COLUMN_ID + TEXT_TYPE + "," +
                        Place.COLUMN_NAME + TEXT_TYPE + "," +
                        Place.COLUMN_ICON+ TEXT_TYPE + "," +
                        Place.COLUMN_LATITUDE + DOUBLE_TYPE + "," +
                        Place.COLUMN_LONGITUDE + DOUBLE_TYPE +");";


        public static final String SQL_DROP_ENTRIES =
                "DROP TABLE " + Place.TABLE_NAME + ";";
    }





}
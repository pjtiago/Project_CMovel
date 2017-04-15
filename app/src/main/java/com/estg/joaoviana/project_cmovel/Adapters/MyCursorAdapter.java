package com.estg.joaoviana.project_cmovel.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.estg.joaoviana.project_cmovel.Database.Contrato;
import com.estg.joaoviana.project_cmovel.R;
import com.estg.joaoviana.project_cmovel.authentication.Connectivity;

import java.net.URL;

/**
 * Created by PJ on 14/04/2017.
 */

public class MyCursorAdapter extends CursorAdapter {
    private Context mContext;
    private int id;
    private Cursor mCursor;


    public MyCursorAdapter(Context context, Cursor c) {
        super(context, c);
        mContext = context;
        mCursor = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.layout_linha2,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        TextView vicinity = (TextView) view.findViewById(R.id.vicinity);
        TextView name = (TextView) view.findViewById(R.id.name);
       /* TextView latitude = (TextView) view.findViewById(R.id.latitude);
        TextView longitude = (TextView) view.findViewById(R.id.longitude);
        TextView ref = (TextView) view.findViewById(R.id.ref);*/
        TextView description = (TextView) view.findViewById(R.id.description);

        String url = mCursor.getString(cursor.getColumnIndexOrThrow(Contrato.Place.COLUMN_ICON));
        icon.setImageBitmap(getBitmap(url));
        vicinity.setText("morada: " + mCursor.getString(cursor.getColumnIndexOrThrow(Contrato.Place.COLUMN_VICINITY)));
        name.setText("name: " + mCursor.getString(cursor.getColumnIndexOrThrow(Contrato.Place.COLUMN_NAME)));
        /*latitude.setText("Lat: " + String.valueOf(mCursor.getDouble(cursor.getColumnIndexOrThrow(Contrato.Place.COLUMN_LATITUDE))));
        longitude.setText("Long: " + String.valueOf(mCursor.getDouble(cursor.getColumnIndexOrThrow(Contrato.Place.COLUMN_LONGITUDE))));
        ref.setText("Ref: " + mCursor.getString(cursor.getColumnIndexOrThrow(Contrato.Place.COLUMN_ID)));*/
        description.setText("Description: " + mCursor.getString(cursor.getColumnIndexOrThrow(Contrato.Place.COLUMN_DESCRIPTION)));
    }

    public Bitmap getBitmap(String bitmapUrl) {
        try
        {
            URL url = new URL(bitmapUrl);
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        }
        catch(Exception ex) {return null;}
    }

}


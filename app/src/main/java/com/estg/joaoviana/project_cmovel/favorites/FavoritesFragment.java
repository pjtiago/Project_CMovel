package com.estg.joaoviana.project_cmovel.favorites;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.estg.joaoviana.project_cmovel.Adapters.MyCursorAdapter;
import com.estg.joaoviana.project_cmovel.Database.Contrato;
import com.estg.joaoviana.project_cmovel.Database.DB;
import com.estg.joaoviana.project_cmovel.R;


public class FavoritesFragment extends Fragment {

    View rootView;

    DB mDbHelper;
    SQLiteDatabase db;
    Cursor c;
    ListView listFavorites;
    SimpleCursorAdapter adapter;
    MyCursorAdapter mAdapter;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    public static FavoritesFragment newInstance() {
        FavoritesFragment fragment = new FavoritesFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listFavorites = (ListView) getActivity().findViewById(R.id.listFavorites);
        preencheLista();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_favorites, null);
        } else {
            ((ViewGroup) container.getParent()).removeView(rootView);
            rootView = inflater.inflate(R.layout.fragment_favorites, null);
        }

        mDbHelper = new DB(rootView.getContext());
        db = mDbHelper.getReadableDatabase();

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (c != null && !c.isClosed()) {
            c.close();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        if (c != null && !c.isClosed()) {
            c.close();
        }

    }

    private void preencheLista() {
        c = db.rawQuery("select "+Contrato.Place._ID+", "+Contrato.Place.COLUMN_ID+", "+
                Contrato.Place.COLUMN_NAME+", "+ Contrato.Place.COLUMN_ICON+", "+
                Contrato.Place.COLUMN_LATITUDE+", "+ Contrato.Place.COLUMN_LONGITUDE+
                " FROM " + Contrato.Place.TABLE_NAME, null);
/*
        String[] adapterCols = new String[]{Contrato.Place._ID,Contrato.Place.COLUMN_ID};
        int[] adapterRowViews = new int[]{android.R.id.text1,android.R.id.text2};
        adapter = new SimpleCursorAdapter(
                getActivity(),
                android.R.layout.simple_list_item_2,
                c,
                adapterCols,
                adapterRowViews,
                SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);*/
        mAdapter = new MyCursorAdapter(getContext(),c);



        listFavorites.setAdapter(mAdapter);
    }

}

package com.estg.joaoviana.project_cmovel.favorites;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
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

    TextView textIdUpdate;
    EditText editTextUpdate;
    Button btn_update,btn_cancel;


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

        textIdUpdate = (TextView) getActivity().findViewById(R.id.textIdUpdate);
        textIdUpdate.setVisibility(View.INVISIBLE);
        editTextUpdate = (EditText)getActivity().findViewById(R.id.editTextUpdate);
        editTextUpdate.setVisibility(View.INVISIBLE);
        btn_update = (Button)getActivity().findViewById(R.id.btn_update);
        btn_update.setVisibility(View.INVISIBLE);
        btn_cancel = (Button)getActivity().findViewById(R.id.btn_cancel);
        btn_cancel.setVisibility(View.INVISIBLE);

        btn_update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int id_place = Integer.parseInt(textIdUpdate.getText().toString());
                hideKeyboard(v);
                updateInDB(id_place);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                hideKeyboard(v);
                cancelButton();
            }
        });

        listFavorites = (ListView) getActivity().findViewById(R.id.listFavorites);
        registerForContextMenu(listFavorites);
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
                Contrato.Place.COLUMN_LATITUDE+", "+ Contrato.Place.COLUMN_LONGITUDE+", "+
                Contrato.Place.COLUMN_DESCRIPTION+", "+ Contrato.Place.COLUMN_VICINITY+
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
    @Override
    public void onCreateContextMenu(ContextMenu menu, View  v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,v,menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.cmenu1,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int itemPosition = info.position;
        c.moveToPosition(itemPosition);
        int id_place = c.getInt(c.getColumnIndex(Contrato.Place._ID));
        String descr = c.getString(c.getColumnIndex(Contrato.Place.COLUMN_DESCRIPTION));
        switch (item.getItemId()){
            case R.id.removeItem:
                deleteFromDB(id_place);
                Toast.makeText(getActivity(), R.string.favorite_deleted, Toast.LENGTH_LONG).show();
                return true;

            case R.id.updateItem:
                getFieldsForUpdate(id_place,descr);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    private void refresh() {
        preencheLista();
        mAdapter.swapCursor(c);
    }

    private void getFieldsForUpdate(int id_place,String description) {
        editTextUpdate.setVisibility(View.VISIBLE);
        btn_update.setVisibility(View.VISIBLE);
        btn_cancel.setVisibility(View.VISIBLE);
        textIdUpdate.setText(id_place+"");
        editTextUpdate.setText(description);
    }

    private void updateInDB(int id_place) {
        editTextUpdate.setVisibility(View.VISIBLE);
        btn_update.setVisibility(View.VISIBLE);
        btn_cancel.setVisibility(View.VISIBLE);
        String description = editTextUpdate.getText().toString();
        ContentValues cv = new ContentValues();
        cv.put(Contrato.Place.COLUMN_DESCRIPTION,description);
        db.update(Contrato.Place.TABLE_NAME,cv,Contrato.Place._ID + "= ?",new String[]{id_place+""});
        Toast.makeText(getActivity(), R.string.updated_sucess, Toast.LENGTH_LONG).show();
        editTextUpdate.setText("");
        refresh();
    }

    private void cancelButton(){
        editTextUpdate.setText("");
        editTextUpdate.setVisibility(View.INVISIBLE);
        btn_update.setVisibility(View.INVISIBLE);
        btn_cancel.setVisibility(View.INVISIBLE);
    }

    private void deleteFromDB(int id) {
        db.delete(Contrato.Place.TABLE_NAME,Contrato.Place._ID + " = ?",new String[]{id+""});
        Toast.makeText(getActivity(), R.string.deleted_success, Toast.LENGTH_LONG).show();
        refresh();
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}

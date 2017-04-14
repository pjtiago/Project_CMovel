package com.estg.joaoviana.project_cmovel.properties;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.estg.joaoviana.project_cmovel.R;
import com.estg.joaoviana.project_cmovel.Utils;

import java.util.ArrayList;

public class PropertiesFragment extends Fragment {

    EditText editTextRadius;
    View rootView;
    Spinner sp;

    ArrayList<String> types_of_maps;
    String mapChoose;

    public PropertiesFragment() {
        // Required empty public constructor
    }


    public static PropertiesFragment newInstance() {
        PropertiesFragment fragment = new PropertiesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        types_of_maps = new ArrayList<>();
        types_of_maps.add("Satellite");
        types_of_maps.add("Terrain");
        types_of_maps.add("Hybrid");
        types_of_maps.add("Normal");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_properties, null);
        } else {
            ((ViewGroup) container.getParent()).removeView(rootView);
            rootView = inflater.inflate(R.layout.fragment_properties, null);
        }
        return rootView;
}
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editTextRadius = (EditText)getActivity().findViewById(R.id.editTextRadius);
        editTextRadius.setText(Utils.radius);

        sp = (Spinner) getActivity().findViewById(R.id.spinner2);
        populateSpinner();
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                mapChoose = parent.getItemAtPosition(pos).toString();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button btnSaveRadius = (Button) getActivity().findViewById(R.id.btnSaveRadius);
        btnSaveRadius.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String radius = editTextRadius.getText().toString();
                setRadius(radius);
                hideKeyboard(v);
                Toast.makeText(getContext(), "Propertie Radius Saved", Toast.LENGTH_LONG).show();

            }
        });
        Button btnSaveMap= (Button) getActivity().findViewById(R.id.btnSaveMap);
        btnSaveMap.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setMap(mapChoose);
                Toast.makeText(getContext(), "Type of Map Saved", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setRadius(String radius){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("tiagoproperties",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("radius",radius);

        editor.commit();
    }

    private void setMap(String map){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("tiagoproperties",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("map",map);

        editor.commit();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void populateSpinner(){
        ArrayAdapter adapter = new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item , types_of_maps);
        sp.setAdapter(adapter);
    }




}


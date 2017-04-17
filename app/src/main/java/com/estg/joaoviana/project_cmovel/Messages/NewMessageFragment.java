package com.estg.joaoviana.project_cmovel.Messages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.estg.joaoviana.project_cmovel.MainActivity;
import com.estg.joaoviana.project_cmovel.MySingleton;
import com.estg.joaoviana.project_cmovel.R;
import com.estg.joaoviana.project_cmovel.authentication.Auth;
import com.estg.joaoviana.project_cmovel.authentication.RegisterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NewMessageFragment extends Fragment {

    View rootView;
    EditText editTextTitle,editTextBody;

    ArrayAdapter<String>adaptador;
    Spinner sp;
    ArrayList<String> users;
    String id_reciver;

    public NewMessageFragment() {
        // Required empty public constructor
    }

    public static NewMessageFragment newInstance() {
        NewMessageFragment fragment = new NewMessageFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_new_message, null);
        } else {
            ((ViewGroup) container.getParent()).removeView(rootView);
            rootView = inflater.inflate(R.layout.fragment_new_message, null);
        }
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        users = new ArrayList<>();
        getUsers(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editTextTitle = (EditText) getActivity().findViewById(R.id.editTextTitle);
        editTextBody = (EditText)getActivity().findViewById(R.id.editTextBody);

        sp = (Spinner) getActivity().findViewById(R.id.spinner2);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String val = parent.getItemAtPosition(pos).toString();

                String[] parts = val.split("\\.");
                String part1 = parts[0];
                String part2 = parts[1];
                id_reciver = part2;


            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button btnSave = (Button) getActivity().findViewById(R.id.buttonSend);
        btnSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendMessage(v);
            }
        });
        Button btnCancel = (Button) getActivity().findViewById(R.id.buttonCancel);
        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                editTextTitle.setText("");
                editTextBody.setText("");

            }
        });
    }

    public void getUsers(Context context){
        String url= "https://pjtiago.000webhostapp.com/cmovel_android/webservices/getUsers_ws.php?id="+ Auth.getId();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url,null,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                JSONArray arr;
                JSONObject obj_msg;
                try {
                    arr = response.getJSONArray("result");
                    //Toast.makeText(getActivity().getApplicationContext(),response.getJSONArray("result").toString(), Toast.LENGTH_LONG).show();
                    for(int i=0;i<arr.length();i++){
                        obj_msg = arr.getJSONObject(i);

                        users.add("User: "+obj_msg.getString("username")+" - N."+obj_msg.getString("id"));

                    }
                    preencheLista();

                }catch(JSONException ex){

                }

            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                //textTemperature.setText(error.toString());
            }

        });
        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }


    private void preencheLista() {                                               //Inteiros -> new String[]{String.valueOf(30)}
        adaptador= new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, users);
        adaptador.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        sp.setAdapter(adaptador);
    }

    private void sendMessage(View v){
        if (!isFieldsCorrect()) {

        } else {
            String url = "https://pjtiago.000webhostapp.com/cmovel_android/webservices/messages_send_ws.php";
            StringRequest stringRequest = new StringRequest
                    (Request.Method.POST, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonoutput = new JSONObject(response);
                                if (jsonoutput.has("content")) {
                                    Toast.makeText(getActivity(), jsonoutput.getString("content"), Toast.LENGTH_LONG).show();
                                    editTextTitle.setText("");
                                    editTextBody.setText("");
                                }

                            } catch (JSONException ex) {
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    String title = editTextTitle.getText().toString();
                    String body = editTextBody.getText().toString();

                    params.put("id_author", Auth.getId());
                    params.put("id_reciver", id_reciver);
                    params.put("title", "\"" + title + "\"");
                    params.put("body", "\"" + body + "\"");


                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        }
    }

    private Boolean isFieldsCorrect() {

        if (editTextTitle.getText().toString().matches("") || editTextBody.getText().toString().matches("")) {
            Toast.makeText(getActivity().getApplicationContext(), R.string.emptyfields, Toast.LENGTH_LONG).show();
            return false;
        }else if(editTextTitle.getText().length() < 3 || editTextBody.getText().length() < 3){
            Toast.makeText(getActivity().getApplicationContext(), R.string.short_tobe_sent, Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }
    }


}

package com.estg.joaoviana.project_cmovel.Messages;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.estg.joaoviana.project_cmovel.Adapters.CustomArrayAdapter;
import com.estg.joaoviana.project_cmovel.MySingleton;
import com.estg.joaoviana.project_cmovel.R;
import com.estg.joaoviana.project_cmovel.authentication.Auth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SentMessageFragment extends Fragment {
    ListView lista;
    CustomArrayAdapter itemsadapter;
    View rootView;

    ArrayList<Message> arrayList;
    public SentMessageFragment() {
        // Required empty public constructor
    }


    public static SentMessageFragment newInstance(String param1, String param2) {
        SentMessageFragment fragment = new SentMessageFragment();

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
            rootView = inflater.inflate(R.layout.fragment_sent_message, null);
        } else {
            ((ViewGroup) container.getParent()).removeView(rootView);
            rootView = inflater.inflate(R.layout.fragment_sent_message, null);
        }
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        arrayList = new ArrayList<>();

        getMessages(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lista = (ListView) getActivity().findViewById(R.id.lista);
    }

    public void getMessages(Context context){
        String url= "https://pjtiago.000webhostapp.com/cmovel_android/webservices/messages_ws.php?id_author="+ Auth.getId();
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

                        Message msg = new Message(
                                "To: "+obj_msg.getString("name_other"),
                                "Title: "+obj_msg.getString("title"),
                                "Body: "+obj_msg.getString("body"));


                        arrayList.add(msg);

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
        itemsadapter = new CustomArrayAdapter(getActivity(),
                arrayList);
        lista.setAdapter(itemsadapter);
    }

}

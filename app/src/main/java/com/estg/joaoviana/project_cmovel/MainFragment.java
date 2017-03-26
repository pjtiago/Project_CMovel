package com.estg.joaoviana.project_cmovel;



import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;


public class MainFragment extends Fragment implements OnMapReadyCallback {

    TextView textSignal;
    TextView textTemperature;
    ImageView imageViewIcon;
    View rootView;
    GoogleMap nMap;

    public MainFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_main, null);
        } else {
            ((ViewGroup) container.getParent()).removeView(rootView);
        }

        return rootView;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // do your variables initialisations here except Views!!!

    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        // initialise your views
        textSignal = (TextView) view.findViewById(R.id.signal);
        textTemperature = (TextView) view.findViewById(R.id.temperature);
        imageViewIcon = (ImageView) view.findViewById(R.id.tIcon);

        flashSign(textSignal);


        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        getWeather(context);
    }

    @Override
    public void onMapReady(GoogleMap map){
        nMap = map;
        LatLng statue = new LatLng(40.68, -74.04);
        map.addMarker(new MarkerOptions()
            .position(statue)
            .title("Portugal"));
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }


    public void flashSign(TextView text){
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(400); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        text.startAnimation(anim);
    }

    public void getWeather(Context context){
        String url= "https://api.darksky.net/forecast/"+
                "52c520f1ba45b4de505970f8b0d2187e/"+"-33.8670522,151.1957362"+
                "?exclude=\"minutely,hourly,daily,alerts,flags\"&units=si";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url,null,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                JSONObject currently;
                try {
                    currently = response.getJSONObject("currently");
                    textTemperature.setText(currently.getString("temperature")+"º");
                    String iconWeather = currently.getString("icon");

                    Drawable drawble = getResources().getDrawable(getIconId(iconWeather));
                    imageViewIcon.setImageDrawable(drawble);

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

    public int getIconId(String mIcon){
        int iconId = R.drawable.clear_day;

        if (mIcon.equals("clear-day")) {
            iconId = R.drawable.clear_day;
        }
        else if (mIcon.equals("clear-night")) {
            iconId = R.drawable.clear_night;
        }
        else if (mIcon.equals("rain")) {
            iconId = R.drawable.rain;
        }
        else if (mIcon.equals("snow")) {
            iconId = R.drawable.snow;
        }
        else if (mIcon.equals("sleet")) {
            iconId = R.drawable.sleet;
        }
        else if (mIcon.equals("wind")) {
            iconId = R.drawable.wind;
        }
        else if (mIcon.equals("fog")) {
            iconId = R.drawable.fog;
        }
        else if (mIcon.equals("cloudy")) {
            iconId = R.drawable.cloudy;
        }
        else if (mIcon.equals("partly-cloudy-day")) {
            iconId = R.drawable.partly_cloudy;
        }
        else if (mIcon.equals("partly-cloudy-night")) {
            iconId = R.drawable.cloudy_night;
        }
        return iconId;
    }



}

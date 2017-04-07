package com.estg.joaoviana.project_cmovel;



import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainFragment extends Fragment implements LocationListener,OnMapReadyCallback,GoogleMap.OnMapLongClickListener {

    TextView textSignal;
    TextView textTemperature;
    ImageView imageViewIcon;
    private View rootView;
    GoogleMap nMap;
    public MapView mapView;
    ArrayList<LatLng> pontos;

    private static final String ARG_TYPE_MAP = "normal";
    private String mTypeMap;
    //LocationRequest mLocationRequest;

    public MainFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String typeMap) {
        MainFragment fragment = new MainFragment();

        Bundle args = new Bundle();
        args.putString(ARG_TYPE_MAP, typeMap);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getWeather(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_main, null);
        } else {
            ((ViewGroup) container.getParent()).removeView(rootView);
            rootView = inflater.inflate(R.layout.fragment_main, null);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }


        return rootView;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTypeMap = getArguments().getString(ARG_TYPE_MAP);
        }
        pontos = new ArrayList<>();
        // do your variables initialisations here except Views!!!
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textSignal = (TextView) getActivity().findViewById(R.id.signal);
        textTemperature = (TextView) getActivity().findViewById(R.id.temperature);
        imageViewIcon = (ImageView) getActivity().findViewById(R.id.tIcon);

/*
        Button b = (Button) v.findViewById(R.id.StartButton);
        b.setOnClickListener(this);
        */
        flashSign(textSignal);

    }





    @Override
    public void onMapReady(GoogleMap map){
        nMap = map;
        LatLng statue = new LatLng(40.68, -74.04);
        nMap.addMarker(new MarkerOptions()
            .position(statue)
            .title("Portugal"));
        nMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(statue)
                .zoom(18)
                .bearing(45)
                .tilt(60)
                .build();
        nMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        //nMap.setOnMapClickListener(this);
        nMap.setOnMapLongClickListener(this);

    };



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




    @Override
    public void onMapLongClick(LatLng latLng) {


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.draw){
            nMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng latLng) {
                    pontos.add(latLng);
                    if(pontos.size()  == 4){
                        PolygonOptions polygonOptions = new PolygonOptions();
                        polygonOptions.addAll(pontos);

                        polygonOptions.strokeColor(Color.RED);
                        polygonOptions.fillColor(Color.BLUE);
                        nMap.addPolygon(polygonOptions);
                        pontos.clear();
                        nMap.setOnMapClickListener(null);
                    }
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

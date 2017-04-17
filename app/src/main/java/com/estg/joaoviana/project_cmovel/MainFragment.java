package com.estg.joaoviana.project_cmovel;



import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.estg.joaoviana.project_cmovel.Database.Contrato;
import com.estg.joaoviana.project_cmovel.Database.DB;
import com.estg.joaoviana.project_cmovel.Model.Place;
import com.estg.joaoviana.project_cmovel.authentication.Connectivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainFragment extends Fragment implements LocationListener,OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener,GoogleMap.OnMapClickListener,GoogleMap.OnMarkerClickListener {

    ArrayList<Place> placeList;

    TextView textSignal;
    TextView textTemperature;
    TextView textStreet;
    TextView textCity;
    TextView textCountry;

    ImageView imageViewIcon;
    View rootView;
    GoogleMap nMap;
    ArrayList<LatLng> pontos;


    Handler handler;
    LocationManager mlocManager;
    LocationListener mlocListener;

    Button b;
    TextView textFavorite;
    Button btn_favorite;

    DB mDbHelper;
    SQLiteDatabase db;
    Cursor c;
    private static final String ARG_TYPE_MAP = "normal";

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

        if (!Connectivity.isGPSEnabled(getActivity())) {
            Toast.makeText(getActivity(), R.string.no_signal, Toast.LENGTH_LONG).show();
        }

        if (!Connectivity.isConnected(getActivity())) {
            Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_LONG).show();
        }
        getRadius();
        getMap();
        givelastLocation();

        getStreet(getContext());
        getWeather(context);

    }

    @Override
    public void onStop() {
        super.onStop();
        //timertask.cancel();
        mlocManager.removeUpdates(mlocListener);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //timertask.cancel();
        mlocManager.removeUpdates(mlocListener);
        if (c != null && !c.isClosed()) {
            c.close();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //timertask.cancel();
        mlocManager.removeUpdates(mlocListener);
        if (c != null && !c.isClosed()) {
            c.close();
        }
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
        mDbHelper = new DB(rootView.getContext());
        db = mDbHelper.getReadableDatabase();

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);

        }
        return rootView;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pontos = new ArrayList<>();
        // do your variables initialisations here except Views!!!

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textSignal = (TextView) getActivity().findViewById(R.id.signal);
        textTemperature = (TextView) getActivity().findViewById(R.id.temperature);
        textStreet = (TextView) getActivity().findViewById(R.id.street);
        textCity = (TextView) getActivity().findViewById(R.id.city);
        textCountry = (TextView) getActivity().findViewById(R.id.country);
        imageViewIcon = (ImageView) getActivity().findViewById(R.id.tIcon);

        textFavorite = (TextView)getActivity().findViewById(R.id.textFavorite);
        btn_favorite = (Button) getActivity().findViewById(R.id.btn_favorite);
        btn_favorite.setVisibility(View.INVISIBLE);

        b = (Button) rootView.findViewById(R.id.update);
        b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getWeather(getContext());
                getStreet(getContext());
                currentLocationListener(v);
            }
        });

        btn_favorite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String val = textFavorite.getText().toString();
                String[] id = val.split("-");
                Place p = placeList.get(Integer.parseInt(id[0]));

                insertPlaceDb(p);
            }
        });


        flashSign(textSignal);
    }

    @Override
    public void onMapReady(GoogleMap map){
        LatLng currentLocal;
        currentLocal = new LatLng(Utils.lastLatitude,Utils.lastLongitude);

        nMap = map;
        nMap.addMarker(new MarkerOptions()
                .position(currentLocal)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title(getString(R.string.you_are_here))).showInfoWindow();

        if(Utils.map.equals("Satellite")){
            nMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }else if(Utils.map.equals("Terrain")){
            nMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }else if(Utils.map.equals("Hybrid")) {
            nMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }else if(Utils.map.equals("Normal")){
            nMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(currentLocal)
                .zoom(19)
                .bearing(45)
                .tilt(60)
                .build();
        nMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        nMap.addCircle(new CircleOptions()
                .center(currentLocal)
                .radius(Integer.parseInt(Utils.radius))
                .strokeWidth(0f)
                .fillColor(0x550000FF));
        //nMap.setOnMapClickListener(this);
        nMap.setOnMapLongClickListener(this);

        getPointsInterest(getActivity().getApplicationContext(),String.valueOf(Utils.radius));
        currentLocationListener(b);
    }

    public void flashSign(TextView text){
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(400); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        text.startAnimation(anim);
    }


    private void currentLocationListener(View v) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mlocManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        mlocListener = new MyLocationListener();
        try{
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
        }catch (SecurityException e){
        }
        handler = new Handler();
        handler.post(new Runnable() {
            public void run() {
                if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (MyLocationListener.latitude > 0) {
                        setLastLocation(MyLocationListener.latitude,MyLocationListener.longitude);
                        givelastLocation();
                        LatLng currentPosition = new LatLng(Utils.lastLatitude, Utils.lastLongitude);
                        textSignal.setText("");
                        nMap.clear();
                        Toast.makeText(getActivity(), Utils.lastLatitude + " | " +Utils.lastLongitude,
                                Toast.LENGTH_LONG).show();



                        nMap.addMarker(new MarkerOptions()
                                .position(currentPosition)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                .title("Você está Aqui")).showInfoWindow();

                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(currentPosition)
                                .zoom(19)
                                .bearing(45)
                                .tilt(60)
                                .build();
                        nMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        nMap.addCircle(new CircleOptions()
                                .center(currentPosition)
                                .radius(Integer.parseInt(Utils.radius))
                                .strokeWidth(0f)
                                .fillColor(0x550000FF));

                        getPointsInterest(getActivity().getApplicationContext(),String.valueOf(Utils.radius));

                    } else {
                        if(textSignal.getText()!=getString(R.string.no_signal) ){
                            textSignal.setText(R.string.no_signal);
                        }

                        if(textSignal.getVisibility()== View.INVISIBLE){
                            textSignal.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(getActivity(), R.string.no_signal,
                                Toast.LENGTH_LONG).show();
                        LatLng currentPosition = new LatLng(Utils.lastLatitude, Utils.lastLongitude );

                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(currentPosition)
                                .zoom(19)
                                .bearing(45)
                                .tilt(60)
                                .build();
                        nMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        getPointsInterest(getActivity().getApplicationContext(),String.valueOf(Utils.radius));

                    }
                } else {
                    if(textSignal.getText()!=getString(R.string.gps_off)){
                        textSignal.setText(R.string.gps_off);
                    }

                    if(textSignal.getVisibility()== View.INVISIBLE){
                        textSignal.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(getActivity(), R.string.no_internet,
                            Toast.LENGTH_LONG).show();
                    LatLng currentPosition = new LatLng(Utils.lastLatitude, Utils.lastLongitude );

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(currentPosition)
                            .zoom(19)
                            .bearing(45)
                            .tilt(60)
                            .build();
                    nMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                }
            }
        });
    }

    public void getWeather(Context context){
        String url= "https://api.darksky.net/forecast/"+
                "52c520f1ba45b4de505970f8b0d2187e/"+Utils.lastLatitude+","+Utils.lastLongitude+
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

    public void getStreet(Context context){
        String url= "http://maps.googleapis.com/maps/api/geocode/json?latlng="+Utils.lastLatitude+","+Utils.lastLongitude;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray arr;
                JSONArray arr_components;
                JSONObject obj;

                String port_number = "";
                String street = "";
                String city = "";
                String country = "";

                JSONObject component;
                JSONArray types;
                try {
                    arr = response.getJSONArray("results");
                    obj = arr.getJSONObject(0);
                    arr_components = obj.getJSONArray("address_components");

                    for(int i=0;i<arr_components.length();i++){
                        component = arr_components.getJSONObject(i);
                        types = component.getJSONArray("types");

                        if(types.getString(0).equals("street_number")){
                            port_number = component.getString("long_name");
                        }else if(types.getString(0).equals("route")){
                            street = component.getString("long_name");
                        }else if(types.getString(0).equals("locality")){
                            city = component.getString("long_name");
                        }else if(types.getString(0).equals("country")){
                            country = component.getString("short_name");
                        }
                    }

                    textStreet.setText(street+", "
                            +port_number);
                    textCity.setText(city);
                    textCountry.setText(country);


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

    public void getPointsInterest(Context context,String radius){
        String url= "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"+
                "location="+Utils.lastLatitude+","+Utils.lastLongitude+"&radius="+radius+
                "&key=AIzaSyDi34DMAOnv3r9suyA-ADYgVn9D3s_B0IQ";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url,null,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                placeList = new ArrayList<>();
                try {
                    JSONArray results;
                    JSONObject obj;
                    JSONObject localization;
                    JSONObject geometry;
                    Place place;
                    results = response.getJSONArray("results");
                    for(int i=0;i<results.length();i++) {
                        obj = results.getJSONObject(i);
                        geometry = obj.getJSONObject("geometry");
                        localization = geometry.getJSONObject("location");
                        String id = obj.getString("id");
                        String name = obj.getString("name");
                        String icon = obj.getString("icon");
                        Double latitude = Double.parseDouble(localization.getString("lat"));
                        Double longitude = Double.parseDouble(localization.getString("lng"));
                        String vicinity = obj.getString("vicinity");
                        place = new Place(id,name,icon,latitude,longitude,vicinity);

                        LatLng placeLocal = new LatLng(place.getLatitude(),place.getLongitude());
                        placeList.add(place);
                        setMarker(placeLocal,name,i);

                    }
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

    private void givelastLocation(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("tiagolocation",Context.MODE_PRIVATE);
        if(sharedPreferences.getString("latitude","") != "" || sharedPreferences.getString("longitude","") != ""){
            Utils.lastLatitude = Double.parseDouble(sharedPreferences.getString("latitude",""));
            Utils.lastLongitude = Double.parseDouble(sharedPreferences.getString("longitude",""));
        }else{
            Utils.lastLatitude = 41.6946;
            Utils.lastLongitude = -8.83016;
        }

    }

    private void setLastLocation(Double latitude,Double longitude){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("tiagolocation",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("latitude",String.valueOf(latitude));
        editor.putString("longitude",String.valueOf(longitude));

        editor.commit();
    }

    private void getRadius(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("tiagoproperties",Context.MODE_PRIVATE);
        if(sharedPreferences.getString("radius","") != ""||sharedPreferences.getString("radius","") != null ){
            Utils.radius = sharedPreferences.getString("radius","");
        }else{
            Utils.radius = "1000";
        }
    }

    private void getMap(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("tiagoproperties",Context.MODE_PRIVATE);
        if(sharedPreferences.getString("map","") != ""||sharedPreferences.getString("map","") != null ){
            Utils.map = sharedPreferences.getString("map","");
        }else{
            Utils.map = "Satellite";
        }

    }

    private void insertPlaceDb(Place place){
        String ref = place.getId();
        String name = place.getName();
        String icon = place.getIcon();
        Double latitude = place.getLatitude();
        Double longitude = place.getLongitude();
        String vicinity = place.getVicinity();
        Boolean bool = favoriteExist(ref);
        if(bool == false) {
            ContentValues cv = new ContentValues();
            cv.put(Contrato.Place.COLUMN_ID, ref);
            cv.put(Contrato.Place.COLUMN_NAME, name);
            cv.put(Contrato.Place.COLUMN_ICON, icon);
            cv.put(Contrato.Place.COLUMN_LATITUDE, latitude);
            cv.put(Contrato.Place.COLUMN_LONGITUDE, longitude);
            cv.put(Contrato.Place.COLUMN_VICINITY, vicinity);
            cv.put(Contrato.Place.COLUMN_DESCRIPTION, "");

            db.insert(Contrato.Place.TABLE_NAME, null, cv);
            Toast.makeText(getContext(), R.string.favorite_save, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(), R.string.favorite_already, Toast.LENGTH_LONG).show();
        }



    }

    private Boolean favoriteExist(String ref){
        c = db.rawQuery("select "+Contrato.Place._ID+", "+Contrato.Place.COLUMN_ID+", "+
                Contrato.Place.COLUMN_NAME+", "+ Contrato.Place.COLUMN_ICON+", "+
                Contrato.Place.COLUMN_LATITUDE+", "+ Contrato.Place.COLUMN_LONGITUDE+", "
                + Contrato.Place.COLUMN_DESCRIPTION+", "+ Contrato.Place.COLUMN_VICINITY+
                " FROM " + Contrato.Place.TABLE_NAME + " WHERE " +Contrato.Place.COLUMN_ID+
                "= '"+ref+"'", null);
        if(c.getCount() > 0){
            return true;
        }
        return false;
    }



    private void setMarker(LatLng latLng,String name,int pos)
    {
        nMap.setOnMarkerClickListener(this);

        nMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(pos+"- "+name));
                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String val = marker.getTitle();

        if(val.equals("You are Here")||val.equals("Você está Aqui")){

        }else {
            String[] parts = val.split("-");
            String part1 = parts[0];
            String part2 = parts[1];
            if (part2.length() > 32) {
                String temp = part2.substring(0, 32);
                part2 = temp;
            }
            int pos = Integer.parseInt(part1);
            //Toast.makeText(getContext(), pos+"", Toast.LENGTH_LONG).show();
            textFavorite.setText(pos + "- " + part2);
            btn_favorite.setVisibility(View.VISIBLE);
        }
        marker.showInfoWindow();
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(marker.getPosition())
                .zoom(19)
                .bearing(45)
                .tilt(60)
                .build();
        nMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        return true;
    }
    @Override
    public void onMapClick(LatLng latLng) {

    }
    @Override
    public void onMapLongClick(LatLng latLng) {

        textFavorite.setText("");
        if(btn_favorite.getVisibility() == View.VISIBLE){
            btn_favorite.setVisibility(View.INVISIBLE);
        }
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

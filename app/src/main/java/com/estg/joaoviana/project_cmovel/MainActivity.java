package com.estg.joaoviana.project_cmovel;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.estg.joaoviana.project_cmovel.authentication.Auth;
import com.estg.joaoviana.project_cmovel.authentication.LoginActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = LoginActivity.class.getSimpleName();

    TextView texttest;
    Fragment mainFrag;
    Fragment messagesFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Welcome "+ "'"+ Auth.getUsername()+"'");

        mainFrag = MainFragment.newInstance();
        messagesFrag = MessagesFrag.newInstance();
        texttest = (TextView)findViewById(R.id.text);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_main, mainFrag)
                .commit();*/
        getPlaces();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.log_off) {
            LoginActivity.dropSession(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_map) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_main, mainFrag)
                    .commit();
        } else if (id == R.id.nav_gallery) {


        } else if (id == R.id.nav_slideshow) {



        } else if (id == R.id.nav_messages) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_main, messagesFrag)
                    .commit();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getPlaces(){
        Toast.makeText(getApplicationContext(), "Teste1", Toast.LENGTH_LONG).show();
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"+
                "location=-33.8670522,151.1957362&radius=1500&key=AIzaSyDi34DMAOnv3r9suyA-ADYgVn9D3s_B0IQ";
        // JSONObject jsonObject = new JSONObject();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url,null,new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    JSONObject j = response;

                    texttest.setText(j.toString());
                    Toast.makeText(getApplicationContext(), "Teste2", Toast.LENGTH_LONG).show();
                }
            },new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {

                }

        });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }
}

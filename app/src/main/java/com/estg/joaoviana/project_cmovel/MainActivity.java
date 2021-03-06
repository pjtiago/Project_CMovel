package com.estg.joaoviana.project_cmovel;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.estg.joaoviana.project_cmovel.Messages.MessagesFrag;
import com.estg.joaoviana.project_cmovel.authentication.Auth;
import com.estg.joaoviana.project_cmovel.authentication.LoginActivity;
import com.estg.joaoviana.project_cmovel.favorites.FavoritesFragment;
import com.estg.joaoviana.project_cmovel.properties.PropertiesFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = LoginActivity.class.getSimpleName();

    TextView texttest;
    Fragment mainFrag;
    Fragment messagesFrag,favoritesFrag,propertiesFrag;
    Fragment streetFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.welcome)+ "  '"+ Auth.getUsername()+"' !!!");

        mainFrag = MainFragment.newInstance("normal");
        messagesFrag = MessagesFrag.newInstance();
        favoritesFrag = FavoritesFragment.newInstance();
        propertiesFrag = PropertiesFragment.newInstance();
        texttest = (TextView)findViewById(R.id.text);
        streetFrag = StreetFragment.newInstance();

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        navigationView.setItemTextColor(ColorStateList.valueOf(Color.BLUE ));

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_main, mainFrag)
                .commit();



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
        } else if (id == R.id.nav_favorites) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_main, favoritesFrag)
                    .commit();

        } else if (id == R.id.nav_messages) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_main, messagesFrag)
                    .commit();

        } else if (id == R.id.nav_properties) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_main, propertiesFrag)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}

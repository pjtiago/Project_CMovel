package com.estg.joaoviana.project_cmovel.authentication;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.estg.joaoviana.project_cmovel.MainActivity;
import com.estg.joaoviana.project_cmovel.MySingleton;
import com.estg.joaoviana.project_cmovel.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    public static final String TAG = LoginActivity.class.getSimpleName();
    public EditText editTextUsername;
    public EditText editTextPassword;
    public TextView failedAuthenticate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Boolean gpsTest = Connectivity.isGPSEnabled(this);

        if(gpsTest){
            Toast.makeText(getApplicationContext(), R.string.gps_turn_on, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), R.string.gps_turn_off, Toast.LENGTH_LONG).show();
        }

        Intent i;
        if(isSessionOpen()){
            giveModelAuth();
            i = new Intent(LoginActivity.this, MainActivity.class);
            finish();
            startActivity(i);
        }
        editTextUsername  = (EditText)findViewById(R.id.editUsername);
        editTextPassword = (EditText)findViewById(R.id.editPassword);

        failedAuthenticate = (TextView)findViewById(R.id.failedAuthenticate);
        failedAuthenticate.setVisibility(View.INVISIBLE);

        editTextUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    messageVisiblity();
                }
            }
        });
        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                   messageVisiblity();
                }
            }
        });
    }

    public void login(View v) {
        if (!Connectivity.isConnected(this)) {
            Toast.makeText(getApplicationContext(), R.string.no_internet, Toast.LENGTH_LONG).show();
        } else {
            if (!isFieldsCorrect()) {
                Toast.makeText(getApplicationContext(), R.string.emptyfields, Toast.LENGTH_LONG).show();
            } else {
                String url = "http://pjtiago.000webhostapp.com/cmovel_android/webservices/login_ws.php";
                // JSONObject jsonObject = new JSONObject();
                StringRequest stringRequest = new StringRequest
                        (Request.Method.POST, url, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                Intent i;
                                try {
                                    JSONObject jsonoutput = new JSONObject(response);

                                    if (jsonoutput.getString("id") == "null" ||
                                            jsonoutput.getString("username") == null ||
                                            jsonoutput.getString("email") == null) {

                                        failedAuthenticate.setVisibility(View.VISIBLE);
                                    } else {
                                        Auth.setId(jsonoutput.getString("id"));
                                        Auth.setUsername(jsonoutput.getString("username"));
                                        Auth.setEmail(jsonoutput.getString("email"));

                                        setSession(Auth.getId(),Auth.getUsername(),Auth.getEmail());

                                        i = new Intent(LoginActivity.this, MainActivity.class);
                                        finish();
                                        startActivity(i);
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
                        String usernameText = editTextUsername.getText().toString();
                        String passwordText = editTextPassword.getText().toString();
                        if (usernameText.contains("@")) {
                            params.put("email", "\"" + usernameText + "\"");
                            params.put("password", "\"" + passwordText + "\"");
                        } else {
                            params.put("username", "\"" + usernameText + "\"");
                            params.put("password", "\"" + passwordText + "\"");
                        }
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Content-Type", "application/x-www-form-urlencoded");
                        return params;
                    }
                };
                MySingleton.getInstance(this).addToRequestQueue(stringRequest);
            }
        }
    }

    public static void dropSession(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("tiagopreferences",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id",null);
        editor.putString("username",null);
        editor.putString("email",null);
        Auth.setId("");Auth.setUsername("");Auth.setEmail("");

        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        ((Activity) context).finish();
        context.startActivity(i);
    }

    private void giveModelAuth(){
        SharedPreferences sharedPreferences = getSharedPreferences("tiagopreferences",Context.MODE_PRIVATE);
        Auth.setId(sharedPreferences.getString("id",""));
        Auth.setUsername(sharedPreferences.getString("username",""));
        Auth.setEmail(sharedPreferences.getString("email",""));
    }

    private void setSession(String id,String username,String email){
        SharedPreferences sharedPreferences = getSharedPreferences("tiagopreferences",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id",id);
        editor.putString("username",username);
        editor.putString("email",email);
        editor.commit();
    }

    public boolean isSessionOpen(){
        SharedPreferences sharedPreferences = getSharedPreferences("tiagopreferences",Context.MODE_PRIVATE);

        String id = sharedPreferences.getString("id",null);
        String username = sharedPreferences.getString("username",null);
        String email = sharedPreferences.getString("email",null);

        if(id == null ||username == null || email == null ){
            return false;
        }else{
            return true;
        }

    }

    private void messageVisiblity(){
        if(failedAuthenticate.getVisibility() == View.VISIBLE ){
            failedAuthenticate.setVisibility(View.INVISIBLE);
        }
    }

    private Boolean isFieldsCorrect(){
        String text = editTextUsername.getText().toString();
        if(editTextUsername.getText().toString().matches("")|| editTextPassword.getText().toString().matches("")){
            return false;
        }else if(text.contains(".")){
            return false;
        }else{
            return true;
        }
    }

    public void btnAnotherActivity(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.btn_register:
                i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                break;

        }
    }



}

package com.estg.joaoviana.project_cmovel.authentication;


import android.content.Intent;
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
        if(!Connectivity.isConnected(this)){
            Toast.makeText(getApplicationContext(),"Network not available", Toast.LENGTH_LONG).show();
        }else {
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

    private void messageVisiblity(){
        if(failedAuthenticate.getVisibility() == View.VISIBLE ){
            failedAuthenticate.setVisibility(View.INVISIBLE);
        }
    }

    private Boolean isFieldsCorrect(){
        if(editTextUsername.getText().toString().matches("")|| editTextPassword.getText().toString().matches("")){
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

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

public class RegisterActivity extends AppCompatActivity {

    public EditText editTextUsername;
    public EditText editTextEmail;
    public EditText editTextPassword;
    public EditText editTextConfirmPassword;
    public TextView failedRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUsername  = (EditText)findViewById(R.id.editUsername);
        editTextEmail = (EditText)findViewById(R.id.editEmail);
        editTextPassword  = (EditText)findViewById(R.id.editPassword);
        editTextConfirmPassword = (EditText)findViewById(R.id.editConfirmPassword);

        failedRegistration = (TextView)findViewById(R.id.failedRegistration);
        failedRegistration.setVisibility(View.INVISIBLE);
    }


    public void registration(View v) {
        if (!Connectivity.isConnected(this)) {
            Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_LONG).show();
        } else {
            if (!isFieldsCorrect()) {

            } else {
                String url = "http://pjtiago.000webhostapp.com/cmovel_android/webservices/register_ws.php";
                final JSONObject jsonObject = new JSONObject();
                StringRequest stringRequest = new StringRequest
                        (Request.Method.POST, url, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                Intent i;
                                try {
                                    JSONObject jsonoutput = new JSONObject(response);
                                    if (jsonoutput.has("content")) {
                                        Toast.makeText(getApplicationContext(), jsonoutput.getString("content"), Toast.LENGTH_LONG).show();
                                    } else {
                                        Auth.setId(jsonoutput.getString("id"));
                                        Auth.setUsername(jsonoutput.getString("username"));
                                        Auth.setEmail(jsonoutput.getString("email"));
                                        Toast.makeText(getApplicationContext(), R.string.regis_succ, Toast.LENGTH_LONG).show();
                                        i = new Intent(RegisterActivity.this, MainActivity.class);
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
                        String usernameEmail = editTextEmail.getText().toString();
                        String passwordText = editTextPassword.getText().toString();

                        params.put("username", "\"" + usernameText + "\"");
                        params.put("email", "\"" + usernameEmail + "\"");
                        params.put("password", "\"" + passwordText + "\"");


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





    private Boolean isFieldsCorrect(){
        String text = editTextUsername.getText().toString();
        if(editTextUsername.getText().toString().matches("")||
                editTextEmail.getText().toString().matches("")||
                editTextPassword.getText().toString().matches("") ||
                editTextConfirmPassword.getText().toString().matches("")
                ){
            Toast.makeText(getApplicationContext(), R.string.emptyfields, Toast.LENGTH_LONG).show();
            return false;
        }else if(!editTextPassword.getText().toString().matches(editTextConfirmPassword.getText().toString())){
            Toast.makeText(getApplicationContext(), R.string.pw_dont_match, Toast.LENGTH_LONG).show();
            return false;
        }else if(!editTextEmail.getText().toString().contains("@")){
            Toast.makeText(getApplicationContext(), R.string.valid_mail, Toast.LENGTH_LONG).show();
            return false;
        }else if(text.contains(".")){
            Toast.makeText(getApplicationContext(),"Invalid Username", Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }
    }

    public void cancel(View v){
        finish();
    }
}

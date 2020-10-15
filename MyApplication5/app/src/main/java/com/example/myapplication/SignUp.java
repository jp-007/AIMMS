package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.xml.transform.ErrorListener;

public class SignUp extends AppCompatActivity {
    String POST_URL = "https://aimm.pythonanywhere.com/api/signup/";
    String GET_URL = "https://aimm.pythonanywhere.com/api/depts/";
    JSONArray j = new JSONArray();
    ArrayList<String> list = new ArrayList<String>();
    int []id;
    JSONObject object = new JSONObject();
    EditText name, designation, email;
    Spinner spinner;
    TextView t;
    ArrayAdapter<String> adapter;
    boolean empty;
    RequestQueue requestQueue;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name = (EditText) findViewById(R.id.name);
        designation = (EditText) findViewById(R.id.designation);
        email = (EditText) findViewById(R.id.email);
        t = (TextView)findViewById(R.id.tv);
        b = (Button) findViewById(R.id.signupButton);
        spinner = (Spinner)findViewById(R.id.department);
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                GET_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            j = response.getJSONArray("data");
                            get_dept(j);
                        }
                        catch (Exception ex) {
                            Log.d("EXCEPTION",""+ex.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest error", error.toString());
                    }
                }
        );
        requestQueue.add(objectRequest);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                                    list.get(position);
                                    try{object.put("department",id[position]);}catch (Exception e){}
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                    Toast.makeText(SignUp.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });
                            if(!isEmpty(name))
                                object.put("name",name.getText());
                            else {
                                Toast.makeText(SignUp.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(!isEmpty(email))
                                object.put("email",email.getText());
                            else {
                                Toast.makeText(SignUp.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(!isEmpty(designation))
                                object.put("designation",designation.getText());
                            else {
                                Toast.makeText(SignUp.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }

                        JsonObjectRequest objectRequest = new JsonObjectRequest(
                                Request.Method.POST,
                                POST_URL,
                                object,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            if(response.getInt("code") != 2)
                                                Toast.makeText(SignUp.this, "Successful Sign up", Toast.LENGTH_SHORT).show();
                                        } catch (JSONException ex) {
                                            ex.printStackTrace();
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(SignUp.this, error.toString(), Toast.LENGTH_SHORT).show();
                                        Log.e("Rest error", error.toString());
                                    }
                                }
                        );
                        requestQueue.add(objectRequest);
                    }
                }
        );

    }
    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

    public void get_dept(JSONArray j){
        id = new int[j.length()];
        for(int i = 0;i<j.length();i++)
        {
            JSONObject o = null;
            try {
                o = j.getJSONObject(i);
                list.add(o.getString("name"));
                adapter.notifyDataSetChanged();
                id[i] = o.getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
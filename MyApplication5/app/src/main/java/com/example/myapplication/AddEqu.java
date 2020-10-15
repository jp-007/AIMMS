package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddEqu extends AppCompatActivity {
    JSONArray j = new JSONArray();
    String POST_URL = "https://aimm.pythonanywhere.com/api/equipment/add/";
    String GET_URL = "https://aimm.pythonanywhere.com/api/depts/";
    JSONObject object = new JSONObject();
    EditText datedisplay,name,description,location,latency;
    Calendar myCalendar;
    Button b;
    ArrayList<String> list = new ArrayList<String>();
    int []id;
    ArrayAdapter<String> adapter1,adapter2;
    Spinner priority,department;
    DatePickerDialog.OnDateSetListener date;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_equ);
        myCalendar = Calendar.getInstance();
        name = (EditText)findViewById(R.id.name);
        description = (EditText)findViewById(R.id.description);
        location = (EditText)findViewById(R.id.location);
        latency = (EditText)findViewById(R.id.latency);
        b = (Button)findViewById(R.id.add_btn);

        //Priority spinner starts
        String[] arrayPriority = new String[] {
                "High", "Medium", "Low"
        };
        priority = (Spinner) findViewById(R.id.priority);
        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayPriority);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priority.setAdapter(adapter1);
        String text = priority.getSelectedItem().toString();
        final String priority_selected = text;
        //Priority spinner ends
        //department spinner starts
        department = (Spinner) findViewById(R.id.department);
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
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        department.setAdapter(adapter2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //department spinner ends

        //POST REQUEST
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JsonObjectRequest objectRequest;
                    object.put("name",name.getText());
                    object.put("description",description.getText());
                    object.put("priority",getpriorityint(priority_selected));
                    object.put("location",location.getText());
                    object.put("maintenance_latency",latency.getText());
                    department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                            list.get(position);
                            try{object.put("department",id[position]);}catch (Exception e){}
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                        }
                    });
                    //Toast.makeText(AddEquipment.this, ""+object.toString(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){};
                JsonObjectRequest objectRequest;
                objectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        POST_URL,
                        object,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(AddEqu.this, ""+response.toString(), Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Rest error",error.toString());
                            }
                        }
                );
                requestQueue.add(objectRequest);
            }
        });
        //END OF POST REQUEST
    }
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        datedisplay.setText(sdf.format(myCalendar.getTime()));
    }
    private int getpriorityint(String text){
        if(text.equals("High"))
            return 1;
        if(text.equals("Medium"))
            return 2;
        if(text.equals("Low"))
            return 3;
        return 0;
    }
    public void get_dept(JSONArray j) {
        id = new int[j.length()];
        for (int i = 0; i < j.length(); i++) {
            JSONObject o = null;
            try {
                o = j.getJSONObject(i);
                list.add(o.getString("name"));
                adapter2.notifyDataSetChanged();
                id[i] = o.getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
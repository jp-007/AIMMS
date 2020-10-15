package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class InvProfile extends AppCompatActivity {
    TextView sn,name,desc,ins_date,dept,loc;
    TextView sn_val,name_val,desc_val,ins_date_val,dept_val,loc_val,eq,eq_val;
    Button history,req_main;
    Connection con;
    String details;
    RequestQueue requestQueue;
    Users users;

    String s_desc,s_ins_date,s_dept,s_loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inv_profile);
        try {
            sn=(TextView)findViewById(R.id.sn_id);

            name=(TextView)findViewById(R.id.name_id);
            desc=(TextView)findViewById(R.id.desc_id);
            ins_date=(TextView)findViewById(R.id.install_id);
            dept=(TextView)findViewById(R.id.dept_id);
            loc=(TextView)findViewById(R.id.loc_id);

            sn_val=(TextView)findViewById(R.id.sn_value);
            eq_val=(TextView)findViewById(R.id.eq_value);
            name_val=(TextView)findViewById(R.id.name_value);
            desc_val=(TextView)findViewById(R.id.desc_value);
            ins_date_val=(TextView)findViewById(R.id.intall_date_value);
            dept_val=(TextView)findViewById(R.id.dept_value);
            loc_val=(TextView)findViewById(R.id.loc_value);


            req_main=(Button)findViewById(R.id.request_maintenance);
            history=(Button)findViewById(R.id.equipment_history);

            Intent intent=getIntent();
            String equpment_key=intent.getStringExtra("details");

            getEquipmentDetails(equpment_key);


            req_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1=new Intent(InvProfile.this,RequestMaintenance.class);
                    intent1.putExtra("equipment_id",sn_val.getText().toString().trim());
                    startActivity(intent1);
                }
            });

            history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    desc.setText("Description :");
                    desc_val.setText(s_desc);
                    dept.setText("Department : ");
                    dept_val.setText(s_dept);
                    ins_date.setText("installation Date : ");
                    ins_date_val.setText(s_ins_date);
                    loc.setText("Location : ");
                    loc_val.setText(s_loc);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private String getEquipmentDetails(String equipment_key)
    {
        users=new Users(this);
        requestQueue = Volley.newRequestQueue(this);

        //details=new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("equipment_id", equipment_key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //HANDLING JSON RESPONSE IN JSONARRAY
        JsonObjectRequest objectrequest = new JsonObjectRequest(
                Request.Method.POST,
                Connection.EQUIPMENT_DETAILS,
                object,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {            //ON RESPONSE SUCCESS

                      //  Toast.makeText(InvProfile.this, "details fetched" + response.toString(), Toast.LENGTH_LONG).show();
                        try {
                            setData(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {              //ON RESPONSE FAILURE
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InvProfile.this, "Error occured : " , Toast.LENGTH_LONG).show();
                        Log.e("ERROR:",error.toString());
                    }
                }

        );

        requestQueue.add(objectrequest);
        return details;
    }

    void setData(JSONObject response) throws JSONException {
        JSONObject resp=new JSONObject(response.getString("data"));

            sn.setText("Id :");
            sn_val.setText(resp.getString("serial_no"));
            name.setText("Name : ");
            name_val.setText(resp.getString("name"));
            desc.setText("Description : ");
            desc_val.setText(resp.getString("description"));

            s_desc=resp.getString("description");
            s_dept=resp.getString("dept");
            s_ins_date=resp.getString("installation_date");
            s_loc= resp.getString("location");

            //desc_val.setVisibility(View.INVISIBLE);
//            t3.setText(	response.getString("description"));
//            t2.setVisibility(0);
////            t4.setText(response.getString("dept"));
////            t5.setText(response.getString("installation_date"));
////            t6.setText(response.getString("location"));

    }
}

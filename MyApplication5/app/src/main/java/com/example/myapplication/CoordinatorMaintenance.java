package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CoordinatorMaintenance extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView zXingScannerView;
    boolean start=false,stop=false;
    String URL,ID;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    Button start_maintain,mark_complete;
    TextView sn,sn_value,desc,desc_val,raised_by,raised_by_val,issue_date,issue_date_val,status,status_val,eq,eq_val;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_maintenance);

        progressDialog=new ProgressDialog(this);
        progressDialog.show();
        Intent intent=getIntent();
        String ID=intent.getStringExtra("id");
        URL="https://aimm.pythonanywhere.com/api/get_ticket_details/"+ID+"/";

        sn=(TextView)findViewById(R.id.sn_id);
        sn.setText("Id : ");
        sn_value=(TextView)findViewById(R.id.sn_value);

        eq=(TextView)findViewById(R.id.equipment);
        eq.setText("Equipment :");
        eq_val=(TextView)findViewById(R.id.eq_value);

        desc=(TextView)findViewById(R.id.desc_id);
        desc.setText("Description : ");
        desc_val=(TextView)findViewById(R.id.desc_value);

        raised_by=(TextView)findViewById(R.id.raised_by);
        raised_by.setText("Raised by : ");
        raised_by_val=(TextView)findViewById(R.id.raised_value);

        issue_date=(TextView)findViewById(R.id.issue_date);
        issue_date.setText("Issue date : ");
        issue_date_val=(TextView)findViewById(R.id.issue_date_value);

        status=(TextView)findViewById(R.id.status);
        status.setText("status : ");
        status_val=(TextView)findViewById(R.id.status_value);

        start_maintain=(Button)findViewById(R.id.start_maintain);
        mark_complete=(Button)findViewById(R.id.finish_maintain);

        start_maintain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start=true;
                scan();
            }
        });

        mark_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop=true;
                scan();
            }
        });


        getDetails();


    }

    void  getDetails()
    {
        //HANDLING JSON RESPONSE IN JSONARRAY***********************************************************

        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectrequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {            //ON RESPONSE SUCCESS

                            Toast.makeText(CoordinatorMaintenance.this,response.toString(),Toast.LENGTH_LONG).show();
                            setData(response);
                            progressDialog.dismiss();

                    }
                },
                new Response.ErrorListener() {              //ON RESPONSE FAILURE
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(CoordinatorMaintenance.this, "Error occured : "+URL+ error.toString(), Toast.LENGTH_LONG).show();
                    }
                }

        );

        requestQueue.add(objectrequest);
    }

    void setData(JSONObject response)
    {
        try {
            JSONObject object=new JSONObject(response.getString("data"));
            ID=object.getString("id");
            sn_value.setText(object.getString("id"));
            eq_val.setText(object.getString("equipment"));
            desc_val.setText(object.getString("description"));
            raised_by_val.setText(object.getString("raised_by"));
            status_val.setText(object.getString("status"));

        }catch (Exception e)
        {
            Toast.makeText(CoordinatorMaintenance.this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void handleResult(Result result) {
        Toast.makeText(getApplicationContext(),result.getText(),Toast.LENGTH_LONG).show();
        zXingScannerView.stopCamera();
        startMaintain(result.toString());
        finish();
        if(start)
        {
            startMaintain(result.toString());
        }else if(stop)
        {
            try {
                finishMaintain(result.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void scan()
    {
        Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_LONG).show();
        zXingScannerView=new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.startCamera();
    }

    void startMaintain(String key)
    {
        progressDialog.show();
        Toast.makeText(CoordinatorMaintenance.this,"started:"+key,Toast.LENGTH_LONG).show();


        final String START_MAINTAIN="https://aimm.pythonanywhere.com/api/start_maintenance/"+ID+"/";
        requestQueue = Volley.newRequestQueue(this);

        //HANDLING JSON RESPONSE IN JSONARRAY
        JsonObjectRequest objectrequest = new JsonObjectRequest(
                Request.Method.PUT,
                START_MAINTAIN,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {            //ON RESPONSE SUCCESS

                            progressDialog.dismiss();

                        Toast.makeText(CoordinatorMaintenance.this, START_MAINTAIN+" details fetched" + response.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {              //ON RESPONSE FAILURE
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CoordinatorMaintenance.this, "Error occured : " + START_MAINTAIN+error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("ERROR:",error.toString());
                    }
                }

        );

        requestQueue.add(objectrequest);


    }

    void finishMaintain(String key) throws JSONException {
        progressDialog.show();
        Toast.makeText(CoordinatorMaintenance.this,"started:"+key,Toast.LENGTH_LONG).show();
        Users users=new Users(this);

        final String START_MAINTAIN="https://aimm.pythonanywhere.com/api/complete_maintenance/"+ID+"/";
        requestQueue = Volley.newRequestQueue(this);

        JSONObject object=new JSONObject();
        object.put("user_id",users.getUserid());

        //HANDLING JSON RESPONSE IN JSONARRAY
        JsonObjectRequest objectrequest = new JsonObjectRequest(
                Request.Method.POST,
                START_MAINTAIN,
                object,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {            //ON RESPONSE SUCCESS

                        Toast.makeText(CoordinatorMaintenance.this, " finished :" , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {              //ON RESPONSE FAILURE
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CoordinatorMaintenance.this, "Error occured : " , Toast.LENGTH_LONG).show();
                        Log.e("ERROR:",error.toString());
                    }
                }

        );

        requestQueue.add(objectrequest);

    }
}

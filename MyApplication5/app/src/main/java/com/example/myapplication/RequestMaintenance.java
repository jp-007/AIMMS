package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class RequestMaintenance extends AppCompatActivity implements View.OnClickListener {

    private CheckBox checkTeam1,checkTeam2,checkTeam3;
    private Button sendBtn;
    private TextView selectedteams;
    private ArrayList<String> mresult;

    private ZXingScannerView zXingScannerView;
    String EQUIPMENT_ID;

    JSONObject object=new JSONObject();
    String GET_URL="https://aimm.pythonanywhere.com/api/get_teams/";
    String POST_URL = "https://aimm.pythonanywhere.com/api/maintenance/add/";

    TextView t1;
    LinearLayout ll;
    Button b;
    EditText e;

    public  String[] teams;
    CheckBox[] cb;
    int id[];
    int selected[];

    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_maintenance);
//        sendBtn=(Button)findViewById(R.id.sendbtn);
//        selectedteams=findViewById(R.id.selectedteams);
//        mresult=new ArrayList<>();
//        selectedteams.setEnabled(false);

        Intent intent=getIntent();
        EQUIPMENT_ID=intent.getStringExtra("equipment_id");

        t1=(TextView)findViewById(R.id.selectedteams);

        connect();
    }
    public void connect()
    {
         requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                GET_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject original) {
                        try {
                            JSONArray j = new JSONArray(original.getString("data"));
                            //Toast.makeText(MainActivity.this, ""+j.length(), Toast.LENGTH_SHORT).show();

                            get_data(j);
                            /*

                             */
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
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

    public void get_data(JSONArray j)
    {
        JSONObject obj = new JSONObject();
        final int error;
        //t1 = (TextView)findViewById(R.id.tv);
        ll = (LinearLayout)findViewById(R.id.ll);
        b = (Button)findViewById(R.id.sendbtn);
        teams = new String[j.length()];
        cb = new CheckBox[j.length()];
        final int len = j.length();
        id = new int[j.length()];
        for(int i = 0 ; i < j.length();i++){
            try {
                obj = j.getJSONObject(i);
                teams[i] = obj.getString("name");
                id[i]=obj.getInt("id");
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }

        for(int i = 0; i < j.length(); i++) {
            cb[i] = new CheckBox(getApplicationContext());
            cb[i].setText(teams[i]);
            ll.addView(cb[i]);
        }
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                check(len);
            }
        });
    }
    @Override
    public void onClick(View view) {

    }
    public void check(int len){

        int error = 0;
        int j=-1;
        e = (EditText)findViewById(R.id.equip_maindesc);
        JSONObject object = new JSONObject();
        JSONObject temp = new JSONObject();
        JSONArray array = new JSONArray();
        selected = new int[len];
        JSONArray teams = new JSONArray();

        for(int i =0;i<len;i++)
        {
            if(cb[i].isChecked()){
                error = 1;
                j++;
                selected[j] = id[i];

            }
        }

        if(error == 0){
            Toast.makeText(this, "Please select a checkbox", Toast.LENGTH_SHORT).show();
            return;
        }
        for(int i = 0 ; i < len ; i++)
        {
            if(selected[i]!=0)
                teams.put(selected[i]);

        }
        Toast.makeText(this, ""+teams.toString(), Toast.LENGTH_SHORT).show();

        try {
            Toast.makeText(this, EQUIPMENT_ID, Toast.LENGTH_SHORT).show();
            object.put("equipment",EQUIPMENT_ID);
            object.put("description",e.getText().toString());
            //put user id from session
            object.put("raised_by",3);
            /*for(int i = 0; i<len;i++){
                if(selected[i] != 0){
                    temp.put("teams",selected[i]);
                }
            }
            array.put(temp);*/
            object.put("teams",teams);
            //t1.setText(object.toString());
        }
        catch (Exception e){
            Toast.makeText(RequestMaintenance.this,"error"+e.toString(),Toast.LENGTH_LONG).show();
            //Log.e("exception"+e.toString());
        }

        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST,
                POST_URL,
                object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        t1.setText(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest error POST",error.toString());
                    }
                }
        );
        requestQueue.add(objectRequest);




    }
}


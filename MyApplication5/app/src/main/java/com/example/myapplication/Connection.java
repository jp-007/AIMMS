package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.LoginActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Users;

import org.json.JSONException;
import org.json.JSONObject;

public class Connection {
    ProgressDialog progressDialog;

    RequestQueue requestQueue;
    Context context;

/******************************* CODES *************************************/
    private static final int SUCCESS_CODE=2;
    private static final int FAILURE_CODE=4;
    private String DESIGNATION="NA";



    /*********************************LINKS***********************************/
    Users users;
    String details="no response";
    private static String URL1="https://aimm.pythonanywhere.com/api/";
    private static String URL="http://192.168.43.24:8000/api/";


    /**************************** API'S ***************************************/
     String STAFF_ID="2";
     String RESPONSE_CODE="0";

    public static String LOGINURL = URL1+"login/";
    public static String URL_GET_USERS=URL1+"users/";
    public static String EQUIPMENT_DETAILS=URL1+"equipment/";
    public static String ADD_STAFF_REQUEST=URL1+"get_staff_requests/3/";

    public static String ASSIGNED_TASK_REQUEST=URL1+"get_user_tasks/";




    /******************************** GETTING CONTEXT *********************************/
    Connection(Context con) {
        this.context = con;
    }
    CardAdapter cardAdapter;


    /***************************** GET LOGIN AUTHENTICATION ***************************************/

    public void authenticate(final String username, final String password, final String designation) {
        users=new Users(context);
        requestQueue = Volley.newRequestQueue(context);

        progressDialog =new ProgressDialog(context);
        progressDialog.show();

        if(designation.equals(Users.Airport_Staff))
        {
            DESIGNATION="AS";
        }
        if(designation.equals(Users.Airport_Admin))
        {
            DESIGNATION="AA";
        }
        if(designation.equals(Users.Maintenance_Coordinator))
        {
            DESIGNATION="MC";
        }if(designation.equals(Users.Maintenance_Team_Leader))
        {
            DESIGNATION="MTL";
        }if(designation.equals(Users.Inventory_Incharge))
        {
            DESIGNATION="II";
        }if(designation.equals(Users.Department_Head))
        {
            DESIGNATION="DH";
        }
        if(designation.equals(Users.Maintenance_Admin))
        {
            DESIGNATION="MA";
        }


        JSONObject object = new JSONObject();
        try {
            object.put("username", username);
            object.put("password", password);
            object.put("designation", DESIGNATION);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //if(users.createSession(response,designation))
//            {
//                progressDialog.dismiss();
//                Toast.makeText(context.getApplicationContext(),"Login successfull",Toast.LENGTH_LONG).show();
//                //logging in...
//                context.startActivity(new Intent(context, MainActivity.class));
//                ((LoginActivity)context).finish();
//            }
//            progressDialog.dismiss();

//        HANDLING JSON RESPONSE IN JSONARRAY***********************************************************
        JsonObjectRequest objectrequest = new JsonObjectRequest(
                Request.Method.POST,
                LOGINURL,
                object,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {            //ON RESPONSE SUCCESS

                        Toast.makeText(context, "Login successful" + response.toString(), Toast.LENGTH_LONG).show();
                        try {
                            if(Integer.valueOf(response.getString("code"))==SUCCESS_CODE)
                            {

                                if(users.createSession(response,designation))
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(context.getApplicationContext(),"Login successfull",Toast.LENGTH_LONG).show();
                                    //logging in...
                                    context.startActivity(new Intent(context, MainActivity.class));
                                    ((LoginActivity)context).finish();
                                }
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {              //ON RESPONSE FAILURE
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Error occured : " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }

        );

        requestQueue.add(objectrequest);

    }



    /********************************* GET EQUIPMENT DETAILS *********************************************/
//    public String getEquipmentDetails(String equipment_key) {
//        users=new Users(context);
//        requestQueue = Volley.newRequestQueue(context);
//
//        //details=new JSONObject();
//        JSONObject object = new JSONObject();
//        try {
//            object.put("equipment_id", equipment_key);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        //HANDLING JSON RESPONSE IN JSONARRAY
//        JsonObjectRequest objectrequest = new JsonObjectRequest(
//                Request.Method.POST,
//                EQUIPMENT_DETAILS,
//                object,
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {            //ON RESPONSE SUCCESS
//
//                        Toast.makeText(context, "details fetched" + response.toString(), Toast.LENGTH_LONG).show();
//                        details=response.toString();
//                    }
//                },
//                new Response.ErrorListener() {              //ON RESPONSE FAILURE
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(context, "Error occured : " + error.toString(), Toast.LENGTH_LONG).show();
//                        Log.e("ERROR:",error.toString());
//                    }
//                }
//
//        );
//
//        requestQueue.add(objectrequest);
//
//        return details;
//    }
    /**************************************** END GET USER AUTHENTICATIONS ************************/


    void approveStaffRequest(String ID)
    {
        final String APPROVE_STAFF_REQUEST=URL1+"signup/approve/"+ID+"/";
        users=new Users(context);
        requestQueue = Volley.newRequestQueue(context);

        //HANDLING JSON RESPONSE IN JSONARRAY
        JsonObjectRequest objectrequest = new JsonObjectRequest(
                Request.Method.GET,
                APPROVE_STAFF_REQUEST,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {            //ON RESPONSE SUCCESS
                        try {
                            RESPONSE_CODE=response.getString("code");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Toast.makeText(context, APPROVE_STAFF_REQUEST+" details fetched" + response.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {              //ON RESPONSE FAILURE
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(context, "Error occured : " + error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("ERROR:",error.toString());
                    }
                }

        );

        requestQueue.add(objectrequest);


    }


    void rejectStaffRequest(String ID)
    {
        final String REJECT_STAFF_REQUEST=URL1+"signup/reject/"+ID+"/";
        users=new Users(context);
        requestQueue = Volley.newRequestQueue(context);


        //HANDLING JSON RESPONSE IN JSONARRAY
        JsonObjectRequest objectrequest = new JsonObjectRequest(
                Request.Method.GET,
                REJECT_STAFF_REQUEST,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {            //ON RESPONSE SUCCESS

                        //Toast.makeText(context, "details fetched" + response.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {              //ON RESPONSE FAILURE
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error occured : " + error.toString(), Toast.LENGTH_LONG).show();
                       // Log.e("ERROR:",error.toString());
                    }
                }

        );

        requestQueue.add(objectrequest);


    }

    void acceptMaintRequest(String id)
    {
        final String REJECT_STAFF_REQUEST=URL1+"signup/reject/"+id+"/";
        users=new Users(context);
        requestQueue = Volley.newRequestQueue(context);


        //HANDLING JSON RESPONSE IN JSONARRAY
        JsonObjectRequest objectrequest = new JsonObjectRequest(
                Request.Method.GET,
                REJECT_STAFF_REQUEST,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {            //ON RESPONSE SUCCESS

                       // Toast.makeText(context, "details fetched" + response.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {              //ON RESPONSE FAILURE
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(context, "Error occured : " + error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("ERROR:",error.toString());
                    }
                }

        );

        requestQueue.add(objectrequest);
    }
}

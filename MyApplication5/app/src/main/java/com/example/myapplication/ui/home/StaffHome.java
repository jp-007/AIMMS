package com.example.myapplication.ui.home;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.Users;

import org.json.JSONException;
import org.json.JSONObject;

public class StaffHome extends Fragment {

    private StaffHomeViewModel mViewModel;
    RequestQueue requestQueue;
    Context context;

    private static final int SUCCESS_CODE=2;
    private static final int FAILURE_CODE=4;
    private boolean AUTHENTICATE=false;



    JSONObject object=new JSONObject();
    String POST_URL="https://aimm.pythonanywhere.com/api/dept_maintenance_count/";
    //String POST_URL = "https://aimm.pythonanywhere.com/api/maintenance/add/";

    TextView ongoing;
    TextView raised;
    TextView completed;

    View v;
    public static StaffHome newInstance() {
        return new StaffHome();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.staff_home_fragment, container, false);
        //Toast.makeText(getActivity(),"Heyy",Toast.LENGTH_LONG).show();
        connect();

        return v;
    }

    public void connect(){
        Users users=new Users(getActivity());
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        try{

            object.put("dept_id",users.getDept_id());

        }catch (Exception e){

        }
        // Toast.makeText(MainActivity.this, "Helllllllo", Toast.LENGTH_SHORT).show();
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST,
                POST_URL,
                object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject original) {
                        //Toast.makeText(MainActivity.this, "got response", Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject j = original.getJSONObject("data");
                           // Toast.makeText(getActivity(), "Helllllllo", Toast.LENGTH_SHORT).show();
                            get_data(j);
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

    public void get_data(JSONObject j){

        ongoing=(TextView)v.findViewById(R.id.staff_ongoing);
        raised=(TextView)v.findViewById(R.id.staff_raised);
        completed=(TextView)v.findViewById(R.id.staff_completed);
        try{
            ongoing.setText(j.getString("count pending"));
            raised.setText(j.getString("count raised"));
            completed.setText(j.getString("count completed"));
            // Toast.makeText(this,"Heyy"+j.getString("count pending"),Toast.LENGTH_LONG);

        }catch (JSONException je){
            Log.e("JSON exception",je.toString());
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(StaffHomeViewModel.class);
        // TODO: Use the ViewModel
    }

}
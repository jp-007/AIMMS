package com.example.myapplication.ui.depthead;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.CardAdapter;
import com.example.myapplication.CardContainer;
import com.example.myapplication.Connection;
import com.example.myapplication.InvProfile;
import com.example.myapplication.R;
import com.example.myapplication.ScanActivity;
import com.example.myapplication.Users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AddStaff extends Fragment implements View.OnClickListener {

    private AddStaffViewModel mViewModel;
    Button addStaff,approve;
    public RecyclerView myRecyclerView;
    CardAdapter cardAdapter;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;


    public static AddStaff newInstance() {
        return new AddStaff();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.add_staff_fragment, container, false);


        myRecyclerView=v.findViewById(R.id.recycler);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getData();

        return v;
    }



    private void getData()
    {
        requestQueue = Volley.newRequestQueue(getContext());

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading");
        progressDialog.show();

        //HANDLING JSON RESPONSE IN JSONARRAY
        JsonObjectRequest objectrequest = new JsonObjectRequest(
                Request.Method.GET,
                Connection.ADD_STAFF_REQUEST,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {            //ON RESPONSE SUCCESS

                        Toast.makeText(getContext(), "details fetched" + response.toString(), Toast.LENGTH_LONG).show();
                        try {
                          setData(response);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {              //ON RESPONSE FAILURE
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "NO DATA FOUND : " + error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("ERROR:",error.toString());
                    }
                }

        );

        requestQueue.add(objectrequest);

    }

    private void setData(JSONObject response) throws JSONException {

        //setData(response);
        progressDialog.dismiss();

        ArrayList<CardContainer> containers=new ArrayList<>();
        CardContainer model;

        JSONArray jsonArray=new JSONArray(response.getString("data"));

        int length=jsonArray.length();

        for(int i=0;i<length;i++)
        {
            model=new CardContainer();
            model.setTitle(jsonArray.getJSONObject(i).getString("name"));
            model.setStaffId(jsonArray.getJSONObject(i).getString("id"));
            containers.add(model);
        }
        cardAdapter=new CardAdapter(getContext(),containers);
        myRecyclerView.setAdapter(cardAdapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddStaffViewModel.class);
        // TODO: Use the ViewModel
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
//            case R.id.btn_approve:
//               // Intent intent=new Intent(getActivity(),ScanActivity.class);
//                //startActivity(intent);
//                Toast.makeText(getActivity(), "Approved!", Toast.LENGTH_SHORT).show();
//                break;
        }

    }
}

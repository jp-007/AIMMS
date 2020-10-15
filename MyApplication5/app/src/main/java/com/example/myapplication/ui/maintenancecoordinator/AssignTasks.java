package com.example.myapplication.ui.maintenancecoordinator;

import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
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
import com.example.myapplication.R;
import com.example.myapplication.TaskAdapter;
import com.example.myapplication.TaskContainer;
import com.example.myapplication.Users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AssignTasks extends Fragment {

    public RecyclerView myRecyclerView;
    TaskAdapter cardAdapter;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    private AssignTasksViewModel mViewModel;
    Users users;
    String URL="https://aimm.pythonanywhere.com/api/get_user_task/";

    public static AssignTasks newInstance() {
        return new AssignTasks();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.assign_tasks_fragment, container, false);
        myRecyclerView=v.findViewById(R.id.recycler_MC);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        users=new Users(getContext());
        getData();


        return v;
    }

    private void getData()
    {
         URL=Connection.ASSIGNED_TASK_REQUEST+users.getUserid()+"/";
        requestQueue = Volley.newRequestQueue(getContext());

        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading");
        progressDialog.show();

        //HANDLING JSON RESPONSE IN JSONARRAY
        JsonObjectRequest objectrequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {            //ON RESPONSE SUCCESS

                        Toast.makeText(getContext(), "details fetched" + response.toString(), Toast.LENGTH_LONG).show();
                        try {

                            setData(response);
                            Toast.makeText(getContext(),"response2:"+response.toString(),Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {              //ON RESPONSE FAILURE
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "NO DATA FOUND : " + error.toString()+URL, Toast.LENGTH_LONG).show();
                        Log.e("ERROR:",error.toString());
                    }
                }

        );

        requestQueue.add(objectrequest);

    }

    private void setData(JSONObject response) throws JSONException {

        //setData(response);
        progressDialog.dismiss();
        ArrayList<TaskContainer> containers=new ArrayList<>();
        TaskContainer model;

        JSONArray jsonArray=new JSONArray(response.getString("data"));
        int length=jsonArray.length();
        for(int i=0;i<length;i++)
        {
            model=new TaskContainer();
            model.setTitle(jsonArray.getJSONObject(i).getString("id"));
            model.setStaffId(jsonArray.getJSONObject(i).getString("description"));
            containers.add(model);
        }

        cardAdapter=new TaskAdapter(getContext(),containers);
        myRecyclerView.setAdapter(cardAdapter);

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AssignTasksViewModel.class);
        // TODO: Use the ViewModel
    }

}

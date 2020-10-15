package com.example.myapplication.ui.InventoryIncharge;

import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddItem extends Fragment {



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


    View v;
    private AddItemViewModel mViewModel;

    public static AddItem newInstance() {
        return new AddItem();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.add_item_fragment, container, false);

        myCalendar = Calendar.getInstance();
        name = (EditText)getActivity().findViewById(R.id.name);
        description = (EditText)getActivity().findViewById(R.id.description);
        location = (EditText)getActivity().findViewById(R.id.location);
        latency = (EditText)getActivity().findViewById(R.id.latency);
        b = (Button)getActivity().findViewById(R.id.add_btn);




        //Priority spinner starts
        String[] arrayPriority = new String[] {
                "High", "Medium", "Low"
        };
        priority = (Spinner)getActivity(). findViewById(R.id.priority);
        adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayPriority);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priority.setAdapter(adapter1);
        String text = priority.getSelectedItem().toString();
        final String priority_selected = text;
        //Priority spinner ends
        //department spinner starts
        department = (Spinner) getActivity().findViewById(R.id.department);
        requestQueue = Volley.newRequestQueue(getActivity());


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


        adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        department.setAdapter(adapter2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);










        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddItemViewModel.class);
        // TODO: Use the ViewModel
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

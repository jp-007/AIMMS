package com.example.myapplication.ui.home;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.graphics.Color;
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

import org.json.JSONException;
import org.json.JSONObject;



public class AirportAdminHome extends Fragment {







    private AirportAdminHomeViewModel mViewModel;

    RequestQueue requestQueue;
    Context context;

    private static final int SUCCESS_CODE=2;
    private static final int FAILURE_CODE=4;
    private boolean AUTHENTICATE=false;

    JSONObject object=new JSONObject();
    String GET_URL="https://aimm.pythonanywhere.com/api/all_dept_maintenance_count/";


    TextView ongoing;
    TextView raised;
    TextView completed;

    View v;


    public static AirportAdminHome newInstance() {
        return new AirportAdminHome();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.airport_admin_home_fragment, container, false);
        //Toast.makeText(getActivity(),"Heyy",Toast.LENGTH_LONG).show();
        connect();
        return  v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AirportAdminHomeViewModel.class);
        // TODO: Use the ViewModel
    }


    public void connect(){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());


        // Toast.makeText(MainActivity.this, "Helllllllo", Toast.LENGTH_SHORT).show();
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                GET_URL,
                null,
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




        ongoing=(TextView)v.findViewById(R.id.airadmin_ongoing);
        raised=(TextView)v.findViewById(R.id.airadmin_raised);
        completed=(TextView)v.findViewById(R.id.airadmin_completed);

        int p1=0;
        int p2=0;
        int p3=0;
        int tpending=0;int traised=0;int tcompleted=0;
        try{
            ongoing.setText(j.getString("count pending"));
            raised.setText(j.getString("count raised"));
            completed.setText(j.getString("count completed"));
            // Toast.makeText(this,"Heyy"+j.getString("count pending"),Toast.LENGTH_LONG);
//            tpending=Integer.parseInt(j.getString("count pending"));
//             traised=Integer.parseInt(j.getString("count raised"));
//             tcompleted=Integer.parseInt(j.getString("count completed"));
//            int total=(tpending+traised+tcompleted);
//             p1=100*(tpending/total);
//             p2=100*(traised/total);
//             p3=100*(tcompleted/total);

        }catch (JSONException je){
            Log.e("JSON exception",je.toString());
        }
//        PieChartView pieChartView = v.findViewById(R.id.chart);
//        List<SliceValue> pieData = new ArrayList<>();
//        pieData.add(new SliceValue(p1, Color.BLUE));
//        pieData.add(new SliceValue(p2, Color.GRAY));
//        pieData.add(new SliceValue(p3, Color.RED));

        Toast.makeText(getActivity(),"hello!"+String.valueOf(tpending),Toast.LENGTH_LONG).show();

//        PieChartData pieChartData = new PieChartData(pieData);
//        pieChartView.setPieChartData(pieChartData);

    }


}

package com.example.myapplication.ui.maintenanceadmin;

import androidx.cardview.widget.CardView;
import androidx.gridlayout.widget.GridLayout;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;

public class ViewTeams extends Fragment implements View.OnClickListener {

    private ViewTeamsViewModel mViewModel;
    GridLayout mainGrid;
    ViewTeams v = new ViewTeams();

    public static ViewTeams newInstance() {
        return new ViewTeams();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       // mainGrid = (GridLayout)getView().findViewById(R.id.mainGrid);
        //setSingleEvent(mainGrid);
        ViewGroup g = (ViewGroup)inflater.inflate(R.layout.view_teams_fragment,container,false);
        v.setSingleEvent(mainGrid);

        return g;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setSingleEvent(mainGrid);

        mViewModel = ViewModelProviders.of(this).get(ViewTeamsViewModel.class);





        // TODO: Use the ViewModel
    }
    private void setSingleEvent(GridLayout mainGrid) {
        //loop all the child item of Main Grid
        mainGrid = (GridLayout)getView().findViewById(R.id.mainGrid);

        for(int i=0;i<mainGrid.getChildCount();i++){
            CardView card = (CardView)mainGrid.getChildAt(i);
            final int finalI = i;
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(),"Clicked at index "+ finalI,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {

    }
}

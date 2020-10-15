package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.myapplication.ui.scan.scan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.zxing.Result;
import com.google.zxing.client.android.Intents;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static final int PERMISSION_REQUEST_CAMERA=1;
    Users user;
    NavigationView navigationView;
    static final String ADMIN="ADMIN";
    static final String STAFF="STAFF";



    NavController navController;

     String USER_LOGGED_IN="";
    TextView t;
    View v;
    NavInflater navInflater;
    NavGraph graph;
    Menu nav_menu;

    //ONCREATE METHOD
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home,R.id.nav_help,R.id.nav_staffhome,R.id.nav_AirportAdminHome,R.id.nav_MCoordinatorHome,
                R.id.nav_MTeamLeaderHome,R.id.nav_InventoryInchargeHome,R.id.nav_DeptHeadHome,R.id.nav_MainAdminHome,R.id.nav_help,
                R.id.nav_history,R.id.nav_addstaff,R.id.nav_report,R.id.nav_additem,R.id.nav_modifyitem,
                R.id.nav_deleteitem,R.id.nav_lists,R.id.nav_adddept,R.id.nav_reportofdepts,R.id.nav_viewteam,
                R.id.nav_analytics,R.id.nav_madmin_gethelp,R.id.nav_allocate,R.id.nav_mainteamleader_gethelp,
                R.id.nav_teamleaderreport,R.id.nav_assigntask)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        //USER SESSION HANDLIng
        user=new Users(MainActivity.this);

        //Toast.makeText(MainActivity.this,"jayesh",Toast.LENGTH_LONG).show();

        USER_LOGGED_IN=user.getUserType();

        navInflater = navController.getNavInflater();
        graph = navInflater.inflate(R.navigation.mobile_navigation);

        checkUser();


        //new  method added
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

                //LISTENERS FOR SIDE BAR MENU ITEM SELECTION
                if(destination.getId()==R.id.nav_home)
                {
                    //SIDE BAR HOME BUTTON
                    //openhome();
                    Toast.makeText(MainActivity.this,"home",Toast.LENGTH_LONG).show();
                }
                if(destination.getId()==R.id.nav_help)
                {
                    //SIDE BAR HELP BUTTON
                    Toast.makeText(MainActivity.this,"nav_help",Toast.LENGTH_LONG).show();
                }
                if(destination.getId()==R.id.nav_logout)
                {
                    //SIDE BAR LOGOUT BUTTON
                    Toast.makeText(MainActivity.this,"successfully logged out!",Toast.LENGTH_LONG).show();
                    logout();

                }

            }
        });


    }



    //SCAN OPERATION
    public void scan() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            try {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            }catch (Exception e)
            {
               // Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        }else
        {
            startActivity(new Intent(MainActivity.this,ScanActivity.class));
        }
    }


    //LISTENER FOR CORNER MENU BUTTONS
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //FOR SCAN BUTTON
        if(item.getItemId()==R.id.scan_button) {
            scan();
        }

        //FOR SCAN BUTTON
        if(item.getItemId()==R.id.logout_button) {
            logout();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    //REQUEST PERMISSION RESULT
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            startActivity(new Intent(MainActivity.this,ScanActivity.class));
        }
        else
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.CAMERA) ){
                showAlert();
            }else
            {
                Toast.makeText(getApplicationContext(),"Cant start camera Allow permissions from settings" ,Toast.LENGTH_LONG).show();
            }
        }
    }

    //ALERT
    public void showAlert(){
        AlertDialog alertDialog=new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs camera permissions");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Don't ALlow",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ALlow",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                PERMISSION_REQUEST_CAMERA);
                        finish();
                    }
                });
        alertDialog.show();
    }



    public void checkUser()
    {
        if(USER_LOGGED_IN.equals(Users.Airport_Admin))
        {
            NavInflater nav = navController.getNavInflater();
            NavGraph grap= nav.inflate(R.navigation.mobile_navigation);
            navigationView= findViewById(R.id.nav_view);
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.airport_admin_menu);
            grap.setStartDestination(R.id.nav_AirportAdminHome);
            navController.setGraph(grap);

        }
        if(USER_LOGGED_IN.equals(Users.Department_Head))
        {
            navigationView= findViewById(R.id.nav_view);
            nav_menu=navigationView.getMenu();
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.dept_head_menu);

            graph.setStartDestination(R.id.nav_DeptHeadHome);
            navController.setGraph(graph);
        }
        if(USER_LOGGED_IN.equals(Users.Airport_Staff))
        {
            navigationView= findViewById(R.id.nav_view);
            nav_menu=navigationView.getMenu();
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.airport_staff_menu);

            graph.setStartDestination(R.id.nav_staffhome);
            navController.setGraph(graph);
        }
        if(USER_LOGGED_IN.equals(Users.Maintenance_Team_Leader))
        {
            navigationView= findViewById(R.id.nav_view);
            nav_menu=navigationView.getMenu();
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.main_team_leader_menu);

            graph.setStartDestination(R.id.nav_MTeamLeaderHome);
            navController.setGraph(graph);
        }
        if(USER_LOGGED_IN.equals(Users.Maintenance_Admin))
        {
            navigationView= findViewById(R.id.nav_view);
            nav_menu=navigationView.getMenu();
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.maintenance_admin_menu);

            graph.setStartDestination(R.id.nav_MainAdminHome);
            navController.setGraph(graph);
        }
        if(USER_LOGGED_IN.equals(Users.Inventory_Incharge))
        {
            navigationView= findViewById(R.id.nav_view);
            nav_menu=navigationView.getMenu();
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.inventory_inch_menu);

            graph.setStartDestination(R.id.nav_InventoryInchargeHome);
            navController.setGraph(graph);
        }
        if(USER_LOGGED_IN.equals(Users.Maintenance_Coordinator))
        {
            navigationView= findViewById(R.id.nav_view);
            nav_menu=navigationView.getMenu();
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.maintainence_coordinator_menu);


            graph.setStartDestination(R.id.nav_MCoordinatorHome);
            navController.setGraph(graph);
        }

    }



    //LOGOUT
    public  void logout()
    {
        user.logOut();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
        finish();
    }

public static class example extends Fragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_help,container,false);
    }


}
public static class assign extends Fragment{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }
}


}


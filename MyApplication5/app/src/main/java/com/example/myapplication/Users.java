package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shree on 11-03-2018.
 */
public class Users {

    private String post;
    private String username;
    private String password;
    private String department;
    private String userid;



    private String dept_id;
    private SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE=0;

    private  static String DESIGNATION="NA";

    private static final String PREF_NAME= "LOGIN";
    private static final String LOGIN= "IS_LOGIN";

    public String getUserid() {
        userid=sharedPreferences.getString(USER_ID, "");
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    private static final String USER_ID= "USERID";

    private static final String USERNAME= "USERNAME";
    private static final String PASSWORD= "PASSWORD";
    private static final String USER_TYPE= "USERTYPE";
    private static final String TEAM= "TEAM";
    private static final String DEPARTMENT= "DEPARTMENT";
    private static final String DEPT_ID= "DEPT_ID";

    static final String Airport_Admin="Airport Admin";
    static final String Maintenance_Coordinator="Maintenance Coordinator";
    static final String Maintenance_Team_Leader="Maintenance Team Leader";
    static final String Inventory_Incharge="Inventory Incharge";
    static final String Airport_Staff="Airport Staff";
    static final String Department_Head="Department Head";
    static final String Maintenance_Admin="Maintenance Admin";





    public Users(Context context){
        this.context=context;
        sharedPreferences=context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor=sharedPreferences.edit();
    }


    public boolean isLogin()
    {
        return sharedPreferences.getBoolean(LOGIN,false);
    }

    public  void checkLogin()
    {
        //Toast.makeText(context,getUsername(),Toast.LENGTH_LONG).show();
        if(this.isLogin())
        {
            //Toast.makeText(context,getUsername(),Toast.LENGTH_LONG).show();
            Intent i=new Intent(context,MainActivity.class);
            context.startActivity(i);
            ((LoginActivity)context).finish();
        }

    }

    /******************** SETTERS *************************/
    //FOR SETTING USERNAME
    public void setUsername(String username){
        this.username=username;
        sharedPreferences.edit().putString(USERNAME, username).commit();
    }
    //FOR SETTING PASSWORD
    public void setPassword(String password){
        this.password=password;
        sharedPreferences.edit().putString(PASSWORD,password).commit();
    }

    //FOR GETTING USERNAME
    public void setUserType(String userType){
        this.password=password;
        sharedPreferences.edit().putString(USER_TYPE,userType).commit();
    }




    public String getDept_id() {
        dept_id=sharedPreferences.getString(DEPT_ID, "");
        return dept_id;
    }

    public void setDept_id(String dept_id) {
        this.dept_id = dept_id;
    }

    /******************** GETERS *************************/
    //FOR GETTING USERNAME
    public String getUsername(){
        username=sharedPreferences.getString(USERNAME, "");
        return username;
    }

    //FOR GETTING USERNAME
    public String getUserType(){
        username=sharedPreferences.getString(USER_TYPE, "");
        return username;
    }

    //FOR GETTING PASSWORD
    public String getPassword(){
        password=sharedPreferences.getString(PASSWORD,"");
        return password;
    }
    //FOR LOGOUT
    public void logOut(){
        sharedPreferences.edit().clear().commit();
    }



    public boolean createSession(JSONObject data,String type) {

        try {
            JSONObject object=new JSONObject(data.getString("data"));
            editor.putBoolean(LOGIN,true);
            editor.putString(USERNAME,object.getString("name"));
            editor.putString(USER_TYPE,type);
            editor.putString(DEPARTMENT,object.getString("dept"));
            editor.putString(USER_ID,object.getString("id"));
            editor.putString(DEPT_ID,object.getString("department"));
            //editor.putString(TEAM, String.valueOf(object.getInt("team")));
            editor.apply();

            return true;

        }catch (Exception e)
        {
            Toast.makeText(context,"error in creating session:"+e.toString(),Toast.LENGTH_LONG).show();
        }

        return  false;

    }
}

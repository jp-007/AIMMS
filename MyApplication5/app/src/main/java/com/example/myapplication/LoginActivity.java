package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    Button loginButton;
    EditText username,password;
    Spinner userTypeSpinner;
    TextView signup;
    Users users;
    Connection con;

    static final String ADMIN="ADMIN";
    static final String STAFF="STAFF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton=(Button)findViewById(R.id.loginbutton);
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);

        userTypeSpinner=(Spinner)findViewById(R.id.usertypespinner);
        signup=(TextView)findViewById(R.id.signup);

        users=new Users(LoginActivity.this);
        con=new Connection(this);
        users.checkLogin();

        //SPINNER CONFIGURATION
        List<String> objects=new ArrayList<String>();
        objects.add("Airport Admin");
        objects.add("Maintenance Coordinator");
        objects.add("Maintenance Team Leader");
        objects.add("Inventory Incharge");
        objects.add("Airport Staff");
        objects.add("Department Head");
        objects.add("Maintenance Admin");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, objects);

        userTypeSpinner.setAdapter(adapter);
        userTypeSpinner.setSelection(adapter.getCount()-1);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignUp.class));
               // Toast.makeText(LoginActivity.this,"hii",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void login()
    {
        //get selected user type
        final String userType = userTypeSpinner.getSelectedItem().toString();
        con.authenticate(username.getText().toString(),password.getText().toString(),userType);
    }
}



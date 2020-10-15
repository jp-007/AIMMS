package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanCoordinator extends AppCompatActivity implements ZXingScannerView.ResultHandler  {

    private ZXingScannerView zXingScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_coordinator);
    }


    public void scan(View view)
    {
        Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_LONG).show();
        zXingScannerView=new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

    @Override
    public void handleResult(Result result) {
        Toast.makeText(getApplicationContext(),result.getText(),Toast.LENGTH_LONG).show();
        zXingScannerView.stopCamera();
        startMaintain(result.toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.startCamera();
    }

    void startMaintain(String key)
    {

    }
}

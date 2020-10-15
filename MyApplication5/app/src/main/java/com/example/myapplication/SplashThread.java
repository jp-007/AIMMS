package com.example.myapplication;

public class SplashThread extends Thread {
    public void run()
    {
        try {
            Thread.currentThread().wait(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

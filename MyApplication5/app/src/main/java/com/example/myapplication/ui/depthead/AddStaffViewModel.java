package com.example.myapplication.ui.depthead;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddStaffViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<String> mText;

    public AddStaffViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
        Log.e("context","Home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}


package com.example.travller_signup.viewmodels;

import android.arch.lifecycle.MutableLiveData;

public class EmailAuthViewModel {

    public MutableLiveData<String> authLD = new MutableLiveData<>();
    public MutableLiveData<String> timerLD = new MutableLiveData<>();
    public MutableLiveData<String> resultLD = new MutableLiveData<>();

    public void onOKClicked(){

    }

    public void onCANCELClicked(){

    }

}

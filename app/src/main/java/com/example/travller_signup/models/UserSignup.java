package com.example.travller_signup.models;

import android.util.Log;
import android.util.Patterns;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.regex.Pattern;

public class UserSignup {

    String us_email;
    String us_pwd;
    String us_nick_name;
    String us_device;

    public UserSignup(String us_email, String us_pwd, String us_nick_name, String us_device) {
        this.us_email = us_email;
        this.us_pwd = us_pwd;
        this.us_nick_name = us_nick_name;
        this.us_device = us_device;
    }

    public String getUs_email() {
        return us_email;
    }

    public void setUs_email(String us_email) {
        this.us_email = us_email;
    }

    public String getUs_pwd() {
        return us_pwd;
    }

    public void setUs_pwd(String us_pwd) {
        this.us_pwd = us_pwd;
    }

    public String getUs_nick_name() {
        return us_nick_name;
    }

    public void setUs_nick_name(String us_nick_name) {
        this.us_nick_name = us_nick_name;
    }

    public String getUs_device() {
        return us_device;
    }

    public void setUs_device(String us_device) {
        this.us_device = us_device;
    }

    @Override
    public String toString() {
        return "UserSignup{" +
                "us_email='" + us_email + '\'' +
                ", us_pwd='" + us_pwd + '\'' +
                ", us_nick_name='" + us_nick_name + '\'' +
                ", us_device='" + us_device + '}';
    }

}


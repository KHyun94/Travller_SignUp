package com.example.travller_signup.views;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.travller_signup.R;
import com.example.travller_signup.databinding.ActivityEmailAuthViewBinding;
import com.example.travller_signup.databinding.ActivitySignUpBinding;
import com.example.travller_signup.viewmodels.EmailAuthViewModel;
import com.example.travller_signup.viewmodels.SignUpViewModel;

public class EmailAuthView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityEmailAuthViewBinding emailAuthBinding = DataBindingUtil.setContentView(this, R.layout.activity_email_auth_view);

        emailAuthBinding.setEmailVM(new EmailAuthViewModel());
        emailAuthBinding.setLifecycleOwner(this);
        emailAuthBinding.executePendingBindings();

    }
}

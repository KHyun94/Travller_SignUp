package com.example.travller_signup.views;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.travller_signup.R;
import com.example.travller_signup.bindingadapter.DataBindingAdapters;
import com.example.travller_signup.databinding.ActivitySignUpBinding;
import com.example.travller_signup.viewmodels.SignUpViewModel;

import java.io.File;

public class SignUpView extends AppCompatActivity {

    String TAG = "TAG";

    Uri uriPath;
    String absolutePath;

    SignUpViewModel svm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivitySignUpBinding signUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        signUpBinding.setSignUpVM(new SignUpViewModel(this));
        signUpBinding.setLifecycleOwner(this);
        signUpBinding.executePendingBindings();

        svm = signUpBinding.getSignUpVM();

        signUpBinding.getSignUpVM().buttonClickResult.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {

                if (integer == 0) {
                    Intent albumIntent = new Intent(Intent.ACTION_PICK);
                    albumIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(albumIntent, integer);
                } else if (integer == 1) {

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File pFile = createDir();

                    if (pFile != null) {

                        if (Build.VERSION.SDK_INT >= 24) {
                            Uri providerPath = FileProvider.getUriForFile
                                    (getApplicationContext(), getPackageName() + ".file_provider", pFile);
                            uriPath = providerPath;
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriPath);
                            startActivityForResult(cameraIntent, 1);

                        } else {
                            uriPath = Uri.fromFile(pFile);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriPath);
                            startActivityForResult(cameraIntent, 1);
                        }
                    }
                } else if (integer == 2) {
                    Intent emailIntent = new Intent(getApplicationContext(), EmailAuthView.class);
                    emailIntent.putExtra("email", svm.emailLD.getValue());
                    startActivityForResult(emailIntent, 2);
                }

            }
        });
    }

    // 디렉토리 생성
    public File createDir() {

        //저장될 파일의 이름
        String imgName = "t_" + System.currentTimeMillis() + ".jpg";

        //저장 디렉토리 주소
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Traveller/");

        //디렉토리가 없을 시 생성
        if (!storageDir.exists())
            storageDir.mkdirs();

        //저장될 파일의 주소
        File imgFilePath = new File(storageDir, imgName);

        absolutePath = imgFilePath.getAbsolutePath();

        return imgFilePath;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                try {
                    Uri tmpUri = data.getData();
                    svm.photoUpdate.setValue(tmpUri.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    File saveFile = new File(absolutePath);
                    uriPath = Uri.fromFile(saveFile);

                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).setData(uriPath));

                    svm.photoUpdate.setValue(uriPath.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                //이메일 인증 이후 처리 인증 or 미인증
                break;
        }
    }


}

package com.example.travller_signup.viewmodels;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.example.travller_signup.R;
import com.example.travller_signup.models.UserSignup;
import com.example.travller_signup.remote.RetrofitCon;
import com.example.travller_signup.setting.Setting_permissionCheck;
import com.example.travller_signup.views.EmailAuthView;
import com.gun0912.tedpermission.PermissionListener;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpViewModel extends BaseObservable {

    Context context;

    public MutableLiveData<String> emailLD = new MutableLiveData<>();
    public MutableLiveData<String> passwordLD = new MutableLiveData<>();
    public MutableLiveData<String> nickNameLD = new MutableLiveData<>();

    //(예정) 이메일 인증 처리하면 미인증 -> 인증
    public MutableLiveData<String> emailAuth = new MutableLiveData<>();

    public MutableLiveData<Integer> buttonClickResult = new MutableLiveData<>();

    public MutableLiveData<String> photoUpdate = new MutableLiveData<>();

    //이메일 인증을 하면 true 값으로 반환, 지금은 이메일 인증을 연결 안해두어서 true 값
    public boolean isAuth = true;

    String TAG = "TAG";

    public SignUpViewModel(Context context) {
        this.context = context;
        emailAuth.setValue("미인증");

        String imageUri = "drawable://" + R.drawable.p_blank_person;
        photoUpdate.setValue(imageUri);

    }

    //디바이스 초기 값, 속성: 유니크, 디바이스 초기화할 때 바뀐다고 함
    private String getDeviceID() {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    //토스트 메시지 메서드화
    private void sendToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    //이메일 정규식 체크
    private boolean isEmailValid() {
        return Patterns.EMAIL_ADDRESS.matcher(emailLD.getValue()).matches();
    }

    //영소문 숫자 조합 8자 이상 특수문자는 !@#$%^*만 사용 가능
    private boolean isPasswordValid() {

        return Pattern.matches("^(?=.*\\d)(?=.*[a-z])[a-z\\d!@#$%^&*]{8,}$", passwordLD.getValue());
    }

    //자음 + 모음 한글, 영어, 숫자 조합
    private boolean isNickValid() {
        return Pattern.matches("^[\\w가-힣]{2,20}$", nickNameLD.getValue());
    }

    //이메일 인증 버튼
    public void onEmailAuthClicked() {
        if (TextUtils.isEmpty(emailLD.getValue())) {
            sendToast(context, "System::Email == null");
        } else if (!isEmailValid()) {
            sendToast(context, "System::Email 정규식 Error");
        } else {
            sendToast(context, "System::Email Clear");

            buttonClickResult.setValue(2);

            //(예정) 여기서 이메일 인증 처리
            isAuth = true;
            emailAuth.setValue("인증");
        }
    }

    //회원 가입 버튼
    public void onSignUpClicked() {

        //각각의 입력 값을 정규식이 맞는지 null이 아닌지 체크
        if (TextUtils.isEmpty(nickNameLD.getValue())) {
            sendToast(context, "System::NickName == null");
        } else if (!isNickValid()) {
            nickNameLD.setValue("");
            sendToast(context, "System::NickName 정규식 Error");
        } else if (TextUtils.isEmpty(emailLD.getValue())) {
            sendToast(context, "System::Email == null");
        } else if (!isEmailValid()) {
            emailLD.setValue("");
            sendToast(context, "System::Email 정규식 Error");
        } else if (TextUtils.isEmpty(passwordLD.getValue())) {
            sendToast(context, "System::Password == null");
        } else if (!isPasswordValid()) {
            passwordLD.setValue("");
            sendToast(context, "System::Password 정규식 Error");
        } else if (!isAuth) {
            sendToast(context, "System::Email 미인증");
        } else {
            //모든 조건에 만족할 시 실행
            sendToast(context, "System::All Clear");

            UserSignup userSignup;

            //프로필 이미지가 null이거나 초기값 사진 또는 에러 이미지일 때
            if (photoUpdate.getValue() == null || photoUpdate.getValue().equals("drawable://" + R.drawable.p_blank_person)) {

                userSignup = new UserSignup(emailLD.getValue(), returnSHA256(passwordLD.getValue()), nickNameLD.getValue(), getDeviceID(), null);

                Log.d(TAG, "1. onSignUpClicked: " + userSignup.toString());
            }
            else {
                //프로필 이미지가 null이거나 초기값 사진 또는 에러 이미지일 때가 아닐때
                File imgFile = new File((Uri.parse(photoUpdate.getValue().toString())).getPath());
                userSignup = new UserSignup(emailLD.getValue(), returnSHA256(passwordLD.getValue()), nickNameLD.getValue(), getDeviceID(), imgFile);

                Log.d(TAG, "2. onSignUpClicked: " + userSignup.toString());
            }

            String IP = "";

            if(userSignup != null){

                final RetrofitCon con = new RetrofitCon(IP);
                Call<Boolean> su_call = con.retrofitService.goSignUp(userSignup);

                su_call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        boolean getValue = response.body();

                        if(getValue){
                            sendToast(context, "회원가입 축하드립니다.");
                            //회원가입 성공, 로그인 화면으로 돌아간다.
                        }else{
                            sendToast(context,"회원가입 실패: 이유 - ");
                            //회원가입 실패
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        sendToast(context, "통신 불량");
                        t.printStackTrace();
                    }
                });
            }

        }
    }

    //프로필 이미지
    public void onImageClicked() {

        Setting_permissionCheck permissionCheck = new Setting_permissionCheck(context, new Activity());

        permissionCheck.setPermissionListener_camera(new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                final AlertDialog.Builder photoAlert = new AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog_Alert);

                photoAlert.setTitle("이미지 등록")
                        .setMessage("앨범/카메라 선택")
                        .setNegativeButton("앨범", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                buttonClickResult.setValue(0);
                            }
                        })
                        .setPositiveButton("카메라", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                buttonClickResult.setValue(1);
                            }
                        })
                        .setNeutralButton("취소", null).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {

            }
        });

    }

    //저장할 때 패스워드를 sha256으로 해시화 해서 보낸다.
    public String returnSHA256(String str) {

        String SHA = "";

        try {
            MessageDigest sh = MessageDigest.getInstance("SHA-256");

            sh.update(str.getBytes());

            byte byteData[] = sh.digest();

            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            SHA = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            SHA = null;
        }

        return SHA;
    }


}

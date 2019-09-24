package com.example.travller_signup.bindingadapter;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.travller_signup.R;

public class DataBindingAdapters {

    @BindingAdapter({"imgLoad"})
    public static void imgLoad(ImageView iv, String imgUri) {

        RequestOptions options
                = RequestOptions.bitmapTransform(new CircleCrop()).error(R.drawable.p_blank_person).circleCrop();

        Glide.with(iv.getContext())
                .load(imgUri)
                .apply(options)
                .into(iv);
    }
}

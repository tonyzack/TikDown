package com.nadhholy.tikdownloader.video.dialogs;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.nadhholy.tikdownloader.video.R;
import com.nadhholy.tikdownloader.video.data.Config;
import com.nadhholy.tikdownloader.video.data.PrefConfig;

import java.util.Objects;


public class RateTfDialog extends Dialog {

    private Activity context;

    public RateTfDialog(@NonNull Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_tf_dialog);

        Objects.requireNonNull(getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        findViewById(R.id.yes).setOnClickListener(v -> {
            String package_ = context.getPackageName();
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+package_)));
            } catch (Exception e){
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+package_)));
            }

        });

    }

}

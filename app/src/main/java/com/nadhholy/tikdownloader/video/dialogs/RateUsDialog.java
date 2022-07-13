package com.nadhholy.tikdownloader.video.dialogs;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.nadhholy.tikdownloader.video.R;
import com.nadhholy.tikdownloader.video.data.Config;
import com.nadhholy.tikdownloader.video.data.PrefConfig;

import java.util.Objects;


public class RateUsDialog extends Dialog {

    private Activity context;
    private Config config;

    private boolean close;

    public RateUsDialog(@NonNull Activity context, boolean close) {
        super(context);
        this.context = context;
        this.close = close;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_rate_app);

        Objects.requireNonNull(getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        config = PrefConfig.getInstance(context.getApplicationContext()).getConfig();

//        dismiss = findViewById(R.id.dismiss);

        ImageView img = findViewById(R.id.img);

        Glide
                .with(getContext())
                .load(R.drawable.like_anim)
                .into(img);

        findViewById(R.id.yes).setOnClickListener(v -> {
            String package_ = context.getPackageName();
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+package_)));
            } catch (Exception e){
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+package_)));
            }

            config.setRated(true);
            PrefConfig.getInstance(context.getApplicationContext()).saveConfig(config);
            dismiss();

            if (close){
                context.finish();
            }
        });

        findViewById(R.id.no).setOnClickListener(v -> {
            if (config.getOpenCount() >= 2) {
                FeedbackDialog feedbackDialog = new FeedbackDialog(getContext());
                feedbackDialog.show();
                config.setRated(true);
                PrefConfig.getInstance(context.getApplicationContext()).saveConfig(config);
            } else {
                config.setOpenCount(config.getOpenCount()+1);
                PrefConfig.getInstance(context.getApplicationContext()).saveConfig(config);
                if (close){
                    context.finish();
                    return;
                }
            }
            dismiss();
        });


    }

}

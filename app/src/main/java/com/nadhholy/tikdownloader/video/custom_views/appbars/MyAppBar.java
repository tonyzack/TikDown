package com.nadhholy.tikdownloader.video.custom_views.appbars;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.nadhholy.tikdownloader.video.R;


public class MyAppBar extends FrameLayout {

    private ImageView actionIcon;
    private TextView barTitle;


    public MyAppBar(Context context) {
        super(context);
        init(null);
    }

    public MyAppBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MyAppBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    public void init(@Nullable AttributeSet attrs) {

        if (attrs == null || getContext() == null)
            return;

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.MyAppBar);

        final String title = ta.getString(R.styleable.MyAppBar_android_text);

        ta.recycle();

        View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_app_bar, this);


        actionIcon = view.findViewById(R.id.action_icon);
        barTitle = view.findViewById(R.id.title);

        if (getContext() instanceof Activity){
            actionIcon.setOnClickListener(v -> ((Activity)getContext()).onBackPressed());
        }

        if (!TextUtils.isEmpty(title))
            barTitle.setText(title);


//        int p = (int) getResources().getDimension(R.dimen.appbar_padding);
//
//        setPadding(p, 0, p,0);
    }


    public void setTitle(String s){
        if (TextUtils.isEmpty(s))
            barTitle.setText("");
        else
            barTitle.setText(s);
    }

    public void setActionIconAction(OnClickListener action){
        actionIcon.setOnClickListener(action);
    }

}

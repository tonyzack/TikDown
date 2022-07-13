package com.nadhholy.tikdownloader.video.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.nadhholy.tikdownloader.video.R;

import java.util.Objects;


public class MyDialog extends Dialog {

    private TextView titleTv;
    private TextView messageTv;
    private TextView positiveBtn;
    private TextView negativeBtn;

    private String title;
    private String message;

    private View.OnClickListener positiveAction;

    private Context context;

    public MyDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_dialog);

        titleTv = findViewById(R.id.title);
        messageTv = findViewById(R.id.message);
        positiveBtn = findViewById(R.id.btn_positive);
        negativeBtn = findViewById(R.id.btn_negative);

        Objects.requireNonNull(getWindow()).setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        messageTv.setText(message);
        if (title != null) {
            titleTv.setVisibility(View.VISIBLE);
            titleTv.setText(title);
        }

        negativeBtn.setOnClickListener(v -> {
            dismiss();
        });

        if (positiveAction != null){
            positiveBtn.setOnClickListener(positiveAction);
        } else {
            positiveBtn.setOnClickListener(v -> dismiss());
        }

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPositiveAction(View.OnClickListener positiveAction) {
        this.positiveAction = positiveAction;
        if (positiveBtn != null)
            positiveBtn.setOnClickListener(positiveAction);
    }
}

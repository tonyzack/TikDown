package com.nadhholy.tikdownloader.video.dialogs;


import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.nadhholy.tikdownloader.video.BuildConfig;
import com.nadhholy.tikdownloader.video.R;
import com.nadhholy.tikdownloader.video.utils.DeviceMemoryUtils;

import java.util.Locale;
import java.util.Objects;


public class FeedbackDialog extends Dialog {


    private Context context;

    private TextView send;
    private TextView dismiss;
    private EditText message;

    private int rate;

    public FeedbackDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        this.rate = -1;
    }

    public FeedbackDialog(@NonNull Context context, int rate) {
        super(context);
        this.context = context;
        this.rate = rate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_fb);

        Objects.requireNonNull(getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        send = findViewById(R.id.send);
        dismiss = findViewById(R.id.dismiss);
        message = findViewById(R.id.message);

        send.setOnClickListener(v1 -> {
            String string = message.getText().toString();
            if (string.isEmpty()) return;

            string = string+"\n\n"+getDeviceInfo();


            Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
            selectorIntent.setData(Uri.parse("mailto:"));

            Intent mailIntent = new Intent(Intent.ACTION_SEND);
            mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@nadhholy.me"});
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback - TikDown");
            mailIntent.putExtra(Intent.EXTRA_TEXT, string);
            mailIntent.setSelector(selectorIntent);

            try {
                context.startActivity(Intent.createChooser(mailIntent,context.getString(R.string.feedback)));
            } catch (Exception e){
                Toast.makeText(context, R.string.no_app, Toast.LENGTH_SHORT).show();
            }


            dismiss();
        });

        dismiss.setOnClickListener(v -> dismiss());
    }

    private String getDeviceInfo(){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        if (activityManager != null)
            activityManager.getMemoryInfo(memoryInfo);

        String devInfo = "[Info]";
        devInfo += "\n Rate: "+rate;
        devInfo += "\n Device: "+Build.DEVICE + " ("+Build.MODEL+")";
        devInfo += "\n OS API Level: "+Build.VERSION.SDK_INT;
        devInfo += "\n OS Version: " +System.getProperty("os.version") + " (" + Build.VERSION.INCREMENTAL + ")";
        devInfo += "\n RAM : " + DeviceMemoryUtils.formatSize(memoryInfo.availMem)+"/"+ DeviceMemoryUtils.formatSize(memoryInfo.totalMem);
        devInfo += "\n Storage : " + DeviceMemoryUtils.getAvailableInternalMemorySize() +"/"+ DeviceMemoryUtils.getTotalInternalMemorySize();
        devInfo += "\n App Version: "+ BuildConfig.VERSION_CODE +" ("+BuildConfig.VERSION_NAME+")";
        devInfo += "\n Locale: "+ Locale.getDefault();
        return devInfo;
    }

}

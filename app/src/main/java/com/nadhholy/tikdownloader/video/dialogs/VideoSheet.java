package com.nadhholy.tikdownloader.video.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.nadhholy.tikdownloader.video.R;
import com.nadhholy.tikdownloader.video.models.Aweme;


public class VideoSheet extends BottomSheetDialogFragment {

    public static int NWM = 1;
    public static int WM = 2;
    public static int MP3 = 3;

    private OnDismissListener onDismissListener;

    private final Aweme data;

    private int selected;

    public static VideoSheet newInstance(OnDismissListener onDismissListener, Aweme data){
        return new VideoSheet(onDismissListener, data);
    }

    public VideoSheet(OnDismissListener onDismissListener, Aweme data) {
        this.onDismissListener = onDismissListener;
        this.data = data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sheet_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView name = view.findViewById(R.id.name);
        TextView desc = view.findViewById(R.id.desc);
        ImageView cover = view.findViewById(R.id.cover_image);
        ImageView avatar = view.findViewById(R.id.avatar);
        View btn_nw = view.findViewById(R.id.btn_nw);
        View btn_w = view.findViewById(R.id.btn_w);
        View btn_mp3 = view.findViewById(R.id.btn_mp3);

        name.setText(data.getUsername());
        desc.setText(data.getTitle());

        Glide
                .with(getContext())
                .load(data.getCoverAnim())
                .placeholder(R.color.dark_accent2)
                .into(cover);

        Glide
                .with(getContext())
                .load(data.getAvatar())
                .placeholder(R.drawable.profile_user_default)
                .into(avatar);

        btn_nw.setOnClickListener(view1 -> {
            selected = NWM;
            dismiss();
        });
        btn_w.setOnClickListener(view1 -> {
            selected = WM;
            dismiss();
        });
        btn_mp3.setOnClickListener(view1 -> {
            selected = MP3;
            dismiss();
        });
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        if (onDismissListener != null)
            onDismissListener.onDismiss(selected);
        super.onDismiss(dialog);
    }

    public interface OnDismissListener {
        void onDismiss(int select);
    }


}

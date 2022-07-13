package com.nadhholy.tikdownloader.video.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.nadhholy.tikdownloader.video.R;


public class VideoMenuSheet extends BottomSheetDialogFragment {

    public final static int SHARE = 10;
    public final static int TRILLER = 11;
    public final static int DELETE = 12;
    public final static int SEARCH_MUSIC = 13;

    private int selected = 0;

    private String message;
    private OnSelectListener onSelectListener;

    public static VideoMenuSheet newInstance(String message, OnSelectListener onSelectListener){
        return new VideoMenuSheet(message, onSelectListener);
    }

    public VideoMenuSheet(String message, OnSelectListener onSelectListener) {
        this.message = message;
        this.onSelectListener = onSelectListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sheet_video_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView ttk = view.findViewById(R.id.ttk);
        View delete = view.findViewById(R.id.delete);
        View share = view.findViewById(R.id.share);
        View findM = view.findViewById(R.id.find_music);
        View dismiss = view.findViewById(R.id.dismiss);


        ttk.setOnClickListener(v -> {
            selected = TRILLER;
            dismiss();
        });

        delete.setOnClickListener(v -> {
            selected = DELETE;
            dismiss();
        });

        share.setOnClickListener(v -> {
            selected = SHARE;
            dismiss();
        });

        findM.setOnClickListener(v -> {
            selected = SEARCH_MUSIC;
            dismiss();
        });

        dismiss.setOnClickListener(v -> {
            selected = 0;
            dismiss();
        });

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog=(BottomSheetDialog)super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(dialogg -> {
            BottomSheetDialog dialog = (BottomSheetDialog) dialogg;
            FrameLayout bottomSheet =  dialog .findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet == null) return;
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(true);
            BottomSheetBehavior.from(bottomSheet).setHideable(true);
        });
        return bottomSheetDialog;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        onSelectListener.onSelect(selected);
        super.onDismiss(dialog);
    }

    public interface OnSelectListener {
        void onSelect(int s);
    }

}

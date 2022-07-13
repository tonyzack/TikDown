package com.nadhholy.tikdownloader.video.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.nadhholy.tikdownloader.video.MyIntents;
import com.nadhholy.tikdownloader.video.R;
import com.nadhholy.tikdownloader.video.activities.DownloadedActivity;
import com.nadhholy.tikdownloader.video.activities.MusicActivity;
import com.nadhholy.tikdownloader.video.activities.VideoActivity;
import com.nadhholy.tikdownloader.video.adapters.SavedAdapter;
import com.nadhholy.tikdownloader.video.data.Repository;
import com.nadhholy.tikdownloader.video.dialogs.SearshMusicSheet;
import com.nadhholy.tikdownloader.video.dialogs.VideoMenuSheet;
import com.nadhholy.tikdownloader.video.models.Aweme;
import com.nadhholy.tikdownloader.video.utils.Utils;


import java.util.ArrayList;
import java.util.List;


public class SavedsFragment extends Fragment {

    private View emptyView;
    private View adPlaceholderLoader;
    private View adPlaceholder;
    private RecyclerView rv;
    private AdView adView;
    private SavedAdapter adapter;
    private List<Aweme> data;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_downloaded, container, false);

        emptyView = root.findViewById(R.id.empty_view);
        rv = root.findViewById(R.id.rv);
        adPlaceholderLoader = root.findViewById(R.id.ad_placeholder_loading);
        adPlaceholder = root.findViewById(R.id.ad_placeholder);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getContext() == null || getView() == null)
            return;

        data = new ArrayList<>();

        adapter = new SavedAdapter(getContext(), data);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);

        adapter.setOnMenuClickListener(data1 -> {
            VideoMenuSheet sheet = VideoMenuSheet.newInstance(null, s -> {
                switch (s){
                    case VideoMenuSheet.TRILLER:
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data1.getTiktokUrl())));
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case VideoMenuSheet.SHARE:
                        Utils.shareVideo(getContext(), data1);
                        break;
                    case VideoMenuSheet.SEARCH_MUSIC:
                        SearshMusicSheet sh = SearshMusicSheet.newInstance(data1.getMusicTitle());
                        sh.show(getChildFragmentManager(), sh.getTag());
                        break;
                    case VideoMenuSheet.DELETE:
                        AlertDialog dialogg = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme)
                                .setMessage(Utils.fromHtml("<font color='#FFFFFF'>"+getString(R.string.delete_video_confirm)+"</font>"))
                                .setPositiveButton(R.string.delete, (dialog, which) -> {

                                    Repository.getInstance(getContext().getApplicationContext())
                                            .remove(data1);
                                    Utils.deleteFile(getContext(),
                                            data1.getLocalPath());
                                    data.remove(data1);
                                    adapter.notifyDataSetChanged();


                                    if (data.isEmpty()){
                                        emptyView.setVisibility(View.VISIBLE);
                                    }

                                })
                                .setNegativeButton(R.string.dismiss, null)
                                .create();


                        dialogg.setOnShowListener(arg0 -> {
                            dialogg.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
                            dialogg.getButton(AlertDialog.BUTTON_POSITIVE)
                                    .setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                        });
                        dialogg.show();
                        break;
                }
            });
            sheet.show(getChildFragmentManager(), sheet.getTag());
        });


        FrameLayout adContainerView = getView().findViewById(R.id.adContainerView);
        adView = new AdView(getContext());
        adView.setAdUnitId(getString(R.string.BAN_1));
        adContainerView.addView(adView);
        loadBanner();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadBanner() {

        AdRequest adRequest =
                new AdRequest.Builder()
                        .build();

        AdSize adSize = getAdSize();

        if (adSize == null) return;

        adView.setAdSize(adSize);

        if (getContext() == null)
            return;
        adPlaceholderLoader.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.alarm));

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                adPlaceholderLoader.clearAnimation();
                adPlaceholder.setVisibility(View.GONE);
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                adPlaceholderLoader.clearAnimation();
                adPlaceholder.setVisibility(View.GONE);
            }

            @Override
            public void onAdClosed() {
                adPlaceholderLoader.clearAnimation();
                adPlaceholder.setVisibility(View.GONE);
            }
        });

        adView.loadAd(adRequest);

    }

    private AdSize getAdSize() {

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(getActivity(), adWidth);
    }

    private void loadData(){
        new Thread(() -> {

            if (getContext() == null) return;
            List<Aweme> d = Repository.getInstance(getContext().getApplicationContext()).getData();

            if (data == null)
                data = new ArrayList<>();

            data.clear();
            data.addAll(d);

            if (getContext() == null) return;
            rv.post(() -> {
                adapter.notifyDataSetChanged();
                if (data.isEmpty())
                    emptyView.setVisibility(View.VISIBLE);
                else
                    emptyView.setVisibility(View.GONE);
            });

        }).start();
    }


    @Override
    public void onDestroy() {
        if (adView != null){
            adView.destroy();
            adView = null;
        }
        super.onDestroy();
    }


}
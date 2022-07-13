package com.nadhholy.tikdownloader.video.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
//import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.nadhholy.tikdownloader.video.MyIntents;
import com.nadhholy.tikdownloader.video.R;
import com.nadhholy.tikdownloader.video.activities.MusicActivity;
import com.nadhholy.tikdownloader.video.activities.VideoActivity;
import com.nadhholy.tikdownloader.video.data.Repository;
import com.nadhholy.tikdownloader.video.dialogs.VideoSheet;
import com.nadhholy.tikdownloader.video.models.AwemRData;
import com.nadhholy.tikdownloader.video.models.Aweme;
import com.nadhholy.tikdownloader.video.services.APIService;
import com.nadhholy.tikdownloader.video.services.APIServiceH;
import com.nadhholy.tikdownloader.video.utils.AdsUtils;
import com.nadhholy.tikdownloader.video.utils.SingleMediaScanner;
import com.nadhholy.tikdownloader.video.utils.State;
import com.nadhholy.tikdownloader.video.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoDownloadFragment extends Fragment {

    private boolean created;
    private final static long adLaps = 30000;


    private EditText urlEt;
    private ViewGroup adFrame;
    private View checkingPan;
    private View downloadCard;
    private View adFrameLoader;
    private View adFrameLoaderTV;

    private AdLoader adLoader;
    private NativeAd unifiedNativeAd;
    private DownloadTask downloadTask;

//    private Map<String, String> headers;
    private long lastAdShowTime;
    private boolean downloading = false;

    private String intentUrl;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_download, container, false);

        urlEt = root.findViewById(R.id.url_et);
        adFrame = root.findViewById(R.id.adFrame);
        checkingPan = root.findViewById(R.id.checking_pan);
        downloadCard = root.findViewById(R.id.download_card);

        View pastBtn = root.findViewById(R.id.btn_past);
        View downloadBtn = root.findViewById(R.id.btn_download);

        progressBar = root.findViewById(R.id.progressBar);
        playIc = root.findViewById(R.id.play_ic);
        completed = root.findViewById(R.id.completed);

        if (getActivity() == null) return root;

        checkingPan.setOnClickListener(null);

        adFrameLoader =
                LayoutInflater.from(getContext()).inflate(R.layout.native_ads_placeholder, null);
        adFrameLoaderTV = adFrameLoader.findViewById(R.id.ad_placeholder_loading);

        intentUrl = getActivity().getIntent().getStringExtra(MyIntents.URL);
        getActivity().getIntent().putExtra(MyIntents.URL, "");



        final ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null && clipboard.hasPrimaryClip() && clipboard.getPrimaryClip() != null) {
            if (clipboard.getPrimaryClip() != null && clipboard.getPrimaryClip().getItemAt(0) != null) {
                CharSequence clip = clipboard.getPrimaryClip().getItemAt(0).coerceToText(getContext()).toString();
                if (!clip.toString().isEmpty()) {
                    urlEt.setText(clip.toString());
                    urlEt.clearFocus();
                    getData();
                }
            }
        }


        pastBtn.setOnClickListener(v -> {
//            final ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null && clipboard.hasPrimaryClip() && clipboard.getPrimaryClip() != null) {
                if (clipboard.getPrimaryClip() != null && clipboard.getPrimaryClip().getItemAt(0) != null) {
                    CharSequence clip = clipboard.getPrimaryClip().getItemAt(0).coerceToText(getContext()).toString();
                    if (!clip.toString().isEmpty()) {
                        urlEt.setText(clip.toString());
                        urlEt.clearFocus();
                        getData();
                    } else openTickTokDialog();
                } else openTickTokDialog();
            } else openTickTokDialog();
        });

        downloadBtn.setOnClickListener(v -> {
            getData();
        });


        AdsUtils.loadAdd(getContext());

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initHelpContent();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getView() == null) return;
        getView().getViewTreeObserver().addOnWindowFocusChangeListener(hasFocus -> {
            if (!hasFocus) return;
            if (getActivity() == null) return;

            if (intentUrl != null){
                urlEt.setText(intentUrl);
                getData();

                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(null, intentUrl);
                if (clipboard != null)
                clipboard.setPrimaryClip(clip);

                intentUrl = null;
            } else {
                final ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboard != null && clipboard.hasPrimaryClip() && clipboard.getPrimaryClip() != null) {
                    CharSequence clip = clipboard.getPrimaryClip().getItemAt(0).coerceToText(getContext()).toString();
                    if (!urlEt.getText().toString().trim().equals(clip.toString()) && clip.toString().contains("tiktok")) {
                        if (url != null && url.equals(clip.toString())){
                            return;
                        }
                        urlEt.setText(clip.toString());
                        getData();
                    }
                }
            }
            urlEt.clearFocus();
            getView().findViewById(R.id.root).requestFocus();

        });

        callNativeAds();
    }

    private void initHelpContent(){

        if (getView() == null || getContext() == null)
            return;

        FrameLayout layout = getView().findViewById(R.id.help_container);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.how_to_download, layout);

        getView().findViewById(R.id.help_divider).setVisibility(View.VISIBLE);

        Glide
                .with(this)
                .load(R.drawable.help_1)
                .into((ImageView) view.findViewById(R.id.image_1));

        Glide
                .with(this)
                .load(R.drawable.help_2)
                .into((ImageView) view.findViewById(R.id.image_2));

        Glide
                .with(this)
                .load(R.drawable.help_3)
                .into((ImageView) view.findViewById(R.id.image_3));

    }

    String url;
    Aweme aweme;
    private void getData(){

        if (getContext() == null || getView() == null || downloading) return;


        url = urlEt.getText().toString().trim();

        if (aweme != null) {
            if (aweme.getTiktokUrl().equals(url)) {
                VideoSheet d = VideoSheet.newInstance(select -> {
                    if (select == VideoSheet.MP3
                            || select == VideoSheet.NWM
                            || select == VideoSheet.WM)
                        start(aweme, select);
                }, aweme);
                d.show(getParentFragmentManager(), d.getTag());
                return;
            }
        }

        if (url.isEmpty()) {
            openTickTokDialog();
            return;
        }

        if (!url.contains("tiktok")){
            Toast.makeText(getContext(), R.string.provide_Valid_link, Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkingPan.getVisibility() == View.VISIBLE)
            return;


        checkingPan.setVisibility(View.VISIBLE);

        if (url.endsWith("/"))
            url = url.substring(0, url.length()-1);

        AwemRData b = new AwemRData();
        b.url = url;

        b.aweme = b.url.substring(url.lastIndexOf("/")+1);
        if (b.aweme.length() >= 12)
            b.aweme = b.aweme.substring(0, 11);
        b.r = State.getState(b.aweme);

        String x;
        String z;

        if (b.r == null){
            Toast.makeText(getContext(), "PPPPPP", Toast.LENGTH_SHORT).show();
//            Toast.makeText(getContext(), R.string.provide_Valid_link, Toast.LENGTH_SHORT).show();
            checkingPan.setVisibility(View.GONE);
            return;
        }

        if (b.r.length() > 3) {
            x = b.r.charAt(0) +""+ b.r.charAt(b.r.length() - 1);
            z = b.r.substring(0,3);
        } else {
            x = "IB";
            z = "ZIK";
        }

        b.x = State.getState(x+Utils.abo);
        b.z = State.getState(z+Utils.abo);
        b.r = State.getState(b.aweme);

//
//        Log.d("DDDDD x", x+Utils.abo);
//        Log.d("DDDDD z", z+Utils.abo);
//        Log.d("DDDDD r", b.aweme);

        APIService
                .getInstance()
                .getDataService()
                .getVideoData(b)
                .enqueue(new Callback<Aweme>() {
                    @Override
                    public void onResponse(@NonNull Call<Aweme> call, @NonNull Response<Aweme> response) {
                        if (getContext() == null) return;

                        checkingPan.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null){
                            aweme = response.body();
                            aweme.setTiktokUrl(url);

                            VideoSheet d = VideoSheet.newInstance(select -> {
                                if (select == VideoSheet.MP3
                                || select == VideoSheet.NWM
                                || select == VideoSheet.WM)
                                    start(aweme, select);
                            }, aweme);
                            d.show(getParentFragmentManager(), d.getTag());

                        } else {
                            Toast.makeText(getContext(),getContext().getString(R.string.unknown_error), Toast.LENGTH_SHORT).show();
                            url = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Aweme> call, @NonNull Throwable t) {
                        if (getContext() == null) return;

                        checkingPan.setVisibility(View.GONE);

                        Toast toast = Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_SHORT);
                        toast.show();
                        url = null;
                    }
                });

    }


    private void downloadedMediaView(Aweme data){

        if (getContext() == null || getView() == null) return;

        completed.setVisibility(View.VISIBLE);
        playIc.setVisibility(View.VISIBLE);

        downloadCard.setOnClickListener(v -> {
            Intent intent;

            if (data.isMusic())
            intent = new Intent(getContext(), MusicActivity.class);
            else
            intent = new Intent(getContext(), VideoActivity.class);

            intent.putExtra(MyIntents.DATA, data);
            startActivity(intent);
        });

    }


    private void start(Aweme data, int selection){
        data.setTiktokUrl(url);
        startDownload(data, selection);
    }


    ProgressBar progressBar;
    TextView completed;
    ImageView playIc;
    private void startDownload(Aweme data, int selection){

        if (getContext() == null || getView() == null || downloading) return;

        ImageView video_image = getView().findViewById(R.id.video_image);
        TextView name = getView().findViewById(R.id.username);

        name.setText(data.getUsername());

        Glide
                .with(getContext())
                .load(data.getCoverAnim())
                .into(video_image);

        downloadCard.setVisibility(View.VISIBLE);
        downloadCard.setOnClickListener(null);

        downloadTask = new DownloadTask(this, data, selection);
        downloadTask.execute("");
    }

    @Override
    public void onDestroy() {
        if (downloadTask != null && !downloadTask.isCancelled()){
            downloadTask.cancel(true);
        }
        if (unifiedNativeAd != null){
            unifiedNativeAd.destroy();
            unifiedNativeAd = null;
        }
        super.onDestroy();
    }


    private void openTickTokDialog(){
        if (getContext() == null) return;
        AlertDialog dialogg = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme)
                .setTitle(Utils.fromHtml("<font color='#FFFFFF'>"+getContext().getString(R.string.open_tiktok_header)+"</font>"))
                .setMessage(Utils.fromHtml("<font color='#FFFFFF'>"+getContext().getString(R.string.open_tiktok_message)+"</font>"))
                .setPositiveButton(R.string.open_ttk, (dialog, which) -> {
                    try {

                        Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(
                                "com.zhiliaoapp.musically"
                        );
                        startActivity(intent);
                    } catch (Exception e){
                        try {

                            Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(
                                    "com.zhiliaoapp.musically.go"
                            );
                            startActivity(intent);
                        } catch (Exception ee){
                            Toast.makeText(getContext(), getString(R.string.tiktok_not_installed), Toast.LENGTH_SHORT).show();
                        }
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
    }


    private void setUpdateProgress(int progress){
        progressBar.setProgress(progress);
    }

    private static class DownloadTask extends AsyncTask<String, Integer, String> {

        private Aweme data;
        private int type;
//        private Map<String, String> headers;
        private WeakReference<VideoDownloadFragment> ref;


        public DownloadTask(VideoDownloadFragment context, Aweme data, int type) {
            this.ref = new WeakReference<>(context);
            this.data = data;
            this.type = type;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {

                URL url = new URL(data.getDownloadUrl(type));
                connection = (HttpURLConnection) url.openConnection();

//                for (Map.Entry<String, String> entry : headers.entrySet()) {
//                    connection.setRequestProperty(entry.getKey(), entry.getValue());
//                }

                connection.connect();


                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "NO";
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();


                Activity context = ref.get().getActivity();

                if (context == null) return "";
                if (context.isFinishing()) return "";

//                String path = Environment.getExternalStorageDirectory().toString();
//                String path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                String name = data.getUsername()+"_"+System.currentTimeMillis()+data.getDownloadExt(type);

                data.setLocalPath(path+"/"+name);


                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(data.getLocalPath());

                byte[] d = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(d)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(d, 0, count);
                }
                new SingleMediaScanner(context, new File(data.getLocalPath()));
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download

            Activity context = ref.get().getActivity();

            if (context == null) return;
            if (context.isFinishing()) return;

            ref.get().downloading = true;

            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


            ref.get().progressBar.setProgress(0);

            ref.get().progressBar.setVisibility(View.VISIBLE);
            ref.get().playIc.setVisibility(View.GONE);

            AdsUtils.loadAdd(context);
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);

            Activity context = ref.get().getActivity();

            if (context == null) return;
            if (context.isFinishing()) return;

            ref.get().setUpdateProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {

            Activity context = ref.get().getActivity();

            if (context == null) return;
            if (context.isFinishing()) return;

            ref.get().downloading = false;

            context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            if (result != null) {
                if (result.equals("NO")){
                    Toast.makeText(context, context.getString(R.string.unknown_error), Toast.LENGTH_SHORT).show();
                } else {

                    Toast toast = Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_LONG);
//                    Utils.customizeToast(toast);
                    toast.show();
                    ref.get().progressBar.setProgress(0);
                    ref.get().progressBar.setVisibility(View.GONE);
                }
            } else {
                Toast toast = Toast.makeText(context, R.string.downloaded, Toast.LENGTH_SHORT);
                toast.show();
                Repository.getInstance(context.getApplicationContext()).add(data);
                ref.get().downloadedMediaView(data);
                AdsUtils.showAdd(context);
            }
        }
    }


    private void callNativeAds(){

        if (getContext() == null) return;

        if (lastAdShowTime > 0) {
            long lap = System.currentTimeMillis() - lastAdShowTime;
            if (lap <= adLaps)
                return;
        }

        if (adLoader != null && adLoader.isLoading())
            return;

        adLoader = new AdLoader.Builder(getContext(), getString(R.string.NATIVE_2))
                .forNativeAd(ads -> {

                    if (this.unifiedNativeAd != null) {
                        this.unifiedNativeAd.destroy();
                        this.unifiedNativeAd = ads;
                        new Handler().postDelayed(() -> {
                            if (getContext() == null) return;
                            adFrame.setVisibility(View.VISIBLE);
                            adFrame.removeAllViews();
                            adFrame.addView(adFrameLoader);
                            adFrameLoaderTV.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.alarm));
                            new Handler().postDelayed(() -> {
                                if (getContext() == null) return;
                                displayNativeAd(unifiedNativeAd);
                            }, 3000);
                        }, 5000);
                    } else {
                        this.unifiedNativeAd = ads;
                        adFrame.setVisibility(View.VISIBLE);
                        adFrame.removeAllViews();
                        adFrame.addView(adFrameLoader);
                        adFrameLoaderTV.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.alarm));
                        new Handler().postDelayed(() -> {
                            if (getContext() == null) return;
                            displayNativeAd(unifiedNativeAd);
                        }, 3000);
                    }
                })
                .withNativeAdOptions(new com.google.android.gms.ads.nativead.NativeAdOptions.Builder()
                        .build())
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }


    private void displayNativeAd(NativeAd ad){

        if (getContext() == null || getView() == null || ad == null) return;

        adFrame.setVisibility(View.VISIBLE);

        // Inflate a layout and add it to the parent ViewGroup.
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        NativeAdView adView = (NativeAdView) inflater
                .inflate(R.layout.ad_layout, null);

        adFrame.removeAllViews();
        adFrame.addView(adView);

        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_icon));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));

        mediaView.setMediaContent(ad.getMediaContent());
        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
        ((TextView)adView.getHeadlineView()).setText(ad.getHeadline());

        if (ad.getHeadline() == null){
            adView.getHeadlineView().setVisibility(View.GONE);
        } else {
            adView.getHeadlineView().setVisibility(View.VISIBLE);
            ((TextView)adView.getHeadlineView()).setText(ad.getHeadline());
        }

        if (ad.getCallToAction() == null){
            adView.getCallToActionView().setVisibility(View.GONE);
        } else {
            ((TextView)adView.getCallToActionView()).setText(ad.getCallToAction());
        }

        if (ad.getIcon() != null){
            ((ImageView)adView.getIconView()).setImageDrawable(ad.getIcon().getDrawable());
        }

        if (ad.getBody() == null){
            adView.getBodyView().setVisibility(View.GONE);
        } else {
            ((TextView)adView.getBodyView()).setText(ad.getBody());
        }

        if (ad.getStore() == null){
            adView.getStoreView().setVisibility(View.GONE);
        } else {
            ((TextView)adView.getStoreView()).setText(ad.getStore());
        }

        if (ad.getPrice() == null){
            adView.getPriceView().setVisibility(View.GONE);
        } else {
            ((TextView)adView.getPriceView()).setText(ad.getPrice());
        }

        if (ad.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(ad.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (ad.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else if (ad.getAdvertiser() != null && ad.getStarRating() == null){
            ((TextView) adView.getAdvertiserView()).setText(ad.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(ad);
        lastAdShowTime = System.currentTimeMillis();
    }




}
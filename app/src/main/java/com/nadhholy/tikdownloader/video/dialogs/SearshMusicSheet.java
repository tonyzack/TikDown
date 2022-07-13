package com.nadhholy.tikdownloader.video.dialogs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.nadhholy.tikdownloader.video.R;


public class SearshMusicSheet extends BottomSheetDialogFragment {

    private String q;

    public static SearshMusicSheet newInstance(String q){
        return new SearshMusicSheet(q);
    }

    public SearshMusicSheet(String q) {
        this.q = q;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sheet_find_music, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View google = view.findViewById(R.id.google);
        View youtube = view.findViewById(R.id.youtube);
        View youtubeMusic = view.findViewById(R.id.youtube_music);
        View spotify = view.findViewById(R.id.spotify);
        View dezeer = view.findViewById(R.id.dezeer);
        View amazon = view.findViewById(R.id.last);


        google.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q="+q)));
            } catch (Exception e){
                e.printStackTrace();
            }
            dismiss();
        });

        youtube.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query="+q)));
            } catch (Exception e){
                e.printStackTrace();
            }
            dismiss();
        });

        youtubeMusic.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://music.youtube.com/search?q="+q)));
            } catch (Exception e){
                e.printStackTrace();
            }
            dismiss();
        });

        spotify.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.spotify.com/search/"+q)));
            } catch (Exception e){
                e.printStackTrace();
            }
            dismiss();
        });

        dezeer.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://preprod.deezer.com/search/"+q)));
            } catch (Exception e){
                e.printStackTrace();
            }
            dismiss();
        });


        amazon.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://music.amazon.com/search/"+q)));
            } catch (Exception e){
                e.printStackTrace();
            }
            dismiss();
        });



    }


}

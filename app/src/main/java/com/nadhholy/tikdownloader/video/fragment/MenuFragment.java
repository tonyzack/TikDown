package com.nadhholy.tikdownloader.video.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.nadhholy.tikdownloader.video.R;
import com.nadhholy.tikdownloader.video.activities.HelpActivity;
import com.nadhholy.tikdownloader.video.activities.InfoActivity;
import com.nadhholy.tikdownloader.video.dialogs.FeedbackDialog;
import com.nadhholy.tikdownloader.video.dialogs.RateTfDialog;
import com.nadhholy.tikdownloader.video.dialogs.RateUsDialog;



public class MenuFragment extends Fragment {


    public MenuFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getView() == null || getContext() == null
        || getActivity() == null)
            return;

        ImageView img = getView().findViewById(R.id.image);
        View imgCard = getView().findViewById(R.id.image_card);

        Glide
                .with(getContext())
                .load(R.drawable.present)
                .into(img);

//        final Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.hv2);
//        animation.setRepeatCount(Animation.INFINITE);
//
//        imgCard.startAnimation(animation);

//        getView().findViewById(R.id.buy).setOnClickListener(v -> {
//            startActivity(new Intent(getContext(), ProductActivity.class));
//        });

        imgCard.setOnClickListener(v -> {
            new RateTfDialog(getActivity()).show();
        });

        getView().findViewById(R.id.help).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), HelpActivity.class));
        });

        getView().findViewById(R.id.feedback).setOnClickListener(v -> {
            new FeedbackDialog(getContext()).show();
        });

        getView().findViewById(R.id.rate).setOnClickListener(v -> {
            new RateUsDialog(getActivity(), false).show();
        });

        getView().findViewById(R.id.share).setOnClickListener(v -> {

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id="+getContext().getPackageName());
            startActivity(shareIntent);
        });

        getView().findViewById(R.id.info).setOnClickListener(v -> {
            startActivity(new Intent(getContext(), InfoActivity.class));
        });

        getView().findViewById(R.id.privacy_policy).setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://nadhholy.me/privacy_policy")));
            } catch (Exception e){
                e.printStackTrace();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

}

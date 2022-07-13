package com.nadhholy.tikdownloader.video.adapters;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nadhholy.tikdownloader.video.MyIntents;
import com.nadhholy.tikdownloader.video.R;
import com.nadhholy.tikdownloader.video.activities.MusicActivity;
import com.nadhholy.tikdownloader.video.activities.VideoActivity;
import com.nadhholy.tikdownloader.video.models.Aweme;
import com.nadhholy.tikdownloader.video.utils.Utils;

import java.util.List;

public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.MyViewHolder> {


    private Context context;
    private List<Aweme> data;

    private OnMenuClickListener onMenuClickListener;


    public SavedAdapter(Context fragment, List<Aweme> data) {
        this.context = fragment;
        this.data = data;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_mediad_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {

        final Aweme d = data.get(i);

        holder.username.setText(Utils.fromHtml(d.getUsername()));
        holder.desc.setText(Utils.fromHtml(d.getTitle()));

        holder.type.setText(d.getFiletype());

        if (d.isMusic()){
            Glide
                    .with(context)
                    .load(d.getMusicCover())
                    .placeholder(R.color.dark_accent2)
                    .timeout(Integer.MAX_VALUE)
                    .into(holder.videoImage);

            holder.container.setOnClickListener(v -> {
                Intent intent = new Intent(context, MusicActivity.class);
                intent.putExtra(MyIntents.DATA, d);
                context.startActivity(intent);
            });
        } else {
            Glide
                    .with(context)
                    .load(d.getCoverAnim())
                    .placeholder(R.color.dark_accent2)
                    .timeout(Integer.MAX_VALUE)
                    .into(holder.videoImage);

            holder.container.setOnClickListener(v -> {
                Intent intent = new Intent(context, VideoActivity.class);
                intent.putExtra(MyIntents.DATA, d);
                context.startActivity(intent);
            });
        }

        Glide
                .with(context)
                .load(d.getAvatar())
                .placeholder(R.color.dark_accent2)
                .timeout(Integer.MAX_VALUE)
                .into(holder.pic);

        holder.menu.setOnClickListener(v -> {
            if (onMenuClickListener != null)
                onMenuClickListener.onClick(d);
        });

    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView videoImage, pic;
        TextView username, desc, type;
        View container, menu;
        private MyViewHolder(@NonNull View itemView) {
            super(itemView);
            videoImage = itemView.findViewById(R.id.video_image);
            pic = itemView.findViewById(R.id.pic);
            username = itemView.findViewById(R.id.username);
            type = itemView.findViewById(R.id.type);
            desc = itemView.findViewById(R.id.desc);
            menu = itemView.findViewById(R.id.more);
            container = itemView.findViewById(R.id.download_card);
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public int getDataItemCount() {
        return getData().size();
    }

    public void remove(Aweme file){

        if (file == null) return;

        int index = data.indexOf(file);

        if (index < 0) return;

        data.remove(index);
        notifyItemRemoved(index);

        new Handler().postDelayed(() -> {
            if (context != null)
                notifyDataSetChanged();
        }, 1000);
    }

    public void remove(String file){

        int index = -1;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) != null) {
                if (data.get(i).getLocalPath().equals(file)) {
                    index = i;
                    break;
                }
            }
        }

        if (index < 0) return;

        data.remove(index);
        notifyItemRemoved(index);
        new Handler().postDelayed(() -> {
            if (context != null)
            notifyDataSetChanged();
        }, 1000);
    }

    public void remove2(String file){

        int index = -1;

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) != null) {
                if (data.get(i).getLocalPath().equals(file)) {
                    index = i;
                    break;
                }
            }
        }

        if (index < 0) return;

        data.remove(index);
        notifyDataSetChanged();

    }

    public List<Aweme> getData() {
        return data;
    }


    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
    }

    public interface OnMenuClickListener {
        void onClick(Aweme data);
    }

}

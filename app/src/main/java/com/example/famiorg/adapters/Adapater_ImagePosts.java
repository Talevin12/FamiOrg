package com.example.famiorg.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.famiorg.R;
import com.example.famiorg.assets.ImageUtils;
import com.example.famiorg.logic.ImagePost;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class Adapater_ImagePosts extends RecyclerView.Adapter<Adapater_ImagePosts.ImagePostViewHolder> {

    private Context context;
    private ArrayList<ImagePost> imagePosts;

    public Adapater_ImagePosts(Context context, ArrayList<ImagePost> imagePosts) {
        this.context = context;
        this.imagePosts = imagePosts;

        imagePosts.sort(Comparator.comparing(ImagePost::getPublishDate));
        for(int i = 0; i < imagePosts.size(); i++) { //Display only images from last month
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.MONTH, -1);
            Date dt = c.getTime();
            if(imagePosts.get(i).getPublishDate().before(dt)) {
                imagePosts.remove(i--);
            }
        }
    }

    @NonNull
    @Override
    public ImagePostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_image_post, parent, false);
        Adapater_ImagePosts.ImagePostViewHolder myImagePostViewHolder = new Adapater_ImagePosts.ImagePostViewHolder(view);
        return myImagePostViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImagePostViewHolder holder, int position) {
        ImagePost imagePost = imagePosts.get(position);

        holder.imagepost_IMG_icon.setImageResource(imagePost.getIcon());
        holder.imagepost_LBL_name.setText(imagePost.getUserName());
        holder.imagepost_LBL_date.setText(new SimpleDateFormat("dd-MM-yyyy").format(imagePost.getPublishDate()));

        ImageUtils.getInstance().loadImageForPost(context, imagePost.getImageLink(), holder.imagepost_IMG_image);

        holder.imagepost_LBL_description.setText(imagePost.getDescription());
    }

    @Override
    public int getItemCount() {
        return imagePosts == null ? 0 : imagePosts.size();
    }

    class ImagePostViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView imagepost_IMG_icon;
        private MaterialTextView imagepost_LBL_name;
        private MaterialTextView imagepost_LBL_date;
        private AppCompatImageView imagepost_IMG_image;
        private MaterialTextView imagepost_LBL_description;


        public ImagePostViewHolder(@NonNull View itemView) {
            super(itemView);

            imagepost_IMG_icon = itemView.findViewById(R.id.imagepost_IMG_icon);
            imagepost_LBL_name = itemView.findViewById(R.id.imagepost_LBL_name);
            imagepost_LBL_date = itemView.findViewById(R.id.imagepost_LBL_date);
            imagepost_IMG_image = itemView.findViewById(R.id.imagepost_IMG_image);
            imagepost_LBL_description = itemView.findViewById(R.id.imagepost_LBL_description);

            imagepost_IMG_icon.setOnClickListener(v -> ImageUtils.getInstance().rotateIcon((AppCompatImageView) v));
        }
    }
}

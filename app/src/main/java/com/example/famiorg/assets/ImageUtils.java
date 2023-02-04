package com.example.famiorg.assets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.famiorg.R;

import jp.wasabeef.blurry.Blurry;

public class ImageUtils {
    private static ImageUtils instance;

    public static ImageUtils getInstance() {
        return instance;
    }

    public static void initHelper() {
        if (instance == null)
            instance = new ImageUtils();
    }


    public void load(Context context, String link, ImageView imageView) {
        Glide.with(context)
                .load(link)
                .centerCrop()
                .into(imageView);
    }

    public void loadImageForPost(Context context, String link, ImageView imageView) {
        Glide.with(context)
                .load(link)
                .placeholder(R.color.white)
                .apply(new RequestOptions().override(720, 720))
                .into(imageView);
    }

    public void loadBlurry(Context context, Resources res, int drawable, ImageView imageView, int sampling) {
        Blurry.with(context)
                .sampling(sampling)
                .async()
                .from(BitmapFactory.decodeResource(res, drawable))
                .into(imageView);
    }

    public void rotateIcon(ImageView icon) {
        RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.ABSOLUTE);
        anim.setDuration(700);

        icon.startAnimation(anim);
    }
}

package com.looky.lookyapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;


/**
 * Created by kangjaeyun on 2017. 6. 12..
 */

public class LikeItemView extends LinearLayout{

    ImageView imageView;
    Context context;

    public LikeItemView(Context context) {
        super(context);
        init(context);
    }

    public LikeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public void init(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.like_item, this, true);

        imageView = (ImageView) findViewById(R.id.imageView);

    }

    public void setImage(String resId) {
        Glide.with(context).load("https://s3.ap-northeast-2.amazonaws.com/looky/" + String.format("%09d", Integer.parseInt(resId)) + ".jpg").into(imageView);
    }

}

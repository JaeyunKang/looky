package com.looky.lookyapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;



/**
 * Created by kangjaeyun on 2017. 6. 12..
 */

public class ChartItemView extends LinearLayout{

    ImageView imageView;
    TextView rankView;
    TextView nameView;
    TextView priceView;
    Context context;


    public ChartItemView(Context context) {
        super(context);
        init(context);
    }

    public ChartItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public void init(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.chart_item, this, true);

        imageView = (ImageView) findViewById(R.id.imageView);
        rankView = (TextView) findViewById(R.id.rankView);
        nameView = (TextView) findViewById(R.id.nameView);
        priceView = (TextView) findViewById(R.id.priceView);

    }

    public void setImage(String resId) {

        Glide.with(context).load("https://s3.ap-northeast-2.amazonaws.com/looky/" + String.format("%09d", Integer.parseInt(resId)) + ".jpg").into(imageView);

    }

    public void setRank(int rank) {
        rankView.setText(String.valueOf(rank));
    }

    public void setName(String name) {
        nameView.setText(name);
    }

    public void setPrice(String price) {
        priceView.setText(price);
    }

}

package com.looky.lookyapp;

/**
 * Created by kangjaeyun on 2017. 6. 12..
 */

public class ChartItem {

    String resId;
    String name;
    String price;
    String link;
    String like;

    public ChartItem(String resId, String name, String price, String link, String like)
    {
        this.resId = resId;
        this.name = name;
        this.price = price;
        this.link = link;
        this.like = like;
    }

    public String getResId() { return this.resId; }

    public String getName() { return this.name; }

    public String getPrice() { return this.price; }

    public String getLink() { return this.link; }

    public String getLike() { return this.like; }


}

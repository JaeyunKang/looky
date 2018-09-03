package com.looky.lookyapp;

/**
 * Created by kangjaeyun on 2017. 6. 12..
 */

public class LikeItem {

    String resId;
    String name;
    String link;

    public LikeItem(String resId, String name, String link)
    {
        this.resId = resId;
        this.name = name;
        this.link = link;
    }

    public String getResId() { return this.resId; }
    public String getName() { return this.name; }
    public String getLink() { return this.link; }
}

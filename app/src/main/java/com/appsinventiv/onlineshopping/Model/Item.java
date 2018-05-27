package com.appsinventiv.onlineshopping.Model;

/**
 * Created by maliahmed on 15/12/2017.
 */

public class Item {

    String title;
    String desc;

    public Item() {
    }

    public Item(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

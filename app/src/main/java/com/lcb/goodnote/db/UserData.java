package com.lcb.goodnote.db;

import android.graphics.Bitmap;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;


public class UserData extends LitePalSupport {
    private int id;

    @Column(unique = true, defaultValue = "unknown")
    private String user_name;//用户名
    private String pass_word;//密码
    private byte[] head_image;//头像
    private List<ActivityData> activities = new ArrayList<ActivityData>();//活动列表

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPass_word() {
        return pass_word;
    }

    public void setPass_word(String pass_word) {
        this.pass_word = pass_word;
    }


    public byte[] getHead_image() {
        return head_image;
    }

    public void setHead_image(byte[] head_image) {
        this.head_image = head_image;
    }

    public List<ActivityData> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityData> activities) {
        this.activities = activities;
    }
}

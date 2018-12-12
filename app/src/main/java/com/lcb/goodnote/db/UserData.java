package com.lcb.goodnote.db;

import android.graphics.Bitmap;

import org.litepal.crud.DataSupport;

public class UserData extends DataSupport {
    private int id;
    private String user_name;//用户名
    private String pass_word;//密码
    private Bitmap head_image;//头像

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

    public Bitmap getHead_image() {
        return head_image;
    }

    public void setHead_image(Bitmap head_image) {
        this.head_image = head_image;
    }
}

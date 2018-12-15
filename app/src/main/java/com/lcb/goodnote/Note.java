package com.lcb.goodnote;

import android.app.Activity;

import com.lcb.goodnote.db.ActivityData;
import com.lcb.goodnote.db.UserData;

import org.litepal.LitePal;

import java.util.List;

/**
 * Created by q6412 on 2018/12/6.
 */

public class Note {
    private String name;
    private String date;
    private int nodeId = R.drawable.note_image_view;

    private int id =1 ;//id从1开始自增
    private String activity_theme;//活动主题
    private String activity_content;//活动内容
    private String activity_address;//活动地点
    private int activity_year;//年
    private int activity_month;//月 控件中获取的是0-11，此处保存为1-12
    private int activity_day;//天

    public Note(String name,String date){//int id,String username
        this.name = name;
        this.date = date;
//        List<UserData> users = LitePal.where("user_name like ?",username).find(UserData.class);
//        ActivityData activity = users.get(0).getActivities().get(id);
//        this.activity_theme = activity.getActivity_theme();
//        this.activity_address = activity.getActivity_address();
//        this.activity_content = activity.getActivity_content();
//        this.activity_year = activity.getActivity_year();
//        this.activity_month = activity.getActivity_month();
//        this.activity_day = activity.getActivity_day();
    }

    public Note(String name,String date,ActivityData activity){
        this.id++;
        this.name = name;
        this.date = date;
        this.activity_theme = activity.getActivity_theme();
        this.activity_address = activity.getActivity_address();
        this.activity_content = activity.getActivity_content();
        this.activity_year = activity.getActivity_year();
        this.activity_month = activity.getActivity_month();
        this.activity_day = activity.getActivity_day();
    }
    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivity_theme() {
        return activity_theme;
    }

    public void setActivity_theme(String activity_theme) {
        this.activity_theme = activity_theme;
    }

    public String getActivity_content() {
        return activity_content;
    }

    public void setActivity_content(String activity_content) {
        this.activity_content = activity_content;
    }

    public String getActivity_address() {
        return activity_address;
    }

    public void setActivity_address(String activity_address) {
        this.activity_address = activity_address;
    }

    public int getActivity_year() {
        return activity_year;
    }

    public void setActivity_year(int activity_year) {
        this.activity_year = activity_year;
    }

    public int getActivity_month() {
        return activity_month;
    }

    public void setActivity_month(int activity_month) {
        this.activity_month = activity_month;
    }

    public int getActivity_day() {
        return activity_day;
    }

    public void setActivity_day(int activity_day) {
        this.activity_day = activity_day;
    }
}

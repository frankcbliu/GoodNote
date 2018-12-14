package com.lcb.goodnote.db;


import org.litepal.crud.LitePalSupport;


public class ActivityData extends LitePalSupport {

    private int id;
    private String activity_theme;//活动主题
    private String activity_content;//活动内容
    private String activity_address;//活动地点
    private String activity_year;//年
    private String activity_month;//月 控件中获取的是0-11，此处保存为1-12
    private String activity_day;//天

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivity_content() {
        return activity_content;
    }

    public void setActivity_content(String activity_content) {
        this.activity_content = activity_content;
    }

    public String getActivity_theme() {
        return activity_theme;
    }

    public void setActivity_theme(String activity_theme) {
        this.activity_theme = activity_theme;
    }

    public String getActivity_address() {
        return activity_address;
    }

    public void setActivity_address(String activity_address) {
        this.activity_address = activity_address;
    }

    public String getActivity_year() {
        return activity_year;
    }

    public void setActivity_year(String activity_year) {
        this.activity_year = activity_year;
    }

    public String getActivity_month() {
        return activity_month;
    }

    public void setActivity_month(String activity_month) {
        this.activity_month = activity_month;
    }

    public String getActivity_day() {
        return activity_day;
    }

    public void setActivity_day(String activity_day) {
        this.activity_day = activity_day;
    }

    @Override
    public String toString() {
        return "ActivityData{" +
                "activity_theme='" + activity_theme + '\'' +
                ", activity_address='" + activity_address + '\'' +
                ", activity_year='" + activity_year + '\'' +
                ", activity_month='" + activity_month + '\'' +
                ", activity_day='" + activity_day + '\'' +
                '}';
    }
}

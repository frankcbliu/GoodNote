package com.lcb.goodnote.db;

import org.litepal.crud.DataSupport;

public class ActivityData extends DataSupport {
    private int id;
    private String activity_theme;
    private String activity_address;
    private String activity_date;

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

    public String getActivity_address() {
        return activity_address;
    }

    public void setActivity_address(String activity_address) {
        this.activity_address = activity_address;
    }

    public String getActivity_date() {
        return activity_date;
    }

    public void setActivity_date(String activity_date) {
        this.activity_date = activity_date;
    }
}

package com.lcb.goodnote;

/**
 * Created by q6412 on 2018/12/6.
 */

public class Note {
    private String name;
    private String date;
    private int nodeId = R.drawable.note_image_view;
    public Note(String name,String date){
        this.name = name;
        this.date = date;
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
}

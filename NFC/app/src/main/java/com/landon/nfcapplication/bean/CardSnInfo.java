package com.landon.nfcapplication.bean;

public class CardSnInfo {
    private int id;

    private String sn;

    private String readTime;

    public CardSnInfo(int id, String sn, String readTime) {
        this.id = id;
        this.sn = sn;
        this.readTime = readTime;
    }

    public int getId() {
        return id;
    }

    public String getSn() {
        return sn;
    }

    public String getReadTime() {
        return readTime;
    }
}

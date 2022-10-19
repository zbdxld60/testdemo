package com.landon.nfcapplication.bean;

import org.litepal.crud.DataSupport;

public class NFCSnInfomation extends DataSupport {
    private int id;
    private String sn;
    private String readTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }
}

package com.eup.screentranslate.model;

public class Example {
    private int id;
    private String content;
    private String mean;
    private String trans;
    private String converted;

    public Example(int id, String content, String mean, String trans, String converted) {
        this.id = id;
        this.content = content;
        this.mean = mean;
        this.trans = trans;
        this.converted = converted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

    public String getConverted() {
        return converted;
    }

    public void setConverted(String converted) {
        this.converted = converted;
    }
}

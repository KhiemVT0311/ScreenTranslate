package com.eup.screentranslate.model;

import java.util.ArrayList;

public class Mean {
    private String kind;
    private String field;
    private String mean;

    private String mean_GG;
    private ArrayList<Integer> examples;
    private ArrayList<Example> exampleList;
    private boolean check;

    public Mean(String kind, String field, String mean, String mean_GG, ArrayList<Integer> examples, ArrayList<Example> exampleList, boolean check) {
        this.kind = kind;
        this.field = field;
        this.mean = mean;
        this.mean_GG = mean_GG;
        this.examples = examples;
        this.exampleList = exampleList;
        this.check = check;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public String getMean_GG() {
        return mean_GG;
    }

    public void setMean_GG(String mean_GG) {
        this.mean_GG = mean_GG;
    }

    public ArrayList<Integer> getExamples() {
        return examples;
    }

    public void setExamples(ArrayList<Integer> examples) {
        this.examples = examples;
    }

    public ArrayList<Example> getExampleList() {
        return exampleList;
    }

    public void setExampleList(ArrayList<Example> exampleList) {
        this.exampleList = exampleList;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}

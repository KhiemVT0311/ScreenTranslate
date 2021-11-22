package com.eup.screentranslate.model;

import java.util.ArrayList;
import java.util.List;

public class Word {
    private int id;
    private String word;
    private String wordBaseForm;
    private String mean;
    private String phonetic;
    private String not;
    private int favorite;
    private String oppositeWord;
    private String synsets;
    private String hanViet;
    private boolean isJaVi;
    private RESULT_TYPE type = RESULT_TYPE.RELATIVE;
    private ListContributeResponse contribute;
    private List<VerbObj> verbObjs;
    private ArrayList<Mean> means;
    private String converted;



    public Word(int id, String word, String wordBaseForm, String mean, String phonetic, String not, int favorite, String oppositeWord, String synsets, String hanViet, boolean isJaVi, RESULT_TYPE type, ListContributeResponse contribute, List<VerbObj> verbObjs, ArrayList<Mean> means, String converted) {
        this.id = id;
        this.word = word;
        this.wordBaseForm = wordBaseForm;
        this.mean = mean;
        this.phonetic = phonetic;
        this.not = not;
        this.favorite = favorite;
        this.oppositeWord = oppositeWord;
        this.synsets = synsets;
        this.hanViet = hanViet;
        this.isJaVi = isJaVi;
        this.type = type;
        this.contribute = contribute;
        this.verbObjs = verbObjs;
        this.means = means;
        this.converted = converted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWordBaseForm() {
        return wordBaseForm;
    }

    public void setWordBaseForm(String wordBaseForm) {
        this.wordBaseForm = wordBaseForm;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getNot() {
        return not;
    }

    public void setNot(String not) {
        this.not = not;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public String getOppositeWord() {
        return oppositeWord;
    }

    public void setOppositeWord(String oppositeWord) {
        this.oppositeWord = oppositeWord;
    }

    public String getSynsets() {
        return synsets;
    }

    public void setSynsets(String synsets) {
        this.synsets = synsets;
    }

    public String getHanViet() {
        return hanViet;
    }

    public void setHanViet(String hanViet) {
        this.hanViet = hanViet;
    }

    public boolean isJaVi() {
        return isJaVi;
    }

    public void setJaVi(boolean jaVi) {
        isJaVi = jaVi;
    }

    public RESULT_TYPE getType() {
        return type;
    }

    public void setType(RESULT_TYPE type) {
        this.type = type;
    }

    public ListContributeResponse getContribute() {
        return contribute;
    }

    public void setContribute(ListContributeResponse contribute) {
        this.contribute = contribute;
    }

    public List<VerbObj> getVerbObjs() {
        return verbObjs;
    }

    public void setVerbObjs(List<VerbObj> verbObjs) {
        this.verbObjs = verbObjs;
    }

    public ArrayList<Mean> getMeans() {
        return means;
    }

    public void setMeans(ArrayList<Mean> means) {
        this.means = means;
    }

    public String getConverted() {
        return converted;
    }

    public void setConverted(String converted) {
        this.converted = converted;
    }
}

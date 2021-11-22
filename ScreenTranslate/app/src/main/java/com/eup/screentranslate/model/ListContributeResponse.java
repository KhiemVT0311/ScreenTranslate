package com.eup.screentranslate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListContributeResponse {
    @SerializedName("status")
    @Expose
    public int status;

    @SerializedName("result")
    @Expose
    public ArrayList<Result> results;

    public String getMyContribute(int userId){
        String myContribute = null;
        if (results != null && userId > 0 && !results.isEmpty()){
            for (int i=0;i<results.size();i++){
                if (results.get(i).getUserId() == userId){
                    myContribute = results.get(i).mean;
                    break;
                }
            }
        }
        return myContribute;
    }

    class Result{
        @SerializedName("action")
        @Expose
        int action;

        @SerializedName("dislike")
        @Expose
        int dislike;

        @SerializedName("like")
        @Expose
        int like;

        @SerializedName("mean")
        @Expose
        String mean;

        @SerializedName("reportId")
        @Expose
        int reportId;

        @SerializedName("status")
        @Expose
        int status;

        @SerializedName("type")
        @Expose
        int type;

        @SerializedName("userId")
        @Expose
        int userId;

        @SerializedName("username")
        @Expose
        String username;

        @SerializedName("word")
        @Expose
        String word;

        @SerializedName("wordId")
        @Expose
        String wordId;

        @SerializedName("dict")
        @Expose
        String dict;

        @SerializedName("id")
        @Expose
        int id;

        @SerializedName("user")
        @Expose
        Account.Result user;

        public Result(int action, int dislike, int like, String mean, int reportId, int status, int type, int userId, String username, String word, String wordId, String dict, Account.Result user) {
            this.action = action;
            this.dislike = dislike;
            this.like = like;
            this.mean = mean;
            this.reportId = reportId;
            this.status = status;
            this.type = type;
            this.userId = userId;
            this.username = username;
            this.word = word;
            this.wordId = wordId;
            this.dict = dict;
            this.user = user;
        }

        public int getAction() {
            return action;
        }

        public void setAction(int action) {
            this.action = action;
        }

        public int getDislike() {
            return dislike;
        }

        public void setDislike(int dislike) {
            this.dislike = dislike;
        }

        public int getLike() {
            return like;
        }

        public void setLike(int like) {
            this.like = like;
        }

        public String getMean() {
            return mean;
        }

        public void setMean(String mean) {
            this.mean = mean;
        }

        public int getReportId() {
            return reportId;
        }

        public void setReportId(int reportId) {
            this.reportId = reportId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getWordId() {
            return wordId;
        }

        public void setWordId(String wordId) {
            this.wordId = wordId;
        }

        public String getDict() {
            return dict;
        }

        public void setDict(String dict) {
            this.dict = dict;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Account.Result getUser() {
            return user;
        }

        public void setUser(Account.Result user) {
            this.user = user;
        }
    }
}

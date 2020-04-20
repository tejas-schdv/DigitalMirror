package com.example.digitalmirror.model.children;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("subreddit")
    @Expose
    private String subreddit;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    @Override
    public String toString() {
        return "Data{" +
                "title='" + title + '\'' +
                ", subreddit='" + subreddit + '\'' +
                '}';
    }
}

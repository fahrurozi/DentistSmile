package com.gemastik.dentistsmile.data.model.article;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseArticle {
    @SerializedName("articles")
    private List<DataArticle> articles;

    public List<DataArticle> getArticles() {
        return articles;
    }

    public void setArticles(List<DataArticle> articles) {
        this.articles = articles;
    }
}

package com.example.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ahmed Wafik Mohamed on 1/21/2018.
 */

public class ReviewsResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("page")
    private int page;

    @SerializedName("total_Pages")
    private int total_pages;

    @SerializedName("total_results")
    private int total_results;

    @SerializedName("results")
    private List<Reviews> results;

    public ReviewsResponse(int id, int page, int total_pages, int total_results, List<Reviews> results) {
        this.id = id;
        this.page = page;
        this.total_pages = total_pages;
        this.total_results = total_results;
        this.results = results;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public List<Reviews> getResults() {
        return results;
    }

    public void setResults(List<Reviews> results) {
        this.results = results;
    }
}

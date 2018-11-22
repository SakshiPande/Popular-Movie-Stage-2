package com.example.popularmoviesstage2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailersBaseResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<TrailersResponse> results = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<TrailersResponse> getResults() {
        return results;
    }

    public void setResults(List<TrailersResponse> results) {
        this.results = results;
    }
}

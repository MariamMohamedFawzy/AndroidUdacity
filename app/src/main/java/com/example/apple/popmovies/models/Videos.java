
package com.example.apple.popmovies.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Videos {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<com.example.apple.popmovies.models.Video> results = new ArrayList<com.example.apple.popmovies.models.Video>();

    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The Video
     */
    public List<com.example.apple.popmovies.models.Video> getResults() {
        return results;
    }

    /**
     * 
     * @param Video
     *     The Video
     */
    public void setResults(List<com.example.apple.popmovies.models.Video> Video) {
        this.results = Video;
    }

}

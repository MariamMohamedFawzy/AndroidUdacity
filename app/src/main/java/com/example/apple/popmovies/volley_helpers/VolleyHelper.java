package com.example.apple.popmovies.volley_helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.apple.popmovies.DetailsActivity;
import com.example.apple.popmovies.MainActivity;
import com.example.apple.popmovies.models.MoviePoster;
import com.example.apple.popmovies.models.Reviews;
import com.example.apple.popmovies.models.Videos;
import com.google.gson.Gson;

/**
 * Created by apple on 1/1/16.
 */
public class VolleyHelper {

    public static String BASE_URL_POPULAR_MOVIES = "http://api.themoviedb.org/3/discover/movie";

    public static String MOVIE_TRAILERS_REVIEWS_URL = "http://api.themoviedb.org/3/movie";

    public static Context ctx = null;

    public static void setContext(Context context) {
        ctx = context;
    }

    public static MoviePoster moviePoster;

    public static void downloadPopularMoviesUrls() {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ctx);
        String sortPref = sharedPref.getString("sort_pref", "1");

        String sorting = "sort_by=popularity.desc";

        if (sortPref.equals("0")) {
            sorting = "sort_by=vote_count.desc";
        } else if (sortPref.equals("1")) {
            sorting = "sort_by=popularity.desc";
        }

        String myUrl = BASE_URL_POPULAR_MOVIES + "?" + sorting + "&api_key=" + "b926e7aa59069adde3edc2d1cb54f27c";

        StringRequest strRequest = new StringRequest(Request.Method.GET, myUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        moviePoster = gson.fromJson(response, MoviePoster.class);
                        MainActivity mainActivity = (MainActivity) ctx;
                        mainActivity.getImages();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        moviePoster = null;
                    }
                });


        MyVolleySingleton.getInstance(ctx).addToRequestQueue(strRequest);


    }


    public static void downloadReviewsOfMovie(int movieId) {
        String myUrl = MOVIE_TRAILERS_REVIEWS_URL + "/" + String.valueOf(movieId) + "/reviews" + "?api_key=" + "b926e7aa59069adde3edc2d1cb54f27c";

        StringRequest strRequest = new StringRequest(Request.Method.GET, myUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Reviews reviews = gson.fromJson(response, Reviews.class);
                        DetailsActivity detailsActivity = (DetailsActivity) ctx;
                        detailsActivity.setReviews(reviews);
                        Log.i("tab rev", String.valueOf(reviews.getResults().size()));

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });


        MyVolleySingleton.getInstance(ctx).addToRequestQueue(strRequest);

    }


    public static void downloadTrailersOfMovie(int movieId) {
        String myUrl = MOVIE_TRAILERS_REVIEWS_URL + "/" + String.valueOf(movieId) + "/videos" + "?api_key=" + "b926e7aa59069adde3edc2d1cb54f27c";


        StringRequest strRequest = new StringRequest(Request.Method.GET, myUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Videos videos = gson.fromJson(response, Videos.class);
                        DetailsActivity detailsActivity = (DetailsActivity) ctx;
                        Log.i("tab vid", String.valueOf(videos.getResults().size()));
                        detailsActivity.setVideos(videos);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });


        MyVolleySingleton.getInstance(ctx).addToRequestQueue(strRequest);

    }

}

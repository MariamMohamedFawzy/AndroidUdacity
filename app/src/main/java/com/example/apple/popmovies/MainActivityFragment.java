package com.example.apple.popmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.example.apple.popmovies.adapters.GridImageAdapter;
import com.example.apple.popmovies.data.MovieDBHelper;
import com.example.apple.popmovies.models.Movie;
import com.example.apple.popmovies.models.MoviePoster;
import com.example.apple.popmovies.volley_helpers.VolleyHelper;

import java.util.List;

/**
 * Created by apple on 1/11/16.
 */
public class MainActivityFragment extends Fragment {

    public static String RESULT_OBJ_KEY = "result_obj_key";

    GridView gridView_mainGrid;
    ProgressBar progressBar;
    MoviePoster moviePoster;

    List<Movie> myMovies;

    View rootView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_main_fr, container, false);

        gridView_mainGrid = (GridView) rootView.findViewById(R.id.gridView_mainGrid);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar_mainLoading);

        gridView_mainGrid.setVisibility(View.INVISIBLE);

        gridView_mainGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (moviePoster != null) {
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    intent.putExtra(MainActivityFragment.RESULT_OBJ_KEY, moviePoster.getResults().get(position));
                    startActivity(intent);
                } else if (myMovies != null) {
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    intent.putExtra(MainActivityFragment.RESULT_OBJ_KEY, myMovies.get(position));
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }



    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortPref = sharedPref.getString("sort_pref", "1");
        if (!sortPref.equals("-1")) {
            mainDownloading();
        } else {
            getFavoriteMovies();
        }
    }

    private void getFavoriteMovies() {
        MovieDBHelper movieDBHelper = new MovieDBHelper(getActivity());
        List<Movie> movies = movieDBHelper.getAllFavoriteMovies();

        getImagesFavorite(movies);
    }

    private void mainDownloading() {
        //Log.i("ya salam", "I am downloading ahoh");
        if (isNetworkAvailable()) {
            VolleyHelper.setContext(getActivity());
            progressBar.setVisibility(View.VISIBLE);
            new VolleyHelper().downloadPopularMoviesUrls(this);
        } else {
//            View v = rootView.findViewById(R.id.main_layout);
            progressBar.setVisibility(View.INVISIBLE);
            final Snackbar snackbar = Snackbar.make(rootView, "No internet connection!", Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                    mainDownloading();
                }
            });

            snackbar.show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null);
    }

    public void getImagesFavorite(List<Movie> movies) {
        Log.i("movies size", "" + movies.size());
        myMovies = movies;
        progressBar.setVisibility(View.GONE);
        gridView_mainGrid.setVisibility(View.VISIBLE);

        GridImageAdapter gridImageAdapter = new GridImageAdapter(getActivity(), movies);

        gridView_mainGrid.setAdapter(gridImageAdapter);
    }

    public void getImages() {
//        Log.i("yasalam", "here");
        progressBar.setVisibility(View.GONE);
        gridView_mainGrid.setVisibility(View.VISIBLE);
        moviePoster = VolleyHelper.moviePoster;

        GridImageAdapter gridImageAdapter = new GridImageAdapter(getActivity(), moviePoster);

        gridView_mainGrid.setAdapter(gridImageAdapter);

    }




}

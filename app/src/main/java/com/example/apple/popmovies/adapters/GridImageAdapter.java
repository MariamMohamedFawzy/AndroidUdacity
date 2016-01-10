package com.example.apple.popmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.apple.popmovies.MainActivity;
import com.example.apple.popmovies.R;
import com.example.apple.popmovies.models.Movie;
import com.example.apple.popmovies.models.MoviePoster;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by apple on 12/31/15.
 */
public class GridImageAdapter extends BaseAdapter {

    Context ctx;

    String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    String IMAGE_SIZE = "w185";

    public MoviePoster moviePoster;

    public List<Movie> movies;


    public GridImageAdapter(Context ctx, MoviePoster moviePoster) {

        this.ctx = ctx;
        this.moviePoster = moviePoster;
        this.movies = moviePoster.getResults();
    }

    public GridImageAdapter(Context ctx, List<Movie> movies) {

        this.ctx = ctx;
        this.movies = movies;
    }

    @Override
    public int getCount() {

        return movies.size();

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = (ImageView) LayoutInflater.from(ctx).inflate(R.layout.default_image_view, null);
        } else {
            imageView = (ImageView) convertView;
        }

        String myUrl = IMAGE_BASE_URL + IMAGE_SIZE + "/" + movies.get(position).getBackdropPath();

        Picasso.with(ctx).load(myUrl).into(imageView);

        if (position == getCount() - 1) {
            MainActivity mainActivity = (MainActivity) ctx;
            ProgressBar progressBar = (ProgressBar) mainActivity.findViewById(R.id.progressBar_mainLoading);
            progressBar.setVisibility(View.GONE);
            GridView gridView = (GridView)mainActivity.findViewById(R.id.gridView_mainGrid);
            gridView.setVisibility(View.VISIBLE);
        }

        return imageView;
    }




}

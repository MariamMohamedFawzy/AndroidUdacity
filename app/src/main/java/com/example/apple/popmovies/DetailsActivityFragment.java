package com.example.apple.popmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.apple.popmovies.adapters.MyListAdapter;
import com.example.apple.popmovies.data.MovieDBHelper;
import com.example.apple.popmovies.models.Movie;
import com.example.apple.popmovies.models.Review;
import com.example.apple.popmovies.models.Reviews;
import com.example.apple.popmovies.models.Video;
import com.example.apple.popmovies.models.Videos;
import com.example.apple.popmovies.volley_helpers.VolleyHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 1/11/16.
 */
public class DetailsActivityFragment extends Fragment {


    Movie currentMovie;

    Reviews reviews;

    Videos videos;

    ListView listView_details;

    public boolean doneDownloadingReviews = false;
    public boolean doneDownloadingTrailers = false;

//    FrameLayout frameLayout;

    boolean hasHeader = false;

    View rootView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_details_fr, container, false);

        listView_details = (ListView) rootView.findViewById(R.id.listView_details);


        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        currentMovie = (Movie) bundle.get(MainActivityFragment.RESULT_OBJ_KEY);

        return rootView;
    }

    public void setVideos(Videos videos) {
        this.videos = videos;
        if (reviews != null) {
//            if (!adapterIsSet) {
            setRecyclerAdapter();
//                adapterIsSet = true;
//            }
        }
    }

    public void setReviews(Reviews reviews) {
        this.reviews = reviews;
//        if (!adapterIsSet) {
        setRecyclerAdapter();
//            adapterIsSet = true;
//        }
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortPref = sharedPref.getString("sort_pref", "1");
        if (!sortPref.equals("-1")) {
            downloadReviewsAndTrailers();
        } else {
            getFavoriteMovieReviewsAndTrailers();
        }

    }

    private void getFavoriteMovieReviewsAndTrailers() {
        MovieDBHelper movieDBHelper = new MovieDBHelper(getActivity());
        reviews = movieDBHelper.getReviewsOfMovie(currentMovie.getId());
        videos = movieDBHelper.getTrailersOfMovie(currentMovie.getId());

        setRecyclerAdapter();

    }



    private void downloadReviewsAndTrailers() {
        if (isNetworkAvailable()) {
            VolleyHelper volleyHelper = new VolleyHelper();
            volleyHelper.setContext(getActivity());
            volleyHelper.downloadReviewsOfMovie(currentMovie.getId(), this);
            volleyHelper.downloadTrailersOfMovie(currentMovie.getId(), this);
        }  else {
//            View v = rootView.findViewById(R.id.main_layout);
            final Snackbar snackbar = Snackbar.make(rootView, "No internet connection!", Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                    downloadReviewsAndTrailers();
                }
            });

            snackbar.show();
        }
    }


    private void setRecyclerAdapter() {
        List<Video> listVideos = new ArrayList<Video>();

        List<Review> listReviews = new ArrayList<Review>();

        if (videos != null) {
            listVideos = videos.getResults();
        }

        if (reviews != null) {
            listReviews = reviews.getResults();
        }

        MyListAdapter myListAdapter = new MyListAdapter(listVideos, listReviews, getActivity());

        listView_details.setAdapter(myListAdapter);

        if (!hasHeader) {
            View header_view = getHeader();
            listView_details.addHeaderView(header_view);
            hasHeader = true;
        }
    }

    public View getHeader() {
        View header_view = LayoutInflater.from(getActivity()).inflate(R.layout.header_recycler, listView_details, false);
        TextView movieName ;
        ImageView moviePoster;
        TextView movieYear;
        TextView movieDuration;
        TextView movieRating;
        final Button movieAddToFavorite;
        TextView movieOverview;

        movieName = (TextView) header_view.findViewById(R.id.textView_movieName);
        moviePoster = (ImageView) header_view.findViewById(R.id.imageView_moviePoster);
        movieYear = (TextView) header_view.findViewById(R.id.textView_movieYear);
//        movieDuration = (TextView) header_view.findViewById(R.id.textView_movieDuration);
        movieRating = (TextView) header_view.findViewById(R.id.textView_movieRating);
        movieAddToFavorite = (Button) header_view.findViewById(R.id.button_movieAddToFavorite);
        movieOverview = (TextView) header_view.findViewById(R.id.textView_movieOverview);

        movieAddToFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (movieAddToFavorite.getText().equals("ADD TO FAVORITE")) {
                    MovieDBHelper movieDBHelper = new MovieDBHelper(getActivity());
                    movieDBHelper.addMovieWithReviewsAndTrailers(currentMovie, reviews.getResults(), videos.getResults());
                    movieAddToFavorite.setText("REMOVE FROM FAVORITE");
                } else {
                    MovieDBHelper movieDBHelper = new MovieDBHelper(getActivity());
                    movieDBHelper.deleteMovie(currentMovie.getId());
                    movieAddToFavorite.setText("ADD TO FAVORITE");
                }




            }
        });

        movieName.setText(currentMovie.getOriginalTitle());

        String myUrl = "http://image.tmdb.org/t/p/" + "w185" + "/" + currentMovie.getBackdropPath();
        Picasso.with(getActivity()).load(myUrl).into(moviePoster);

        movieYear.setText(currentMovie.getReleaseDate());
        movieRating.setText(currentMovie.getVoteAverage() + "/10");
//        movieDuration.setText("Duration");

        // TODO: check if it is in the favorite movies

        MovieDBHelper movieDBHelper = new MovieDBHelper(getActivity());
        boolean exists= movieDBHelper.getCertainMovie(currentMovie.getId());
        if (exists) {
            movieAddToFavorite.setText("REMOVE FROM FAVORITE");
        } else {
            movieAddToFavorite.setText("ADD TO FAVORITE");
        }

        movieOverview.setText(currentMovie.getOverview());

        return header_view;
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null);
    }



}

package com.example.apple.popmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    View headerView;

    public int headerMovieId;

    ShareActionProvider mShareActionProvider;

    Context ctx;

    Context context;

    public DetailsActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (getActivity() != null) {
            getActivity().getMenuInflater().inflate(R.menu.share_menu, menu);

//            inflater.inflate(R.menu.share_menu, menu);
//        }


            // Locate MenuItem with ShareActionProvider
            MenuItem item = menu.findItem(R.id.menu_item_share);

            // Fetch and store ShareActionProvider
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

            setShareIntent(createShareForecastIntent());
        }

//        super.onCreateOptionsMenu(menu, inflater);
    }


    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        String movieTitle = "";
        if (currentMovie != null) {
            movieTitle = currentMovie.getOriginalTitle();
        }
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                "Movie : " + movieTitle);
        return shareIntent;
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_details_fr, container, false);

        listView_details = (ListView) rootView.findViewById(R.id.listView_details);

//        if (currentMovie == null && getArguments() != null) {
//            Log.i("details", "args");
//            Bundle args = getArguments();
//            currentMovie = (Movie) args.getSerializable(MainActivityFragment.RESULT_OBJ_KEY);
//        }
//       else
        if (currentMovie == null && getActivity().getIntent() != null) {
            Log.i("details", "intent");
            Intent intent = getActivity().getIntent();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                currentMovie = (Movie) bundle.get(MainActivityFragment.RESULT_OBJ_KEY);
            }
        }

        if (currentMovie != null) {
            Log.i("frag", "details");
        } else {
            Log.i("frag", "details null");
        }

        context = getActivity() == null? ctx : getActivity();


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

        if (currentMovie != null) {
            getReviewsAndTrailers();
        }

    }

    public void updateView(Movie movie) {
        Log.i("update", "update" + movie.getId());
        currentMovie = movie;
        if (currentMovie != null) {
            getReviewsAndTrailers();
            setShareIntent(createShareForecastIntent());
        }
    }

    public void getReviewsAndTrailers() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String sortPref = sharedPref.getString("sort_pref", "1");
        if (!sortPref.equals("-1")) {
//            Log.i("details", "get");
            downloadReviewsAndTrailers();
        } else {
            getFavoriteMovieReviewsAndTrailers();
        }
    }

    private void getFavoriteMovieReviewsAndTrailers() {
        MovieDBHelper movieDBHelper = new MovieDBHelper(context);
        reviews = movieDBHelper.getReviewsOfMovie(currentMovie.getId());
        videos = movieDBHelper.getTrailersOfMovie(currentMovie.getId());

        setRecyclerAdapter();

    }



    private void downloadReviewsAndTrailers() {
        if (isNetworkAvailable()) {
            VolleyHelper volleyHelper = new VolleyHelper();
            volleyHelper.setContext(context);
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
        Log.i("details", "rec");
        List<Video> listVideos = new ArrayList<Video>();

        List<Review> listReviews = new ArrayList<Review>();

        if (videos != null) {
            listVideos = videos.getResults();
        }

        if (reviews != null) {
            listReviews = reviews.getResults();
        }

        MyListAdapter myListAdapter = new MyListAdapter(listVideos, listReviews, context);

        listView_details.setAdapter(myListAdapter);

        if (!hasHeader) {
            addHeader();
            hasHeader = true;
        } else if (currentMovie.getId() != headerMovieId) {
            listView_details.removeHeaderView(headerView);
            addHeader();
        }
//      else {
//            listView_details.removeHeaderView(headerView);
//            headerView = getHeader();
//            listView_details.addHeaderView(headerView);
//        }



    }

    public void addHeader() {
        headerView = getHeader();
        listView_details.addHeaderView(headerView);
    }

    public View getHeader() {
        headerMovieId = currentMovie.getId();
        View header_view = LayoutInflater.from(context).inflate(R.layout.header_recycler, listView_details, false);
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
                    MovieDBHelper movieDBHelper = new MovieDBHelper(context);
                    movieDBHelper.addMovieWithReviewsAndTrailers(currentMovie, reviews.getResults(), videos.getResults());
                    movieAddToFavorite.setText("REMOVE FROM FAVORITE");
                } else {
                    MovieDBHelper movieDBHelper = new MovieDBHelper(context);
                    movieDBHelper.deleteMovie(currentMovie.getId());
                    movieAddToFavorite.setText("ADD TO FAVORITE");
                }




            }
        });

        movieName.setText(currentMovie.getOriginalTitle());

        String myUrl = "http://image.tmdb.org/t/p/" + "w185" + "/" + currentMovie.getBackdropPath();
        Picasso.with(context).load(myUrl).into(moviePoster);

        movieYear.setText(currentMovie.getReleaseDate());
        movieRating.setText(currentMovie.getVoteAverage() + "/10");
//        movieDuration.setText("Duration");

        // TODO: check if it is in the favorite movies


        MovieDBHelper movieDBHelper = new MovieDBHelper(context);
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
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null);
    }


}

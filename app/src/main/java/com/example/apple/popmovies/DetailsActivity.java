package com.example.apple.popmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class DetailsActivity extends AppCompatActivity {


//    RecyclerView recyclerView;

    Movie currentMovie;

    Reviews reviews;

    Videos videos;

    ListView listView_details;

    public boolean doneDownloadingReviews = false;
    public boolean doneDownloadingTrailers = false;

//    FrameLayout frameLayout;

    boolean hasHeader = false;

//    boolean adapterIsSet = false;

//    public static String FAVORITE_KEY = "fav_key";
//    public static String FAVORITE_JSON_KEY = "fav_JSON_key";
//    public static final String MyPREFERENCES = "MyPrefs_fav" ;
//    public static final String MyPREFERENCES_json = "MyPrefs_JSON_fav" ;
//    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

//        frameLayout = (FrameLayout) findViewById(R.id.details_layout);

        listView_details = (ListView) findViewById(R.id.listView_details);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        currentMovie = (Movie) bundle.get(MainActivity.RESULT_OBJ_KEY);

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
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String sortPref = sharedPref.getString("sort_pref", "1");
        if (!sortPref.equals("-1")) {
            downloadReviewsAndTrailers();
        } else {
            getFavoriteMovieReviewsAndTrailers();
        }

    }

    private void getFavoriteMovieReviewsAndTrailers() {
        MovieDBHelper movieDBHelper = new MovieDBHelper(this);
        reviews = movieDBHelper.getReviewsOfMovie(currentMovie.getId());
        videos = movieDBHelper.getTrailersOfMovie(currentMovie.getId());

        setRecyclerAdapter();

    }



    private void downloadReviewsAndTrailers() {
        if (isNetworkAvailable()) {
            VolleyHelper volleyHelper = new VolleyHelper();
            volleyHelper.setContext(this);
            volleyHelper.downloadReviewsOfMovie(currentMovie.getId());
            volleyHelper.downloadTrailersOfMovie(currentMovie.getId());
        }  else {
            View v = findViewById(R.id.main_layout);
            final Snackbar snackbar = Snackbar.make(v, "No internet connection!", Snackbar.LENGTH_INDEFINITE);

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

        MyListAdapter myListAdapter = new MyListAdapter(listVideos, listReviews, this);

        listView_details.setAdapter(myListAdapter);

        if (!hasHeader) {
            View header_view = getHeader();
            listView_details.addHeaderView(header_view);
            hasHeader = true;
        }
    }

    public View getHeader() {
        View header_view = LayoutInflater.from(this).inflate(R.layout.header_recycler, listView_details, false);
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
                    MovieDBHelper movieDBHelper = new MovieDBHelper(getApplicationContext());
                    movieDBHelper.addMovieWithReviewsAndTrailers(currentMovie, reviews.getResults(), videos.getResults());
                    movieAddToFavorite.setText("REMOVE FROM FAVORITE");
                } else {
                    MovieDBHelper movieDBHelper = new MovieDBHelper(getApplicationContext());
                    movieDBHelper.deleteMovie(currentMovie.getId());
                    movieAddToFavorite.setText("ADD TO FAVORITE");
                }




            }
        });

        movieName.setText(currentMovie.getOriginalTitle());

        String myUrl = "http://image.tmdb.org/t/p/" + "w185" + "/" + currentMovie.getBackdropPath();
        Picasso.with(this).load(myUrl).into(moviePoster);

        movieYear.setText(currentMovie.getReleaseDate());
        movieRating.setText(currentMovie.getVoteAverage() + "/10");
//        movieDuration.setText("Duration");

        // TODO: check if it is in the favorite movies

        MovieDBHelper movieDBHelper = new MovieDBHelper(this);
        boolean exists= movieDBHelper.getCertainMovie(currentMovie.getId());
        if (exists) {
            movieAddToFavorite.setText("REMOVE FROM FAVORITE");
        } else {
            movieAddToFavorite.setText("ADD TO FAVORITE");
        }

        movieOverview.setText(currentMovie.getOverview());

        return header_view;
    }



    @Override
    protected void onPause() {
        super.onPause();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
//            startActivity(new Intent(DetailsActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}





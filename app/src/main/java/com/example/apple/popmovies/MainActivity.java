package com.example.apple.popmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.example.apple.popmovies.adapters.GridImageAdapter;
import com.example.apple.popmovies.data.MovieDBHelper;
import com.example.apple.popmovies.models.Movie;
import com.example.apple.popmovies.models.MoviePoster;
import com.example.apple.popmovies.volley_helpers.VolleyHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    public static String RESULT_OBJ_KEY = "result_obj_key";

    GridView gridView_mainGrid;
    ProgressBar progressBar;
    MoviePoster moviePoster;

    List<Movie> myMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView_mainGrid = (GridView) findViewById(R.id.gridView_mainGrid);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_mainLoading);

        gridView_mainGrid.setVisibility(View.INVISIBLE);

        gridView_mainGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (moviePoster != null) {
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra(MainActivity.RESULT_OBJ_KEY, moviePoster.getResults().get(position));
                    startActivity(intent);
                } else if (myMovies != null) {
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra(MainActivity.RESULT_OBJ_KEY, myMovies.get(position));
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String sortPref = sharedPref.getString("sort_pref", "1");
        if (!sortPref.equals("-1")) {
            mainDownloading();
        } else {
            getFavoriteMovies();
        }
    }

    private void getFavoriteMovies() {
        MovieDBHelper movieDBHelper = new MovieDBHelper(this);
        List<Movie> movies = movieDBHelper.getAllFavoriteMovies();

        getImagesFavorite(movies);
    }

    private void mainDownloading() {
        //Log.i("ya salam", "I am downloading ahoh");
        if (isNetworkAvailable()) {
            VolleyHelper.setContext(this);
            progressBar.setVisibility(View.VISIBLE);
            VolleyHelper.downloadPopularMoviesUrls();
        } else {
            View v = findViewById(R.id.main_layout);
            progressBar.setVisibility(View.INVISIBLE);
            final Snackbar snackbar = Snackbar.make(v, "No internet connection!", Snackbar.LENGTH_INDEFINITE);

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
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null);
    }

    public void getImagesFavorite(List<Movie> movies) {
        Log.i("movies size", "" + movies.size());
        myMovies = movies;
        progressBar.setVisibility(View.GONE);
        gridView_mainGrid.setVisibility(View.VISIBLE);

        GridImageAdapter gridImageAdapter = new GridImageAdapter(this, movies);

        gridView_mainGrid.setAdapter(gridImageAdapter);
    }

    public void getImages() {
        moviePoster = VolleyHelper.moviePoster;

        GridImageAdapter gridImageAdapter = new GridImageAdapter(this, moviePoster);

        gridView_mainGrid.setAdapter(gridImageAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

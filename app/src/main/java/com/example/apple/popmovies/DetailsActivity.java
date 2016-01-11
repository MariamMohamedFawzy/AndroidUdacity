package com.example.apple.popmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.apple.popmovies.models.Movie;
import com.example.apple.popmovies.models.Reviews;
import com.example.apple.popmovies.models.Videos;

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

    private ShareActionProvider mShareActionProvider;

//    DetailsActivityFragment detailsActivityFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

//        detailsActivityFragment = (DetailsActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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





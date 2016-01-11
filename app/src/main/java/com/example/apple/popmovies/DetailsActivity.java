package com.example.apple.popmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

//        frameLayout = (FrameLayout) findViewById(R.id.details_layout);



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





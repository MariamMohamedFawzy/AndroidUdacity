package com.example.apple.popmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.apple.popmovies.models.Movie;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.OnMovieClickListener {

    public boolean isTwoPane = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.main_layout);

        if (frameLayout != null) {
            isTwoPane = false;

//            MainActivityFragment mainActivityFragment = new MainActivityFragment();
//
//            getSupportFragmentManager().beginTransaction().add(mainActivityFragment, "mainFrag").commit();


        } else {
            isTwoPane = true;
        }

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

    @Override
    public void onMovieSelected(Movie movie) {

        Log.i("the pane", String.valueOf(isTwoPane));

        if (movie == null) {
            Log.i("the moview", "null");
        } else {
            Log.i("the movie", "not null");
        }

//        if (isTwoPane) {
//
//            Log.i("pane", "yes");
//
//            Bundle arguments = new Bundle();
//            arguments.putSerializable(MainActivityFragment.RESULT_OBJ_KEY, movie);
//            DetailsActivityFragment fragment = new DetailsActivityFragment();
//            fragment.setArguments(arguments);
//            getSupportFragmentManager().beginTransaction()
////                    .replace(R.id.main_layout, fragment)
//                    .add(fragment, "detailsFrag")
//                    .commit();
//
//
//
//        } else {
//
//            Intent detailIntent = new Intent(this, DetailsActivity.class);
//            detailIntent.putExtra(MainActivityFragment.RESULT_OBJ_KEY, movie);
//            startActivity(detailIntent);
//
//
//        }


        DetailsActivityFragment detailsActivityFragment = (DetailsActivityFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_details);

        if (detailsActivityFragment != null) {
            detailsActivityFragment.updateView(movie);
        } else {

            Intent detailIntent = new Intent(this, DetailsActivity.class);
            detailIntent.putExtra(MainActivityFragment.RESULT_OBJ_KEY, movie);
            startActivity(detailIntent);


//            DetailsActivityFragment newFragment = new DetailsActivityFragment();
//            Bundle args = new Bundle();
//            args.putSerializable(MainActivityFragment.RESULT_OBJ_KEY, movie);
//            newFragment.setArguments(args);
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//            // Replace whatever is in the fragment_container view with this fragment,
//            // and add the transaction to the back stack so the user can navigate back
//            transaction.replace(R.id., newFragment);
//            transaction.addToBackStack(null);
//
//            // Commit the transaction
//            transaction.commit();
        }





    }
}

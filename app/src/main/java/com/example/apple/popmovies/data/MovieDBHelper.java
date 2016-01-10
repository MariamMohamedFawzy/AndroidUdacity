package com.example.apple.popmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.apple.popmovies.models.Movie;
import com.example.apple.popmovies.models.Review;
import com.example.apple.popmovies.models.Reviews;
import com.example.apple.popmovies.models.Video;
import com.example.apple.popmovies.models.Videos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 1/9/16.
 */
public class MovieDBHelper {
    Context ctx;

    public MovieDBHelper(Context ctx) {
        this.ctx = ctx;
    }


    public List<Movie> getAllFavoriteMovies() {
        Cursor result;

        MovieDB movieDB = new MovieDB(ctx);

        SQLiteDatabase sqLiteDatabase = movieDB.getReadableDatabase();

        result = sqLiteDatabase.query(MovieContract.MovieEntry.TABLE_NAME, null, null, null, null, null, null);

        List<Movie> movieList = new ArrayList<Movie>();

        result.moveToFirst();
        int counter = result.getCount();
        Log.i("counter", "" + counter);
        for (int i = 0; i < counter; i++) {
            Log.i("inside", "" + i);
            Movie movie = new Movie();

            int idCol = result.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
            movie.setId(result.getInt(idCol));

            int titleCol = result.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE);
            movie.setOriginalTitle(result.getString(titleCol));

            int overviewCol = result.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW);
            movie.setOverview(result.getString(overviewCol));

            int posterPathCol = result.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
            movie.setBackdropPath(result.getString(posterPathCol));

            int releaseDateCol = result.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
            movie.setReleaseDate(result.getString(releaseDateCol));

            int voreAvgCol = result.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE);
            movie.setVoteAverage(result.getDouble(voreAvgCol));

            movieList.add(movie);

            result.moveToNext();
        }
        result.close();

        movieDB.close();

        return movieList;
    }

    public boolean getCertainMovie(int movieId) {
        Cursor result;

        MovieDB movieDB = new MovieDB(ctx);

        SQLiteDatabase sqLiteDatabase = movieDB.getReadableDatabase();

        result = sqLiteDatabase.query(MovieContract.MovieEntry.TABLE_NAME, null,  MovieContract.MovieEntry.COLUMN_MOVIE_ID +
        " = ?", new String [] {String.valueOf(movieId)}, null, null, null);

        boolean exists = result.getCount() != 0;

        movieDB.close();

        return exists;
    }

    public Reviews getReviewsOfMovie(int movieId) {
        Cursor result;

        MovieDB movieDB = new MovieDB(ctx);

        SQLiteDatabase sqLiteDatabase = movieDB.getReadableDatabase();

        result = sqLiteDatabase.query(MovieContract.ReviewEntry.TABLE_NAME, null, MovieContract.ReviewEntry.COLUMN_REVIEW_MOVIE_ID +
                " = ?", new String [] {String.valueOf(movieId)}, null, null, null);



        int counter = result.getCount();

        Reviews reviews = new Reviews();

        ArrayList<Review> reviewArrayList = new ArrayList<Review>();

        result.moveToFirst();

        for (int i = 0; i < counter; i++) {

            Review review = new Review();

            int idCol = result.getColumnIndex(MovieContract.ReviewEntry.COLUMN_REVIEW_ID);
            review.setId(String.valueOf(result.getInt(idCol)));

            int authorCol = result.getColumnIndex(MovieContract.ReviewEntry.COLUMN_REVIEW_AUTHER);
            review.setAuthor(result.getString(authorCol));

            int contentCol = result.getColumnIndex(MovieContract.ReviewEntry.COLUMN_REVIEW_Content);
            review.setContent(result.getString(contentCol));

            reviewArrayList.add(review);

            result.moveToNext();
        }

        result.close();

        reviews.setResults(reviewArrayList);

        movieDB.close();

        return reviews;
    }

    public Videos getTrailersOfMovie(int movieId) {
        Cursor result;

        MovieDB movieDB = new MovieDB(ctx);

        SQLiteDatabase sqLiteDatabase = movieDB.getReadableDatabase();

        result = sqLiteDatabase.query(MovieContract.TrailerEntry.TABLE_NAME, null, MovieContract.TrailerEntry.COLUMN_TRAILER_MOVIE_ID +
                " = ?", new String [] {String.valueOf(movieId)}, null, null, null);

        int counter = result.getCount();

        Videos videos = new Videos();

        ArrayList<Video> videoArrayList = new ArrayList<Video>();

        result.moveToFirst();

        for (int i = 0; i < counter; i++) {

            Video video = new Video();

            int idCol = result.getColumnIndex(MovieContract.TrailerEntry.COLUMN_TRAILER_ID);
            video.setId(String.valueOf(result.getInt(idCol)));

            int urlCol = result.getColumnIndex(MovieContract.TrailerEntry.COLUMN_TRAILER_URL);
            video.setKey(result.getString(urlCol));

            int nameCol = result.getColumnIndex(MovieContract.TrailerEntry.COLUMN_TRAILER_NAME);
            video.setName(result.getString(nameCol));

            videoArrayList.add(video);

            result.moveToNext();
        }

        result.close();

        videos.setResults(videoArrayList);

        movieDB.close();

        return videos;
    }


    public long addMovieWithReviewsAndTrailers(Movie movie, List<Review> reviewList, List<Video> videoList) {

        MovieDB movieDB = new MovieDB(ctx);

        SQLiteDatabase sqLiteDatabase = movieDB.getWritableDatabase();

        long result = sqLiteDatabase.insert(MovieContract.MovieEntry.TABLE_NAME, null, createMovieValues(movie));

        movieDB.close();

       // TODO HANDLE THE EXCEPTION OF NOT INSERTING THE MOVIE OR REVIEWS OR TRAILERS
        insertReviews(reviewList, movie.getId());

        insertTrailers(videoList, movie.getId());

        return result;
    }


    public ContentValues createMovieValues(Movie movie) {
        ContentValues movieValues = new ContentValues();

        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_TITLE, movie.getOriginalTitle());
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getBackdropPath());
        movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());

        return movieValues;
    }



    public int deleteMovie(int movieId) {

        MovieDB movieDB = new MovieDB(ctx);

        SQLiteDatabase sqLiteDatabase = movieDB.getWritableDatabase();

        int result = sqLiteDatabase.delete(MovieContract.MovieEntry.TABLE_NAME,  MovieContract.MovieEntry.COLUMN_MOVIE_ID +
                " = ?", new String[]{String.valueOf(movieId)});

        sqLiteDatabase.delete(MovieContract.ReviewEntry.TABLE_NAME,  MovieContract.ReviewEntry.COLUMN_REVIEW_MOVIE_ID +
                " = ?", new String[]{String.valueOf(movieId)});

        sqLiteDatabase.delete(MovieContract.TrailerEntry.TABLE_NAME,  MovieContract.TrailerEntry.COLUMN_TRAILER_MOVIE_ID +
                " = ?", new String[]{String.valueOf(movieId)});

        movieDB.close();

        return result;

    }

    public int insertReviews(List<Review> reviewList, int movieId) {

        MovieDB movieDB = new MovieDB(ctx);

        SQLiteDatabase sqLiteDatabase = movieDB.getWritableDatabase();

            List<ContentValues> contentValues = getContentValuesForReviews(reviewList, movieId);

        sqLiteDatabase.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : contentValues) {
                long _id = sqLiteDatabase.insert(MovieContract.ReviewEntry.TABLE_NAME, null, value);
                if (_id != -1) {
                    returnCount++;
                }
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }

        movieDB.close();

        return returnCount;
    }

    public ArrayList<ContentValues> getContentValuesForReviews(List<Review> reviewList, int movieId) {
        ArrayList<ContentValues> result = new ArrayList<ContentValues>();

        for (Review review: reviewList) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_ID, review.getId());
            contentValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_MOVIE_ID, movieId);
            contentValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_AUTHER, review.getAuthor());
            contentValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_Content, review.getContent());

            result.add(contentValues);
        }

        return result;
    }

    public int insertTrailers(List<Video> trailerList, int movieId) {

        MovieDB movieDB = new MovieDB(ctx);

        SQLiteDatabase sqLiteDatabase = movieDB.getWritableDatabase();

        List<ContentValues> contentValues = getContentValuesForTrailers(trailerList, movieId);

        sqLiteDatabase.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : contentValues) {
                long _id = sqLiteDatabase.insert(MovieContract.TrailerEntry.TABLE_NAME, null, value);
                if (_id != -1) {
                    returnCount++;
                }
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }

        movieDB.close();

        return returnCount;
    }

    public ArrayList<ContentValues> getContentValuesForTrailers(List<Video> trailerList, int movieId) {
        ArrayList<ContentValues> result = new ArrayList<ContentValues>();

        for (Video video: trailerList) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_ID, video.getId());
            contentValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_MOVIE_ID, movieId);
            contentValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_NAME, video.getName());
            contentValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_URL, video.getKey());

            result.add(contentValues);
        }

        return result;
    }



}

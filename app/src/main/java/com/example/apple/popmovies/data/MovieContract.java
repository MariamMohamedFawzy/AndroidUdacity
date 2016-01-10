package com.example.apple.popmovies.data;

import android.provider.BaseColumns;

/**
 * Created by apple on 1/9/16.
 */
public class MovieContract {


    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_MOVIE_OVERVIEW = "movie_overview";

        public static final String COLUMN_MOVIE_ORIGINAL_TITLE = "movie_original_title";

        public static final String COLUMN_VOTE_AVERAGE = "movie_vote_average";

        public static final String COLUMN_RELEASE_DATE = "movie_release_date";

        public static final String COLUMN_POSTER_PATH = "movie_poster_path";

    }

    public static final class ReviewEntry implements BaseColumns {

        public static final String TABLE_NAME = "review";

        public static final String COLUMN_REVIEW_MOVIE_ID = "movie_id";

        public static final String COLUMN_REVIEW_ID = "review_id";

        public static final String COLUMN_REVIEW_AUTHER = "review_auther";

        public static final String COLUMN_REVIEW_Content = "review_content";

    }

    public static final class TrailerEntry implements BaseColumns {

        public static final String TABLE_NAME = "trailer";

        public static final String COLUMN_TRAILER_MOVIE_ID = "movie_id";

        public static final String COLUMN_TRAILER_ID = "trailer_id";

        public static final String COLUMN_TRAILER_NAME = "trailer_name";

        public static final String COLUMN_TRAILER_URL = "trailer_url";

    }



}

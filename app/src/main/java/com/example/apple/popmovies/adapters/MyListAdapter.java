package com.example.apple.popmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.apple.popmovies.R;
import com.example.apple.popmovies.enums.RecyclerItemType;
import com.example.apple.popmovies.models.Movie;
import com.example.apple.popmovies.models.Review;
import com.example.apple.popmovies.models.Video;

import java.util.List;

/**
 * Created by apple on 1/9/16.
 */
public class MyListAdapter extends BaseAdapter {

    Movie movie;

    List<Video> videoList;
    List<Review> reviewList;
    Context ctx;

    public MyListAdapter(List<Video> videoList, List<Review> reviewList, Context ctx) {
        this.movie = movie;
        this.videoList = videoList;
        this.reviewList = reviewList;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return videoList.size() + reviewList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (videoList.size() > 0) {
            if (i == 0) {
                view = LayoutInflater.from(ctx).inflate(R.layout.trailer_item, null);
                bindView(view, RecyclerItemType.trailer, i);
            } else if (i == videoList.size()) {
                view = LayoutInflater.from(ctx).inflate(R.layout.review_item, null);
                bindView(view, RecyclerItemType.review, i);
            } else if (i > 0 && i < videoList.size()) {
                if (view != null) {
                    Button videoButton = (Button) view.findViewById(R.id.btn_trailer);
                    if (videoButton == null) {
                        view = LayoutInflater.from(ctx).inflate(R.layout.trailer_item, null);
                    }
                } else {
                    view = LayoutInflater.from(ctx).inflate(R.layout.trailer_item, null);
                }
                bindView(view, RecyclerItemType.trailer, i);
            } else {
                if (view != null) {
                    TextView contentTextView = (TextView) view.findViewById(R.id.textView_content);
                    if (contentTextView == null) {
                        view = LayoutInflater.from(ctx).inflate(R.layout.review_item, null);
                    }
                } else {
                    view = LayoutInflater.from(ctx).inflate(R.layout.review_item, null);
                }
                bindView(view, RecyclerItemType.review, i);
            }

        } else {
            if (i == 0) {
                view = LayoutInflater.from(ctx).inflate(R.layout.review_item, null);
            }
           TextView contentTextView = (TextView) view.findViewById(R.id.textView_content);
            if (contentTextView == null) {
                view = LayoutInflater.from(ctx).inflate(R.layout.review_item, null);
            }
            bindView(view, RecyclerItemType.review, i);
        }
        return view;
    }

    public void bindView(View v, RecyclerItemType itemType, int position) {

        switch (itemType) {
            case trailer:
                Button videoButton = (Button) v.findViewById(R.id.btn_trailer);
                TextView trailerName = (TextView) v.findViewById(R.id.textView_trailer);
                final Video video = videoList.get(position);
                trailerName.setText(video.getName());
                videoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        // TODO check if it is a video on youtube
                        Uri uri = Uri.parse("http://www.youtube.com/watch?v=" + video.getKey());
                        intent.setData(uri);
                        Intent chooser = Intent.createChooser(intent, "Choose an app to view the video");
                        if (intent.resolveActivity(ctx.getPackageManager()) != null) {
                            ctx.startActivity(chooser);
                        }
                    }
                });
                break;
            case review:
                TextView contentTextView = (TextView) v.findViewById(R.id.textView_content);
                TextView authorTextView = (TextView) v.findViewById(R.id.textView_author);
                int position2 = position - videoList.size();
                Review review = reviewList.get(position2);
                contentTextView.setText(review.getContent());
                authorTextView.setText(review.getAuthor());
                break;
        }

    }

}



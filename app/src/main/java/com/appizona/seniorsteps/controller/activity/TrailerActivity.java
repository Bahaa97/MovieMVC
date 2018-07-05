package com.appizona.seniorsteps.controller.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.appizona.seniorsteps.R;
import com.appizona.seniorsteps.adapter.TrailersAdapter;
import com.appizona.seniorsteps.api.Networking;
import com.appizona.seniorsteps.callback.OnTrailerClickListener;
import com.appizona.seniorsteps.callback.OnTrailerRetievalListener;
import com.appizona.seniorsteps.model.Trailer;
import com.appizona.seniorsteps.util.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailerActivity extends AppCompatActivity {

    @BindView(R.id.recycler_trailer)
    RecyclerView recyclerTrailer;

    private RecyclerView.LayoutManager mLayoutManager;
    private TrailersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        mLayoutManager = new LinearLayoutManager(this);
        recyclerTrailer.setLayoutManager(mLayoutManager);
        int id = getIntent().getExtras().getInt(Constant.MOVIE_ID);
        Networking.getTrailers(String.valueOf(id), new OnTrailerRetievalListener() {
            @Override
            public void onReteived(List<Trailer> trailers) {
                adapter = new TrailersAdapter(TrailerActivity.this, trailers);
                adapter.setOnTrailerClickListener(new OnTrailerClickListener() {
                    @Override
                    public void onTrailerClicked(String key) {
                        watchYoutubeVideo(key);
                    }
                });
                recyclerTrailer.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMsg) {
                Toast.makeText(TrailerActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }
}

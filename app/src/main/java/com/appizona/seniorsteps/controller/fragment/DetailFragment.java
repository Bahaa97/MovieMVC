package com.appizona.seniorsteps.controller.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appizona.seniorsteps.R;
import com.appizona.seniorsteps.controller.activity.TrailerActivity;
import com.appizona.seniorsteps.model.Movie;
import com.appizona.seniorsteps.util.Constant;
import com.appizona.seniorsteps.util.Util;
import com.mindorks.nybus.NYBus;
import com.mindorks.nybus.annotation.Subscribe;
import com.mindorks.nybus.event.Channel;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.cover_img)
    ImageView coverImg;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.titleTV)
    TextView titleTV;
    @BindView(R.id.ratingTV)
    TextView ratingTV;
    @BindView(R.id.fav_btn)
    FloatingActionButton favBtn;
    @BindView(R.id.descriptionTV)
    TextView descriptionTV;
    @BindView(R.id.trailers_btn)
    Button trailersBtn;

    private Movie mMovie;


    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);

        init();

        NYBus.get().register(this, Channel.ONE);

        return view;
    }

    private void init() {
        if (!getResources().getBoolean(R.bool.isTablet)) {
            Movie movie = getActivity().getIntent().getExtras().getParcelable(Constant.MOVIE_EXTRA);
            loadMovie(movie);
        }


        favBtn.setOnClickListener(this);
        trailersBtn.setOnClickListener(this);
    }

    private void loadMovie(Movie movie) {
        mMovie = movie;

        boolean isFav = Util.isFavorite(mMovie.getId());
        if (isFav) {
            favBtn.setImageResource(R.drawable.star_selected);
        } else {
            favBtn.setImageResource(R.drawable.star);
        }

        titleTV.setText(movie.getTitle());
        ratingTV.setText(String.valueOf(movie.getVoteAverage()));
        descriptionTV.setText(movie.getOverview());


        Picasso.get()
                .load(Constant.BASE_IMAGE_URL + movie.getBackdropPath())
                .into(coverImg);

        Picasso.get()
                .load(Constant.BASE_IMAGE_URL + movie.getPosterPath())
                .into(img);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fav_btn:
                boolean isFav = Util.isFavorite(mMovie.getId());

                if (isFav) {
                    Util.removeFromFav(mMovie.getId());
                    favBtn.setImageResource(R.drawable.star);
                } else {
                    Util.addToFavorite(mMovie);
                    favBtn.setImageResource(R.drawable.star_selected);
                }
                break;

            case R.id.trailers_btn:

                Intent intent = new Intent(getActivity(), TrailerActivity.class);
                intent.putExtra(Constant.MOVIE_ID, mMovie.getId());
                startActivity(intent);
                break;
        }


    }

    @Subscribe(channelId = Channel.ONE)
    public void onGetMovie(Movie mMovie) {
        loadMovie(mMovie);
    }
}

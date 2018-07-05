package com.appizona.seniorsteps.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.appizona.seniorsteps.R;
import com.appizona.seniorsteps.controller.activity.DetailActivity;
import com.appizona.seniorsteps.model.Movie;
import com.appizona.seniorsteps.util.Constant;
import com.mindorks.nybus.NYBus;
import com.mindorks.nybus.event.Channel;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesHolder> {


    private Context mContext;
    private List<Movie> list;

    public MoviesAdapter(Context mContext, List<Movie> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public MoviesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_movie, parent, false);
        return new MoviesHolder(inflate);
    }

    @Override
    public void onBindViewHolder(MoviesHolder holder, final int position) {

        final Movie movie = list.get(position);

        if (position == 0) {
            NYBus.get().post(movie, Channel.ONE);
        }

        Picasso.get()
                .load(Constant.BASE_IMAGE_URL + movie.getPosterPath())
                .error(R.drawable.img)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.img);

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!mContext.getResources().getBoolean(R.bool.isTablet)) {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra(Constant.MOVIE_EXTRA, movie);
                    mContext.startActivity(intent);
                } else {
                    NYBus.get().post(movie, Channel.ONE);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MoviesHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img)
        ImageView img;

        @BindView(R.id.root)
        LinearLayout root;

        MoviesHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

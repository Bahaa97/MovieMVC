package com.appizona.seniorsteps.controller.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appizona.seniorsteps.R;
import com.appizona.seniorsteps.adapter.MoviesAdapter;
import com.appizona.seniorsteps.api.Networking;
import com.appizona.seniorsteps.callback.OnMoviesRetreivalListener;
import com.appizona.seniorsteps.model.Movie;
import com.appizona.seniorsteps.util.Connection;
import com.appizona.seniorsteps.util.Constant;
import com.appizona.yehiahd.fastsave.FastSave;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.recycler_movies)
    RecyclerView recyclerMovies;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private MoviesAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerMovies.setLayoutManager(mLayoutManager);
        swipeLayout.setOnRefreshListener(this);

        if (Connection.isNetworkAvailable(getActivity()))
            getMovies(Constant.POPULAR_KEY);
        else {
            loadFromCache();
        }

        return view;
    }

    private void loadFromCache() {
        List<Movie> cachedPopular = FastSave.getInstance().getObjectsList(Constant.POP_MOVIES_CACHED, Movie.class);
        List<Movie> cachedTopRated = FastSave.getInstance().getObjectsList(Constant.TOP_MOVIES_CACHED, Movie.class);

        if (cachedPopular.size() > 0) {
            adapter = new MoviesAdapter(getActivity(), cachedPopular);
            recyclerMovies.setAdapter(adapter);
        } else if (cachedTopRated.size() > 0) {
            adapter = new MoviesAdapter(getActivity(), cachedTopRated);
            recyclerMovies.setAdapter(adapter);
        } else {
            Toast.makeText(getActivity(), "No Cached Data", Toast.LENGTH_SHORT).show();
            adapter = new MoviesAdapter(getActivity(), new ArrayList<Movie>());
            recyclerMovies.setAdapter(adapter);
        }
    }

    private void getMovies(final String moviesType) {
        Networking.getMovies(moviesType, new OnMoviesRetreivalListener() {
            @Override
            public void onSucess(List<Movie> movies) {
                adapter = new MoviesAdapter(getActivity(), movies);
                recyclerMovies.setAdapter(adapter);

                if (swipeLayout.isRefreshing())
                    swipeLayout.setRefreshing(false);

                if (moviesType.equals(Constant.POPULAR_KEY)) {
                    FastSave.getInstance().saveObjectsList(Constant.POP_MOVIES_CACHED, movies);
                } else {
                    FastSave.getInstance().saveObjectsList(Constant.TOP_MOVIES_CACHED, movies);
                }

            }

            @Override
            public void onError(String errorMsg) {
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                swipeLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular:
                getMovies(Constant.POPULAR_KEY);
                return true;

            case R.id.top_rated:
                getMovies(Constant.RATED_KEY);
                return true;

            case R.id.favorite:
                List<Movie> movies = FastSave.getInstance().getObjectsList(Constant.FAV_MOVIES, Movie.class);
                adapter = new MoviesAdapter(getActivity(), movies);
                recyclerMovies.setAdapter(adapter);
                return true;
        }

        return false;
    }

    @Override
    public void onRefresh() {
        getMovies(Constant.POPULAR_KEY);
    }

}

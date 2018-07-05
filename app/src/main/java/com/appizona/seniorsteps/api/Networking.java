package com.appizona.seniorsteps.api;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.appizona.seniorsteps.callback.OnMoviesRetreivalListener;
import com.appizona.seniorsteps.callback.OnTrailerRetievalListener;
import com.appizona.seniorsteps.model.MovieResponse;
import com.appizona.seniorsteps.model.TrailerResponse;
import com.appizona.seniorsteps.util.Constant;

public class Networking {

    public static void getMovies(String moviesType, final OnMoviesRetreivalListener listener) {
        AndroidNetworking.get(Constant.BASE_URL + moviesType)
                .addQueryParameter(Constant.API_KEY, Constant.API_KEY_VALUE)
                .build()
                .getAsObject(MovieResponse.class, new ParsedRequestListener<MovieResponse>() {
                    @Override
                    public void onResponse(MovieResponse response) {
                        listener.onSucess(response.getMovies());
                    }

                    @Override
                    public void onError(ANError anError) {
                        listener.onError(anError.getErrorDetail());
                    }
                });
    }

    public static void getTrailers(String movieID, final OnTrailerRetievalListener listener) {
        AndroidNetworking.get(Constant.BASE_URL + "{movie_id}" + Constant.VIDEOS)
                .addPathParameter("movie_id", movieID)
                .addQueryParameter(Constant.API_KEY, Constant.API_KEY_VALUE)
                .build()
                .getAsObject(TrailerResponse.class, new ParsedRequestListener<TrailerResponse>() {

                    @Override
                    public void onResponse(TrailerResponse response) {
                        listener.onReteived(response.getTrailers());
                    }

                    @Override
                    public void onError(ANError anError) {
                        listener.onError(anError.getErrorDetail());
                    }
                });
    }

}

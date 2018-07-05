package com.appizona.seniorsteps.callback;

import com.appizona.seniorsteps.model.Movie;

import java.util.List;

public interface OnMoviesRetreivalListener {

    void onSucess(List<Movie> movies);

    void onError(String errorMsg);
}

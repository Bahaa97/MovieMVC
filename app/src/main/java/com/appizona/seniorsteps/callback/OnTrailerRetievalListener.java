package com.appizona.seniorsteps.callback;

import com.appizona.seniorsteps.model.Trailer;

import java.util.List;

public interface OnTrailerRetievalListener {

    void onReteived(List<Trailer> trailers);

    void onError(String errorMsg);
}

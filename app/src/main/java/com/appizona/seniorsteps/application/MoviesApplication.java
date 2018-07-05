package com.appizona.seniorsteps.application;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.appizona.seniorsteps.util.Constant;
import com.appizona.yehiahd.fastsave.FastSave;

import java.util.ArrayList;

public class MoviesApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
        FastSave.init(getApplicationContext());

        if (!FastSave.getInstance().isKeyExists(Constant.FAV_MOVIES))
            FastSave.getInstance().saveObjectsList(Constant.FAV_MOVIES, new ArrayList<>());


        if (!FastSave.getInstance().isKeyExists(Constant.POP_MOVIES_CACHED))
            FastSave.getInstance().saveObjectsList(Constant.POP_MOVIES_CACHED, new ArrayList<>());


        if (!FastSave.getInstance().isKeyExists(Constant.TOP_MOVIES_CACHED))
            FastSave.getInstance().saveObjectsList(Constant.TOP_MOVIES_CACHED, new ArrayList<>());


    }
}

package com.example.whetherdata.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.whetherdata.dbhelper.WeatherDataHandler;
import com.example.whetherdata.model.Weather;

import java.util.List;

/**
 * Created by pallavshah on 5/30/15.
 */
public class WeatherListLoader extends AsyncTaskLoader<List<Weather>>{
    public WeatherListLoader(Context context){
        super(context);
    }

    @Override
    public List<Weather> loadInBackground() {
        final List<Weather> weatherList = new WeatherDataHandler(getContext()).getWeathers();

        return weatherList;
    }


}

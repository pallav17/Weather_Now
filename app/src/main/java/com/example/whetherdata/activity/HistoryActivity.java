package com.example.whetherdata.activity;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.whetherdata.R;
import com.example.whetherdata.adapter.WeatherAdapter;
import com.example.whetherdata.loader.WeatherListLoader;
import com.example.whetherdata.model.Weather;

import java.util.List;

public class HistoryActivity extends Activity implements LoaderManager.LoaderCallbacks<List<Weather>>{

    private static final int THE_LOADER = 0x01;

    private ListView weatherHistoryListView;
    private WeatherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //initialize
        weatherHistoryListView = (ListView) findViewById(R.id.weather_history_lv);

        //call loader
        getLoaderManager().initLoader(THE_LOADER, null, this).forceLoad();
    }




    @Override
    public Loader<List<Weather>> onCreateLoader(int i, Bundle bundle) {
        WeatherListLoader weatherListLoader = new WeatherListLoader(this);
        return weatherListLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<Weather>> loader, List<Weather> weatherList) {

        if (weatherList != null && weatherList.size()== 0)
        {
            Toast.makeText(HistoryActivity.this,"No Weather histroy available. Please ensure once you fetch the data",Toast.LENGTH_LONG).show();
            finish();
        }
        adapter = new WeatherAdapter(this,R.layout.weather_list_item,weatherList);
        weatherHistoryListView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<List<Weather>> loader) {
        weatherHistoryListView.setAdapter(null);
    }
}

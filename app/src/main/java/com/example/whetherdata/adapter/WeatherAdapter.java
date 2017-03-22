package com.example.whetherdata.adapter;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whetherdata.R;
import com.example.whetherdata.model.Weather;
import com.example.whetherdata.util.Utils;

import java.util.Date;
import java.util.List;

/**
 * Created by pallavshah on 5/30/15.
 */
public class WeatherAdapter extends ArrayAdapter<Weather> {
    private final Activity activity;
    private final List<Weather> weatherList;

    public WeatherAdapter(Activity activity, int resource, List<Weather> weatherList) {
        super(activity, resource, weatherList);
        this.activity = activity;
        this.weatherList = weatherList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder holder = null;
        if (rowView == null) {
            rowView = activity.getLayoutInflater().inflate(R.layout.weather_list_item, null);
            holder = new ViewHolder();
            holder.imageImageView = (ImageView) rowView.findViewById(R.id.image_imageview);
            holder.weatherTypeTextView = (TextView) rowView.findViewById(R.id.weather_type_textview);
            holder.weatherDataTextView = (TextView) rowView.findViewById(R.id.weather_data_textview);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }
        Weather weather = weatherList.get(position);
        holder.weatherTypeTextView.setText(weather.getWeatherType());
        holder.weatherDataTextView.setText(
            "Place: " + weather.getName() + "\n" +
            "Temperature: " + weather.getTemperature() + "\n" +
            "Humidity: " + weather.getHumidity() + "\n" +
            "Pressure: " + weather.getPressure() + "\n" +
            "Time: " + new Date(weather.getTime()).toString() + "\n"
        );

        String s = weather.getWeatherType();
        if("Clear".equals(s))
        {

            holder.imageImageView.setImageBitmap(Utils.getCroppedBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.clear)));

        }

        else if("Rain".equals(s))
        {
            holder.imageImageView.setImageBitmap(Utils.getCroppedBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.rain)));
        }

        else if("Clouds".equals(s))
        {
            holder.imageImageView.setImageBitmap(Utils.getCroppedBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.cloudy)));
        }

        else if("Mist".equals(s))
        {
            holder.imageImageView.setImageBitmap(Utils.getCroppedBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.mist)));

        }
        else if("Haze".equals(s))
        {
            holder.imageImageView.setImageBitmap(Utils.getCroppedBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.haze1)));
        }

        else{
            holder.imageImageView.setImageBitmap(Utils.getCroppedBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.logo)));
        }
        holder.imageImageView.setVisibility(View.VISIBLE);
        return rowView;
    }

    static class ViewHolder {
        private ImageView imageImageView;
        private TextView weatherTypeTextView;
        private TextView weatherDataTextView;
    }
}

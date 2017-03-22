package com.example.whetherdata.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whetherdata.R;
import com.example.whetherdata.dbhelper.WeatherDataHandler;
import com.example.whetherdata.model.Weather;
import com.example.whetherdata.util.GPSService;
import com.example.whetherdata.util.JSONParser;
import com.example.whetherdata.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {

    double lat;
    double lon;
    RelativeLayout root;
    Animation fadein;
    ImageView imageImageView;
    TextView weatherTypeTextView;
    TextView weatherDataTextView;
    private Button weatherHistoryButton;
    private Button refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);

        root = (RelativeLayout) findViewById(R.id.root);
        imageImageView = (ImageView) findViewById(R.id.image_imageview);
        weatherTypeTextView = (TextView) findViewById(R.id.weather_type_textview);
        weatherDataTextView = (TextView) findViewById(R.id.weather_data_textview);
        weatherHistoryButton = (Button) findViewById(R.id.weather_history_btn);
        refreshButton = (Button) findViewById(R.id.refresh_btn);

        weatherHistoryButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        refreshButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                checkWeather();
            }
        });

        if(savedInstanceState==null)
            checkWeather();
        else
            checkWeather();



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("is_checked",true);
        super.onSaveInstanceState(outState);

    }

    public void checkWeather(){
        String address = "";
        GPSService mGPSService = new GPSService(getApplicationContext());
        mGPSService.getLocation();

        if (mGPSService.isLocationAvailable == false) {

            // Here you can ask the user to try again, using return; for that
            Toast.makeText(getApplicationContext(), "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
            showNoLocationDialog(MainActivity.this);

            return;

            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
            // address = "Location not available";
        } else {

            // Getting location co-ordinates
            lat = mGPSService.getLatitude();
            lon = mGPSService.getLongitude();
            //Toast.makeText(getApplicationContext(), "Latitude:" + lat + " | Longitude: " + lon, Toast.LENGTH_LONG).show();

            address = mGPSService.getLocationAddress();

            //tvLocation.setText("Latitude: " + latitude + " \nLongitude: " + longitude);
            //tvAddress.setText("Address: " + address);
        }


        //Toast.makeText(getApplicationContext(), "Your address is: " + address, Toast.LENGTH_SHORT).show();

        // make sure you close the gps after using it. Save user's battery power
        mGPSService.closeGPS();


        // Post().execute();


        // lon lat


        new Post().execute();

    }

    public class Post extends AsyncTask<String, Weather, Weather> {


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Weather doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&APPID=fd7dc33252ff1f2687689fce1c7d4c3b";
            // String url = "http://api.openweathermap.org/data/2.5/weather?lat=54&lon=54&APPID=fd7dc33252ff1f2687689fce1c7d4c3b";
            Weather w = null;
            JSONParser json = new JSONParser();
            try{
                JSONObject jsonobj = json.getJSONFromUrl(url);
                Log.e("json", url);
                JSONObject mainobj;


                String name = jsonobj.getString("name");

                mainobj = jsonobj.getJSONObject("main");
                String temp = mainobj.getString("temp");
                String pressure = mainobj.getString("pressure");
                String humidity = mainobj.getString("humidity");

                JSONArray weather = jsonobj.getJSONArray("weather");
                    /*for(int i=0;i<weather.length();i++){
						weatherobj = weather.getJSONObject(i);
					}*/
                String main = weather.getJSONObject(0).getString("main");

                w = new Weather(main, name, humidity, pressure, temp, Calendar.getInstance().getTimeInMillis());
                new WeatherDataHandler(MainActivity.this).add(w);
                //Log.e("Weathers stored", new WeatherDataHandler(MainActivity.this).getWeatherCount() + " count");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NullPointerException e){
                return null;
            } catch (IOException e) {
                return null;
            }
            return w;
        }



        @Override
        protected void onPostExecute(Weather weather) {
            // TODO Auto-generated method stub
            if(weather==null){
                Toast.makeText(MainActivity.this,"Internet not active!",Toast.LENGTH_LONG).show();
                showNoConnectionDialog(MainActivity.this);
                return;
            }
            weatherTypeTextView.setText(weather.getWeatherType());
            double farenheit = Double.parseDouble( weather.getTemperature());
            double a = farenheit*9/5-459.67;
            a =Double.parseDouble(new DecimalFormat("##.##").format(a));

            weatherDataTextView.setText(
                    "Place: " + weather.getName() + "\n" +
                            "Temperature: " + a + "\n" +
                            "Humidity: " + weather.getHumidity() + "\n" +
                            "Pressure: " + weather.getPressure() + "\n" +
                            "Time: " + new Date(weather.getTime()).toString() + "\n"
            );

            String s = weather.getWeatherType();
            if ("Clear".equals(s)) {

                imageImageView.setImageBitmap(Utils.getCroppedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.clear)));

            } else if ("Rain".equals(s)) {
                imageImageView.setImageBitmap(Utils.getCroppedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rain)));
            } else if ("Clouds".equals(s)) {
                imageImageView.setImageBitmap(Utils.getCroppedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cloudy)));
            } else if ("Mist".equals(s)) {
                imageImageView.setImageBitmap(Utils.getCroppedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mist)));

            } else if ("Haze".equals(s)) {
                imageImageView.setImageBitmap(Utils.getCroppedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.haze1)));
            } else {
                imageImageView.setImageBitmap(Utils.getCroppedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logo)));
            }
            imageImageView.setVisibility(View.VISIBLE);
            super.onPostExecute(weather);

        }

    }

    public void showNoConnectionDialog(Context ctx1)
    {
        final Context ctx = ctx1;
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(true);
        builder.setMessage("Internet not active!");
        builder.setTitle("Activate Internet");
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();MainActivity.this.finish();
                //dialog.cancel();
                return;
            }
        });

        builder.setNeutralButton("Last Updated",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Intent intent = new Intent(MainActivity.this,HistoryActivity.class);
                startActivity(intent);
                dialog.dismiss();finish();
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();MainActivity.this.finish();
                return;
            }
        });
        builder.show();
    }

    public void showNoLocationDialog(Context ctx1)
    {
        final Context ctx = ctx1;
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(true);
        builder.setMessage("Location not available!");
        builder.setTitle("Activate GPS");
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                MainActivity.this.finish();
                //dialog.cancel();
                return;
            }
        });

        builder.setNeutralButton("Last Updated",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Intent intent = new Intent(MainActivity.this,HistoryActivity.class);
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            public void onCancel(DialogInterface dialog) {
                MainActivity.this.finish();
                dialog.dismiss();
                return;
            }
        });
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkWeather();

    }
}










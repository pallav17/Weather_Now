package com.example.whetherdata.model;

/**
 * Created by pallavshah on 5/29/15.
 */
public class Weather {

    private int _id;
    private String weatherType;
    private String name;
    private String humidity;
    private String pressure;
    private String temperature;
    private Long time;


    public Weather(String weatherType, String name, String humidity, String pressure, String temperature, Long time) {
        this.weatherType = weatherType;
        this.name = name;
        this.humidity = humidity;
        this.pressure = pressure;
        this.temperature = temperature;
        this.time = time;
    }

    public Weather(int _id, String weatherType, String name, String humidity, String pressure, String temperature, Long time) {
        this._id = _id;
        this.weatherType = weatherType;
        this.name = name;
        this.humidity = humidity;
        this.pressure = pressure;
        this.temperature = temperature;
        this.time = time;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}

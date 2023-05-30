package com.example.weather;

public class weathermodel {
    private String time;
    private String temperature;
    private String wind;
    private String icon;

    public weathermodel(String time, String temperature, String wind, String icon) {
        this.time = time;
        this.temperature = temperature;
        this.wind = wind;
        this.icon = icon;
    }

    public String getTime() {
        return time;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getWind() {
        return wind;
    }

    public String getIcon() {
        return icon;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}

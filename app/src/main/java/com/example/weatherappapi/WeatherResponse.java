package com.example.weatherappapi;

public class WeatherResponse {
    private Current current;


    public int getRelative_humidity_2m() {
        return current.relative_humidity_2m;
    }

    public double getTemperature_2m() {
        return current.temperature_2m;
    }
    public static class Current{
        public double temperature_2m;
        public int relative_humidity_2m;
    }
}

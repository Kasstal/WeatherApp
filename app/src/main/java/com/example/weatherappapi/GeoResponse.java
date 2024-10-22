package com.example.weatherappapi;

import java.io.ObjectStreamException;
import java.util.List;

public class GeoResponse {
    List<Result> results;
    public double getLatitude() {
        return results.get(0).latitude;
    }

    public double getLongitude() {
        return results.get(0).longitude;
    }
    public String getName(){
        return results.get(0).name;
    }

    public static class Result{
        public double latitude;
        public double longitude;
        public String name;
    }
}


package com.example.weatherappapi;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    EditText inputBar;
    Button btn;
    TextView tempView;
    TextView humidityView;
    TextView location;
    Retrofit retrofitCoords, retrofitWeather;
    GeoApi geoApi;
    WeatherApi weatherApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        inputBar = findViewById(R.id.city_input);
        btn = findViewById(R.id.get_weather_button);
        tempView = findViewById(R.id.temp_text);
        location = findViewById(R.id.location_text);
        humidityView = findViewById(R.id.humidity_text);
        retrofitCoords = new Retrofit.Builder()
                .baseUrl("https://geocoding-api.open-meteo.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        geoApi = retrofitCoords.create(GeoApi.class);

        retrofitWeather = new Retrofit.Builder()
                .baseUrl("https://api.open-meteo.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        weatherApi = retrofitWeather.create(WeatherApi.class);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName;
                cityName = inputBar.getText().toString();
                if(!cityName.isEmpty()){
                    fetchCoordinates(cityName);
                }
            }
        });
    }

    private void fetchCoordinates(String cityName) {
        Call<GeoResponse> geoCall = geoApi.getCoordinates(cityName, 1, "en", "json");
        geoCall.enqueue(new Callback<GeoResponse>() {
            @Override
            public void onResponse(Call<GeoResponse> call, Response<GeoResponse> response) {
                if(response.isSuccessful() && response.body() !=null){
                    double latitude = response.body().getLatitude();
                    double longitude = response.body().getLongitude();
                    location.setText(cityName);
                    fetchWeather(latitude, longitude);
                } else{
                    Toast.makeText(MainActivity.this, "Failed to get coordinates", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeoResponse> call, Throwable throwable) {
                Log.e("MainActivity", "Geocoding API call failed: " + throwable.getMessage());
                Toast.makeText(MainActivity.this, "Failed to fetch coordinates", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchWeather(double lat, double lon) {
        Call<WeatherResponse> weatherCall = weatherApi.getWeather(lat, lon, "temperature_2m,relative_humidity_2m");
        weatherCall.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    double temp = response.body().getTemperature_2m();
                    int humidity = response.body().getRelative_humidity_2m();
                    tempView.setText(String.format("Temperature: %.2f°C", temp));
                    humidityView.setText(String.format("Humidity: %d%%", humidity));
                } else{
                    Toast.makeText(MainActivity.this, "Failed to get weather data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable throwable) {
                Log.e("MainActivity", "Weather API call failed: " + throwable.getMessage());
                Toast.makeText(MainActivity.this, "Failed to fetch weather", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
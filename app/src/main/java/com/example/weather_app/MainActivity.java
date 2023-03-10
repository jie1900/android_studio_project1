package com.example.weather_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    private double temperature=20,windSpeed=5,humidity=5;
    private String description="Sunny";
    private EditText cityEditText;
    private static final int REQUEST_CODE_CITY_SELECTION = 1;
    private TextView cityTextView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue= Volley.newRequestQueue(this);

        if(savedInstanceState!=null){
            description=savedInstanceState.getString("WEATHER_DESCRIPTION");
            if(description ==null){
                description="Click to refresh!";
            }
            temperature=savedInstanceState.getDouble("TEMPERATURE",20);
            humidity=savedInstanceState.getDouble("HUMIDITY",5);
            windSpeed=savedInstanceState.getDouble("WIND",5);
        }
        TextView descriptionText=findViewById(R.id.descriptionText);
        descriptionText.setText(description);
        TextView temperatureText=findViewById(R.id.temperatureText);
        temperatureText.setText(""+temperature+"C");
        TextView humidityText=findViewById(R.id.humidityView);
        humidityText.setText("humidity:"+humidity);
        TextView windspeedText=findViewById(R.id.windspeedText);
        windspeedText.setText(""+windSpeed+"m/s");
        cityTextView = findViewById(R.id.cityTextView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("WEATHER_DESCRIPTION",description);
        outState.putDouble("TEMPERATURE",temperature);
        outState.putDouble("HUMIDITY",humidity);
        outState.putDouble("WIND",windSpeed);
    }

    public void fetchWeatherData(View view) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=tampere&units=metric&appid=41d79d4453b17509dc28d0dacd830c52";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Display the first 500 characters of the response string.
                    Log.d("Weather_app",response);
                    parseJsonAndUpdateUI(response);
                }, error -> {
            Log.d("Weather_app",error.toString());
        });
        queue.add(stringRequest);
    }


    private void parseJsonAndUpdateUI(String response) {
        try {
            JSONObject weatherResponse=new JSONObject(response);

            temperature=weatherResponse.getJSONObject("main").getDouble("temp");
            windSpeed=weatherResponse.getJSONObject("wind").getDouble("speed");
            humidity=weatherResponse.getJSONObject("main").getDouble("humidity");
            description=weatherResponse.getJSONArray("weather").getJSONObject(0).getString("description");

            TextView descriptionText=findViewById(R.id.descriptionText);
            descriptionText.setText(description);
            TextView temperatureText=findViewById(R.id.temperatureText);
            temperatureText.setText(""+temperature+"C");
            TextView humidityText=findViewById(R.id.humidityView);
            humidityText.setText("humidity:"+humidity);
            TextView windspeedText=findViewById(R.id.windspeedText);
            windspeedText.setText(""+windSpeed+"m/s");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void openWebPage(View view) {
        String url="https://www.leagueoflegends.com";
        Uri uri=Uri.parse(url);
        Intent openwebpage=new Intent(Intent.ACTION_VIEW,uri);

        try {
            startActivity(openwebpage);
        }catch (ActivityNotFoundException e){

        }
    }

    public void setAlarm(View view) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER)
                .putExtra(AlarmClock.EXTRA_MESSAGE, "Time out!")
                .putExtra(AlarmClock.EXTRA_LENGTH, 15)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, false);
        try {
            startActivity(intent);
        }catch (ActivityNotFoundException e){

        }
    }

    public void setLocation(View view) {
        Uri gmmIntentUri = Uri.parse("geo:latitude,longitude?q=your+location");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mapIntent);

    }

    public void MakeACall(View view) {
        String phoneNumber = "tel:" + "012233445";
        Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(dialIntent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
    }

    public void goToCitySelectionPage(View view) {
        Intent intent = new Intent(this, CitySelectionActivity.class);
        startActivityForResult(intent, REQUEST_CODE_CITY_SELECTION);
    }


    public void fetchWeatherData2(String url) {
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Display the first 500 characters of the response string.
                    Log.d("Weather_app",response);
                    parseJsonAndUpdateUI(response);
                }, error -> {
            Log.d("Weather_app",error.toString());
        });
        queue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CITY_SELECTION && resultCode == RESULT_OK) {
            String cityName = data.getStringExtra("cityName");
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&units=metric&appid=41d79d4453b17509dc28d0dacd830c52";
            fetchWeatherData2(url);
            cityTextView.setText(cityName);
        }
    }


}
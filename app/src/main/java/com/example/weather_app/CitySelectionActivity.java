package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CitySelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_selection);
    }
    public void submitCitySelection(View view) {
        EditText cityEditText = findViewById(R.id.cityEditText);
        String cityName = cityEditText.getText().toString().trim();
        if (TextUtils.isEmpty(cityName)) {
            Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent();
            intent.putExtra("cityName", cityName);
            setResult(RESULT_OK, intent);
            finish();
        }
    }


}
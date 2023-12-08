package com.example.assignment6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.*;
public class MainActivity extends AppCompatActivity {

    private ImageView currentweatherIconImageView;
    private TextView currenttemperatureTextView;
    private TextView currentweatherTypeTextView;
    private TextView todayHighLowTextView;
    private TextView currentFeelsLikeTextView;
    private TextView currentPrecipitationTextView;
    private TextView currentWindSpeedTextView;
    private TextView currentHumidityTextView;
    private int hour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hour = current_time();
        setContentView(R.layout.activity_main);

        String api_url = "https://api.open-meteo.com/v1/forecast?latitude=30.28&longitude=-97.76&current=temperature_2m,relative_humidity_2m,apparent_temperature,precipitation,weather_code,wind_speed_10m&hourly=temperature_2m,weather_code&daily=weather_code,temperature_2m_max,temperature_2m_min&temperature_unit=fahrenheit&wind_speed_unit=mph&precipitation_unit=inch&timezone=America%2FChicago";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, api_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject current = response.getJSONObject("current");

                    currenttemperatureTextView = findViewById(R.id.currentTemp);
                    String currentTemperature = current.getString("temperature_2m");
                    int numcurrentTemperature = (int) Math.round(Double.parseDouble(currentTemperature));
                    currenttemperatureTextView.setText(numcurrentTemperature + "°");


                    currentweatherTypeTextView = findViewById(R.id.weatherType);
                    currentweatherIconImageView = findViewById(R.id.weatherIcon);
                    String currentweatherType = current_weather_type_check(current.getString("weather_code"));
                    currentweatherTypeTextView.setText(currentweatherType);

                    JSONObject daily = response.getJSONObject("daily");
                    JSONArray dailytimeArray = daily.getJSONArray("time");
                    JSONArray dailyhightemperatureArray = daily.getJSONArray("temperature_2m_max");
                    JSONArray dailylowtemperatureArray = daily.getJSONArray("temperature_2m_min");


                    todayHighLowTextView = findViewById(R.id.curHighLow);
                    int todayHighTemperature = (int) Math.round(Double.parseDouble(dailyhightemperatureArray.get(0).toString()));
                    int todayLowTemperature = (int) Math.round(Double.parseDouble(dailylowtemperatureArray.get(0).toString()));
                    todayHighLowTextView.setText("H: " + todayHighTemperature + "°  L: " + todayLowTemperature + "°");

                    currentFeelsLikeTextView = findViewById(R.id.curFeelsLike);
                    int numcurrentFeelsLike = current.getInt("apparent_temperature");
                    currentFeelsLikeTextView.setText("Feels Like: " + numcurrentFeelsLike + "°");

                    currentPrecipitationTextView = findViewById(R.id.curPrecipitation);
                    String currentPrecipitation = current.getString("precipitation");
                    double numcurrentPrecipitation = Double.parseDouble(currentPrecipitation);
                    currentPrecipitationTextView.setText("Precipitation: " + numcurrentPrecipitation + " in");

                    currentWindSpeedTextView = findViewById(R.id.curWindSpeed);
                    String currentWindSpeed = current.getString("wind_speed_10m");
                    double numcurrentWindSpeed = Double.parseDouble(currentWindSpeed);
                    currentWindSpeedTextView.setText("Wind Speed: " + numcurrentWindSpeed + " mph");

                    currentHumidityTextView = findViewById(R.id.curHumidity);
                    int numcurrentHumidity = current.getInt("relative_humidity_2m");
                    currentHumidityTextView.setText("Humidity: " + numcurrentHumidity + "%");

                    JSONObject hourly = response.getJSONObject("hourly");
                    set_hourly(hourly);

                    JSONArray daily_weather_code = daily.getJSONArray("weather_code");
                    set_daily(dailyhightemperatureArray, dailylowtemperatureArray, daily_weather_code, dailytimeArray);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Handle
            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private String current_weather_type_check(String code) {
        String weather = "";
        if (code.equals("0")) {
            weather = "Clear";
            if (hour >= 6 && hour <= 17) {
                currentweatherIconImageView.setImageResource(R.drawable.clear_day);
            } else {
                currentweatherIconImageView.setImageResource(R.drawable.clear_night);
            }
        }
        if (code.equals("1")) {
            weather = "Mostly Clear";
            if (hour >= 6 && hour <= 17) {
                currentweatherIconImageView.setImageResource(R.drawable.clear_day);
            } else {
                currentweatherIconImageView.setImageResource(R.drawable.clear_night);
            }
        }
        if (code.equals("2")) {
            weather = "Partly Cloudy";
            if (hour >= 6 && hour <= 17) {
                currentweatherIconImageView.setImageResource(R.drawable.partly_cloudy_day);
            } else {
                currentweatherIconImageView.setImageResource(R.drawable.partly_cloudy_night);
            }
        }
        if (code.equals("3")) {
            weather = "Overcast";
            if (hour >= 6 && hour <= 17) {
                currentweatherIconImageView.setImageResource(R.drawable.overcast_day);
            } else {
                currentweatherIconImageView.setImageResource(R.drawable.overcast_night);
            }
        }
        if (code.equals("45") || code.equals("48")) {
            weather = "Foggy";
            if (hour >= 6 && hour <= 17) {
                currentweatherIconImageView.setImageResource(R.drawable.foggy_day);
            } else {
                currentweatherIconImageView.setImageResource(R.drawable.foggy_night);
            }
        }
        if (code.equals("51")) {
            weather = "Light Drizzle";
            currentweatherIconImageView.setImageResource(R.drawable.drizzle);
        }
        if (code.equals("53")) {
            weather = "Moderate Drizzle";
            currentweatherIconImageView.setImageResource(R.drawable.drizzle);
        }
        if (code.equals("55")) {
            weather = "Intense Drizzle";
            currentweatherIconImageView.setImageResource(R.drawable.drizzle);
        }
        if (code.equals("56")) {
            weather = "Light Freezing Drizzle";
            currentweatherIconImageView.setImageResource(R.drawable.freezing_drizzle_rain);
        }
        if (code.equals("57")) {
            weather = "Intense Freezing Drizzle";
            currentweatherIconImageView.setImageResource(R.drawable.freezing_drizzle_rain);
        }
        if (code.equals("61")) {
            weather = "Light Rain";
            currentweatherIconImageView.setImageResource(R.drawable.light_moderate_rain_showers);
        }
        if (code.equals("63")) {
            weather = "Moderate Rain";
            currentweatherIconImageView.setImageResource(R.drawable.light_moderate_rain_showers);
        }
        if (code.equals("65")) {
            weather = "Heavy Rain";
            currentweatherIconImageView.setImageResource(R.drawable.heavy_rain_showers);
        }
        if (code.equals("66")) {
            weather = "Light Freezing Rain";
            currentweatherIconImageView.setImageResource(R.drawable.freezing_drizzle_rain);
        }
        if (code.equals("67")) {
            weather = "Heaving Freezing Rain";
            currentweatherIconImageView.setImageResource(R.drawable.freezing_drizzle_rain);
        }
        if (code.equals("71")) {
            weather = "Light Snow Fall";
            currentweatherIconImageView.setImageResource(R.drawable.light_moderate_snow_fall_grain_showers);
        }
        if (code.equals("73")) {
            weather = "Moderate Snow Fall";
            currentweatherIconImageView.setImageResource(R.drawable.light_moderate_snow_fall_grain_showers);
        }
        if (code.equals("75")) {
            weather = "Heavy Snow Fall";
            currentweatherIconImageView.setImageResource(R.drawable.heavy_snow_fall_showers);
        }
        if (code.equals("77")) {
            weather = "Snow Grains";
            currentweatherIconImageView.setImageResource(R.drawable.light_moderate_snow_fall_grain_showers);
        }
        if (code.equals("80")) {
            weather = "Light Rain Showers";
            currentweatherIconImageView.setImageResource(R.drawable.light_moderate_rain_showers);
        }
        if (code.equals("81")) {
            weather = "Moderate Rain Showers";
            currentweatherIconImageView.setImageResource(R.drawable.light_moderate_rain_showers);
        }
        if (code.equals("82")) {
            weather = "Violent Rain Showers";
            currentweatherIconImageView.setImageResource(R.drawable.heavy_rain_showers);
        }
        if (code.equals("85")) {
            weather = "Light Snow Showers";
            currentweatherIconImageView.setImageResource(R.drawable.light_moderate_snow_fall_grain_showers);
        }
        if (code.equals("86")) {
            weather = "Heavy Snow Showers";
            currentweatherIconImageView.setImageResource(R.drawable.heavy_snow_fall_showers);
        }
        return weather;
    }

    private int current_time() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    private void set_daily(JSONArray dailyhighTemps, JSONArray dailylowTemps, JSONArray daily_weather_code, JSONArray dates) throws JSONException {
        TextView date_disp_TV;
        ImageView pic_disp_TV;
        TextView temps2_disp_tv;
        int high;
        int low;
        int index = 0;
        String date = "";

        date_disp_TV = findViewById(R.id.date1);
        date_disp_TV.setText("Today");

        pic_disp_TV = findViewById(R.id.dailyicon1);
        hourly_weather_type_check(daily_weather_code.get(index).toString(), pic_disp_TV);

        temps2_disp_tv = findViewById(R.id.dailyHL1);
        high = (int) Math.round(Double.parseDouble(dailyhighTemps.get(index).toString()));
        low = (int) Math.round(Double.parseDouble(dailylowTemps.get(index).toString()));
        temps2_disp_tv.setText("H: " + high + "°  L:" + low + "°");
        index++;

        date_disp_TV = findViewById(R.id.date2);
        date = dates.get(index).toString().substring(5, 10);
        date_disp_TV.setText(date);

        pic_disp_TV = findViewById(R.id.dailyicon2);
        hourly_weather_type_check(daily_weather_code.get(index).toString(), pic_disp_TV);

        temps2_disp_tv = findViewById(R.id.dailyHL2);
        high = (int) Math.round(Double.parseDouble(dailyhighTemps.get(index).toString()));
        low = (int) Math.round(Double.parseDouble(dailylowTemps.get(index).toString()));
        temps2_disp_tv.setText("H: " + high + "°  L:" + low + "°");
        index++;

        date_disp_TV = findViewById(R.id.date3);
        date = dates.get(index).toString().substring(5, 10);
        date_disp_TV.setText(date);

        pic_disp_TV = findViewById(R.id.dailyicon3);
        hourly_weather_type_check(daily_weather_code.get(index).toString(), pic_disp_TV);

        temps2_disp_tv = findViewById(R.id.dailyHL3);
        high = (int) Math.round(Double.parseDouble(dailyhighTemps.get(index).toString()));
        low = (int) Math.round(Double.parseDouble(dailylowTemps.get(index).toString()));
        temps2_disp_tv.setText("H: " + high + "°  L:" + low + "°");
        index++;

        date_disp_TV = findViewById(R.id.date4);
        date = dates.get(index).toString().substring(5, 10);
        date_disp_TV.setText(date);

        pic_disp_TV = findViewById(R.id.dailyicon4);
        hourly_weather_type_check(daily_weather_code.get(index).toString(), pic_disp_TV);

        temps2_disp_tv = findViewById(R.id.dailyHL4);
        high = (int) Math.round(Double.parseDouble(dailyhighTemps.get(index).toString()));
        low = (int) Math.round(Double.parseDouble(dailylowTemps.get(index).toString()));
        temps2_disp_tv.setText("H: " + high + "°  L:" + low + "°");
        index++;

        date_disp_TV = findViewById(R.id.date5);
        date = dates.get(index).toString().substring(5, 10);
        date_disp_TV.setText(date);

        pic_disp_TV = findViewById(R.id.dailyicon5);
        hourly_weather_type_check(daily_weather_code.get(index).toString(), pic_disp_TV);

        temps2_disp_tv = findViewById(R.id.dailyHL5);
        high = (int) Math.round(Double.parseDouble(dailyhighTemps.get(index).toString()));
        low = (int) Math.round(Double.parseDouble(dailylowTemps.get(index).toString()));
        temps2_disp_tv.setText("H: " + high + "°  L:" + low + "°");
        index++;

        date_disp_TV = findViewById(R.id.date6);
        date = dates.get(index).toString().substring(5, 10);
        date_disp_TV.setText(date);

        pic_disp_TV = findViewById(R.id.dailyicon6);
        hourly_weather_type_check(daily_weather_code.get(index).toString(), pic_disp_TV);

        temps2_disp_tv = findViewById(R.id.dailyHL6);
        high = (int) Math.round(Double.parseDouble(dailyhighTemps.get(index).toString()));
        low = (int) Math.round(Double.parseDouble(dailylowTemps.get(index).toString()));
        temps2_disp_tv.setText("H: " + high + "°  L:" + low + "°");
        index++;

        date_disp_TV = findViewById(R.id.date7);
        date = dates.get(index).toString().substring(5, 10);
        date_disp_TV.setText(date);

        pic_disp_TV = findViewById(R.id.dailyicon7);
        hourly_weather_type_check(daily_weather_code.get(index).toString(), pic_disp_TV);

        temps2_disp_tv = findViewById(R.id.dailyHL7);
        high = (int) Math.round(Double.parseDouble(dailyhighTemps.get(index).toString()));
        low = (int) Math.round(Double.parseDouble(dailylowTemps.get(index).toString()));
        temps2_disp_tv.setText("H: " + high + "°  L:" + low + "°");
        index++;
    }
    private void set_hourly(JSONObject hourly) throws JSONException {
        TextView hour_disp_TV;
        ImageView pic_disp_TV;
        TextView temp_disp_TV;
        int hour_temp;

        JSONArray hourlyTemperatureArray = hourly.getJSONArray("temperature_2m");
        JSONArray hourlyCodeArray = hourly.getJSONArray("weather_code");

        int hour_index = hour;

        hour_disp_TV = findViewById(R.id.hour_time_01);
        hour_disp_TV.setText("Now");

        pic_disp_TV = findViewById(R.id.hour_pic_01);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_01);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_02);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_02);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_02);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_03);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_03);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_03);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_04);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_04);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_04);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_05);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_05);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_05);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_06);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_06);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_06);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_07);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_07);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_07);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_08);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_08);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_08);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_09);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_09);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_09);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_10);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_10);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_10);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_11);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_11);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_11);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_12);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_12);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_12);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_13);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_13);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_13);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_14);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_14);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_14);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_15);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_15);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_15);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_16);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_16);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_16);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_17);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_17);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_17);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_18);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_18);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_18);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_19);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_19);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_19);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_20);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_20);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_20);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_21);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_21);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_21);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_22);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_22);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_22);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_23);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_23);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_23);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_24);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_24);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_24);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_25);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_25);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_25);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_26);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_26);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_26);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_27);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_27);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_27);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_28);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_28);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_28);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_29);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_29);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_29);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_30);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_30);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_30);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_31);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_31);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_31);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_32);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_32);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_32);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_33);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_33);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_33);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_34);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_34);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_34);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_35);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_35);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_35);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_36);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_36);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_36);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_37);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_37);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_37);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_38);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_38);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_38);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_39);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_39);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_39);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_40);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_40);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_40);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_41);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_41);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_41);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_42);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_42);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_42);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_43);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_43);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_43);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_44);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_44);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_44);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_45);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_45);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_45);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_46);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_46);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_46);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_47);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_47);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_47);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

        if(hour == 23){
            hour = -1;
        }
        hour++;
        hour_index++;
        hour_disp_TV = findViewById(R.id.hour_time_48);
        hour_disp_TV.setText(hour + ":00");

        pic_disp_TV = findViewById(R.id.hour_pic_48);
        hourly_weather_type_check(hourlyCodeArray.get(hour_index).toString(), pic_disp_TV);

        temp_disp_TV = findViewById(R.id.hour_temp_48);
        hour_temp = (int) Math.round(Double.parseDouble(hourlyTemperatureArray.get(hour_index).toString()));
        temp_disp_TV.setText(hour_temp + "°");

    }

    private void hourly_weather_type_check(String code, ImageView weather_pic) {
        if (code.equals("0")) {
            if (hour >= 6 && hour <= 17) {
                weather_pic.setImageResource(R.drawable.clear_day);
            } else {
                weather_pic.setImageResource(R.drawable.clear_night);
            }
        }
        if (code.equals("1")) {
            if (hour >= 6 && hour <= 17) {
                weather_pic.setImageResource(R.drawable.clear_day);
            } else {
                weather_pic.setImageResource(R.drawable.clear_night);
            }
        }
        if (code.equals("2")) {
            if (hour >= 6 && hour <= 17) {
                weather_pic.setImageResource(R.drawable.partly_cloudy_day);
            } else {
                weather_pic.setImageResource(R.drawable.partly_cloudy_night);
            }
        }
        if (code.equals("3")) {
            if (hour >= 6 && hour <= 17) {
                weather_pic.setImageResource(R.drawable.overcast_day);
            } else {
                weather_pic.setImageResource(R.drawable.overcast_night);
            }
        }
        if (code.equals("45") || code.equals("48")) {
            if (hour >= 6 && hour <= 17) {
                weather_pic.setImageResource(R.drawable.foggy_day);
            } else {
                weather_pic.setImageResource(R.drawable.foggy_night);
            }
        }
        if (code.equals("51")) {
            weather_pic.setImageResource(R.drawable.drizzle);
        }
        if (code.equals("53")) {
            weather_pic.setImageResource(R.drawable.drizzle);
        }
        if (code.equals("55")) {
            weather_pic.setImageResource(R.drawable.drizzle);
        }
        if (code.equals("56")) {
            weather_pic.setImageResource(R.drawable.freezing_drizzle_rain);
        }
        if (code.equals("57")) {
            weather_pic.setImageResource(R.drawable.freezing_drizzle_rain);
        }
        if (code.equals("61")) {
            weather_pic.setImageResource(R.drawable.light_moderate_rain_showers);
        }
        if (code.equals("63")) {
            weather_pic.setImageResource(R.drawable.light_moderate_rain_showers);
        }
        if (code.equals("65")) {
            weather_pic.setImageResource(R.drawable.heavy_rain_showers);
        }
        if (code.equals("66")) {
            weather_pic.setImageResource(R.drawable.freezing_drizzle_rain);
        }
        if (code.equals("67")) {
            weather_pic.setImageResource(R.drawable.freezing_drizzle_rain);
        }
        if (code.equals("71")) {
            weather_pic.setImageResource(R.drawable.light_moderate_snow_fall_grain_showers);
        }
        if (code.equals("73")) {
            weather_pic.setImageResource(R.drawable.light_moderate_snow_fall_grain_showers);
        }
        if (code.equals("75")) {
            weather_pic.setImageResource(R.drawable.heavy_snow_fall_showers);
        }
        if (code.equals("77")) {
            weather_pic.setImageResource(R.drawable.light_moderate_snow_fall_grain_showers);
        }
        if (code.equals("80")) {
            weather_pic.setImageResource(R.drawable.light_moderate_rain_showers);
        }
        if (code.equals("81")) {
            weather_pic.setImageResource(R.drawable.light_moderate_rain_showers);
        }
        if (code.equals("82")) {
            weather_pic.setImageResource(R.drawable.heavy_rain_showers);
        }
        if (code.equals("85")) {
            weather_pic.setImageResource(R.drawable.light_moderate_snow_fall_grain_showers);
        }
        if (code.equals("86")) {
            weather_pic.setImageResource(R.drawable.heavy_snow_fall_showers);
        }
    }
}
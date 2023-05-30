package com.example.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ImageView bg,weathericon,search;
    private TextView cityname,temp,condition,forecast,humidity;
    private EditText searchcity;
    private RelativeLayout Rlhome;
    private RecyclerView rv;
    private ArrayList<weathermodel> weatherlist;
    private WeatherAdapter weatherAdapter;
    AlertDialog.Builder builder;
    AlertDialog dialog;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);
        bg=findViewById(R.id.bg);
        forecast=findViewById(R.id.forecast);
        humidity=findViewById(R.id.humidity);
        search=findViewById(R.id.search);
        weathericon=findViewById(R.id.weathericon);
        cityname=findViewById(R.id.cityname);
        temp=findViewById(R.id.temp);
        condition=findViewById(R.id.condition);
        searchcity=findViewById(R.id.searchcity);
        Rlhome=findViewById(R.id.Rlhome);
        rv=findViewById(R.id.rv);
        builder=new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progessbar);
        dialog= builder.create();
        weatherlist=new ArrayList<>();
        weatherAdapter=new WeatherAdapter(this,weatherlist);
        rv.setAdapter(weatherAdapter);
        String fileContents="";
        String filename = "store.txt";

        try {
            FileInputStream inputStream = openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            inputStream.close();
            fileContents = stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!fileContents.equals("")){
            dialog.show();
            weatherinfo(fileContents);
        }

        search.setOnClickListener(view -> {
            String city=searchcity.getText().toString();
            if(city.isEmpty()){
                Toast.makeText(MainActivity.this,"Enter city name",Toast.LENGTH_SHORT).show();
            }
            else{
                dialog.show();
                weatherinfo(city);
            }
        });
    }




    private void weatherinfo(String city){
        String url="http://api.weatherapi.com/v1/forecast.json?key=83b5e6db31644cf5bcd84652233005&q="+city+"&days=1&aqi=yes&alerts=yes";
        cityname.setText(city);
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url, null, response -> {
            String content="Today Weather Forecast";
            forecast.setText(content);
            weatherlist.clear();
            try{
                dialog.dismiss();
                String temperature=response.getJSONObject("current").getString("temp_c");
                temp.setText(temperature);
                int isDay=response.getJSONObject("current").getInt("is_day");
                String conditions=response.getJSONObject("current").getJSONObject("condition").getString("text");
                condition.setText(conditions);
                String winds=response.getJSONObject("current").getString("wind_kph");
                String humid=response.getJSONObject("current").getString("humidity");
                String set="Humidity:"+humid+"  Wind:"+winds+"km/h";
                humidity.setText(set);
                String conditionicon=response.getJSONObject("current").getJSONObject("condition").getString("icon");
                Picasso.get().load("http:".concat(conditionicon)).into(weathericon);
                if(isDay==1){
                    bg.setImageResource(R.drawable.bgimage);
                }
                else{
                    bg.setImageResource(R.drawable.night);
                }
                JSONObject forecast=response.getJSONObject("forecast");
                JSONObject forecast1=forecast.getJSONArray("forecastday").getJSONObject(0);
                JSONArray hour=forecast1.getJSONArray("hour");
                for(int i=0;i<hour.length();i++){
                    JSONObject hourobj=hour.getJSONObject(i);
                    String time=hourobj.getString("time");
                    String temper=hourobj.getString("temp_c");
                    String windsp=hourobj.getString("wind_kph");
                    String forecasticon=hourobj.getJSONObject("condition").getString("icon");
                    weatherlist.add(new weathermodel(time,temper,windsp,forecasticon));
                }
                weatherAdapter.notifyDataSetChanged();
                String filename = "store.txt";
                String fileContents = city;
                try {
                    FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(fileContents.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }, error -> {
            dialog.dismiss();
            weatherlist.clear();
            weatherAdapter.notifyDataSetChanged();
            String set="";
            humidity.setText(set);
            temp.setText(set);
            condition.setText(set);
            forecast.setText(set);
            cityname.setText("Invalid city");

            Toast.makeText(MainActivity.this, "Please enter valid city name", Toast.LENGTH_SHORT).show();
            //Log.e("ERROR : ", error.getMessage());
        });
        requestQueue.add(jsonObjectRequest);
    }
}
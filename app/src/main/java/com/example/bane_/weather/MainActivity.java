package com.example.bane_.weather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {

    EditText cityField;
    TextView lastUpdate;
    ImageView weatherIcon;
    TextView informations;
    TextView temperature;
    Button check;
    SharedPreferences storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityField = (EditText) findViewById(R.id.city_field);
        lastUpdate = (TextView) findViewById(R.id.lastUpdate);
        weatherIcon = (ImageView) findViewById(R.id.imageView);
        informations = (TextView) findViewById(R.id.informations);
        temperature = (TextView) findViewById(R.id.temperature);
        check = (Button) findViewById(R.id.checkWeather);
        storage = getSharedPreferences("storage", MODE_PRIVATE);

        cityField.setText(storage.getString("cityfield", ""));
        lastUpdate.setText(storage.getString("lastupdate", ""));
        informations.setText(storage.getString("informations", ""));
        temperature.setText(storage.getString("temperature", ""));
        Picasso.with(MainActivity.this).load("http://openweathermap.org/img/w/" + storage.getString("picassa","")+ ".png").into(weatherIcon);


    }

    public void onClick(View view) {
        new GetListOfCountryNames().execute(("http://api.openweathermap.org/data/2.5/weather?q=" + cityField.getText().toString() + "&units=metric&appid=6a5fa1454fff68ccdfd50f1d6baa8c07"));
    }
    public void newIntent(View view){
        final Intent intent = new Intent(this, Second_activity.class);
        startActivity(intent);
    }

    private class GetListOfCountryNames extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String responseData = "";
            String link=params[0];
            try {
                // Creating http request
                URL obj = new URL(link);

                //   URL obj = new URL("http://samples.openweathermap.org/data/2.5/weather?q=London&appid=b1b15e88fa797225412429c1c50c122");

                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                //add request header
                con.setRequestMethod("GET");


                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                while ((line = br.readLine()) != null)
                    responseData += line;
                con.disconnect();

                int responseCode = con.getResponseCode();

                System.out.println("Response Code : " + responseCode);
                System.out.println("Response Data: " + responseData);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseData;
        }

        @Override
        protected void onPostExecute(String result) {

            try {

                SharedPreferences.Editor edit = storage.edit();
                JSONObject response = new JSONObject(result);
                JSONArray json = response.getJSONArray("weather");
                JSONObject main = response.getJSONObject("main");
                JSONObject weather = json.getJSONObject(0);

                temperature.setText(response.getJSONObject("main").getString("temp") + "℃");
                informations.setText("Max temperature "+main.getString("temp_max")+'\n'+"Min temperature "+main.getString("temp_min"));
                curentTime(lastUpdate);
                Picasso.with(MainActivity.this).load("http://openweathermap.org/img/w/" + weather.getString("icon") + ".png").into(weatherIcon);

                edit.putString("cityfield", cityField.getText().toString());
                edit.putString("lastupdate", lastUpdate.getText().toString());
                edit.putString("informations", informations.getText().toString());
                edit.putString("temperature", temperature.getText().toString());
                edit.putString( "picassa", weather.getString("icon"));
                edit.apply();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void curentTime(TextView text) {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        text.setText("Last time updated : " + formattedDate);
    }
}

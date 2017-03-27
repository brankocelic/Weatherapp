package com.example.bane_.weather;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.Calendar;

public class Second_activity extends AppCompatActivity {

    ImageView imageDay1, imageDay2, imageDay3, imageDay4, imageDay5, imageDay6, imageDay7;
    TextView descriptionDay1, descriptionDay2, descriptionDay3, descriptionDay4, descriptionDay5, descriptionDay6, descriptionDay7;
    TextView tempDay1, tempDay2, tempDay3, tempDay4, tempDay5, tempDay6, tempDay7;
    TextView city;
    SharedPreferences storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_activity);

        imageDay1 = (ImageView) findViewById(R.id.imageDay1);
        imageDay2 = (ImageView) findViewById(R.id.imageDay2);
        imageDay3 = (ImageView) findViewById(R.id.imageDay3);
        imageDay4 = (ImageView) findViewById(R.id.imageDay4);
        imageDay5 = (ImageView) findViewById(R.id.imageDay5);
        imageDay6 = (ImageView) findViewById(R.id.imageDay6);
        imageDay7 = (ImageView) findViewById(R.id.imageDay7);

        descriptionDay1 = (TextView) findViewById(R.id.descriptionDay1);
        descriptionDay2 = (TextView) findViewById(R.id.descriptionDay2);
        descriptionDay3 = (TextView) findViewById(R.id.descriptionDay3);
        descriptionDay4 = (TextView) findViewById(R.id.descriptionDay4);
        descriptionDay5=(TextView)findViewById(R.id.descriptionDay5);
        descriptionDay6 = (TextView) findViewById(R.id.descriptionDay6);
        descriptionDay7 = (TextView) findViewById(R.id.descriptionDay7);

        tempDay1 = (TextView) findViewById(R.id.tempDay1);
        tempDay2 = (TextView) findViewById(R.id.tempDay2);
        tempDay3 = (TextView) findViewById(R.id.tempDay3);
        tempDay4 = (TextView) findViewById(R.id.tempDay4);
        tempDay5 = (TextView) findViewById(R.id.tempDay5);
        tempDay6 = (TextView) findViewById(R.id.tempDay6);
        tempDay7 = (TextView) findViewById(R.id.tempDay7);

        city = (TextView) findViewById(R.id.city);

        storage = getSharedPreferences("storage", MODE_PRIVATE);
       // http://api.openweathermap.org/data/2.5/forecast/daily?q=BanjaLuka&units=metric&appid=6a5fa1454fff68ccdfd50f1d6baa8c07
          new SetDayTemperatures().execute(("http://api.openweathermap.org/data/2.5/forecast/daily?q="+storage.getString("cityfield","")+"&units=metric&appid=6a5fa1454fff68ccdfd50f1d6baa8c07"));
     //   new SetDayTemperatures().execute("http://api.openweathermap.org/data/2.5/forecast/daily?q=BanjaLuka&units=metric&appid=6a5fa1454fff68ccdfd50f1d6baa8c07");
    }

    private class SetDayTemperatures extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            String responseData = "";
            String link = params[0];
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
                JSONObject response = new JSONObject(result);
                JSONArray list = response.getJSONArray("list");
                ArrayList<ImageView> images = new ArrayList<>();
                ArrayList<TextView> describe = new ArrayList<>();
                ArrayList<TextView> temp = new ArrayList<>();
                images.add(imageDay1);
                describe.add(descriptionDay1);
                temp.add(tempDay1);
                images.add(imageDay2);
                describe.add(descriptionDay2);
                temp.add(tempDay2);
                images.add(imageDay3);
                describe.add(descriptionDay3);
                temp.add(tempDay3);
                images.add(imageDay4);
                describe.add(descriptionDay4);
                temp.add(tempDay4);
                images.add(imageDay5);
                describe.add(descriptionDay5);
                temp.add(tempDay5);
                images.add(imageDay6);
                describe.add(descriptionDay6);
                temp.add(tempDay6);
                images.add(imageDay7);
                describe.add(descriptionDay7);
                temp.add(tempDay7);


                for (int i = 0; i < 7; i++) {
                    setTheTemperatures doTheJob = new setTheTemperatures(describe.get(i), images.get(i), temp.get(i)); //ImageView image,  images.get(i), ;
                    JSONObject json = list.getJSONObject(i);

                    doTheJob.setValues(Second_activity.this, json);
//                    JSONArray weather = new JSONArray("weather");
//                    JSONObject picture = weather.getJSONObject(0);
//                    JSONObject temp = weatherForOneDay.getJSONObject("temp");
//
//                    Picasso.with(Second_activity.this).load("http://openweathermap.org/img/w/" + picture.getString("icon") + ".png").into(images.get(i));
//                    describe.get(i).setText(picture.getString("description"));
//                    temp.get(i).setText("Morning: "+ temp.getString("morn") +'\n'+ "Day"+ temp.getString("day")+'\n'+"Evening: "+temp.getString("eve"));
                }
                city.setText(storage.getString("cityfield", ""));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}

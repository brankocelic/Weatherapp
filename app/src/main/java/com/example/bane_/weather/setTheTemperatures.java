package com.example.bane_.weather;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bane- on 3/24/2017.
 */

public class setTheTemperatures {
    TextView description;
    ImageView image;
    TextView temperature;

    public setTheTemperatures(TextView description,ImageView image, TextView temperature) {
        this.description = description;
        this.image = image;
        this.temperature = temperature;
    }

    public void setValues(Context context, JSONObject weatherForOneDay) throws JSONException {
        try {
            JSONArray weather = weatherForOneDay.getJSONArray("weather");
            JSONObject picture = weather.getJSONObject(0);
            JSONObject temp = weatherForOneDay.getJSONObject("temp");

            Picasso.with(context).load("http://openweathermap.org/img/w/" + picture.getString("icon") + ".png").into(image);
            description.setText(picture.getString("description"));
            temperature.setText("Morning: "+ temp.getString("morn") +'\n'+ "Day: "+ temp.getString("day")+'\n'+"Evening: "+temp.getString("eve"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

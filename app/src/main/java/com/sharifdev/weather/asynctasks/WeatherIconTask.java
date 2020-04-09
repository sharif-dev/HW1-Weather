package com.sharifdev.weather.asynctasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.InputStream;
import java.util.Objects;

public class WeatherIconTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView bmImage;
    private float displayDensity;
    private int sizeDp;

    public WeatherIconTask(ImageView bmImage, float displayDensity, int sizeDp) {
        this.bmImage = bmImage;
        this.displayDensity = displayDensity;
        this.sizeDp = sizeDp;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", Objects.requireNonNull(e.getMessage()));
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
        final float scale = this.displayDensity;
        int dpWidthInPx = (int) (sizeDp * scale);
        int dpHeightInPx = (int) (sizeDp * scale);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dpWidthInPx, dpHeightInPx);
        bmImage.setLayoutParams(layoutParams);
    }
}

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

    public WeatherIconTask(ImageView bmImage, float displayDensity) {
        this.bmImage = bmImage;
        this.displayDensity = displayDensity;
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
        int dpWidthInPx = (int) (64 * scale);
        int dpHeightInPx = (int) (64 * scale);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dpWidthInPx, dpHeightInPx);
        bmImage.setLayoutParams(layoutParams);
    }
}

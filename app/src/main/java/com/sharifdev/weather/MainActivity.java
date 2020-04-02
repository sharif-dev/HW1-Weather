package com.sharifdev.weather;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.downloader.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;


public class MainActivity extends AppCompatActivity {

    Button mDownloadBtn;
    ProgressBar mProgressBar;
    TextView mProgressPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setting timeout globally for the download network requests:
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder().build();
        PRDownloader.initialize(getApplicationContext(), config);


        mDownloadBtn = findViewById(R.id.activity_main_download_button);
        mProgressBar = findViewById(R.id.activity_main_progress_bar);
        mProgressPercent = findViewById(R.id.activity_main_progress_percent_tv);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        final AtomicReference<DownloadTask> downloadTask = new AtomicReference<>();
        downloadTask.set(new DownloadTask());

        mDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://irsv.upmusics.com/99/Hamed%20Zamani%20%7C%20Sepidar%20(128).mp3";


                if (downloadTask.get().getStatus() == AsyncTask.Status.RUNNING) {
                    downloadTask.get().cancel(true);
                    mDownloadBtn.setText(R.string.start_download);
                } else {
                    try {
                        downloadTask.getAndSet(new DownloadTask());
                        downloadTask.get().execute(new URL(url));
                        mDownloadBtn.setText(R.string.end_download);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private class DownloadTask extends AsyncTask<URL, Integer, String> {

        private String downloadPath = MainActivity.this.getApplicationInfo().dataDir ;
        String TAG = "Download Task";
        File outputFile;

        @Override
        protected String doInBackground(URL... urls) {
            try {
                HttpURLConnection connection = (HttpURLConnection) urls[0].openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                    Log.e(TAG, "HTTP connection returned " + connection.getResponseCode() +
                            " " + connection.getResponseMessage());
                outputFile = new File(downloadPath, "download.mp3");

                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                    Log.d(TAG, "File Created in " + downloadPath);
                }
                Log.e(TAG, downloadPath);

                InputStream is =  connection.getInputStream();
                FileOutputStream fos = new FileOutputStream(outputFile);

                byte[] buffer = new byte[1024];

                int len = 0;
                long total = 0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    total += len;
                    if (isCancelled()) {
                        Log.e(TAG, "download canceled");
                        connection.disconnect();
                        fos.close();
                        is.close();
                        return downloadPath;
                    }
                    Log.i(TAG, String.valueOf((int) (100 * total / connection.getContentLength())));
                    publishProgress((int) (100 * total / connection.getContentLength()));
                }

                fos.close();
                is.close();

            } catch (Exception e) {

                e.printStackTrace();
                outputFile = null;
                Log.e(TAG, "Download Error Exception " + e.getMessage());
            }
            return downloadPath;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(), "File downloaded at " + downloadPath, Toast.LENGTH_SHORT).show();
            super.onPostExecute(s);
            mProgressBar.setVisibility(View.INVISIBLE);
            mProgressPercent.setVisibility(View.INVISIBLE);
            mDownloadBtn.setText(getString(R.string.start_download));
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressBar.setProgress(values[0]);
            mProgressPercent.setText(values[0] + "%");
        }

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressPercent.setVisibility(View.VISIBLE);
            mProgressBar.setProgress(0);
            mProgressPercent.setText("0%");
        }


        @Override
        protected void onCancelled() {
            mProgressBar.setVisibility(View.INVISIBLE);
            mProgressPercent.setVisibility(View.INVISIBLE);
            super.onCancelled();
        }
    }
}

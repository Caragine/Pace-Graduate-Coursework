package com.example.millennialnews;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadImageNews extends AsyncTask<String, Void, Bitmap> {
    @SuppressLint("StaticFieldLeak")
    ImageView imageView;

    public LoadImageNews(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        final int maxBitmapSize = 100 * 1024 * 1024;
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new Exception("Failed to connect");
            }
            InputStream is = con.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            if (bitmap.getByteCount() > maxBitmapSize) {
                bitmap = null;
            }
            is.close();
            return bitmap;
        } catch (Exception e) {
            imageView.setImageResource(R.drawable.not_found_image);
            Log.w("LoadImageNews", "Failed to load image - Default image set up!");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        try {
            if (imageView != null && bitmap != null) {
                Log.d("LoadImageNews: PostExec", "Load Image");
                imageView.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            Log.d("LoadImageNews: PostExec", "Error: Default image set up!");
            imageView.setImageResource(R.drawable.not_found_image);
        }
    }
}

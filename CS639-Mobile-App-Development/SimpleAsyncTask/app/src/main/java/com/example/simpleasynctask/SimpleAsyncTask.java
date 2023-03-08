package com.example.simpleasynctask;

import android.os.AsyncTask;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import java.util.Random;

public class SimpleAsyncTask extends AsyncTask<Void, Void, String> {

    private WeakReference<TextView> mTextView;

    SimpleAsyncTask(TextView tv) {
        mTextView = new WeakReference<>(tv);
    }

    @Override
    protected String doInBackground(Void... voids) {
        // Generate Random number bt 0-10
        Random r = new Random();
        int n = r.nextInt(11);

        //Make task take long enough that we have time to rotate phone while running
        int s = n * 200;

        // Sleep for random amount of time
        try {
            Thread.sleep(s);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Awake at last after sleeping for " + s + " milliseconds!";
    }

    protected void onPostExecute(String result) {
        mTextView.get().setText(result);
    }
}
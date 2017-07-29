package com.example.daniel.newsapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by Daniel on 7/21/2017.
 */

public class NewsJob extends JobService {
    AsyncTask mBackgroundTask;

    @Override
    public boolean onStartJob(final JobParameters job) {
        mBackgroundTask = new AsyncTask() {

            @Override
            // Before job is called/performed, show a global toast stating so.
            protected void onPreExecute() {
                Log.d("newsjob","pre job in bg");
                Toast.makeText(NewsJob.this, "News refreshed", Toast.LENGTH_SHORT).show();
                super.onPreExecute();
            }
            // Perform refresh via api in our background thread.
            @Override
            protected Object doInBackground(Object[] params) {
                Log.d("newsjob","job in bg");
                RefreshTask.refreshArticles(NewsJob.this);
                return null;
            }
            // When this job is done, set this iteration of job to false
            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);
                super.onPostExecute(o);
            }
        };

        mBackgroundTask.execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        // If there is a pending job not yet done, cancel next iteration of job
        if (mBackgroundTask != null) mBackgroundTask.cancel(false);

        return true;
    }
}

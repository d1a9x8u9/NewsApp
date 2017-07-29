package com.example.daniel.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;


/**
 * Created by Daniel on 7/21/2017.
 */

public class ScheduleUtilities {

    // NewsJob item tag name
    private static final int SCHEDULE_INTERVAL_MINUTES = 1;
    private static final int SYNC_FLEXTIME_SECONDS = 30;
    private static final String NEWS_JOB_TAG = "news_job_tag";

    private static boolean sInitialized;

    synchronized public static void scheduleRefresh(@NonNull final Context context){
        // Check if schedule has been set already, if not create job and set sInitalized to true;
        if(sInitialized) return;

        Log.d("scheduleutilities","Creating new job");
        // Driver for job, Firebase to store driver
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        // Create job with necessary conditions
        Job constraintRefreshJob = dispatcher.newJobBuilder()
                .setService(NewsJob.class)
                .setTag(NEWS_JOB_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(60,60))
                .setReplaceCurrent(true)
                .build();

        // Set job to dispatcher
        dispatcher.schedule(constraintRefreshJob);
        sInitialized = true;
    }
}
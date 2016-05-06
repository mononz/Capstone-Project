package net.mononz.paragon;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.analytics.ExceptionReporter;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import net.mononz.paragon.database.Database;

public class Paragon extends Application {

    private static String LOG_TAG = Paragon.class.getSimpleName();

    private static GoogleAnalytics analytics;
    private static Tracker mTracker;

    public static Database database;

    public static final String ASSET_PATH = "file:///android_asset/static/";
    public static final String DEVICE_ID =  "92F64085265F89AD88C43A7E749B8E5Fx";

    @Override
    public void onCreate() {
        super.onCreate();

        analytics = GoogleAnalytics.getInstance(this);

        database = new Database(this);

        mTracker = getDefaultTracker();
        if (mTracker != null) {
            Thread.UncaughtExceptionHandler myHandler = new ExceptionReporter(
                    getDefaultTracker(),                         // Currently used Tracker.
                    Thread.getDefaultUncaughtExceptionHandler(), // Current default uncaught exception handler.
                    this);                                       // Context of the application.
            // Make myHandler the new default uncaught exception handler.
            Thread.setDefaultUncaughtExceptionHandler(myHandler);
        }

    }

    synchronized private static Tracker getDefaultTracker() {
        try {
            if (mTracker == null) {
                mTracker = analytics.newTracker(R.xml.global_tracker);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mTracker;
    }

    public static void sendScreen(String screen) {
        if (!BuildConfig.DEBUG) {
            Log.i(LOG_TAG, "Hit: " + screen);
            Tracker mTracker = getDefaultTracker();
            if (mTracker != null) {
                mTracker.setScreenName(screen);
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            }
        }
    }

}
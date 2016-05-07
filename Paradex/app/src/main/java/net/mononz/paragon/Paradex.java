package net.mononz.paragon;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.android.gms.analytics.ExceptionReporter;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import net.mononz.paragon.database.Database;
import net.mononz.paragon.database.NetworkInterface;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Paradex extends Application {

    private static String LOG_TAG = Paradex.class.getSimpleName();

    private static GoogleAnalytics analytics;
    private static Tracker mTracker;
    public static Database database;
    public static NetworkInterface network;

    public final OkHttpClient client = new OkHttpClient();

    private static final String BASE_URL = "http://paragon.mononz.net/";
    public static final String ASSET_PATH = "https://storage.googleapis.com/paragon/";

    public static final String SyncPreferences = "SyncPreferences";
    public static final String LastUpdated = "last_updated";

    @Override
    public void onCreate() {
        super.onCreate();

        analytics = GoogleAnalytics.getInstance(this);

        database = new Database(this);

        if (BuildConfig.DEBUG) {
            // Add Stetho only in debug mode to analyse database in chrome inspector
            Stetho.initializeWithDefaults(this);
        }

        OkHttpClient newClient = client.newBuilder()
                .readTimeout(getResources().getInteger(R.integer.network_time_out), TimeUnit.SECONDS)
                .connectTimeout(getResources().getInteger(R.integer.network_time_out), TimeUnit.SECONDS)
                .writeTimeout(getResources().getInteger(R.integer.network_time_out), TimeUnit.SECONDS)
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request.Builder builder = originalRequest.newBuilder();

                        originalRequest = builder.build();
                        Response response = chain.proceed(originalRequest);
                        if (BuildConfig.DEBUG) {
                            String bodyString = response.body().string();
                            Log.d(LOG_TAG, String.format("%s | %s | %s | %s", originalRequest.url(), response.code(), originalRequest.headers().toString().replaceAll("\n", ""), bodyString));
                            response = response.newBuilder().body(ResponseBody.create(response.body().contentType(), bodyString)).build();
                        }
                        return response;
                    }
                })
                .build();

        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(newClient)
                .build();

        network = restAdapter.create(NetworkInterface.class);

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
            Tracker mTracker = getDefaultTracker();
            if (mTracker != null) {
                mTracker.setScreenName(screen);
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            }
        }
    }


    public boolean timeForSync() {
        SharedPreferences sharedpreferences = getSharedPreferences(SyncPreferences, Context.MODE_PRIVATE);
        long lastSyncTime = sharedpreferences.getLong(LastUpdated, 0);
        long threshold_millis  = getResources().getInteger(R.integer.sync_threshold) * 60 * 60 * 1000;
        return System.currentTimeMillis() > (lastSyncTime + threshold_millis);
    }

}
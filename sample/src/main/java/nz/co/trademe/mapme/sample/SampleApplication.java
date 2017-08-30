package nz.co.trademe.mapme.sample;

import android.app.Application;

import com.mapbox.mapboxsdk.MapboxAccountManager;

import timber.log.Timber;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        MapboxAccountManager.start(this, getResources().getString(R.string.mapbox_key));
    }
}

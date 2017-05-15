package com.phunware.advertising.sample.rvisit;

import android.app.Application;
import com.phunware.core.PwCoreSession;
import com.phunware.engagement.Engagement;
import com.phunware.engagement.log.LogLogger;

/**
 * Class that starts the location manager to listen for location based events
 */

public class MessagingApplication extends Application {

    private static final String TAG="MessagingApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        initMessaging();
    }

    private void initMessaging() {

        PwCoreSession.getInstance()
                .registerKeys(this, getString(R.string.app_id),
                        getString(R.string.access_key),
                        getString(R.string.sig_key),
                        "");

        //initialize LocationMessaging
        new Engagement.Builder(this)
                .appId(Long.parseLong(getString(R.string.app_id)))
                .addLogger(new LogLogger())
                .build();

        //start location manager to receive location based events
        Engagement.locationManager().start();
    }
}

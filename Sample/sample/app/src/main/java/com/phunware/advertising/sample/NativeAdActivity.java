package com.phunware.advertising.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.phunware.advertising.PwAdLoader;
import com.phunware.advertising.PwNativeAd;
import com.phunware.core.PwLog;

import java.util.List;


public class NativeAdActivity extends Activity {
    private static final String TAG = NativeAdActivity.class.getSimpleName();

    public static final String TYPE_CONTENT_STREAM = "nativeAdContentStream";
    public static final String TYPE_APP_WALL = "nativeAdAppWall";
    public static final String EXTRA_TYPE = "nativeAdType";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.native_ad);
        final ViewGroup container = (ViewGroup) findViewById(R.id.adContainer);

        final String adType = getIntent().getStringExtra(EXTRA_TYPE);

        // native ad zone id
        String zoneId = getResources().getString(R.string.native_zone_id);

        if (TYPE_CONTENT_STREAM.equals(adType)) {
            PwNativeAd nativeAd = PwNativeAd.getInstance(this, zoneId);
            nativeAd.setTestMode(true);

            nativeAd.setListener(new PwNativeAd.PwNativeAdListener() {
                @Override
                public void nativeAdDidLoad(PwNativeAd nativeAd) {
                    View view = PwNativeAd.GenericViews.getContentStreamView(NativeAdActivity.this, nativeAd);
                    container.addView(view);
                }

                @Override
                public void nativeAdDidFail(PwNativeAd nativeAd, String errMsg) {
                    // The ad failed to load and the errMsg describes why.
                    // Error messages are not intended for user display.
                    PwLog.e(TAG, errMsg);
                }
            });

            nativeAd.load();
        }
        else if (TYPE_APP_WALL.equals(adType)) {
            PwAdLoader<PwNativeAd> adLoader = PwAdLoader.getNativeAdLoader();
            adLoader.setZone(zoneId);
            adLoader.setTestMode(true);

            adLoader.loadAds(this, 8, new PwAdLoader.PwAdLoaderListener<PwNativeAd>() {
                @Override
                public void onSuccess(PwAdLoader loader, List<PwNativeAd> ads) {
                    View innerView = PwNativeAd.GenericViews.getAppWallView(NativeAdActivity.this, ads);
                    container.addView(innerView);
                }

                @Override
                public void onFail(PwAdLoader loader, String error) {
                    PwLog.e(TAG, error);
                }
            });
        }
    }
}

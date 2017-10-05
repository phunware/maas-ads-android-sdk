package com.phunware.advertising.sample;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.phunware.advertising.PwAdLoader;
import com.phunware.advertising.PwAdvertisingModule;
import com.phunware.advertising.PwBannerAdView;
import com.phunware.advertising.PwInterstitialAd;
import com.phunware.advertising.PwLandingPageAd;
import com.phunware.advertising.PwNativeAd;
import com.phunware.advertising.PwRewardedVideoAd;
import com.phunware.advertising.PwVideoInterstitialAd;
import com.phunware.advertising.internal.vast.RVSuccessInfo;
import com.phunware.advertising.internal.vast.TVASTRewardedVideoInfo;
import com.phunware.core.PwLog;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ExampleActivity extends AppCompatActivity {
    private final static String TAG = "AdvertisingSample";
    private static final String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int PERMISSION_WRITE_EXTERNAL_REQUEST = 101;

    private PwBannerAdView bannerAdView;
    private ViewGroup nativeAdHolder;
    private PwAdvertisingModule pwAdModule = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        bannerAdView = (PwBannerAdView) findViewById(R.id.bannerAdView);
        nativeAdHolder = (ViewGroup) findViewById(R.id.native_ad_view_placeholder);
        pwAdModule = PwAdvertisingModule.getInstance();

        // Enable debug logs during development
        PwLog.setShowLog(true);

        // Test that you've integrated properly
        // NOTE: remove this before your app goes live!
        PwAdvertisingModule.getInstance().validateSetup(this);

        requestPermission();
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Request permission
            ActivityCompat.requestPermissions(this,
                    PERMISSIONS, PERMISSION_WRITE_EXTERNAL_REQUEST);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PwAdvertisingModule.getInstance().cleanUp(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean selected = true;
        switch (item.getItemId()) {
            case R.id.action_news_feed:
                fireNativeNewsFeed();
                break;
            case R.id.action_content_wall:
                fireNativeContentWall();
                break;
            case R.id.action_content_stream:
                fireNativeContentStream();
                break;
            case R.id.action_app_wall:
                fireNativeAppWall();
                break;
            case R.id.action_icons:
                fireNativeIcons();
                break;
            case R.id.action_clean:
                fireNativeAdClean();
                break;
            case R.id.action_3up_left:
                fireNativeAd3Up(Gravity.START);
                break;
            case R.id.action_3up_center:
                fireNativeAd3Up(Gravity.CENTER_HORIZONTAL);
                break;
            case R.id.action_3up_right:
                fireNativeAd3Up(Gravity.END);
                break;
            default:
                selected = super.onOptionsItemSelected(item);
                break;
        }
        return selected;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_WRITE_EXTERNAL_REQUEST: {
                // If permission was denied then we disable caching
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    //This is how the app can disable media caching.
                    pwAdModule.setAdsCacheSize(this, 0);
                }
            }
        }
    }

    public void simpleInterstitialExample() {
        String zoneId = getString(R.string.intrs_zone_id);
        PwInterstitialAd interstitialAd = PwInterstitialAd.getInstance(this, zoneId);
        interstitialAd.show();
    }

    public void advancedInterstitialExample() {
        // Generate a customized request
        String zoneId = getString(R.string.intrs_zone_id);

        PwInterstitialAd interstitialAd = PwInterstitialAd.getInstance(this, zoneId);
        interstitialAd.setTestMode(true);
        interstitialAd.setKeywords(Arrays.asList("keyword1", "keyword2"));

        // Register for ad lifecycle callbacks
        interstitialAd.setListener(new PwInterstitialAd.PwInterstitialAdListener() {
            @Override
            public void interstitialDidLoad(PwInterstitialAd ad) {
                // show ad as soon as it's loaded
                PwLog.d(TAG, "Interstitial Did Load");
                ad.show();
            }

            @Override
            public void interstitialDidClose(PwInterstitialAd ad) {
                PwLog.d(TAG, "Interstitial Did Close");
            }

            @Override
            public void interstitialDidFail(PwInterstitialAd ad, String error) {
                PwLog.d(TAG, "Interstitial Did Fail: " + error);
            }

            @Override
            public void interstitialActionWillLeaveApplication(PwInterstitialAd ad) {
                PwLog.d(TAG, "Interstitial Will Leave App");
            }
        });

        // Load ad... we'll be notified when it's ready
        interstitialAd.load();
    }

    public void simpleVideoExample() {
        String zoneId = getString(R.string.video_zone_id);
        PwVideoInterstitialAd videoAd = PwVideoInterstitialAd.getInstance(getApplicationContext(), zoneId);
        videoAd.show();
    }


    public void advancedVideoExample() {
        // generate a customized request
        String zoneId = getString(R.string.video_zone_id);

        // register for ad lifecycle callbacks
        PwVideoInterstitialAd videoAd = PwVideoInterstitialAd.getInstance(this, zoneId);
        videoAd.setTestMode(true);
        videoAd.setPlacementType(PwVideoInterstitialAd.TYPE_ALL);
        videoAd.updateLocation(42.621535114613685, -5.595249100000046);
        videoAd.setKeywords(Arrays.asList("keyword1", "keyword2"));
        videoAd.setListener(new PwVideoInterstitialAd.PwVideoInterstitialAdListener() {
            @Override
            public void videoInterstitialDidLoad(PwVideoInterstitialAd videoInterstitialAd) {
            }

            @Override
            public void videoInterstitialDidClose(PwVideoInterstitialAd videoInterstitialAd) {

            }

            @Override
            public void videoInterstitialDidFail(PwVideoInterstitialAd videoInterstitialAd, String error) {

            }

            @Override
            public void videoInterstitialActionWillLeaveApplication(PwVideoInterstitialAd videoInterstitialAd) {

            }

            @Override
            public void onCacheCompleted(PwVideoInterstitialAd videoInterstitialAd) {
                videoInterstitialAd.show();
            }

            @Override
            public void onCacheProgress(PwVideoInterstitialAd videoInterstitialAd, int percentageCompleted) {

            }
        });

        videoAd.load();
    }

    public void simpleBannerExample() {
        String zoneId = getString(R.string.banner_zone_id);
        bannerAdView.startRequestingAdsForZone(zoneId);
    }

    public void advancedBannerExample() {
        String zoneId = getString(R.string.banner_zone_id);
        PwLog.d(TAG, "advancedBannerExample");

        // Banner rotation interval; defaults to 60 seconds.
        // bannerAdView.setAdUpdateInterval(0); // no auto rotation
        bannerAdView.setAdUpdateInterval(30); // rotate every 30 seconds.

        bannerAdView.setZone(zoneId)
                .setTestMode(true)                                  // enable during the development phase
                .setKeywords(Arrays.asList("keyword1", "keyword2")); // optional keywords for custom targeting

        // register for ad lifecycle callbacks
        bannerAdView.setListener(new PwBannerAdView.BannerAdListener() {
            @Override
            public void onReceiveBannerAd(PwBannerAdView ad) {
                PwLog.d(TAG, "Banner onReceiveBannerAd");
            }

            @Override
            public void onBannerAdError(PwBannerAdView ad, String errorMsg) {
                PwLog.d(TAG, "Banner onBannerAdError: " + errorMsg);
            }

            @Override
            public void onBannerAdFullscreen(PwBannerAdView ad) {
                PwLog.d(TAG, "Banner onBannerAdFullscreen");
            }

            @Override
            public void onBannerAdDismissFullscreen(PwBannerAdView ad) {
                PwLog.d(TAG, "Banner onBannerAdDismissFullscreen");
            }

            @Override
            public void onBannerAdLeaveApplication(PwBannerAdView ad) {
                PwLog.d(TAG, "Banner onBannerAdLeaveApplication");
            }
        });

        /**
         * Optionally set location manually.
         * double lat = 40.7787895;
         * double lng = -73.9660945;
         * bannerAdView.updateLocation(lat, lng);
         */
        // start banner rotating
        bannerAdView.startRequestingAds();
    }


    public void fireInterstitial(View sender) {
        advancedInterstitialExample();
    }

    public void fireVideoInterstitial(View sender) {
        advancedVideoExample();
    }

    public void fireBanner(View sender) {
        clearViews();
        bannerAdView.setVisibility(View.VISIBLE);
        advancedBannerExample();
    }


    public void simpleLandingPageExample() {
        String zoneId = getString(R.string.landing_page_zone_id);
        PwLandingPageAd pageAd = PwLandingPageAd.getInstance(this, zoneId);
        pageAd.show();
    }

    public void advancedLandingPagelExample() {
        // Generate a customized request
        String zoneId = getString(R.string.landing_page_zone_id);

        PwLandingPageAd pageAd = PwLandingPageAd.getInstance(this, zoneId);
        pageAd.setTestMode(true);
        pageAd.setKeywords(Arrays.asList("keyword1", "keyword2"));

        // Register for ad lifecycle callbacks
        pageAd.setListener(new PwLandingPageAd.PwPageAdListener() {
            @Override
            public void pageDidLoad(PwLandingPageAd ad) {
                // show ad as soon as it's loaded
                PwLog.d(TAG, "Page Did Load");
                ad.show();
            }

            @Override
            public void pageDidClose(PwLandingPageAd ad) {
                PwLog.d(TAG, "Page Did Close");
            }

            @Override
            public void pageDidFail(PwLandingPageAd ad, String error) {
                PwLog.d(TAG, "Page Did Fail: " + error);
            }

            @Override
            public void pageActionWillLeaveApplication(PwLandingPageAd ad) {
                PwLog.d(TAG, "Page Will Leave App");
            }
        });

        // Load ad... we'll be notified when it's ready
        pageAd.load();
    }

    public void fire3dBanner(View sender) {
        String zoneId = getString(R.string.banner_zone_id);

        PwLog.d(TAG, "3dBannerExample");
        bannerAdView.setAdUpdateInterval(30); // rotate every 30 seconds.
        bannerAdView.set3dAnimation(true); //This is importat to see the 3d animation. False is by default.

        bannerAdView.setZone(zoneId)
                .setTestMode(true)                                  // enable during the development phase
                .setKeywords(Arrays.asList("keyword1", "keyword2")); // optional keywords for custom targeting

        // Register for ad lifecycle callbacks
        bannerAdView.setListener(new PwBannerAdView.BannerAdListener() {
            @Override
            public void onReceiveBannerAd(PwBannerAdView ad) {
                PwLog.d(TAG, "Banner onReceiveBannerAd");
            }

            @Override
            public void onBannerAdError(PwBannerAdView ad, String errorMsg) {
                PwLog.d(TAG, "Banner onBannerAdError: " + errorMsg);
            }

            @Override
            public void onBannerAdFullscreen(PwBannerAdView ad) {
                PwLog.d(TAG, "Banner onBannerAdFullscreen");
            }

            @Override
            public void onBannerAdDismissFullscreen(PwBannerAdView ad) {
                PwLog.d(TAG, "Banner onBannerAdDismissFullscreen");
            }

            @Override
            public void onBannerAdLeaveApplication(PwBannerAdView ad) {
                PwLog.d(TAG, "Banner onBannerAdLeaveApplication");
            }
        });

        /**
         * Optionally set location manually.
         * double lat = 40.7787895;
         * double lng = -73.9660945;
         * bannerAdView.updateLocation(lat, lng);
         */
        // start banner rotating
        bannerAdView.startRequestingAds();
    }

    public void fireLandingPage(View sender) {
        advancedLandingPagelExample();
    }

    public void fireNativeNewsFeed() {
        clearViews();
        String zoneId = getString(R.string.native_zone_id);
        PwNativeAd nativeAd = PwNativeAd.getInstance(this, zoneId);
        nativeAd.setTestMode(true);
        nativeAd.setListener(new PwNativeAd.PwNativeAdListener() {
            @Override
            public void nativeAdDidLoad(PwNativeAd nativeAd) {
                View view = PwNativeAd.GenericViews.getNewsFeedView(ExampleActivity.this, nativeAd);
                nativeAdHolder.addView(view);
                nativeAdHolder.setVisibility(View.VISIBLE);
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

    public void fireNativeContentWall() {
        clearViews();
        String zoneId = getString(R.string.native_zone_id);
        PwNativeAd nativeAd = PwNativeAd.getInstance(this, zoneId);
        nativeAd.setTestMode(true);
        nativeAd.setListener(new PwNativeAd.PwNativeAdListener() {
            @Override
            public void nativeAdDidLoad(PwNativeAd nativeAd) {
                View view = PwNativeAd.GenericViews.getContentWallView(ExampleActivity.this, nativeAd);
                nativeAdHolder.addView(view);
                nativeAdHolder.setVisibility(View.VISIBLE);
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

    public void fireNativeContentStream() {
        Intent intent = new Intent(this, NativeAdActivity.class);
        intent.putExtra(NativeAdActivity.EXTRA_TYPE, NativeAdActivity.TYPE_CONTENT_STREAM);
        startActivity(intent);
    }

    public void fireNativeAppWall() {
        Intent intent = new Intent(this, NativeAdActivity.class);
        intent.putExtra(NativeAdActivity.EXTRA_TYPE, NativeAdActivity.TYPE_APP_WALL);
        startActivity(intent);
    }

    public void fireNativeIcons() {
        clearViews();
        String zoneId = getString(R.string.native_zone_id);
        PwAdLoader<PwNativeAd> adLoader = PwAdLoader.getNativeAdLoader();
        adLoader.setZone(zoneId);
        adLoader.setTestMode(true);

        adLoader.loadAds(this, 3, new PwAdLoader.PwAdLoaderListener<PwNativeAd>() {
            @Override
            public void onSuccess(PwAdLoader loader, List<PwNativeAd> ads) {
                View innerView = PwNativeAd.GenericViews.getIconsView(ExampleActivity.this, ads, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nativeAdHolder.setVisibility(View.GONE);
                        nativeAdHolder.removeAllViewsInLayout();
                    }
                });
                nativeAdHolder.addView(innerView);
                nativeAdHolder.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFail(PwAdLoader loader, String error) {
                PwLog.e(TAG, error);
            }
        });
    }

    public void fireNativeAdClean() {
        clearViews();
        String zoneId = getString(R.string.native_zone_id);
        PwNativeAd nativeAd = PwNativeAd.getInstance(this, zoneId);
        nativeAd.setTestMode(true);
        nativeAd.setListener(new PwNativeAd.PwNativeAdListener() {
            @Override
            public void nativeAdDidLoad(PwNativeAd nativeAd) {
                View view = PwNativeAd.GenericViews.getCleanView(ExampleActivity.this, nativeAd);
                nativeAdHolder.addView(view);
                nativeAdHolder.setVisibility(View.VISIBLE);
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

    public void fireNativeAd3Up(final int gravity) {
        clearViews();
        String zoneId = getString(R.string.native_zone_id);
        PwAdLoader<PwNativeAd> adLoader = PwAdLoader.getNativeAdLoader();
        adLoader.setZone(zoneId);
        adLoader.setTestMode(true);

        int countAds;

        if (gravity == Gravity.CENTER_HORIZONTAL) {
            countAds = 3;
        } else {
            countAds = 2;
        }

        adLoader.loadAds(this, countAds, new PwAdLoader.PwAdLoaderListener<PwNativeAd>() {
            @Override
            public void onSuccess(PwAdLoader loader, List<PwNativeAd> ads) {
                View innerView = PwNativeAd.GenericViews.get3UpView(ExampleActivity.this, ads, gravity);
                nativeAdHolder.addView(innerView);
                nativeAdHolder.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFail(PwAdLoader loader, String error) {
                PwLog.e(TAG, error);
            }
        });
    }

    public void fireRewardedVideo(View sender) {
        String zoneId = getString(R.string.rewarded_video_zone_id);
        double lat = 40.7787895;
        double lng = -73.9660945;
        HashMap<String, String> customData = new HashMap<>();
        for (int i = 0; i < 8; i++) {
            customData.put("reward" + String.valueOf(i), "gold");
            customData.put("key" + String.valueOf(i), "someValue");
        }

        PwRewardedVideoAd rewardedVideoAd = PwRewardedVideoAd.getInstance(this, zoneId);
        //To use Ads in test mode uncomment the following line
        //rewardedVideoAd.setTestMode(true);
        rewardedVideoAd.setKeywords(Arrays.asList("keyword1", "keyword2"));
        rewardedVideoAd.updateLocation(lat, lng);
        rewardedVideoAd.setUserId("5487G54d30OsdZt79");
        rewardedVideoAd.setCustomData(customData);
        rewardedVideoAd.setListener(new PwRewardedVideoAd.PwRewardedVideoAdListener() {
            @Override
            public void rewardedVideoDidLoad(PwRewardedVideoAd rewardedVideoAd, TVASTRewardedVideoInfo rewardedVideoInfo) {
            }

            @Override
            public void rewardedVideoDidClose(PwRewardedVideoAd rewardedVideoAd, TVASTRewardedVideoInfo rewardedVideoInfo) {
                PwLog.d(TAG, "Rewarded Video onCloseRewardedVideo");
            }

            @Override
            public void rewardedVideoDidFail(PwRewardedVideoAd rewardedVideoAd, String error, TVASTRewardedVideoInfo rewardedVideoInfo) {
                PwLog.d(TAG, "Rewarded Video onRewardedVideoFail"
                        + error
                        + ". Code: "
                        + String.valueOf(rewardedVideoInfo.getError()));
                showDialog(error);
            }

            @Override
            public void rewardedVideoActionWillLeaveApplication(PwRewardedVideoAd rewardedVideoAd, TVASTRewardedVideoInfo rewardedVideoInfo) {
                PwLog.d(TAG, "Rewarded Video onLeaveApplication");
            }

            @Override
            public void rewardedVideoDidEndPlaybackSuccessfully(PwRewardedVideoAd rewardedVideoAd, RVSuccessInfo rewardedVideoSuccessInfo, TVASTRewardedVideoInfo rewardedVideoInfo) {
                String message = String.format(getString(R.string.rewarded),
                        rewardedVideoSuccessInfo.getCurrencyId(),
                        rewardedVideoSuccessInfo.getAmount(),
                        rewardedVideoSuccessInfo.getRemainingViews());

                showDialog(message);
            }

            @Override
            public void onCacheCompleted(PwRewardedVideoAd rewardedVideoAd, TVASTRewardedVideoInfo rewardedVideoInfo) {
                if (rewardedVideoAd != null) {
                    rewardedVideoAd.show();
                }
            }

            @Override
            public void onCacheProgress(PwRewardedVideoAd rewardedVideoAd, int percentageCompleted) {

            }
        });

        rewardedVideoAd.load();
    }

    private void showDialog(String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(message);

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    private void clearViews() {
        if (bannerAdView != null) {
            bannerAdView.stopRequestingAds();
            bannerAdView.setVisibility(View.GONE);
        }
        if (nativeAdHolder != null) {
            nativeAdHolder.removeAllViewsInLayout();
        }
    }
}

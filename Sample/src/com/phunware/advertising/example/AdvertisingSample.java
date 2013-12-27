package com.phunware.advertising.example;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.phunware.advertising.*;
import com.phunware.core.PwCoreSession;

import java.util.HashMap;
import java.util.Map;

public class AdvertisingSample extends Activity {

    private final static String TAG = "Phunware";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Resources r = getResources();

        // Initialize MaaS Core
        PwCoreSession.getInstance().registerKeys(this,
                r.getString(R.string.app_appid),
                r.getString(R.string.app_accesskey),
                r.getString(R.string.app_signaturekey),
                r.getString(R.string.app_encryptionkey));

        // Register MaaS Advertising module
        PwCoreSession.getInstance().installModules(PwAdvertisingModule.getInstance());

        // test that you've integrated properly
        // NOTE: remove this before your app goes live!
        PwAdvertisingModule.get().validateSetup(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*
         * Start Core Session
         */
        PwCoreSession.getInstance().activityStartSession(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        /*
         * Stop Core Session
         */
        PwCoreSession.getInstance().activityStopSession(this);
    }

    public void simpleAdPromptExmple() {
        String zoneId = getResources().getString(R.string.adprompt_zone_id);
        PwAdPrompt adPrompt = PwAdvertisingModule.get().getAdPromptForZone(this, zoneId);
        adPrompt.show();
    }

    public void advancedAdPromptExample() {
        // generate a customized request
        String zoneId = getResources().getString(R.string.adprompt_zone_id);
        PwAdRequest request = PwAdvertisingModule.get().getAdRequestBuilder(zoneId)
                                                   .setTestMode(true)
                                    .getPwAdRequest();

        // get an ad instance using request
        PwAdPrompt adPrompt = PwAdvertisingModule.get().getAdPrompt(this, request);

        // register for ad lifecycle callbacks
        adPrompt.setListener(new PwAdPrompt.PwAdPromptListener() {
            @Override
            public void adPromptDidLoad(PwAdPrompt ad) {
                // show ad as soon as it's loaded
                Log.d(TAG, "AdPrompt Loaded");
                ad.show();
            }

            @Override
            public void adPromptDisplayed(PwAdPrompt ad) {
                Log.d(TAG, "Ad Prompt Displayed");
            }

            @Override
            public void adPromptDidFail(PwAdPrompt ad, String error) {
                Log.d(TAG, "Ad Prompt Error: " + error);
            }

            @Override
            public void adPromptClosed(PwAdPrompt ad, boolean didAccept) {
                String btnName = didAccept ? "YES" : "NO";
                Log.d(TAG, "Ad Prompt Closed with \"" + btnName + "\" button");
            }
        });

        // load ad... we'll be notified when it's ready
        adPrompt.load();
    }


    public void simpleInterstitialExample() {
        String zoneId = getResources().getString(R.string.intrs_zone_id);
        PwInterstitialAd interstitialAd = PwAdvertisingModule.get().getInterstitialAdForZone(this, zoneId);
        interstitialAd.show();
    }


    public void advancedInterstitialExample() {
        // generate a customized request
        String zoneId = getResources().getString(R.string.intrs_zone_id);
        PwAdRequest request = PwAdvertisingModule.get().getAdRequestBuilder(zoneId)
                                                                    .setTestMode(true)
                                                        .getPwAdRequest();

        // get an ad instance using request
        PwInterstitialAd interstitialAd = PwAdvertisingModule.get().getInterstitialAd(this, request);

        // register for ad lifecycle callbacks
        interstitialAd.setListener(new PwInterstitialAd.PwInterstitialAdListener() {
            @Override
            public void interstitialDidLoad(PwInterstitialAd ad) {
                // show ad as soon as it's loaded
                Log.d(TAG, "Interstitial Did Load");
                ad.show();
            }

            @Override
            public void interstitialDidClose(PwInterstitialAd ad) {
                Log.d(TAG, "Interstitial Did Close");
            }

            @Override
            public void interstitialDidFail(PwInterstitialAd ad, String error) {
                Log.d(TAG, "Interstitial Did Fail: " + error);
            }

            @Override
            public void interstitialActionWillLeaveApplication(PwInterstitialAd ad) {
                Log.d(TAG, "Interstitial Will Leave App");
            }
        });

        // load ad... we'll be notified when it's ready
        interstitialAd.load();
    }


    public void simpleVideoExample() {
        String zoneId = getResources().getString(R.string.video_zone_id);
        PwVideoInterstitialAd videoAd = PwAdvertisingModule.get().getVideoInterstitialAdForZone(this, zoneId);
        videoAd.show();
    }


    public void advancedVideoExample() {
        // generate a customized request
        String zoneId = getResources().getString(R.string.video_zone_id);
        PwAdRequest request = PwAdvertisingModule.get().getAdRequestBuilder(zoneId)
                                                                    .setTestMode(true)
                                                        .getPwAdRequest();

        // get an ad instance using request
        PwVideoInterstitialAd videoAd = PwAdvertisingModule.get().getVideoInterstitialAd(this, request);

        // register for ad lifecycle callbacks
        videoAd.setListener(new PwVideoInterstitialAd.PwVideoInterstitialAdListener() {
            @Override
            public void videoInterstitialDidLoad(PwVideoInterstitialAd ad) {
                // show ad as soon as it's loaded
                Log.d(TAG, "VideoAd Did Load");
                ad.show();
            }

            @Override
            public void videoInterstitialDidClose(PwVideoInterstitialAd ad) {
                Log.d(TAG, "videoInterstitialDidClose");
            }

            @Override
            public void videoInterstitialDidFail(PwVideoInterstitialAd ad, String error) {
                Log.d(TAG, "videoInterstitialDidFail: " + error);
            }

            @Override
            public void videoInterstitialActionWillLeaveApplication(PwVideoInterstitialAd ad) {
                Log.d(TAG, "videoInterstitialActionWillLeaveApplication");
            }
        });

        // load ad... we'll be notified when it's ready
        videoAd.load();
    }


    public void simpleBannerExample() {
        PwBannerAdView bannerAdView = (PwBannerAdView)findViewById(R.id.bannerAdView);
        String zoneId = getResources().getString(R.string.banner_zone_id);
        bannerAdView.startRequestingAdsForZone(zoneId);
    }

    public void advancedBannerExample() {
        Log.d(TAG, "advancedBannerExample");
        // find the view in your layout
        PwBannerAdView bannerAdView = (PwBannerAdView)findViewById(R.id.bannerAdView);

        bannerAdView.setAdUpdateInterval(0); // no auto rotation

        // generate a customized request
        String zoneId = getResources().getString(R.string.banner_zone_id);

        Map<String, String> params = new HashMap<String, String>(1);
        params.put("cid", "238409");

        PwAdRequest request = PwAdvertisingModule.get().getAdRequestBuilder(zoneId)
//                                                                    .setTestMode(true)
                                                                    .setCustomParameters(params)
                                                            .getPwAdRequest();

        // register for ad lifecycle callbacks
        bannerAdView.setListener(new PwBannerAdView.BannerAdListener() {
            @Override
            public void onReceiveBannerAd(PwBannerAdView ad) {
                Log.d(TAG, "Banner onReceiveBannerAd");
            }

            @Override
            public void onBannerAdError(PwBannerAdView ad, String errorMsg) {
                Log.d(TAG, "Banner onBannerAdError: " + errorMsg);
            }

            @Override
            public void onBannerAdFullscreen(PwBannerAdView ad) {
                Log.d(TAG, "Banner onBannerAdFullscreen");
            }

            @Override
            public void onBannerAdDismissFullscreen(PwBannerAdView ad) {
                Log.d(TAG, "Banner onBannerAdDismissFullscreen");
            }

            @Override
            public void onBannerAdLeaveApplication(PwBannerAdView ad) {
                Log.d(TAG, "Banner onBannerAdLeaveApplication");
            }
        });

        // start banner rotating
        bannerAdView.startRequestingAds(request);
    }


    public void fireAdPrompt(View sender) {
        simpleAdPromptExmple();
//        advancedAdPromptExample();
    }

    public void fireInterstitial(View sender) {
        simpleInterstitialExample();
//        advancedInterstitialExample();
    }

    public void fireVideoInterstitial(View sender) {
        simpleVideoExample();
//        advancedVideoExample();
    }

    public void fireBanner(View sender) {
//        simpleBannerExample();
        advancedBannerExample();
    }
}

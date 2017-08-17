
<!--- make sure you update the package-info file as well! --->

MaaS Advertising SDK for Android
================

Version 2.4.3

This is Phunware's Android SDK for the MaaS Advertising module. Visit http://maas.phunware.com/ for more details and to sign up.



Requirements
------------
- Android SDK 4.0.3 (API level 15) or above
- Google Play Services to enable Advertising ID support (recommended); installation instructions [here](https://developer.android.com/google/play-services/id.html).


Getting Started
---------------

- [Download the MaaS Advertising SDK](https://github.com/phunware/maas-ads-android-sdk/archive/master.zip) and run the included sample app.
- Continue reading below for installation and integration instructions.
- Be sure to read the [documentation](http://phunware.github.io/maas-ads-android-sdk/) for additional details.



Installation
------------

To use Advertising SDK include the following dependency:
````
compile ('com.phunware.advertising:ads:2.4.3:release@aar'){
        transitive = true;
}
````

Update your `AndroidManifest.xml` to include the following permissions and activity:

````xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>

<!-- Inside of the application tag: -->
<activity
    android:name="com.phunware.advertising.internal.PwAdActivity"
    android:configChanges="keyboard|keyboardHidden|orientation|screenSize" />

````
See [AndroidManifest.xml](https://github.com/phunware/maas-ads-android-sdk/blob/master/Sample/sample/app/src/main/AndroidManifest.xml) for an example manifest file.

Documentation
------------

Documentation is included in HTML format in the `Docs` folder.



Integration
-----------

Ads SDK supports caching to internal storage which does not require any permissions.

It also supports Rewarded Visit and its ad experience, which utilizes the Mobile Engagement SDK to coordinate geofence entry campaigns with Rewarded Video advertisement campaigns.

Please see https://developer.phunware.com/pages/viewpage.action?pageId=3411004 for more details on integrating this feature.

For using all other Ad types see https://developer.phunware.com/pages/viewpage.action?pageId=3408830


The primary methods in MaaS Advertising involve displaying the various ad types:


### Banner Usage

Banners are inline ads that are shown alongside your app's interface.

**NOTE**: For XML usage only.
Add this to your layout xml:
````xml
<!-- Add a banner to your layout xml. -->
<!-- This will cause an ad to be created, which will automatically kick off ad rotation. -->
<com.phunware.advertising.PwBannerAdView
       android:id="@+id/bannerAd"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"
       app:zone="YOUR_ZONE_ID"
       app:auto_load="true" />
````
Or add this to your layout xml: (note that "zone" is not specified)
````xml
<!-- Add a banner to your layout xml. -->
<!-- This will cause a 320x50 ad to be created. -->
<com.phunware.advertising.PwBannerAdView
    android:id="@+id/bannerAd"
    android:layout_width="320dp"
    android:layout_height="50dp" />
````

Add this to your activity:
````java
import com.phunware.advertising.*;

// ...

PwBannerAdView bannerAdView = (PwBannerAdView)findViewById(R.id.bannerAd);
bannerAdView.startRequestingAdsForZone("YOUR_BANNER_ZONE_ID");

bannerAdView.set3dAnimation(true); // set true if you want the rotation with a 3D animation. False by default.
````

An advanced implementation can be found in the [example code](https://github.com/phunware/maas-ads-android-sdk/blob/master/Sample/sample/app/src/main/java/com/phunware/advertising/sample/ExampleActivity.java).


### Interstitial Usage

Interstitials are best used at discrete stopping points in your app's flow, such as at the end of a game level or when the player dies.

*Example Usage*
````java
import com.phunware.advertising.*;

// ...

PwInterstitialAd interstitialAd = PwInterstitialAd.getInstance(this, "YOUR_INTERSTITIAL_ZONE_ID");
interstitialAd.show();
````

An advanced implementation can be found in the [example code](https://github.com/phunware/maas-ads-android-sdk/blob/master/Sample/sample/app/src/main/java/com/phunware/advertising/sample/ExampleActivity.java).


### Landing Page Usage

Landing pages are used to display a fullscreen URL in your app.

*Example Usage*
````java
import com.phunware.advertising.*;

// ...

PwLandingPageAd landingPage = PwLandingPageAd.getInstance(this, "YOUR_ZONE_ID");
landingPage.show();
````

An advanced implementation can be found in the [example code](https://github.com/phunware/maas-ads-android-sdk/blob/master/Sample/sample/app/src/main/java/com/phunware/advertising/sample/ExampleActivity.java).


### Video Ads Usage

Video ads are interstitial ads that play a video. They are best used at discrete
stopping points in your app's flow, such as at the end of a game level or when the player dies.

*Example Usage*
````java
import com.phunware.advertising.*;

// ...

PwVideoInterstitialAd videoAd = PwVideoInterstitialAd.getInstance(this, "YOUR_VIDEO_ZONE_ID");
videoAd.show();
````
An advanced implementation can be found in the [example code](https://github.com/phunware/maas-ads-android-sdk/blob/master/Sample/sample/app/src/main/java/com/phunware/advertising/sample/ExampleActivity.java).


### Rewarded Video Ad
Rewarded Video ads are interstitial ads that play a video and reward the user after see the video. They are best used on games making the user wants to see an Ad to be rewarded.

*Example Usage*
````java
import com.phunware.advertising.*;

//...

PwRewardedVideoAd rewardedVideoAd = PwRewardedVideoAd.getInstance(this, "YOUR_REWARDED_VIDEO_ZONE_ID");
rewardedVideoAd.setUserId("YOUR_LOCAL_PLAYER_ID"); //This is required.

//You can send custom data on a HashMap
HashMap<String, String> customData = new HashMap<>();
        customData.put("Data 1", "value 1");
        customData.put("Data 2", "value 2");
        //Note: this custom data is converted to JSON, and had a limit of 255 characters, if this exceed the 255 limit the SDK will delete the necessary keys of data to reach the limit.
        mRewardedVideoAd.setCustomData(customData);

//Setting listeners.
rewardedVideoAd.setListener(new PwRewardedVideoAd.PwRewardedVideoAdListener() {
            @Override
            public void rewardedVideoDidLoad(PwRewardedVideoAd rewardedVideoAd, TVASTRewardedVideoInfo rewardedVideoInfo) {

            }

            @Override
            public void rewardedVideoDidClose(PwRewardedVideoAd rewardedVideoAd, TVASTRewardedVideoInfo rewardedVideoInfo) {
                Log.d("TAG", "rewardedVideoDidClose");
            }

            @Override
            public void rewardedVideoDidFail(PwRewardedVideoAd rewardedVideoAd, String error, TVASTRewardedVideoInfo rewardedVideoInfo) {
                //If rewarded video doesn't have remaining views, you can check the error code if this exist.
                if(rewardedVideoInfo.getError() == 557){
                    Toast.makeText("getContext()", "You don't have remaining views", Toast.SHORT).show();
                }
            }

            @Override
            public void rewardedVideoActionWillLeaveApplication(PwRewardedVideoAd rewardedVideoAd, TVASTRewardedVideoInfo rewardedVideoInfo) {

            }

            @Override
            public void rewardedVideoDidEndPlaybackSuccessfully(PwRewardedVideoAd rewardedVideoAd, RVSuccessInfo rewardedVideoSuccessInfo, TVASTRewardedVideoInfo rewardedVideoInfo) {
            
                Log.d("REWARD:", rewardedVideoSuccessInfo.getCurrencyId());
                Log.d("AMOUNT:", String.valueOf(rewardedVideoSuccessInfo.getAmount()));

                //Remaining views after video completes.
                Log.d("REMAINING VIEWS:", String.valueOf(rewardedVideoSuccessInfo.getRemainingViews()));
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
````

### Pre-caching for Video Ads

Video Interstitial Ads and Rewarded Video Ads support pre-caching. Once the ad is loaded it will be cached on to the device for a better playback experience. This feature is enabled by default and set to a maximum capacity of 256 MB but can be turned off by setting the cache size to 0. Caching is supported on both external and internal storage. However the former will take precedence if the required permissions are present.

````java
PwAdvertisingModule pwAdModule=PwAdvertisingModule.getInstance();
pwAdModule.setAdsCacheSize(this, 0);
````

Progress of caching the media can be tracked by using methods in ad listeners.


### Native Ad Usage

Native ads are advertisements designed to fit naturally into your app's look and feel.
Predefined ad features are provided which your app consumes in a template that follows your UI's theme.

*Example Code Usage*
````java
import com.phunware.advertising.*;

...

String zoneId = "YOUR_NATIVE_AD_ZONE_ID";
PwNativeAd nativeAd = PwNativeAd.getInstance(context, zoneId);
nativeAd.setListener(new PwNativeAd.PwNativeAdListener() {
    @Override
    public void nativeAdDidLoad(PwNativeAd nativeAd) {
        renderUiFromNativeAd(nativeAd);
    }

    @Override
    public void nativeAdDidFail(PwNativeAd nativeAd, String errMsg) {
        // The ad failed to load and errMsg describes why.
        // Error messages are not intended for user display.
    }
});

nativeAd.load();

````

````java

private void renderUiFromNativeAd(PwNativeAd ad) {
    //Get a NativeAd News Feed type view from the example template.
    //NativeAds.getNewsFeedView() extract all elements from PwNativeAd and set on the returned view.
    //See the class NativeAds on the Example code to see more templates like News Feed, App Wall, Content Stream, and Content Wall.
    View nativeAdView = NativeAds.getNewsFeedView(ExampleActivity.this, ad);

    //Then you need to add the view to another in your layout.
    mViewWhereYourAdShouldBe.addView(nativeAdView);

    //This way you can extract the information of the native ad and build your own template.
    /*String adtitle = ad.getAdTitle();
    String iconurl = ad.getImageUrl();
    double stars = ad.getRating();
    String adtext = ad.getAdText();
    String cta = ad.getCta();

    // ... when native ad data is displayed on screen:
    nativeAd.trackImpression();

    ...

    // ... when native ad is clicked:
    nativeAd.click(context);*/
}
````

To request multiple ads at once:
````java
String zoneId = "YOUR_NATIVE_AD_ZONE_ID";
int numberOfAdsToLoad = 10;


PwAdLoader<PwNativeAd> adLoader = PwAdLoader.getNativeAdLoader();
adLoader.setZone(zoneId);
adLoader.setTestMode(true);

adLoader.loadAds(context, numberOfAdsToLoad,
    new PwAdLoader.PwAdLoaderListener<PwNativeAd>() {
        @Override
        public void onSuccess(PwAdLoader adLoader, List<PwNativeAd> nativeAdsList) {
            for (PwNativeAd nativeAd : nativeAdsList) {
                // Use the native ad to build a view item.
                renderUiFromNativeAd(nativeAd);
            }
            
            /* Or you can call one of multiple ads template example.
            *View multipleAdsView = NativeAds.get3UpView(ExampleActivity.this, nativeAdsList, Gravity.CENTER_HORIZONTAL);
            *mViewWhereYourAdShouldBe.addView(multipleAdsView);
            */
        }

        @Override
        public void onFail(PwAdLoader adLoader, String errMsg) {
            // No ads are returned and the errMsg describes why.
            // Error messages are not intended for user display.
        }
    }
);
````

The sample code provides example views for News Feed, App Wall, Content Stream, and Content Wall.
Code samples and advanced implementation examples can be found in the
[native ad example code](https://github.com/phunware/maas-ads-android-sdk/blob/master/Sample/sample/app/src/main/java/com/phunware/advertising/sample/NativeAdActivity.java) which is included with the SDK sample app.


### Location Data

To provide your app with greater control over performance and location data, the Ads SDK no longer
supports automatic location tracking.  To enable geotargeting, you're app will need to provide location
data to the ads.

*Example Code*
````java
PwBannerAdView bannerAdView;
LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
int canAccessLocation = context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
if (canAccessLocation == PackageManager.PERMISSION_GRANTED) {
    boolean isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    if (isEnabled) {
        MyLocationListener listener = new MyLocationListener();
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 60, 0, listener);
    }
    else {
        Log.d(TAG, "location not available");
    }
}
else {
    Log.d(TAG, "don't have permission for location");
    // for Android 6.0, you can request your permissions
}
...
private class MyLocationListener implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {
        ...
        bannerAdView.updateLocation(location.getLatitude(), location.getLongitude());
        ...
    }
...
}

````

Also, your `AndroidManifest.xml` will need to include the one of the following permissions:

````xml
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>

````

Privacy
-----------
You understand and consent to Phunware’s Privacy Policy located at www.phunware.com/privacy. If your use of Phunware’s software requires a Privacy Policy of your own, you also agree to include the terms of Phunware’s Privacy Policy in your Privacy Policy to your end users.

Terms
-----------
Use of this software requires review and acceptance of our terms and conditions for developer use located at 
http://www.phunware.com/terms/

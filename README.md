<!--- make sure you update the package-info file as well! --->

MaaS Advertising SDK for Android
================

Version 2.2.2

This is Phunware's Android SDK for the MaaS Advertising module. Visit http://maas.phunware.com/ for more details and to sign up.



Requirements
------------
- Android SDK 4.0.3 (API level 15) or above
- MaaS Core v1.3.13 or greater
- Google Play Services to enable Advertising ID support (recommended); installation instructions [here](https://developer.android.com/google/play-services/id.html).


Getting Started
---------------

- [Download the MaaS Advertising SDK](https://github.com/phunware/maas-ads-android-sdk/archive/master.zip) and run the included sample app.
- Continue reading below for installation and integration instructions.
- Be sure to read the [documentation](http://phunware.github.io/maas-ads-android-sdk/) for additional details.



Installation
------------

The following libraries are required:
````
PWCore-1.3.13.jar
````

MaaS Advertising depends on the MaaS Core SDK, which is available here: https://github.com/phunware/maas-core-android-sdk

With version 2.2.2 of the Advertising SDK, it is recommended that you use the Android Archive file (.aar).
This is the modern format for Android libraries and provides improved support.  JAR versions of the SDK are
provided for legacy compatibility but may be phased out in the future.

Consult the instructions of your development tools on how to add the libraries to your app project.

In Android Studio, you can add the libraries as modules to your project using the menu option
File->New->New Module and selecting "Import .JAR/.AAR Package".  Then add the library modules as
dependencies of your app in the File->Project Structure window.

Update your `AndroidManifest.xml` to include the following permissions and activity:

````xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>

<!-- Inside of the application tag: -->
<activity
    android:name="com.phunware.advertising.internal.PwAdActivity"
    android:configChanges="keyboard|keyboardHidden|orientation|screenSize" />

````
See [AndroidManifest.xml](sample/src/main/AndroidManifest.xml) for an example manifest file.

Documentation
------------

Documentation is included in HTML format in the `Docs` folder and [zipped](https://github.com/phunware/maas-ads-android-sdk/blob/master/MaaSAdvertising-javadoc.zip?raw=true) as well.



Integration
-----------

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

An advanced implementation can be found in the [example code](sample/src/main/java/com/phunware/advertising/sample/ExampleActivity.java).


### Interstitial Usage

Interstitials are best used at discrete stopping points in your app's flow, such as at the end of a game level or when the player dies.

*Example Usage*
````java
import com.phunware.advertising.*;

// ...

PwInterstitialAd interstitialAd = PwInterstitialAd.getInstance(this, "YOUR_INTERSTITIAL_ZONE_ID");
interstitialAd.show();
````

An advanced implementation can be found in the [example code](sample/src/main/java/com/phunware/advertising/sample/ExampleActivity.java).


### Landing Page Usage

Landing pages are used to display a fullscreen URL in your app.

*Example Usage*
````java
import com.phunware.advertising.*;

// ...

PwLandingPageAd landingPage = PwLandingPageAd.getInstance(this, "YOUR_ZONE_ID");
landingPage.show();
````

An advanced implementation can be found in the [example code](sample/src/main/java/com/phunware/advertising/sample/ExampleActivity.java).


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
An advanced implementation can be found in the [example code](sample/src/main/java/com/phunware/advertising/sample/ExampleActivity.java).



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
[native ad examples library](nativeads) which is included with the SDK sample app.


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
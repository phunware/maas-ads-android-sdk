MaaS Advertising SDK for Android
================

Version 1.1.1

This is Phunware's Android SDK for the MaaS Advertising module. Visit http://maas.phunware.com/ for more details and to sign up.



Requirements
------------

- MaaS Core v1.3.2 or greater



Getting Started
---------------

- [Download MaaS Advertising](https://github.com/phunware/maas-ads-android-sdk/archive/master.zip) and run the included sample app.
- Continue reading below for installation and integration instructions.
- Be sure to read the [documentation](http://phunware.github.io/maas-ads-android-sdk/) for additional details.



Installation
------------

The following libraries are required:
````
MaaSCore.jar
````

MaaS Advertising depends on MaaSCore.jar which is available here: https://github.com/phunware/maas-core-android-sdk

It's recommended that you add the MaaS libraries to the 'libs' directory. This directory should contain MaaSCore.jar
and MaaSMaaSAdvertising.jar  as well as any other MaaS libraries that you are using.

Update your `AndroidManifest.xml` to include the following permissions and activity:

````xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>

<!-- Optional permissions to enable ad geotargeting:
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
-->

<!-- Inside of the application tag: -->
<activity
    android:name="com.phunware.advertising.internal.PwAdActivity"
    android:configChanges="keyboard|keyboardHidden|orientation|screenSize" />

````
See [AndroidManifest.xml](https://github.com/phunware/maas-ads-android-sdk/blob/master/Sample/AndroidManifest.xml) for an example manifest file.



Documentation
------------

Documentation is included in HTML format in the `Docs` folder, as well as [zipped](https://github.com/phunware/maas-ads-android-sdk/blob/master/MaaSAdvertising-javadoc.zip?raw=true).



Integration
-----------

The primary methods in MaaS Advertising involve displaying the various ad types:


### Banner Usage

Banners are inline ads that are shown alongside your apps interface.

*Xml Only Usage*
Add this to your layout xml:
````xml
<!-- Add a banner to your layout xml: -->
<!-- This will cause a 320x50 ad to be created, which will automatically kick off ad rotation. -->
<com.phunware.advertising.PwBannerAdView
    android:id="@+id/bannerAd"
    android:layout_width="320dp"
    android:layout_height="50dp"
    zone="YOUR_ZONE_ID" />
````

*Example Code Usage*
Add this to your layout xml: (note that "zone" is not specified)
````xml
<!-- Add a banner to your layout xml -->
<!-- This will cause a 320x50 ad to be created, which will automatically kick off ad rotation. -->
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
````

Advanced implementation can be found in the [Example Code](https://github.com/phunware/maas-ads-android-sdk/blob/master/Sample/src/com/phunware/advertising/example/AdvertisingSample.java).


### Interstitial Usage

Interstitials are best used at discrete stopping points in your app's flow, such as at the end of a game level
or when the player dies.

*Example Usage*
````java
import com.phunware.advertising.*;

// ...

PwInterstitialAd interstitialAd = PwAdvertisingModule.get().getInterstitialAdForZone(this, "YOUR_INTERSTITIAL_ZONE_ID");
interstitialAd.show();
````

Advanced implementation can be found in the [Example Code](https://github.com/phunware/maas-ads-android-sdk/blob/master/Sample/src/com/phunware/advertising/example/AdvertisingSample.java).


### Video Ads Usage

Video ads are interstitial ads that play a video.  They are best used at discrete
stopping points in your app's flow, such as at the end of a game level or when the player dies.

*Example Usage*
````java
import com.phunware.advertising.*;

// ...

PwVideoInterstitialAd videoAd = PwAdvertisingModule.get().getVideoInterstitialAdForZone(this, "YOUR_VIDEO_ZONE_ID");
videoAd.show();
````
Advanced implementation can be found in the [Example Code](https://github.com/phunware/maas-ads-android-sdk/blob/master/Sample/src/com/phunware/advertising/example/AdvertisingSample.java).

MaaSAdvertising Android SDK
================

Version 1.0.0

This is the Android SDK for the MaaS Advertising module. Visit http://maas.phunware.com/ for more details and to sign up.

Getting Started
---------------

- [Download MaaSAdvertising](https://github.com/phunware/maas-ads-android-sdk/archive/master.zip) and run the included sample app.
- Continue reading below for installation and integration instructions.
- Be sure to read the [documentation](http://phunware.github.io/maas-ads-android-sdk/) for additional details.


Installation
------------

The following libraries are required:
````
MaaSCore.jar
````

MaaSAdvertising has a dependency on MaaSCore.jar which is available here: https://github.com/phunware/maas-core-android-sdk

It's recommended that you add the MaaS libraries to the 'libs' directory. This directory should contain MaaSCore.jar and MaaSMaaSAdvertising.jar  as well as any other MaaS libraries that you are using.

Update your `AndroidManifest.xml` to include the following permissions and activity:

````xml
<uses-permission android:name="android.permission.INTERNET"></uses-permission>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
<uses-permission android:name="android.permission.READ_PHONE_STATE"></uses-permission>

<!-- Optional permissions to enable ad geotargeting
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"></uses-permission>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"></uses-permission>
-->

<!-- inside of the application tag: -->
<activity
    android:name="com.phunware.advertising.internal.PwAdActivity"
    android:configChanges="keyboard|keyboardHidden|orientation" />

````
See [AndroidManifest.xml](https://github.com/phunware/maas-ads-android-sdk/blob/master/Sample/AndroidManifest.xml) for an example manifest file.


Documentation
------------

Documentation is included in the Documents folder in the repository as both HTML and .jar.



Integration
-----------

The primary methods in MaaSAdvertising revolve displaying the various ad types:

AdPrompt Usage
--------------
AdPrompts are a simple ad unit designed to have a native feel. The user is given the option to download an app, and if they accept, they are taken to the app within the app marketplace.

*Example Usage*
````java
import com.phunware.advertising.*;

// ...

PwAdPrompt adPrompt = PwAdvertisingModule.get().getAdPromptForZone(this, "YOUR_ADPROMPT_ZONE_ID");
adPrompt.show();
````

Advanced implementation can be found in the [Example Code](https://github.com/phunware/maas-ads-android-sdk/blob/master/Sample/src/com/phunware/advertising/example/MyActivity.java)


Banner Usage
------------
Banners are inline ads that are shown alongside your apps interface.

*Xml Only Usage*
Add this in you layout xml:
````xml
<!-- Add banner to your layout xml -->
<!-- this will cause a 320x50 ad to be created, automatically kicking off ad rotation -->
<com.phunware.advertising.PwBannerAdView
    android:id="@+id/bannerAd"
    android:layout_width="320dp"
    android:layout_height="50dp"
    zone="YOUR_ZONE_ID" />
````

*Example Code Usage*
Add this in you layout xml: (note that "zone" is not specified)
````xml
<!-- Add banner to your layout xml -->
<!-- this will cause a 320x50 ad to be created, automatically kicking off ad rotation -->
<com.phunware.advertising.PwBannerAdView
    android:id="@+id/bannerAd"
    android:layout_width="320dp"
    android:layout_height="50dp" />
````

Add this to your activity:
````java
import com.phunware.advertising.*;

// ...

PwBannerAdView bannerAdView = (PwBannerAdView)findViewById(R.id.bannerAdView);
bannerAdView.startRequestingAdsForZone("YOUR_BANNER_ZONE_ID");
````

Advanced implementation can be found in the [Example Code](https://github.com/phunware/maas-ads-android-sdk/blob/master/Sample/src/com/phunware/advertising/example/MyActivity.java)


Interstitial Usage
------------------
Interstitials are best used at discrete stopping points in your app's flow, such as at the end of a game level, or when the player dies.

*Example Usage*
````java
import com.phunware.advertising.*;

// ...

PwInterstitialAd interstitialAd = PwAdvertisingModule.get().getInterstitialAdForZone(this, "YOUR_INTERSTITIAL_ZONE_ID");
interstitialAd.show();
````

Advanced implementation can be found in the [Example Code](https://github.com/phunware/maas-ads-android-sdk/blob/master/Sample/src/com/phunware/advertising/example/MyActivity.java)


Video Ads Usage
----------------
Video ads are interstitials that play a video.  They are best used at discrete
stopping points in your app's flow, such as at the end of a game level, or when the player dies.

*Example Usage*
````java
import com.phunware.advertising.*;

// ...

PwVideoInterstitialAd videoAd = PwAdvertisingModule.get().getVideoInterstitialAdForZone(this, "YOUR_VIDEO_ZONE_ID");
videoAd.show();
````
Advanced implementation can be found in the [Example Code](https://github.com/phunware/maas-ads-android-sdk/blob/master/Sample/src/com/phunware/advertising/example/MyActivity.java)



Requirements
------------

- MaaSCore v1.0.0 or greater.

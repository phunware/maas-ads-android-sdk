package com.phunware.advertising.sample

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.view.MenuItem
import android.view.Gravity
import android.view.Menu
import com.phunware.advertising.*
import com.phunware.advertising.internal.vast.RVSuccessInfo
import com.phunware.advertising.internal.vast.TVASTRewardedVideoInfo
import com.phunware.core.PwLog
import java.util.Arrays
import kotlin.collections.HashMap

/**
 * Sample to demonstrate Ads SDK usage
 * This sample gives usage examples for the Ad units Banner, 3d Banner, Interstitial, Video Interstitial,
 * RewardedVideo and Landing Page
 */
class ExampleActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    private val TAG: String = ExampleActivity::class.java.simpleName
    private val PERMISSION_WRITE_EXTERNAL_REQUEST: Int = 101
    private val PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val maxAdsToReturn: Int = 3

    lateinit private var bannerAdView: PwBannerAdView
    lateinit private var nativeAdHolder: ViewGroup
    lateinit private var pwAdvertisingModule: PwAdvertisingModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        bannerAdView = findViewById(R.id.bannerAdView) as PwBannerAdView
        nativeAdHolder = findViewById(R.id.native_ad_view_placeholder) as ViewGroup
        pwAdvertisingModule = PwAdvertisingModule.getInstance()

        PwLog.setShowLog(true)

        // test that you've integrated properly
        // NOTE: remove this before your app goes live!
        PwAdvertisingModule.getInstance().validateSetup(this)

        requestPermission()
    }

    override fun onDestroy() {
        super.onDestroy();
        // Make sure to call clean up to release resources
        PwAdvertisingModule.getInstance().cleanUp(this)
    }

    /**
     * Request permissions required for caching
     */
    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(this, PERMISSIONS[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_WRITE_EXTERNAL_REQUEST)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_WRITE_EXTERNAL_REQUEST -> {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    //disable caching
                    pwAdvertisingModule.setAdsCacheSize(this, 0)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var selected: Boolean = true
        when (item?.itemId) {
            R.id.action_news_feed -> fireNativeNewsFeed()
            R.id.action_content_wall -> fireNativeContentWall()
            R.id.action_content_stream -> fireNativeContentStream()
            R.id.action_app_wall -> fireNativeAppWall()
            R.id.action_icons -> fireNativeIcons()
            R.id.action_clean -> fireNativeAdClean()
            R.id.action_3up_left -> fireNativeAd3Up(Gravity.START)
            R.id.action_3up_center -> fireNativeAd3Up(Gravity.CENTER_HORIZONTAL)
            R.id.action_3up_right -> fireNativeAd3Up(Gravity.END)
            else -> selected = super.onOptionsItemSelected(item)
        }
        return selected
    }

    /**
     * Display banner ads
     */
    fun fireBanner(view: View) {
        clearViews()
        bannerAdView.visibility = View.VISIBLE
        advancedBannerExample()
    }

    /**
     * Display landing page Ads
     */
    fun fireLandingPage(view: View) {
        advancedLandingPageExample()
    }

    /**
     * Display Interstitial Ads
     */
    fun fireInterstitial(view: View) {
        advancedInterstitialExample()
    }

    /**
     * Display Video Interstitial Ads
     */
    fun fireVideoInterstitial(view: View) {
        advancedVideoExample()
    }

    /**
     * Display Rewarded Video Ad
     */
    fun fireRewardedVideo(view: View) {
        val zoneId: String = getString(R.string.rewarded_video_zone_id)
        val lat: Double = 40.7787895
        val lng: Double = -73.9660945

        val customData: HashMap<String, String> = hashMapOf()

        // Add some custom data
        for (i in 0..8) {
            customData.put("reward" + i, "gold")
            customData.put("key" + i, "someValue")
        }

        // Create Ad
        var rewardedVideoAd: PwRewardedVideoAd = PwRewardedVideoAd.getInstance(this, zoneId)
        rewardedVideoAd.keywords = Arrays.asList("keyword1", "keyword2")
        rewardedVideoAd.updateLocation(lat, lng)
        rewardedVideoAd.userId = "5487G54d30OsdZt79"
        rewardedVideoAd.customData = customData

        rewardedVideoAd.listener = object : PwRewardedVideoAd.PwRewardedVideoAdListener {
            override fun onCacheProgress(rewardedVideoAd: PwRewardedVideoAd?, percentageCompleted: Int) {

            }

            override fun onCacheCompleted(rewardedVideoAd: PwRewardedVideoAd?,
                                          rewardedVideoInfo: TVASTRewardedVideoInfo?) {
                rewardedVideoAd?.show()
            }

            override fun rewardedVideoDidClose(rewardedVideoAd: PwRewardedVideoAd?,
                                               rewardedVideoInfo: TVASTRewardedVideoInfo?) {
                PwLog.d(TAG, "Rewarded Video onCloseRewardedVideo")
            }

            override fun rewardedVideoActionWillLeaveApplication(rewardedVideoAd: PwRewardedVideoAd?,
                                                                 rewardedVideoInfo: TVASTRewardedVideoInfo?) {
                PwLog.d(TAG, "Rewarded Video onLeaveApplication")

            }

            override fun rewardedVideoDidFail(rewardedVideoAd: PwRewardedVideoAd?, error: String?,
                                              rewardedVideoInfo: TVASTRewardedVideoInfo?) {
                PwLog.d(TAG, "Rewarded Video onRewardedVideoFail"
                        + error
                        + ", Code: "
                        + rewardedVideoInfo?.getError())
                showDialog(error);
            }

            override fun rewardedVideoDidEndPlaybackSuccessfully(rewardedVideoAd: PwRewardedVideoAd?,
                                                                 rewardedVideoSuccessInfo: RVSuccessInfo?,
                                                                 rewardedVideoInfo: TVASTRewardedVideoInfo?) {
                val message: String = String.format(getString(R.string.rewarded),
                        rewardedVideoSuccessInfo?.getCurrencyId(),
                        rewardedVideoSuccessInfo?.getAmount(),
                        rewardedVideoSuccessInfo?.getRemainingViews())

                showDialog(message);
            }

            override fun rewardedVideoDidLoad(p0: PwRewardedVideoAd?, p1: TVASTRewardedVideoInfo?) {
                PwLog.d(TAG, "Rewarded Video Ad loaded")
            }
        }

        rewardedVideoAd.load()
    }

    /**
     * Display 3d banner ads
     */
    fun fire3dBanner(view: View) {
        PwLog.d(TAG, "3dBannerExample")

        bannerAdView.adUpdateInterval = 30 // rotate every 30 seconds.
        bannerAdView.set3dAnimation(true) //This is importat to see the 3d animation. Default is false

        val zoneId: String = getString(R.string.banner_zone_id);

        bannerAdView.zone = zoneId
        bannerAdView.isTestMode = true // enable during the development phase
        bannerAdView.keywords = Arrays.asList("keyword1", "keyword2") // optional keywords for custom targeting

        //Register for Ad lifecycle callbacks
        bannerAdView.listener = object : PwBannerAdView.BannerAdListener {
            override fun onReceiveBannerAd(ad: PwBannerAdView?) {
                PwLog.d(TAG, "Banner onReceiveBannerAd")
            }

            override fun onBannerAdFullscreen(ad: PwBannerAdView?) {
                PwLog.d(TAG, "Banner onBannerAdFullscreen")
            }

            override fun onBannerAdError(ad: PwBannerAdView?, errorMsg: String?) {
                PwLog.d(TAG, "Banner onBannerAdError: " + errorMsg)
            }

            override fun onBannerAdLeaveApplication(ad: PwBannerAdView?) {
                PwLog.d(TAG, "Banner onBannerAdLeaveApplication")
            }

            override fun onBannerAdDismissFullscreen(ad: PwBannerAdView?) {
                PwLog.d(TAG, "Banner onBannerAdDismissFullscreen")
            }
        }

        // start banner rotating
        bannerAdView.startRequestingAds()
    }

    fun simpleBannerExample() {
        val zoneId: String = getString(R.string.banner_zone_id)
        bannerAdView.startRequestingAdsForZone(zoneId)
    }

    fun advancedBannerExample() {
        PwLog.d(TAG, "advancedBannerExample")

        // Banner rotation interval; defaults to 60 seconds.
        //bannerAdView.adUpdateInterval = 0 // no auto rotation
        bannerAdView.adUpdateInterval = 30 // rotate every 30 seconds.

        val zoneId: String = getString(R.string.banner_zone_id)

        bannerAdView.zone = zoneId
        bannerAdView.isTestMode = true // enable during the development phase
        bannerAdView.keywords = Arrays.asList("keyword1", "keyword2") // optional keywords for custom targeting

        // register for ad lifecycle callbacks
        bannerAdView.setListener(object : PwBannerAdView.BannerAdListener {
            override fun onReceiveBannerAd(ad: PwBannerAdView?) {
                PwLog.d(TAG, "Banner onReceiveBannerAd")
            }

            override fun onBannerAdFullscreen(ad: PwBannerAdView?) {
                PwLog.d(TAG, "Banner onBannerAdFullscreen")
            }

            override fun onBannerAdError(ad: PwBannerAdView?, errorMsg: String?) {
                PwLog.d(TAG, "Banner onBannerAdError: " + errorMsg)
            }

            override fun onBannerAdLeaveApplication(ad: PwBannerAdView?) {
                PwLog.d(TAG, "Banner onBannerAdLeaveApplication")
            }

            override fun onBannerAdDismissFullscreen(ad: PwBannerAdView?) {
                PwLog.d(TAG, "Banner onBannerAdDismissFullscreen")
            }
        });

        /**
         * Optionally set location manually.
         * double lat = 40.7787895;
         * double lng = -73.9660945;
         * bannerAdView.updateLocation(lat, lng);
         */
        // start banner rotation
        bannerAdView.startRequestingAds();
    }

    fun simpleInterstitialExample() {
        val zoneId: String = getString(R.string.intrs_zone_id)
        var interstitialAd: PwInterstitialAd = PwInterstitialAd.getInstance(this, zoneId)
        interstitialAd.show()
    }

    fun advancedInterstitialExample() {
        val zoneId: String = getString(R.string.intrs_zone_id)
        var interstitialAd: PwInterstitialAd = PwInterstitialAd.getInstance(this, zoneId)
        interstitialAd.isTestMode = true
        interstitialAd.keywords = Arrays.asList("keyword1", "keyword2")

        // Register for ad lifecycle callbacks
        interstitialAd.listener = object : PwInterstitialAd.PwInterstitialAdListener {
            override fun interstitialActionWillLeaveApplication(ad: PwInterstitialAd?) {
                PwLog.d(TAG, "Interstitial Will Leave App")
            }

            override fun interstitialDidClose(ad: PwInterstitialAd?) {
                PwLog.d(TAG, "Interstitial Did Close")
            }

            override fun interstitialDidLoad(ad: PwInterstitialAd?) {
                // Show ad as soon as it's loaded
                PwLog.d(TAG, "Interstitial Did Load")
                ad?.show()
            }

            override fun interstitialDidFail(ad: PwInterstitialAd?, error: String?) {
                PwLog.d(TAG, "Interstitial Did Fail: " + error)
            }
        }

        interstitialAd.load()
    }

    fun simpleLandingPageExample() {
        // generate a customized request
        val zoneId: String = getString(R.string.landing_page_zone_id);
        var pageAd: PwLandingPageAd = PwLandingPageAd.getInstance(this, zoneId)
        pageAd.show()
    }

    fun advancedLandingPageExample() {
        // generate a customized request
        val zoneId: String = getString(R.string.landing_page_zone_id);

        var pageAd: PwLandingPageAd = PwLandingPageAd.getInstance(this, zoneId)
        pageAd.isTestMode = true;
        pageAd.keywords = Arrays.asList("keyword1", "keyword2")

        // Register for ad lifecycle callbacks
        pageAd.listener = object : PwLandingPageAd.PwPageAdListener {
            override fun pageDidFail(ad: PwLandingPageAd?, error: String?) {
                PwLog.d(TAG, "Page Did Fail: " + error)
            }

            override fun pageDidLoad(ad: PwLandingPageAd?) {
                // show ad as soon as it's loaded
                PwLog.d(TAG, "Page Did Load")
                ad?.show()
            }

            override fun pageDidClose(ad: PwLandingPageAd?) {
                PwLog.d(TAG, "Page Did Close")
            }

            override fun pageActionWillLeaveApplication(ad: PwLandingPageAd?) {
                PwLog.d(TAG, "Page Will Leave App")
            }
        }

        // Load ad... we'll be notified when it's ready
        pageAd.load()
    }

    fun simpleVideoExample() {
        val zoneId: String = getString(R.string.video_zone_id)
        var videoAd: PwVideoInterstitialAd = PwVideoInterstitialAd.getInstance(this, zoneId)
        videoAd.show()
    }

    fun advancedVideoExample() {
        val zoneId: String = getString(R.string.video_zone_id)
        var videoAd: PwVideoInterstitialAd = PwVideoInterstitialAd.getInstance(this, zoneId)

        videoAd.isTestMode = true
        videoAd.placementType = PwVideoInterstitialAd.TYPE_ALL
        videoAd.updateLocation(42.621535114613685, -5.595249100000046)
        videoAd.keywords = Arrays.asList("keyword1", "keyword2")

        videoAd.listener = object : PwVideoInterstitialAd.PwVideoInterstitialAdListener {
            override fun videoInterstitialActionWillLeaveApplication(ad: PwVideoInterstitialAd?) {
                PwLog.d(TAG, "Video Ad Will Leave App")
            }

            override fun videoInterstitialDidClose(ad: PwVideoInterstitialAd?) {
                PwLog.d(TAG, "Video Ad Did Close")
            }

            override fun onCacheProgress(ad: PwVideoInterstitialAd?, percentageCompleted: Int) {

            }

            override fun onCacheCompleted(ad: PwVideoInterstitialAd?) {
                videoAd.show()
            }

            override fun videoInterstitialDidFail(ad: PwVideoInterstitialAd?, error: String?) {
                PwLog.d(TAG, "Video Ad Did Fail: " + error)
            }

            override fun videoInterstitialDidLoad(ad: PwVideoInterstitialAd?) {
                PwLog.d(TAG, "Video Ad Did Load")
            }
        }

        videoAd.load()
    }


    private fun fireNativeNewsFeed() {
        clearViews()

        val zoneId: String = getString(R.string.native_zone_id)
        var nativeAd: PwNativeAd = PwNativeAd.getInstance(this, zoneId)
        nativeAd.isTestMode = true
        nativeAd.setListener(object : PwNativeAd.PwNativeAdListener {
            override fun nativeAdDidLoad(nativeAd: PwNativeAd?) {
                var view: View = PwNativeAd.GenericViews.getNewsFeedView(this@ExampleActivity, nativeAd)
                nativeAdHolder.addView(view)
                nativeAdHolder.visibility = View.VISIBLE
            }

            override fun nativeAdDidFail(nativeAd: PwNativeAd?, errMsg: String?) {
                // The ad failed to load and the errMsg describes why.
                // Error messages are not intended for user display.
                PwLog.e(TAG, errMsg);
            }

        })
        nativeAd.load()
    }

    private fun fireNativeContentWall() {
        clearViews()

        val zoneId: String = getString(R.string.native_zone_id)
        var nativeAd: PwNativeAd = PwNativeAd.getInstance(this, zoneId)
        nativeAd.isTestMode = true
        nativeAd.setListener(object : PwNativeAd.PwNativeAdListener {
            override fun nativeAdDidLoad(nativeAd: PwNativeAd?) {
                var view: View = PwNativeAd.GenericViews.getContentWallView(this@ExampleActivity, nativeAd)
                nativeAdHolder.addView(view)
                nativeAdHolder.visibility = View.VISIBLE
            }

            override fun nativeAdDidFail(nativeAd: PwNativeAd?, errMsg: String?) {
                // The ad failed to load and the errMsg describes why.
                // Error messages are not intended for user display.
                PwLog.e(TAG, errMsg)
            }
        })
        nativeAd.load()
    }

    private fun fireNativeContentStream() {
        val intent = Intent(this, NativeAdActivity::class.java)
        intent.putExtra(NativeAdActivity.EXTRA_TYPE, NativeAdActivity.TYPE_CONTENT_STREAM)
        startActivity(intent)
    }

    private fun fireNativeAppWall() {
        var intent = Intent(this, NativeAdActivity::class.java)
        intent.putExtra(NativeAdActivity.EXTRA_TYPE, NativeAdActivity.TYPE_APP_WALL)
        startActivity(intent)
    }

    private fun fireNativeIcons() {
        clearViews()

        val zoneId: String = getString(R.string.native_zone_id)
        var adLoader: PwAdLoader<PwNativeAd> = PwAdLoader.getNativeAdLoader()

        adLoader.zone = zoneId
        adLoader.isTestMode = true
        adLoader.loadAds(this, maxAdsToReturn, object : PwAdLoader.PwAdLoaderListener<PwNativeAd> {
            override fun onSuccess(loader: PwAdLoader<*>?, ads: MutableList<PwNativeAd>?) {
                var innerView: View = PwNativeAd.GenericViews.getIconsView(this@ExampleActivity, ads, object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        nativeAdHolder.visibility = View.GONE
                        nativeAdHolder.removeAllViewsInLayout()
                    }
                })
                nativeAdHolder.addView(innerView)
                nativeAdHolder.visibility = View.VISIBLE
            }

            override fun onFail(loader: PwAdLoader<*>?, error: String?) {
                PwLog.e(TAG, error)
            }
        })
    }

    private fun fireNativeAdClean() {
        clearViews()
        val zoneId: String = getString(R.string.native_zone_id)
        var nativeAd: PwNativeAd = PwNativeAd.getInstance(this, zoneId)

        nativeAd.isTestMode = true
        nativeAd.setListener(object : PwNativeAd.PwNativeAdListener {
            override fun nativeAdDidLoad(nativeAd: PwNativeAd?) {
                var view: View = PwNativeAd.GenericViews.getCleanView(this@ExampleActivity, nativeAd)
                nativeAdHolder.addView(view)
                nativeAdHolder.visibility = View.VISIBLE
            }

            override fun nativeAdDidFail(nativeAd: PwNativeAd?, errMsg: String?) {
                // The ad failed to load and the errMsg describes why.
                // Error messages are not intended for user display.
                PwLog.e(TAG, errMsg)
            }
        })

        nativeAd.load()
    }

    private fun fireNativeAd3Up(gravity: Int) {
        clearViews()
        val zoneId: String = getString(R.string.native_zone_id)

        var adLoader: PwAdLoader<PwNativeAd> = PwAdLoader.getNativeAdLoader()
        adLoader.zone = zoneId
        adLoader.isTestMode = true

        val countAds: Int = if (gravity == Gravity.CENTER_HORIZONTAL) 3 else 2

        adLoader.loadAds(this, countAds, object : PwAdLoader.PwAdLoaderListener<PwNativeAd> {
            override fun onSuccess(loader: PwAdLoader<*>?, ads: MutableList<PwNativeAd>?) {
                var innerView: View = PwNativeAd.GenericViews.get3UpView(this@ExampleActivity, ads, gravity)
                nativeAdHolder.addView(innerView)
                nativeAdHolder.visibility = View.VISIBLE
            }

            override fun onFail(loader: PwAdLoader<*>?, error: String?) {
                PwLog.e(TAG, error)
            }
        })
    }


    private fun showDialog(message: String?) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setPositiveButton(getString(R.string.ok),
                object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, id: Int) {
                        dialog?.dismiss()
                    }
                })

        builder.setTitle(getString(R.string.app_name))
        builder.setMessage(message)
        runOnUiThread(object : Runnable {
            override fun run() {
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        })
    }

    private fun clearViews() {
        bannerAdView.stopRequestingAds()
        bannerAdView.visibility = View.GONE
        nativeAdHolder.removeAllViewsInLayout()
    }
}
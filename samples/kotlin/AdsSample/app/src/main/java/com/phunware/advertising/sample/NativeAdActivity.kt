package com.phunware.advertising.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.phunware.advertising.PwAdLoader
import com.phunware.advertising.PwNativeAd
import com.phunware.core.PwLog

class NativeAdActivity : AppCompatActivity() {

    companion object {
        const val TYPE_CONTENT_STREAM: String = "nativeAdContentStream"
        const val TYPE_APP_WALL: String = "nativeAdAppWall"
        const val EXTRA_TYPE = "nativeAdType"
    }

    private val TAG: String = "NativeAdActivity"
    private val maxAdsToReturn: Int = 8

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.native_ad)

        val container: ViewGroup = findViewById(R.id.adContainer) as ViewGroup
        val adType: String = getIntent().getStringExtra(EXTRA_TYPE)

        // Native Ad zone id
        val zoneId: String = getResources().getString(R.string.native_zone_id)

        if (adType.equals(TYPE_CONTENT_STREAM)) {
            var nativeAd: PwNativeAd = PwNativeAd.getInstance(this, zoneId)
            nativeAd.isTestMode = true

            nativeAd.setListener(object : PwNativeAd.PwNativeAdListener {
                override fun nativeAdDidLoad(nativeAd: PwNativeAd?) {
                    val view: View = PwNativeAd.GenericViews.getContentStreamView(this@NativeAdActivity, nativeAd)
                    container.addView(view)
                }

                override fun nativeAdDidFail(nativeAd: PwNativeAd?, errMsg: String?) {
                    // The ad failed to load and the errMsg describes why.
                    // Error messages are not intended for user display.
                    PwLog.e(TAG, errMsg)
                }
            })

            nativeAd.load()

        } else if (adType.equals(TYPE_APP_WALL)) {
            var adLoader: PwAdLoader<PwNativeAd> = PwAdLoader.getNativeAdLoader()
            adLoader.zone = zoneId
            adLoader.isTestMode = true

            adLoader.loadAds(this, maxAdsToReturn, object : PwAdLoader.PwAdLoaderListener<PwNativeAd> {
                override fun onSuccess(loader: PwAdLoader<*>?, ads: MutableList<PwNativeAd>?) {
                    var innerView: View = PwNativeAd.GenericViews.getAppWallView(this@NativeAdActivity, ads)
                    container.addView(innerView)
                }

                override fun onFail(loader: PwAdLoader<*>?, error: String?) {
                    PwLog.e(TAG, error)
                }

            })
        }

    }

}

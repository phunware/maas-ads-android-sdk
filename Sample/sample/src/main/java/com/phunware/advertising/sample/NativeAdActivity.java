package com.phunware.advertising.sample;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.phunware.advertising.PwNativeAd;
import com.phunware.core.PwLog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by NB-RogerAng on 2/11/16.
 */
public class NativeAdActivity extends Activity {

    private static final String TAG = NativeAdActivity.class.getSimpleName();

    private TextView mAdTitle;
    private ImageView mAdIcon;
    private ImageView mAdImage;
    private RatingBar mAdRating;
    private WebView mAdHtml;
    private TextView mAdText;
    private TextView mAdCtaText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.native_ad);

        mAdTitle = (TextView) findViewById(R.id.title);
        mAdIcon = (ImageView) findViewById(R.id.iconView);
        mAdImage = (ImageView) findViewById(R.id.imageView);
        mAdRating = (RatingBar) findViewById(R.id.ratingBar);
        mAdHtml = (WebView) findViewById(R.id.html);
        mAdText = (TextView) findViewById(R.id.text);
        mAdCtaText = (TextView) findViewById(R.id.ctaText);

        // native ad zone id
        String zoneId = getResources().getString(R.string.native_zone_id);

        PwNativeAd nativeAd = PwNativeAd.getInstance(this, zoneId);
        nativeAd.setTestMode(true);

        nativeAd.setListener(new PwNativeAd.PwNativeAdListener() {
            @Override
            public void nativeAdDidLoad(PwNativeAd nativeAd) {
                try {
                    renderUiFromNativeAd(nativeAd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void nativeAdDidFail(PwNativeAd nativeAd, String errMsg) {
                // The ad failed to load and the errMsg describes why.
                // Error messages are not intended for user display.
                PwLog.e(TAG, errMsg);
            }
        });

        nativeAd.load();

        // if anything was clickable, should call this
        // nativeAd.click(this);
    }

    private void renderUiFromNativeAd(final PwNativeAd nativeAd) throws JSONException {
        JSONObject json = new JSONObject(nativeAd.getAdData());
        String adtitle = json.optString("adtitle");
        if (!TextUtils.isEmpty(adtitle)) {
            mAdTitle.setText(adtitle);
        }

        String iconUrl = json.optString("iconurl");
        if (!TextUtils.isEmpty(iconUrl)) {
            TextView emptyText = (TextView) findViewById(R.id.iconEmptyText);
            emptyText.setVisibility(View.GONE);
            Glide.with(this).load(iconUrl).into(mAdIcon);
            mAdIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nativeAd.click(NativeAdActivity.this);
                }
            });
        }

        String imageUrl = json.optString("imageurl");
        if (!TextUtils.isEmpty(imageUrl)) {
            TextView emptyText = (TextView) findViewById(R.id.imageEmptyText);
            emptyText.setVisibility(View.GONE);
            Glide.with(this).load(imageUrl).into(mAdImage);
            mAdImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nativeAd.click(NativeAdActivity.this);
                }
            });
        }

        double stars = json.optDouble("rating");
        if (stars != Double.NaN) {
            mAdRating.setRating((float) stars);
        }

        String data = json.optString("html");
        if (TextUtils.isEmpty(data)) {
            data = "HTML would be here if there was any";
        }
        mAdHtml.loadDataWithBaseURL("", data, "text/html", "UTF-8", "");

        String adtext = json.optString("adtext");
        if (!TextUtils.isEmpty(adtext)) {
            mAdText.setText(adtext);
        }

        String cta = json.optString("cta");
        if (!TextUtils.isEmpty(adtext)) {
            mAdCtaText.setText(cta);
        }
    }
}

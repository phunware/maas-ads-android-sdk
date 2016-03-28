package com.yourcompany.nativeadsexample;

import android.content.Context;


import com.phunware.advertising.PwNativeAd;

import org.json.JSONException;
import org.json.JSONObject;

public class Ad {
    private final String adtitle;
    private final String imageurl;
    private final int stars;
    private final String html;
    private final String adtext;
    private final String cta;
    private final PwNativeAd pwNativeAd;

    public Ad(PwNativeAd pwNativeAd) throws JSONException {
        this.pwNativeAd = pwNativeAd;
        JSONObject json = new JSONObject(pwNativeAd.getAdData());
        this.adtitle = json.optString("adtitle");
        this.imageurl = json.optString("iconurl");
        this.stars = json.optInt("rating");
        this.html = json.optString("html");
        this.adtext = json.optString("adtext");
        this.cta = json.optString("cta");
    }

    public String getAdtitle() {
        return adtitle;
    }

    public String getImageurl() {
        return imageurl;
    }

    public int getStars() {
        return stars;
    }

    public String getHtml() {
        return html;
    }

    public String getAdtext() {
        return adtext;
    }

    public String getCta() {
        return cta;
    }

    public void impression() {
        pwNativeAd.trackImpression();
    }

    public void click(Context ctx) {
        pwNativeAd.click(ctx);
    }

    @Override
    public String toString() {
        return "Title: " + adtitle + " (" + pwNativeAd.toString() + ")";
    }
}

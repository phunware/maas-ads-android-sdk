package com.yourcompany.nativeadsexample;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.phunware.advertising.*;
import com.phunware.core.PwCoreSession;
import com.phunware.core.PwLog;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        PwLog.setShowLog(true);

        // Initialize MaaS Core
        PwCoreSession.getInstance().registerKeys(this,
                getString(R.string.app_appid),
                getString(R.string.app_accesskey),
                getString(R.string.app_signaturekey),
                getString(R.string.app_encryptionkey));

        // Register MaaS Advertising module
        PwCoreSession.getInstance().installModules(PwAdvertisingModule.getInstance());

        // test that you've integrated properly
        // NOTE: remove this before your app goes live!
        PwAdvertisingModule.get().validateSetup(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String TAG = "NA";
        private ListView mListView;
        private TextView mEmptyView;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            mListView = (ListView) rootView.findViewById(R.id.listViewMain);
            mEmptyView = (TextView) rootView.findViewById(R.id.empty_view);
            mListView.setEmptyView(mEmptyView);
            final Context finalContext = getActivity();

            String zoneId = finalContext.getString(R.string.zone_id);
            PwAdRequest request = PwAdvertisingModule.get().getAdRequestBuilder(zoneId)
                    .setTestMode(true)
                    .getPwAdRequest();

            PwAdLoader<PwNativeAd> adLoader = PwAdvertisingModule.get().getNativeAdLoader();
            adLoader.multiLoad(finalContext, request, 10,
                    new PwAdLoader.PwAdLoaderListener<PwNativeAd>() {
                        @Override
                        public void onSuccess(PwAdLoader pwAdLoader, List<PwNativeAd> pwNativeAds) {
                            ArrayList<Ad> listOfAds = new ArrayList<Ad>(pwNativeAds.size());
                            for (PwNativeAd tna : pwNativeAds) {
                                try {
                                    Ad ad = new Ad(tna);
                                    Log.d("NAP", "THE AD:\n" + ad.toString());
                                    listOfAds.add(ad);
                                } catch (JSONException e) {
                                    Log.e("NAP", "Failed to parse native ad, skipping: " + tna.getAdData());
                                }
                            }
                            AdAdapter adapter = new AdAdapter(finalContext, listOfAds);
                            mListView.setAdapter(adapter);
                            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Ad ad = (Ad)mListView.getAdapter().getItem(position);
                                    Log.e(TAG, "onItemClick(" + position + ") -> " + ad);
                                    if (ad != null) {
                                        ad.click(getActivity());
                                    }
                                    else {
                                        Log.e(TAG, "Ad doesn't exist at position " + position);
                                    }
                                }
                            });
                            mListView.setClickable(true);

                        }

                        @Override
                        public void onFail(PwAdLoader pwAdLoader, String s) {
                            String msg = "Native ads failed to load: " + s;
                            mEmptyView.setText(msg);
                            Log.e("NAP", msg);
                        }
                    });
            return rootView;
        }

    }
}

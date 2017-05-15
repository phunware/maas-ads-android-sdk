package com.phunware.advertising.sample.rvisit;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.phunware.advertising.PwAdvertisingModule;
import com.phunware.advertising.PwRewardedVideoAd;
import com.phunware.advertising.internal.vast.RVSuccessInfo;
import com.phunware.advertising.internal.vast.TVASTRewardedVideoInfo;
import com.phunware.core.PwCoreSession;
import com.phunware.core.PwLog;

import java.util.Arrays;
import java.util.HashMap;

public class ExampleActivity extends AppCompatActivity {
    private final static String TAG = "RewardedVisitSample";
    private PwAdvertisingModule pwAdModule=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PwCoreSession pwCoreSession=PwCoreSession.getInstance();
        pwCoreSession.registerKeys(this, getString(R.string.app_id),
                        getString(R.string.access_key),
                        getString(R.string.sig_key),
                        "");

        setContentView(R.layout.main);

        pwAdModule=PwAdvertisingModule.getInstance();

        // enable debug logs during development
        PwLog.setShowLog(true);

        // test that you've integrated properly
        // NOTE: remove this before your app goes live!
        //pwAdModule.validateSetup(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle selection
        switch (item.getItemId()) {
            case R.id.view_rewards:
                Intent viewRewardsIntent=new Intent(this,ViewRewardsActivity.class);
                startActivity(viewRewardsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PwAdvertisingModule.getInstance().cleanUp(this);
    }


    public void fireRewardedVisit(View sender){
        String zoneId = getString(R.string.rewarded_visit_zone_id);
        HashMap<String, String> customData = new HashMap<>();
        customData.put("rewardType" , "gold");
        customData.put("rewardValue" ,"100");

        final Activity activity=this;


        PwRewardedVideoAd rewardedVideoAd = PwRewardedVideoAd.getInstance(this, zoneId);
        rewardedVideoAd.setKeywords(Arrays.asList("keyword1", "keyword2"));
        rewardedVideoAd.setUserId("5487G54d30OsdZt79");
        rewardedVideoAd.setCustomData(customData);
        rewardedVideoAd.setListener(new PwRewardedVideoAd.PwRewardedVideoAdListener() {
            @Override
            public void rewardedVideoDidLoad(PwRewardedVideoAd rewardedVideoAd, TVASTRewardedVideoInfo rewardedVideoInfo) {
                if(rewardedVideoAd != null){
                    rewardedVideoAd.show();
                }
            }

            @Override
            public void rewardedVideoDidClose(PwRewardedVideoAd rewardedVideoAd, TVASTRewardedVideoInfo rewardedVideoInfo) {
                PwLog.d(TAG, "Rewarded Visit: onCloseRewardedVideo");
            }

            @Override
            public void rewardedVideoDidFail(PwRewardedVideoAd rewardedVideoAd, String error, TVASTRewardedVideoInfo rewardedVideoInfo) {
                PwLog.d(TAG, "Rewarded Visit onRewardedVideoFail"
                        + error
                        + ". Code: "
                        + String.valueOf(rewardedVideoInfo.getError()));
                showDialog(error);
            }

            @Override
            public void rewardedVideoActionWillLeaveApplication(PwRewardedVideoAd rewardedVideoAd, TVASTRewardedVideoInfo rewardedVideoInfo) {
                PwLog.d(TAG, "Rewarded Visit onLeaveApplication");
            }

            @Override
            public void rewardedVideoDidEndPlaybackSuccessfully(PwRewardedVideoAd rewardedVideoAd, RVSuccessInfo rewardedVideoSuccessInfo, TVASTRewardedVideoInfo rewardedVideoInfo) {
                String message = String.format(getString(R.string.rewarded),
                        rewardedVideoSuccessInfo.getCurrencyId(),
                        rewardedVideoSuccessInfo.getAmount(),
                        rewardedVideoSuccessInfo.getRemainingViews());

                showDialog(message);
            }
        });

        rewardedVideoAd.load();
    }

    private void showDialog(String message){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.setTitle(getString(R.string.app_name));

        builder.setMessage(message);

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

}

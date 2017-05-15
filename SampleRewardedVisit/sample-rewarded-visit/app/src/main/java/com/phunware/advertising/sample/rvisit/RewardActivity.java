package com.phunware.advertising.sample.rvisit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.phunware.core.PwLog;
import com.phunware.engagement.Callback;
import com.phunware.engagement.Engagement;
import com.phunware.engagement.entities.Message;
import com.phunware.engagement.entities.MessageMetadata;
import com.phunware.engagement.messages.MessageManager;

import java.io.InputStream;
import java.util.List;

/**
 * Activity that is invoked when user visits the promoted location
 */

public class RewardActivity extends AppCompatActivity {

    private static final String TAG="Reward Activity";

    private static final String METADATA_KEY_URL="url";
    private static final String METADATA_KEY_CURRENCY_ID="currencyId";
    private static final String METADATA_KEY_AMOUNT="amount";

    private String currencyId;
    private String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        Intent intent=getIntent();

        if(intent.getAction() == Intent.ACTION_VIEW){
            handleIntent();
        }
        else{
            String url=intent.getStringExtra("url");
            if(url!=null){
                //display reward
                showReward(url);
            }
        }
    }


    private void handleIntent() {
        Intent intent=getIntent();

        Message intentMessage = getIntent().getParcelableExtra(MessageManager.EXTRA_MESSAGE);

        Engagement.analytics().trackCampaignAppLaunched(intentMessage.campaignId(),
                intentMessage.campaignType());

        boolean hasPromo = intent.getBooleanExtra(MessageManager.EXTRA_HAS_EXTRAS, false);
        if (hasPromo) {
            long messageId = intentMessage.campaignId();

            PwLog.d(TAG,"Campaign ID="+messageId);

            Engagement.messageManager().getMessage(messageId, new Callback<Message>() {
                @Override
                public void onSuccess(Message data) {

                    String key=null;
                    String value=null;

                    //print message details
                    PwLog.d(TAG,data.notificationTitle());
                    PwLog.d(TAG,data.notificationMessage());
                    PwLog.d(TAG,data.campaignType());

                    List<MessageMetadata> metadataList=data.metadata();

                    for(MessageMetadata tempMData: metadataList){
                        String tempKey=tempMData.key();
                        String tempValue=tempMData.value();
                        PwLog.d(TAG,"metadata-key="+tempKey);
                        PwLog.d(TAG,"metadata-value="+tempValue);

                        if(tempKey.equalsIgnoreCase(METADATA_KEY_URL)){
                            key=tempKey;
                            value=tempValue;
                        }

                        if(tempKey.equalsIgnoreCase(METADATA_KEY_CURRENCY_ID)){
                            currencyId=tempValue;
                        }

                        if(tempKey.equalsIgnoreCase(METADATA_KEY_AMOUNT)){
                            amount=tempValue;
                        }
                    }

                    //show the in-app reward
                    if(currencyId!=null && amount!=null){
                        showRewardDialog(currencyId,amount);
                    }

                    //display the reward
                    PwLog.d(TAG,"Showing reward, key="+key+", value="+value);
                    showReward(value);
                }

                @Override
                public void onFailed(Throwable e) {
                    PwLog.e(TAG,"Failed to get message for id: ", e);
                }
            });
        }

    }


    private void showRewardDialog(String rewardType,String amount){
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name);
        builder.setMessage("You just earned an in-app reward of "+amount+" "+rewardType);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                builder.create().show();
            }
        });
    }


    private void showReward(String url) {
        ImageView imgView=(ImageView) findViewById(R.id.img_view);
        DownloadTask task=new DownloadTask(imgView);
        task.execute(url);
    }


    private class DownloadTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imgView;

        public DownloadTask(ImageView imgView) {
            this.imgView = imgView;
        }

        protected Bitmap doInBackground(String... urls) {
            String displayUrl = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(displayUrl).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            }
            catch (Exception e) {
                PwLog.e("DownloadTask", "Download Error: "+e.getMessage());
                //e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            imgView.setImageBitmap(result);
        }
    }


}

package com.phunware.advertising.sample.rvisit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.phunware.core.PwLog;
import com.phunware.locationmessaging.Callback;
import com.phunware.locationmessaging.LocationMessaging;
import com.phunware.locationmessaging.entities.Message;
import com.phunware.locationmessaging.entities.MessageMetadata;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity that displays all stored rewards
 */
public class ViewRewardsActivity extends AppCompatActivity {

    private static final String TAG="Reward Activity";
    private static final String METADATA_KEY_URL="url";

    private ArrayList<Message> messages=new ArrayList<>();
    private ArrayList<RewardDetails> rewardDetails=new ArrayList<>();

    private RecyclerView recyclerView=null;
    private TextView emptyView=null;
    private RewardsAdapter rewardsAdapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_rewards);
        setupView();
        getAllMessages();
    }

    private void setupView() {
        //Get recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        emptyView=(TextView)findViewById(R.id.empty_view);
        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);

        rewardsAdapter=new RewardsAdapter();
        recyclerView.setAdapter(rewardsAdapter);
    }

    private void getAllMessages() {
        LocationMessaging.messageManager().getMessages(new Callback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> data) {
                messages.addAll(data);
                getRewardDetails(data);
                displayRewards();
            }

            @Override
            public void onFailed(Throwable e) {

            }
        });
    }

    private void getRewardDetails(List<Message> data){
        for(Message msg: data){
            String notifTitle=msg.notificationTitle();
            String notifMsg=msg.notificationMessage();
            List<MessageMetadata> metadata=msg.metadata();
            String url=getUrl(metadata);
            RewardDetails rDetails=new RewardDetails(notifTitle,notifMsg,url);
            rewardDetails.add(rDetails);
        }
    }

    private String getUrl(List<MessageMetadata> metadata){
        String url=null;
        if(metadata!=null) {
            for (MessageMetadata tempMData : metadata) {
                String tempKey = tempMData.key();
                String tempValue = tempMData.value();

                if (tempKey.equalsIgnoreCase(METADATA_KEY_URL)) {
                    url = tempValue;
                }
            }
        }
        return url;
    }

    private void displayRewards() {
        rewardsAdapter.notifyDataSetChanged();

        if(rewardsAdapter.getItemCount()<=0){
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else{
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    private void showReward(int position) {
        Intent urlIntent=new Intent(this,RewardActivity.class);
        RewardDetails rDetais=rewardDetails.get(position);
        String url=rDetais.getRewardUrl();
        urlIntent.putExtra("url",url);
        startActivity(urlIntent);
    }


    private class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.ViewHolder>{

        RewardsAdapter(){
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reward_list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(itemView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            RewardDetails rDetais=rewardDetails.get(position);
            String notifTitle=rDetais.getNotificationTitle();
            String notifMsg=rDetais.getNotificationMessage();
            String url=rDetais.getRewardUrl();
            if(notifTitle!=null){
                holder.txtViewTitle.setText(notifTitle);
            }
            if(notifMsg!=null){
                holder.txtViewMessage.setText(notifMsg);
            }
            if(url!=null){
                DownloadTask task=new DownloadTask(holder.imgViewIcon);
                task.execute(url);
            }
        }



        @Override
        public int getItemCount() {
            return rewardDetails.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ImageView imgViewIcon = null;
            private TextView txtViewTitle = null;
            private TextView txtViewMessage = null;

            public ViewHolder(View itemView) {
                super(itemView);
                imgViewIcon = (ImageView) itemView.findViewById(R.id.image);
                txtViewTitle = (TextView) itemView.findViewById(R.id.title);
                txtViewMessage = (TextView) itemView.findViewById(R.id.message);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                showReward(position);
            }
        }//class viewholder


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


    }//class adapter


    class RewardDetails{
        public String getNotificationTitle() {
            return notificationTitle;
        }

        public String getNotificationMessage() {
            return notificationMessage;
        }

        public String getRewardUrl() {
            return rewardUrl;
        }

        private String notificationTitle=null;
        private String notificationMessage=null;
        private String rewardUrl=null;

        RewardDetails(String notificationTitle,String notificationMessage,String rewardUrl){
            this.notificationTitle=notificationTitle;
            this.notificationMessage=notificationMessage;
            this.rewardUrl=rewardUrl;
        }
    }

}

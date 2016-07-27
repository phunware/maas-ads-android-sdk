package com.phunware.advertising.sample.nativeads;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.phunware.advertising.PwNativeAd;

import java.util.ArrayList;
import java.util.List;

/**
 * Example views for native ad data.  This code is provided for illustration.  No warranty or
 * assumption of liability is provided by using this code.
 */
public class NativeAds {
    public static View getNewsFeedView(final Context context, final PwNativeAd ad) {
        View view = LayoutInflater.from(context).inflate(R.layout.native_news_feed, null);
        ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lparams);

        ImageView iconView = (ImageView) view.findViewById(R.id.imageViewAdIcon);
        TextView textTitle = (TextView) view.findViewById(R.id.textViewAdTitle);
        RatingBar ratingBarStars = (RatingBar) view.findViewById(R.id.ratingBarStars);
        ratingBarStars.setEnabled(false);
        Button buttonCta = (Button) view.findViewById(R.id.buttonCta);

        Glide.with(context).load(ad.getIconUrl()).into(iconView);
        textTitle.setText(ad.getAdTitle());
        ratingBarStars.setRating((float) ad.getRating());
        buttonCta.setText(ad.getCta());
        buttonCta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.click(context);
            }
        });
        ad.trackImpression();

        return view;
    }

    public static View getContentWallView(final Context context, final PwNativeAd ad) {
        View view = LayoutInflater.from(context).inflate(R.layout.native_content_wall, null);
        ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lparams);

        ImageView iconView = (ImageView) view.findViewById(R.id.imageViewAdIcon);
        if (!TextUtils.isEmpty(ad.getIconUrl())) {
            Glide.with(context).load(ad.getIconUrl()).into(iconView);
        }
        else if (!TextUtils.isEmpty(ad.getImageUrl())) {
            Glide.with(context).load(ad.getImageUrl()).into(iconView);
        }

        iconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.click(context);
            }
        });
        ad.trackImpression();
        return view;
    }

    public static View getContentStreamView(final Context context, final PwNativeAd ad) {
        View view = LayoutInflater.from(context).inflate(R.layout.native_content_stream, null);
        ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lparams);

        ImageView iconView = (ImageView) view.findViewById(R.id.imageViewAdIcon);
        TextView textTitle = (TextView) view.findViewById(R.id.textViewAdTitle);
        RatingBar ratingBarStars = (RatingBar) view.findViewById(R.id.ratingBarStars);
        ratingBarStars.setEnabled(false);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageViewAd);
        TextView textAd = (TextView) view.findViewById(R.id.textViewAdText);
        Button buttonCta = (Button) view.findViewById(R.id.buttonCta);

        Glide.with(context).load(ad.getIconUrl()).into(iconView);
        textTitle.setText(ad.getAdTitle());
        ratingBarStars.setRating((float) ad.getRating());

        // scale image to view
        Glide.with(context).load(ad.getImageUrl()).fitCenter().into(imageView);
        textAd.setText(ad.getAdText());

        buttonCta.setText(ad.getCta());
        buttonCta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.click(context);
            }
        });
        ad.trackImpression();

        return view;
    }

    public static View getAppWallView(final Context context, final List<PwNativeAd> ads) {
        View view = LayoutInflater.from(context).inflate(R.layout.native_app_wall, null);
        ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lparams);

        RecyclerView appWallView = (RecyclerView) view.findViewById(R.id.app_wall_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        appWallView.setLayoutManager(layoutManager);

        AppWallAdapter adapter = new AppWallAdapter(context, ads);
        appWallView.setAdapter(adapter);

        return view;
    }

    public static class AppWallAdapter extends RecyclerView.Adapter<AppWallAdapter.ViewHolder> {
        private Context mContext;
        private List<PwNativeAd> mAds;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView mIconView;
            public TextView mTextTitle;
            public RatingBar mRatingBarStars;
            public Button mButtonCta;

            public ViewHolder(View v) {
                super(v);

                mIconView =(ImageView) v.findViewById(R.id.imageViewAdIcon);
                mTextTitle = (TextView) v.findViewById(R.id.textViewAdTitle);
                mRatingBarStars = (RatingBar) v.findViewById(R.id.ratingBarStars);
                mRatingBarStars.setEnabled(false);
                mButtonCta = (Button) v.findViewById(R.id.buttonCta);
            }
        }

        public AppWallAdapter(Context context, List<PwNativeAd> ads) {
            this.mContext = context;
            this.mAds = ads;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.native_app_wall_item, parent, false);
            ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            v.setLayoutParams(lparams);

            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final PwNativeAd ad = mAds.get(position);

            Glide.with(mContext).load(ad.getIconUrl()).into(holder.mIconView);
            holder.mTextTitle.setText(ad.getAdTitle());
            holder.mRatingBarStars.setRating((float) ad.getRating());
            holder.mButtonCta.setText(ad.getCta());
            holder.mButtonCta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.click(mContext);
                }
            });
            ad.trackImpression();
        }

        @Override
        public int getItemCount() {
            return mAds.size();
        }
    }

    public static View getIconsView(final Context context, final List<PwNativeAd> ads, View.OnClickListener onCloseButtonClick) {
        View view = LayoutInflater.from(context).inflate(R.layout.native_icons, null);
        // WRAP_CONTENT height can't work because the recyclerview size is not known at layout, so the custom view sets the height
        ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lparams);

        RecyclerView iconsView = (RecyclerView) view.findViewById(R.id.icons_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        iconsView.setLayoutManager(layoutManager);

        IconsAdapter adapter = new IconsAdapter(context, ads);
        iconsView.setAdapter(adapter);

        View closeButton = view.findViewById(R.id.close_button);
        closeButton.setOnClickListener(onCloseButtonClick);

        /* For performance, preload images into glide cache.
         * This work better if done earlier, well before the view is displayed.
         */
        for (PwNativeAd ad : ads) {
            Glide.with(context).load(ad.getIconUrl())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .preload();
        }

        return view;
    }

    private static class IconsAdapter extends RecyclerView.Adapter<IconsAdapter.ViewHolder> {
        private Context mContext;
        private List<PwNativeAd> mAds;

        private RecyclerView recyclerView = null;
        private boolean animatingLoad = true;
        private List<View> viewsToAnimate = new ArrayList<>();
        private View loadingView;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView mIcon;

            public ViewHolder(View v) {
                super(v);

                mIcon = (ImageView) v.findViewById(R.id.icon_view);
            }
        }

        public void	onAttachedToRecyclerView(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        public void onDetachedFromRecyclerView (RecyclerView recyclerView) {
            if (this.recyclerView == recyclerView) {
                this.recyclerView = null;
            }
        }

        public IconsAdapter(Context context, List<PwNativeAd> ads) {
            this.mContext = context;
            this.mAds = ads;
        }

        private class LoadAnimationListener implements Animation.AnimationListener {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loadingView.setAnimation(null);

                synchronized (viewsToAnimate) {
                    if (viewsToAnimate.isEmpty()) {
                        animatingLoad = false;
                    } else {
                        // animate next view
                        loadingView = viewsToAnimate.remove(0);
                        loadingView.setVisibility(View.VISIBLE);

                        // can't reuse animation, start/end not triggered properly
                        Animation loadAnimation= AnimationUtils.loadAnimation(mContext, R.anim.icons_view_load);
                        loadAnimation.setAnimationListener(new LoadAnimationListener());
                        loadingView.startAnimation(loadAnimation);
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.native_icon_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final PwNativeAd ad = mAds.get(position);

            Glide.with(mContext).load(ad.getIconUrl()).into(holder.mIcon);
            holder.mIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.click(mContext);
                }
            });
            ad.trackImpression();

            if (animatingLoad) {
                if (position == 0) {
                    loadingView = holder.itemView;
                    holder.itemView.setVisibility(View.VISIBLE);

                    Animation loadAnimation= AnimationUtils.loadAnimation(mContext, R.anim.icons_view_load);
                    loadAnimation.setAnimationListener(new LoadAnimationListener());
                    holder.itemView.startAnimation(loadAnimation);
                } else {
                    synchronized (viewsToAnimate) {
                        viewsToAnimate.add(holder.itemView);
                    }
                }
            }
            else {
                holder.itemView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return mAds.size();
        }
    }

    public static View getCleanView(final Context context, final PwNativeAd ad){
        View view = LayoutInflater.from(context).inflate(R.layout.native_clean, null);
        ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.native_clean_width), ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lparams);
        ImageView ivIcon = (ImageView)view.findViewById(R.id.imageViewAdIcon);
        TextView tvTitle = (TextView)view.findViewById(R.id.tvAdTitle);
        TextView tvDescription = (TextView)view.findViewById(R.id.tvAdDescription);
        Button btnAction = (Button)view.findViewById(R.id.btnAction);

        Glide.with(context).load(ad.getIconUrl()).into(ivIcon);
        tvTitle.setText(ad.getAdTitle());
        tvDescription.setText(ad.getAdText());
        btnAction.setText(ad.getCta());

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.click(context);
            }
        });

        return view;
    }

    public static View get3UpView(final Context context, final List<PwNativeAd> ads, final int gravity){
        LinearLayout adsContainer = new LinearLayout(context);
        adsContainer.setOrientation(LinearLayout.HORIZONTAL);
        adsContainer.setGravity(gravity);

        ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        adsContainer.setLayoutParams(lparams);

        for(final PwNativeAd ad : ads){
            View viewAd = LayoutInflater.from(context).inflate(R.layout.native_3up_item, null);
            ViewGroup.LayoutParams lparamsAd = new ViewGroup.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.native_3_up_width),
                    context.getResources().getDimensionPixelSize(R.dimen.native_3_up_height));
            viewAd.setLayoutParams(lparamsAd);

            RelativeLayout rlAdContainer = (RelativeLayout) viewAd.findViewById(R.id.rl_child_ad_container);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    context.getResources().getDimensionPixelSize(R.dimen.native_3_up_width),
                    context.getResources().getDimensionPixelSize(R.dimen.native_3_up_height));
            layoutParams.setMargins(context.getResources().getDimensionPixelSize(R.dimen.native_margin_3_up),
                    0,
                    context.getResources().getDimensionPixelSize(R.dimen.native_margin_3_up),
                    0);
            rlAdContainer.setLayoutParams(layoutParams);

            ImageView ivIcon = (ImageView)viewAd.findViewById(R.id.imageViewAdIcon);
            TextView tvTitle = (TextView)viewAd.findViewById(R.id.title_ad);
            TextView tvText = (TextView)viewAd.findViewById(R.id.text_ad);
            Button btnAction = (Button)viewAd.findViewById(R.id.btn_action);

            Glide.with(context).load(ad.getIconUrl()).into(ivIcon);
            tvTitle.setText(ad.getAdTitle());
            tvText.setText(ad.getAdText());
            btnAction.setText(ad.getCta());

            btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.click(context);
                }
            });

            adsContainer.addView(viewAd,0);
        }

        return adsContainer;
    }
}


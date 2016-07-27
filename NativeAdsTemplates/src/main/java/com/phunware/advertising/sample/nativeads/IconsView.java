package com.phunware.advertising.sample.nativeads;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Custom view to handle sizing of the icons view.
 */
public class IconsView extends RelativeLayout {
    int heightForAds = 0;

    public IconsView(Context context) {
        super(context);
    }

    public IconsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IconsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // WRAP_CONTENT height can't work because recyclerview size is not known, so calculate it
    public void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        if (heightForAds == 0) {
            RecyclerView iconsView = (RecyclerView) findViewById(R.id.icons_view);
            if (iconsView != null && iconsView.getAdapter() != null) {
                int numAds = iconsView.getAdapter().getItemCount();
                int itemSize = getContext().getResources().getDimensionPixelSize(R.dimen.icon_size);
                int itemMargin = getContext().getResources().getDimensionPixelSize(R.dimen.icon_margin);
                heightForAds = numAds * itemSize + (numAds+1) * itemMargin;
            }
        }
        if (heightForAds > 0 &&  heightForAds < h) {
            h = resolveSizeAndState(heightForAds, heightMeasureSpec, 0);
            setMeasuredDimension(getMeasuredWidthAndState(), h);
        }
    }
}

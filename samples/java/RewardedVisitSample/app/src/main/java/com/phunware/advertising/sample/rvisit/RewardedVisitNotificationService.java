package com.phunware.advertising.sample.rvisit;

import android.support.v4.app.NotificationCompat;

import com.phunware.engagement.messages.services.NotificationCustomizationService;

/**
 * Customizes the notification displayed
 */

public class RewardedVisitNotificationService extends NotificationCustomizationService {

    @Override
    public void editNotification(NotificationCompat.Builder notificationBuilder) {
        notificationBuilder.setSmallIcon(R.drawable.ic_stat_reward);
        notificationBuilder.setColor(0xFF4081);
    }
}

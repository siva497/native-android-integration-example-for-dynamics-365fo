// Microsoft Azure Notification Hubs example code for sending, channel creation and message receipt listener, not needed, keep for reference only
// Catch push notifications from Firebase service instead
package com.sertanyaman.dynamics365test.nothub;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import com.microsoft.windowsazure.notifications.NotificationsHandler;
import com.microsoft.windowsazure.notifications.NotificationsManager;
import com.sertanyaman.dynamics365test.activities.TasksActivity;

public class AzHubNotificationsHandler extends com.microsoft.windowsazure.notifications.NotificationsHandler {
    public static final String NOTIFICATION_CHANNEL_ID = "nh-demo-channel-id";
    public static final String NOTIFICATION_CHANNEL_NAME = "Notification Hubs Demo Channel";
    public static final String NOTIFICATION_CHANNEL_DESCRIPTION = "Notification Hubs Demo Channel";

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    Context ctx;

    @Override
    public void onReceive(Context context, Bundle bundle) {
        ctx = context;
        String nhMessage = bundle.getString("message");
        sendNotification(nhMessage);

        //TODO : crappy example code, write a better one
        if (TasksActivity.isVisible) {
            TasksActivity.mainActivity.toastNotify(nhMessage);
        }
    }

    private void sendNotification(String msg) {

        Intent intent = new Intent(ctx, com.sertanyaman.dynamics365test.activities.TasksActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        mNotificationManager = (NotificationManager)
                ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                ctx,
                NOTIFICATION_CHANNEL_ID)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL);

        notificationBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    public static void createChannelAndHandleNotifications(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION);
            channel.setShowBadge(true);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            NotificationsManager.handleNotifications(context, "", AzHubNotificationsHandler.class);
        }
    }
}
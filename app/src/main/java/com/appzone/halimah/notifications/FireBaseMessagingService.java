package com.appzone.halimah.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.appzone.halimah.R;
import com.appzone.halimah.models.NotificationReadModel;
import com.appzone.halimah.models.UserModel;
import com.appzone.halimah.preferences.Preferences;
import com.appzone.halimah.tags.Tags;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class FireBaseMessagingService extends FirebaseMessagingService {
    private Preferences preferences = Preferences.getInstance();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String,String> map = remoteMessage.getData();
        for (String key:map.keySet())
        {
            Log.e("key= ",key+" Value= "+map.get(key));

        }

        ManageNotification(map);


    }

    private void ManageNotification(final Map<String, String> map) {
        if (getSession().equals(Tags.LOGIN_STATE))
        {
            String user_id = getUserData().getUser_id();
            String to_id =map.get("to_user_id");

            if (user_id.equals(to_id))
            {
                new Handler(Looper.getMainLooper())
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                createNotification(map);

                            }
                        },100);
            }
        }
    }

    private void createNotification(final Map<String, String> map) {
        final String sound_path ="android.resource://"+getPackageName()+"/"+ R.raw.not;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            final int NOTIFICATION_ID=1;
            String CHANNEL_ID = "my_channel_12";
            CharSequence CHANNEL_NAME ="my_channel_name";
            int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

            final NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,IMPORTANCE);
            channel.setShowBadge(true);
            channel.setSound(Uri.parse(sound_path),new AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                    .build()
            );
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setChannelId(CHANNEL_ID);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setSound(Uri.parse(sound_path));
            builder.setContentTitle(map.get("from_user_name"));
            if (map.get("notification_type").equals("reservation"))
            {
                builder.setContentText(getString(R.string.reservation));

            }else if (map.get("notification_type").equals("accept_date"))
            {
                builder.setContentText(getString(R.string.reservation_accepted));

            }else if (map.get("notification_type").equals("refuse_date"))
            {
                builder.setContentText(getString(R.string.reservation_refused));

            }else if (map.get("notification_type").equals("accept_payment"))
            {
                builder.setContentText(getString(R.string.reservation_confirmed));

            }else if (map.get("notification_type").equals("refuse_payment"))
            {
                builder.setContentText(getString(R.string.reservation_money_refused));

            }else if (map.get("notification_type").equals("cancel_reservation"))
            {
                builder.setContentText(getString(R.string.reservation_canceled));

            }else if (map.get("notification_type").equals("payment_reservation"))
            {
                builder.setContentText(getString(R.string.reservation_money_transferred));

            }






            final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            Target target = new Target() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    builder.setLargeIcon(bitmap);
                    if (manager!=null)
                    {
                        manager.createNotificationChannel(channel);
                        manager.notify(NOTIFICATION_ID,builder.build());
                        EventBus.getDefault().post(new NotificationReadModel());
                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            if (map.get("from_user_type").equals(Tags.CLIENT_TYPE))
            {
                Picasso.with(this).load(R.drawable.logo).into(target);
            }else if (map.get("from_user_type").equals(Tags.NURSERY_TYPE))
            {
                Picasso.with(this).load(Uri.parse(Tags.IMAGE_PATH)+map.get("from_user_image")).into(target);

            }



        }else
            {
                final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);


                final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                Target target = new Target() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        builder.setLargeIcon(bitmap);
                        if (manager!=null)
                        {
                            builder.setSmallIcon(R.mipmap.ic_launcher);
                            builder.setSound(Uri.parse(sound_path));
                            builder.setContentTitle(map.get("from_user_name"));
                            if (map.get("notification_type").equals("reservation"))
                            {
                                builder.setContentText(getString(R.string.reservation));

                            }else if (map.get("notification_type").equals("accept_date"))
                            {
                                builder.setContentText(getString(R.string.reservation_accepted));

                            }else if (map.get("notification_type").equals("refuse_date"))
                            {
                                builder.setContentText(getString(R.string.reservation_refused));

                            }else if (map.get("notification_type").equals("accept_payment"))
                            {
                                builder.setContentText(getString(R.string.reservation_confirmed));

                            }else if (map.get("notification_type").equals("refuse_payment"))
                            {
                                builder.setContentText(getString(R.string.reservation_money_refused));

                            }else if (map.get("notification_type").equals("cancel_reservation"))
                            {
                                builder.setContentText(getString(R.string.reservation_canceled));

                            }else if (map.get("notification_type").equals("payment_reservation"))
                            {
                                builder.setContentText(getString(R.string.reservation_money_transferred));

                            }


                            manager.notify(1,builder.build());
                            EventBus.getDefault().post(new NotificationReadModel());

                        }
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };

                if (map.get("from_user_type").equals(Tags.CLIENT_TYPE))
                {
                    Picasso.with(this).load(R.drawable.logo).into(target);
                }else if (map.get("from_user_type").equals(Tags.NURSERY_TYPE))
                {
                    Picasso.with(this).load(Uri.parse(Tags.IMAGE_PATH)+map.get("from_user_image")).into(target);

                }

            }
    }

    private String getSession()
    {
        return preferences.getSession(this);
    }
    private UserModel getUserData()
    {
        return preferences.getUserModel(this);
    }
}

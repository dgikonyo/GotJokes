package com.gikonyo.startlab;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */

public class MessageService extends IntentService {
    //this is a constant key to pass message from MainActivity to the service
    public static final String EXTRA_MESSAGE="MESSAGE";
    public static final String  CHANNEL_1_ID="sendJoke";
    private NotificationManagerCompat notificationManagerCompat;

    // private Handler handler;//we use the handler to post on the main thread

    public static  final int NOTIFICATION_ID=1;// will be used to identify the notification

    public MessageService() {
        super("MessageService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {//will run the service when it receives an intent
        createNotificationChannel();
        notificationManagerCompat= NotificationManagerCompat.from(this);//will get reference to notification manager

        synchronized (this){//synchronised() is a method that allows us to lock access of specific lines of code from other java threads

            try{
                wait(10000);//wait for 10secs
            }catch(InterruptedException error){
                error.printStackTrace();
            }
        }
        //get the text from the Intent
        String text=intent.getStringExtra(EXTRA_MESSAGE);
        //call showText method
        showText(text);
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel channel1=new NotificationChannel(
                    CHANNEL_1_ID,
                    "channel1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("We now want to send jokes");

            NotificationManager jokeManager=getSystemService(NotificationManager.class);
            jokeManager.createNotificationChannel(channel1);
        }
    }

    private void showText(final String text) {
        Log.v("DelayedMessageService","What do we call a bee that is hungry??"+text);//this line will be seen in the LogCat

        Intent intent=new Intent(this,MainActivity.class);//explicit intent

        TaskStackBuilder stackBuilder=TaskStackBuilder.create(this);//allows us to access the history of activities used by the back button
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent=stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);//will get any pending intent an update it using the data from the new intent

        Notification notification=new NotificationCompat.Builder(this,CHANNEL_1_ID)
                //this is the notification icon
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                //this is the application icon
                .setContentTitle(getString(R.string.app_name))
                //content text
                .setContentText(text)
                //application will disappear when clicked
                .setAutoCancel(true)
                //we give it a maximum priority to allow peaking
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)//open main activity when clicking the notification
                .build();
        //NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);//we are issuing/instantiating the notification
        notificationManagerCompat.notify(NOTIFICATION_ID,notification);


    }

}

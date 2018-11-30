package com.durzoflint.mrpuncture_shop.firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.durzoflint.mrpuncture_shop.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.durzoflint.mrpuncture_shop.LoginActivity.LOGIN_PREFS;
import static com.durzoflint.mrpuncture_shop.LoginActivity.USER_ID;

public class MyFirebaseMessagingService extends FirebaseMessagingService{
    public static final String SHOPS = "shops";
    public static final String TOKEN = "token";
    String CHANNEL_ID = "MyChannelId";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_PREFS, Context
                .MODE_PRIVATE);
        String id = sharedPreferences.getString(USER_ID, "");
        if (!id.isEmpty()) {
            sendTokenToServer(s, SHOPS, id);
        } else {
            SharedPreferences firebasePreferences = getSharedPreferences(LOGIN_PREFS, Context
                    .MODE_PRIVATE);
            firebasePreferences.edit().putString(TOKEN, s).apply();
        }
    }

    private void sendTokenToServer(String s, String table, String index) {
        String baseUrl = "http://www.mrpuncture.com/app/";
        String webPage = "";
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            String myURL = baseUrl + "addfirebaseid.php?i="+index+"&t="+table+"&s="+s;
            myURL = myURL.replaceAll(" ", "%20");
            myURL = myURL.replaceAll("\'", "%27");
            myURL = myURL.replaceAll("\'", "%22");
            myURL = myURL.replaceAll("\\(", "%28");
            myURL = myURL.replaceAll("\\)", "%29");
            myURL = myURL.replaceAll("\\{", "%7B");
            myURL = myURL.replaceAll("\\}", "%7B");
            myURL = myURL.replaceAll("\\]", "%22");
            myURL = myURL.replaceAll("\\[", "%22");
            url = new URL(myURL);
            urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String data;
            while ((data = br.readLine()) != null)
                webPage = webPage + data;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.radiobutton_on_background)
                    .setColor(getResources().getColor(R.color.colorSecondary))
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManager notificationManager =
                    (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Create the NotificationChannel, but only on API 26+ because
                // the NotificationChannel class is new and not in the support library
                CharSequence name = "Default";
                String description = "Default Channel";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);
                // Register the channel with the system
                notificationManager.createNotificationChannel(channel);
            }

            //NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify((int) (Math.random() * 10), mBuilder.build());
        }
    }
}

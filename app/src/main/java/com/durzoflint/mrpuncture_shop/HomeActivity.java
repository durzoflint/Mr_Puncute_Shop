package com.durzoflint.mrpuncture_shop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.durzoflint.mrpuncture_shop.firebase.MyFirebaseMessagingService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.durzoflint.mrpuncture_shop.LoginActivity.LOGIN_PREFS;
import static com.durzoflint.mrpuncture_shop.LoginActivity.USER_ID;
import static com.durzoflint.mrpuncture_shop.firebase.MyFirebaseMessagingService.TOKEN;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("On Going Orders");

        SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_PREFS, Context
                .MODE_PRIVATE);
        String id = sharedPreferences.getString(USER_ID, "");
        if (id.isEmpty()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            SharedPreferences firebasePreferences = getSharedPreferences(LOGIN_PREFS, Context
                    .MODE_PRIVATE);
            String token = firebasePreferences.getString(TOKEN, "");
            if (!token.isEmpty()) {
                new SendTokenToServer().execute(token, id);
            }
        }
    }

    class SendTokenToServer extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            String baseUrl = "http://www.mrpuncture.com/app/";
            String webPage = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                String myURL = baseUrl + "addfirebaseid.php?i=" + strings[1] + "&t=" +
                        MyFirebaseMessagingService.SHOPS + "&s=" + strings[0];
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
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection
                        .getInputStream()));
                String data;
                while ((data = br.readLine()) != null)
                    webPage = webPage + data;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.previous:
                startActivity(new Intent(this, PreviousOrdersActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
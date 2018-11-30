package com.durzoflint.mrpuncture_shop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.durzoflint.mrpuncture_shop.Adapters.RecyclerViewItemClickListener;
import com.durzoflint.mrpuncture_shop.Adapters.Recycler_View_Adapter;
import com.durzoflint.mrpuncture_shop.Adapters.Store;
import com.durzoflint.mrpuncture_shop.firebase.MyFirebaseMessagingService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.durzoflint.mrpuncture_shop.LoginActivity.LOGIN_PREFS;
import static com.durzoflint.mrpuncture_shop.LoginActivity.USER_ID;
import static com.durzoflint.mrpuncture_shop.firebase.MyFirebaseMessagingService.TOKEN;

public class HomeActivity extends AppCompatActivity {

    public static final String USER = "user";
    List<Store> stores;
    LinearLayout progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        progress = findViewById(R.id.progress_layout);
        progress.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_PREFS, Context
                .MODE_PRIVATE);
        String id = sharedPreferences.getString(USER_ID, "");
        if (id.isEmpty()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            SharedPreferences firebasePreferences = getSharedPreferences(LOGIN_PREFS, Context.MODE_PRIVATE);
            String token = firebasePreferences.getString(TOKEN, "");
            if (!token.isEmpty()) {
                new SendTokenToServer().execute(token, id);
            }
        }

        new FetchOrders().execute(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.logout:
                SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_PREFS, Context
                        .MODE_PRIVATE);
                sharedPreferences.edit().putString(USER_ID, "").apply();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(final List<Store> stores) {
        progress.setVisibility(View.GONE);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new Recycler_View_Adapter(stores, this, new
                RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                ArrayList<String> shop = new ArrayList<>();
                shop.add(stores.get(position).orderId);
                shop.add(stores.get(position).userId);
                shop.add(stores.get(position).name);
                shop.add(stores.get(position).status);
                shop.add(stores.get(position).number);

                Intent intent = new Intent(HomeActivity.this, UserActivity.class);
                intent.putExtra(USER, shop);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    class FetchOrders extends AsyncTask<String, Void, Void> {
        String baseUrl = "http://www.mrpuncture.com/app/";
        String webPage = "";

        @Override
        protected Void doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                String myURL = baseUrl + "fetchorders.php?i=" + strings[0];
                myURL = myURL.replaceAll(" ", "%20");
                myURL = myURL.replaceAll("\'", "%27");
                myURL = myURL.replaceAll("\'", "%22");
                myURL = myURL.replaceAll("\\+'", "%2B");
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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            String response[] = webPage.split("<br>");
            stores = new ArrayList<>();
            for (int i = 0, k = 0; k < response.length / 5; i += 5, k++) {
                stores.add(new Store(response[i], response[i + 1], response[i + 2], response[i +
                        3], response[i + 4]));
            }
            setupRecyclerView(stores);
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
}
package com.durzoflint.mrpuncture_shop;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.durzoflint.mrpuncture_shop.HomeActivity.USER;

public class UserActivity extends AppCompatActivity {

    public static final String USERS = "users";
    String userId;
    String name;
    String orderId;
    String number;
    TextView numberTV;
    Button negativeButton;
    Button positiveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();
        ArrayList<String> user = intent.getStringArrayListExtra(USER);

        orderId = user.get(0);
        userId = user.get(1);
        name = user.get(2);
        String status = user.get(3);
        number = user.get(4);

        updateUI(status);
    }

    private void updateUI(String status) {
        TextView nameTV = findViewById(R.id.name);
        TextView statusTV = findViewById(R.id.status);
        numberTV = findViewById(R.id.number);

        positiveButton = findViewById(R.id.positive);
        negativeButton = findViewById(R.id.negative);

        nameTV.setText(name);
        statusTV.setText(status);
        numberTV.setText(number);

        switch (status) {
            case "pending":
                negativeButton.setText("Reject");
                positiveButton.setText("Accept");
                negativeButton.setVisibility(View.VISIBLE);
                positiveButton.setVisibility(View.VISIBLE);
                statusTV.setText("Pending");
                break;
            case "approved":
                numberTV.setVisibility(View.VISIBLE);
                negativeButton.setVisibility(View.VISIBLE);
                negativeButton.setText("Cancel");
                positiveButton.setVisibility(View.VISIBLE);
                positiveButton.setText("Complete");
                statusTV.setText("Approved");
                break;
            case "completed":
                statusTV.setText("Completed");
                break;
            case "cancelled_by_shop":
                statusTV.setText("Cancelled By You");
                break;
            case "cancelled_by_user":
                statusTV.setText("Cancelled By User");
                break;
        }

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (negativeButton.getText().toString().equals("Reject")) {
                    new ChangeStatus().execute("cancelled_by_shop");
                    new Notify().execute(userId, USERS, "Request Rejected", "Your Request has " +
                            "been Rejected.");
                    Toast.makeText(UserActivity.this, "Order Rejected", Toast.LENGTH_SHORT).show();
                } else if (negativeButton.getText().toString().equals("Cancel")) {
                    new ChangeStatus().execute("cancelled_by_shop");
                    new Notify().execute(userId, USERS, "Request Approved", "Your Request has " +
                            "been Cancelled.");
                    Toast.makeText(UserActivity.this, "Order Cancelled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (positiveButton.getText().toString().equals("Accept")) {
                    new ChangeStatus().execute("approved");
                    new Notify().execute(userId, USERS, "Request Approved", "Your Request has " +
                            "been approved.");
                    Toast.makeText(UserActivity.this, "Order Approved", Toast.LENGTH_SHORT).show();
                } else if (positiveButton.getText().toString().equals("Complete")) {
                    new ChangeStatus().execute("completed");
                    new Notify().execute(userId, USERS, "Request Completed", "Your Request has " +
                            "been completed");
                    Toast.makeText(UserActivity.this, "Request Completed", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    class FetchStatus extends AsyncTask<Void, Void, Void> {
        String baseUrl = "http://www.mrpuncture.com/app/";
        String webPage = "";

        @Override
        protected Void doInBackground(Void... voids) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                String myURL = baseUrl + "updatestatus.php?i=" + orderId;
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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            String status = webPage;

            numberTV.setVisibility(View.GONE);
            negativeButton.setVisibility(View.GONE);
            positiveButton.setVisibility(View.GONE);
            updateUI(status);
        }
    }

    class ChangeStatus extends AsyncTask<String, Void, Void> {
        String baseUrl = "http://www.mrpuncture.com/app/";
        String webPage = "";

        @Override
        protected Void doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                String myURL = baseUrl + "changeorderstatus.php?i=" + orderId + "&s=" + strings[0];
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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new FetchStatus().execute();
        }
    }

    class Notify extends AsyncTask<String, Void, Void> {
        String baseUrl = "http://www.mrpuncture.com/app/";
        String webPage = "";

        @Override
        protected Void doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                String myURL = baseUrl + "sendnotification.php?i=" + strings[0] + "&t=" + strings[1]
                        + "&title=" + strings[2] + "&body=" + strings[3];
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
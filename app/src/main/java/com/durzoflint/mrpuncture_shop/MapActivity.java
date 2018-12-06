package com.durzoflint.mrpuncture_shop;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    int REQUEST_LOCATION = 1;
    LatLng target;
    String name, address, phone, password, four, three, two;
    private View mapView;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Intent intent = getIntent();
        name = intent.getStringExtra(RegisterActivity.NAME);
        address = intent.getStringExtra(RegisterActivity.ADDRESS);
        phone = intent.getStringExtra(RegisterActivity.PHONE);
        password = intent.getStringExtra(RegisterActivity.PASSWORD);
        four = intent.getStringExtra(RegisterActivity.FOUR);
        three = intent.getStringExtra(RegisterActivity.THREE);
        two = intent.getStringExtra(RegisterActivity.TWO);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);

        Button useThis = findViewById(R.id.use_this);
        useThis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                target = mMap.getCameraPosition().target;
                new Register().execute(name, address, phone, password, target.latitude + "",
                        target.longitude + "", four, three, two);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableUserLocation();
    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                    .ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            //To Place the MyLocationButton to the bottom right
            if (mapView != null &&
                    mapView.findViewById(Integer.parseInt("1")) != null) {
                View locationButton = ((View) mapView.findViewById(Integer.parseInt("1"))
                        .getParent()).findViewById(Integer.parseInt("2"));
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                        locationButton.getLayoutParams();
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                layoutParams.setMargins(0, 0, 30, 30);
            }

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new
                    OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        LatLng user = new LatLng(location.getLatitude(), location.getLongitude());

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user, 16f));
                    } else {
                        /*LatLng fipola = new LatLng(13.092498, 80.2179698);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fipola, 16f));*/
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[]
            grantResults) {

        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
            }
        }
    }

    class Register extends AsyncTask<String, Void, Void> {
        String baseUrl = "http://www.mrpuncture.com/app/";
        String webPage = "";

        @Override
        protected Void doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                String myURL = baseUrl + "addshop.php?name=" + strings[0] + "&address=" +
                        strings[1] +
                        "&phone=" + strings[2] + "&pass=" + strings[3] + "&lati=" + strings[4] +
                        "&longi=" + strings[5] + "&four=" + strings[6] + "&three=" + strings[7] +
                        "&two=" + strings[8];
                myURL = myURL.replaceAll(" ", "%20");
                myURL = myURL.replaceAll("\'", "%27");
                myURL = myURL.replaceAll("\\+", "%2B");
                myURL = myURL.replaceAll("\'", "%22");
                myURL = myURL.replaceAll("\\(", "%28");
                myURL = myURL.replaceAll("\\)", "%29");
                myURL = myURL.replaceAll("\\{", "%7B");
                myURL = myURL.replaceAll("\\}", "%7B");
                myURL = myURL.replaceAll("\\]", "%22");
                myURL = myURL.replaceAll("\\[", "%22");
                Log.d("Abhinav", myURL);
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

            if (!webPage.equals("success")) {
                Toast.makeText(MapActivity.this, "Some Error Occured while registering",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MapActivity.this, "Registration Success", Toast.LENGTH_SHORT)
                        .show();
                startActivity(new Intent(MapActivity.this, LoginActivity.class));
                finish();
            }
        }
    }
}

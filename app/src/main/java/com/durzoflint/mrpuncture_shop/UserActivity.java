package com.durzoflint.mrpuncture_shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import static com.durzoflint.mrpuncture_shop.HomeActivity.USER;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();
        ArrayList<String> user = intent.getStringArrayListExtra(USER);

        TextView name = findViewById(R.id.name);
        TextView status = findViewById(R.id.status);
        TextView number = findViewById(R.id.number);

        Button positive = findViewById(R.id.positive);
        Button negative = findViewById(R.id.negative);

        name.setText(user.get(1));
        status.setText(user.get(2));
        number.setText(user.get(3));

        switch (user.get(2)) {
            case "pending":
                break;
            case "approved":
                break;
            case "completed":
                break;
            case "cancelled_by_shop":
                break;
            case "cancelled_by_user":
                break;
        }
    }
}
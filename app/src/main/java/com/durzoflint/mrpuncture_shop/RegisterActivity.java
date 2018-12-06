package com.durzoflint.mrpuncture_shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String PHONE = "phone";
    public static final String PASSWORD = "password";
    public static final String FOUR = "four";
    public static final String THREE = "three";
    public static final String TWO = "two";
    EditText name, address, phone, password;
    CheckBox four, three, two;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        four = findViewById(R.id.four);
        three = findViewById(R.id.three);
        two = findViewById(R.id.two);

        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    Intent intent = new Intent(RegisterActivity.this, MapActivity.class);
                    intent.putExtra(NAME, name.getText().toString().trim());
                    intent.putExtra(ADDRESS, address.getText().toString().trim());
                    intent.putExtra(PHONE, phone.getText().toString().trim());
                    intent.putExtra(PASSWORD, password.getText().toString().trim());
                    intent.putExtra(FOUR, (four.isChecked() ? "y" : "n"));
                    intent.putExtra(THREE, (three.isChecked() ? "y" : "n"));
                    intent.putExtra(TWO, (two.isChecked() ? "y" : "n"));
                    startActivity(intent);
                }
            }
        });
    }

    private boolean validate() {
        if (name.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "The name cannot empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (address.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "The address cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (phone.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "The Phone cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "The password cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}

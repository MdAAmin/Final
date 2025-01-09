package com.example.menumade;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) View layout = inflater.inflate(R.layout.toaster, findViewById(R.id.go));

        Toast customToast = new Toast(getApplicationContext());
        customToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        customToast.setDuration(Toast.LENGTH_SHORT);
        customToast.setView(layout);

        Button buttonToUser = findViewById(R.id.loginButton);
        buttonToUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customToast.show();
                Intent intent = new Intent(WelcomePage.this, AdminAndOldTable.class);
                startActivity(intent);
            }
        });

        Button buttonPerson = findViewById(R.id.getStartedButton);
        buttonPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customToast.show();
                Intent intent = new Intent(WelcomePage.this, TableBooking.class);
                startActivity(intent);
            }
        });
    }
}

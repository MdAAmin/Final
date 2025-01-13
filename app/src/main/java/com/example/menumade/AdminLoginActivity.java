package com.example.menumade;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText emailEditText, passEditText;
    private Button submit;
    private TextView register;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        emailEditText = findViewById(R.id.email);
        passEditText = findViewById(R.id.pass);
        submit = findViewById(R.id.submit);
        register = findViewById(R.id.register);
        progressBar = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();

        submit.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String pass = passEditText.getText().toString();

            // Input validation
            if (email.isEmpty()) {
                emailEditText.setError("Email cannot be empty!");
                emailEditText.requestFocus();
            } else if (pass.isEmpty()) {
                passEditText.setError("Password cannot be empty!");
                passEditText.requestFocus();
            } else {
                progressBar.setVisibility(View.VISIBLE);

                // Admin login with Firebase Authentication
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        FirebaseUser user = auth.getCurrentUser();

                        if (task.isSuccessful()) {
                            if (user != null && user.isEmailVerified()) {
                                // Admin successfully logged in
                                Toast.makeText(AdminLoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                                // Redirect to Admin Dashboard Activity (assuming AdminDashboardActivity is the next screen)
                                startActivity(new Intent(AdminLoginActivity.this, AdminHome.class));
                                finish(); // Finish the login activity to prevent back navigation
                            } else {
                                // Email not verified, sign out and show an error message
                                Toast.makeText(AdminLoginActivity.this, "Please verify your email.", Toast.LENGTH_SHORT).show();
                                auth.signOut(); // Sign out the user
                            }
                        } else {
                            // Login failed, show error
                            Toast.makeText(AdminLoginActivity.this, "Login Failed! Check your credentials.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        register.setOnClickListener(v -> {
            // Redirect to the AdminSignUpActivity for registration
            startActivity(new Intent(AdminLoginActivity.this, AdminSignUp.class));
        });
    }
}

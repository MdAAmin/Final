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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class AdminSignUp extends AppCompatActivity {

    private EditText emailEditText, passEditText, confirmPassEditText, phoneEditText, adminNameEditText, defaultAdminNameEditText, defaultAdminPassEditText;
    private String email, pass, confirmPass, phone, adminName, defaultAdminName, defaultAdminPass;
    private Button submit;
    private ProgressBar progressBar;
    private TextView alreadyRegisteredText;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private static final String DEFAULT_ADMIN_NAME = "admin";
    private static final String DEFAULT_ADMIN_PASS = "12345Aa@";

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String PHONE_REGEX = "^01[3-9]\\d{8}$";
    private static final String ADMIN_NAME_REGEX = "^[a-zA-Z0-9\\-_\\.]+([a-zA-Z0-9\\-_\\.\\s]*)$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signup);

        firestore = FirebaseFirestore.getInstance();

        emailEditText = findViewById(R.id.email);
        passEditText = findViewById(R.id.pass);
        confirmPassEditText = findViewById(R.id.confirm_pass);
        phoneEditText = findViewById(R.id.phone);
        adminNameEditText = findViewById(R.id.adminName);
        defaultAdminNameEditText = findViewById(R.id.default_adminName);
        defaultAdminPassEditText = findViewById(R.id.defalult_adminPass);
        submit = findViewById(R.id.submit);
        progressBar = findViewById(R.id.progressBar);
        alreadyRegisteredText = findViewById(R.id.alreadyRegistered);
        auth = FirebaseAuth.getInstance();

        defaultAdminPassEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                defaultAdminName = defaultAdminNameEditText.getText().toString();
                defaultAdminPass = defaultAdminPassEditText.getText().toString();

                if (defaultAdminName.equals(DEFAULT_ADMIN_NAME) && defaultAdminPass.equals(DEFAULT_ADMIN_PASS)) {
                    submit.setEnabled(true);
                    Toast.makeText(this, "Admin credentials verified!", Toast.LENGTH_SHORT).show();
                } else {
                    submit.setEnabled(false);
                    Toast.makeText(this, "Invalid admin credentials!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        submit.setOnClickListener(v -> {
            email = emailEditText.getText().toString();
            pass = passEditText.getText().toString();
            confirmPass = confirmPassEditText.getText().toString();
            phone = phoneEditText.getText().toString();
            adminName = adminNameEditText.getText().toString();

            progressBar.setVisibility(View.VISIBLE);

            if (email.isEmpty()) {
                emailEditText.setError("Email cannot be empty!");
                emailEditText.requestFocus();
            } else if (!email.matches(EMAIL_REGEX)) {
                emailEditText.setError("Invalid email format!");
                emailEditText.requestFocus();
            } else if (pass.isEmpty()) {
                passEditText.setError("Password cannot be empty!");
                passEditText.requestFocus();
            } else if (pass.length() < 6) {
                passEditText.setError("Password must be at least 6 characters!");
                passEditText.requestFocus();
            } else if (!pass.equals(confirmPass)) {
                confirmPassEditText.setError("Passwords do not match!");
                confirmPassEditText.requestFocus();
            } else if (phone.isEmpty()) {
                phoneEditText.setError("Phone number cannot be empty!");
                phoneEditText.requestFocus();
            } else if (!phone.matches(PHONE_REGEX)) {
                phoneEditText.setError("Invalid phone number!");
                phoneEditText.requestFocus();
            } else if (adminName.isEmpty()) {
                adminNameEditText.setError("Admin name cannot be empty!");
                adminNameEditText.requestFocus();
            } else if (!adminName.matches(ADMIN_NAME_REGEX)) {
                adminNameEditText.setError("Admin name can only contain letters, numbers, spaces, and -_. characters!");
                adminNameEditText.requestFocus();
            } else {
                auth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);

                                if (task.isSuccessful()) {
                                    FirebaseUser user = auth.getCurrentUser();
                                    if (user != null) {
                                        user.sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(AdminSignUp.this, "Verification email sent!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                        storeUserDetailsInFirestore(user);
                                    }

                                    Toast.makeText(AdminSignUp.this, "Admin registration successful! Please verify your email.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(AdminSignUp.this, AdminLoginActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(AdminSignUp.this, "Registration Failed! Try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        alreadyRegisteredText.setOnClickListener(v -> {
            startActivity(new Intent(AdminSignUp.this, AdminLoginActivity.class));
            finish();
        });
    }

    private void storeUserDetailsInFirestore(FirebaseUser user) {
        String userId = user.getUid();
        String email = user.getEmail();
        String phone = phoneEditText.getText().toString();
        String adminName = this.adminName;

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("name", adminName);
        userDetails.put("email", email);
        userDetails.put("phone", phone.isEmpty() ? "No phone number" : phone);

        DocumentReference userDocRef = firestore.collection("users").document(userId);
        userDocRef.set(userDetails)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getApplicationContext(), "User details saved to Firestore", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Failed to save user details to Firestore", Toast.LENGTH_SHORT).show();
                });
    }
}

package com.example.watertrackingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;


import com.example.watertrackingsystem.network.RegisterHelper;

import org.json.JSONObject;

public class Register extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, phoneEditText, fullNameEditText; // Added fullNameEditText
    private TextView loginLabel;
    private AppCompatButton registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullNameEditText = findViewById(R.id.fullname);  // Full name field
        emailEditText = findViewById(R.id.emailAddress);
        passwordEditText = findViewById(R.id.password);
        phoneEditText = findViewById(R.id.phone);  // Phone number field
        registerButton = findViewById(R.id.createAccountButton);
        loginLabel = findViewById(R.id.loginLabel);

        loginLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = fullNameEditText.getText().toString().trim();  // Get full name
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();

                if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(phone)) {
                    Toast.makeText(Register.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Prepare JSON payload
                JSONObject userData = new JSONObject();
                try {
                    userData.put("name", fullName);  // Add full name
                    userData.put("email", email);
                    userData.put("password", password);
                    userData.put("phone", phone);

                    // Call RegisterHelper to handle registration
                    RegisterHelper.registerUser(userData, new RegisterHelper.RegisterCallback() {
                        @Override
                        public void onRegisterSuccess() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Register.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Register.this, Login.class);
                                    startActivity(intent);
                                }
                            });
                        }

                        @Override
                        public void onRegisterFailure(String errorMessage) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Register.this, errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } catch (Exception e) {
                    Log.e("Register", "Error creating JSON payload: " + e.getMessage());
                    Toast.makeText(Register.this, "Error creating data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

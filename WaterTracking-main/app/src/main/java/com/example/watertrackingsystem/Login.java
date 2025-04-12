package com.example.watertrackingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.example.watertrackingsystem.network.LoginHelper;

public class Login extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private RadioButton radioButton;
    private AppCompatButton loginButton;
    private TextView forgotPasswordTextView, signUpTextView;

    // Initialize your views
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.password);
        radioButton = findViewById(R.id.radioButton);
        loginButton = findViewById(R.id.loginButton);
        forgotPasswordTextView = findViewById(R.id.ForgotPasswordLabel);
        signUpTextView = findViewById(R.id.signUpLabel);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                } else {
                    // Call the LoginHelper to handle login asynchronously
                    LoginHelper.loginUser(email, password, new LoginHelper.LoginCallback() {
                        @Override
                        public void onLoginSuccess() {
                            // Login success: Show a success message and transition to Dashboard
                            Toast.makeText(Login.this, "Login Success!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, Dashboard.class);
                            startActivity(intent);
                            finish();  // Ensure the user can't return to login after logging in
                        }

                        @Override
                        public void onLoginFailure(String errorMessage) {
                            // Login failure: Show the error message
                            Toast.makeText(Login.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        // Redirect to Sign Up activity
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        // Handle Forgot Password action
        String forgotPasswordText = "Forgot Password";
        SpannableString spannableString = new SpannableString(forgotPasswordText);
        spannableString.setSpan(new UnderlineSpan(), 0, forgotPasswordText.length(), 0);
        spannableString.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(this, R.color.primary)),
                0,
                forgotPasswordText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        forgotPasswordTextView.setText(spannableString);

        // Handle Remember Me functionality
        radioButton.setOnClickListener(v -> radioButton.setChecked(!radioButton.isChecked()));
    }
}

package com.example.watertrackingsystem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Onboarding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding); // Replace with your actual layout file name
        initializeOnboarding();
    }

    private void initializeOnboarding() {  // Logic for onboarding setup, such as view pager or tutorial steps
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, Onboarding.class);
        context.startActivity(intent);
    }
}

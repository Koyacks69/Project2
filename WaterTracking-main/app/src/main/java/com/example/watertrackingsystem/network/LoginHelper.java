package com.example.watertrackingsystem.network;

import okhttp3.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import android.util.Log;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

public class LoginHelper {

    private static final String LOGIN_URL = "http://192.168.1.38/water_tracking_api/login.php";  // Update URL for emulator

    public interface LoginCallback {
        void onLoginSuccess();
        void onLoginFailure(String errorMessage);
    }

    public static void loginUser(String email, String password, LoginCallback callback) {
        OkHttpClient client = new OkHttpClient();

        // Create JSON payload
        JsonObject json = new JsonObject();
        json.addProperty("email", email);
        json.addProperty("password", password);

        // Build request
        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(LOGIN_URL)
                .post(body)
                .build();

        // Execute request
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle failure
                new Handler(Looper.getMainLooper()).post(() -> {
                    callback.onLoginFailure("Request failed: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JsonObject responseJson = JsonParser.parseString(responseBody).getAsJsonObject();
                    String status = responseJson.get("status").getAsString();
                    String message = responseJson.get("message").getAsString();

                    // Handle response
                    new Handler(Looper.getMainLooper()).post(() -> {
                        if ("success".equals(status)) {
                            callback.onLoginSuccess();
                        } else {
                            callback.onLoginFailure("Login failed: " + message);
                        }
                    });
                } else {
                    // Server error
                    new Handler(Looper.getMainLooper()).post(() -> {
                        callback.onLoginFailure("Server error: " + response.code());
                    });
                }
            }
        });
    }
}

package com.example.watertrackingsystem.network;

import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterHelper {

    // Define callback interface for registration success and failure
    public interface RegisterCallback {
        void onRegisterSuccess();
        void onRegisterFailure(String errorMessage);
    }

    // Function to register user
    public static void registerUser(JSONObject userData, final RegisterCallback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                // URL of your API endpoint for user registration
                URL url = new URL("http://192.168.1.38/water_tracking_api/register.php");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");  // Set content type to application/json
                connection.setDoOutput(true);

                // Send JSON data to the server
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(userData.toString().getBytes("UTF-8"));
                    os.flush();
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                    // Read server response
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        Log.d("RegisterHelper", "Server Response: " + response.toString());
                        callback.onRegisterSuccess();
                    }
                } else {
                    // Read server error response
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                        StringBuilder errorResponse = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            errorResponse.append(line);
                        }
                        Log.e("RegisterHelper", "Error Response: " + errorResponse.toString());
                        callback.onRegisterFailure("Registration failed: " + errorResponse.toString());
                    }
                }
            } catch (Exception e) {
                Log.e("RegisterHelper", "Error during registration: " + e.getMessage(), e);
                callback.onRegisterFailure("Error during registration: " + e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }
}

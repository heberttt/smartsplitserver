package com.smartsplit.accountservice.Firebase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.google.firebase.cloud.StorageClient;

@Service
public class FirebaseStorageService {

    public String uploadImage(String googleLink, String filePath) throws IOException {
        URL url = new URL(googleLink);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.connect();

        if (connection.getResponseCode() != 200) {
            throw new IOException("Failed to download image: " + connection.getResponseCode());
        }

        InputStream inputStream = connection.getInputStream();
        byte[] imageBytes = inputStream.readAllBytes();
        inputStream.close();
        connection.disconnect();

        StorageClient.getInstance().bucket().create(filePath, new ByteArrayInputStream(imageBytes), "image/jpeg");

        return "https://firebasestorage.googleapis.com/v0/b/" +
                StorageClient.getInstance().bucket().getName() +
                "/o/" + filePath.replace("/", "%2F") +
                "?alt=media";
    }
}
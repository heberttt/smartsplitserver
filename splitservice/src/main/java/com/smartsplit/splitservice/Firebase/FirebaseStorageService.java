package com.smartsplit.splitservice.Firebase;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.firebase.cloud.StorageClient;

@Service
public class FirebaseStorageService {

    public String uploadImage(MultipartFile file, String filePath) throws IOException {
        StorageClient.getInstance().bucket().create(filePath, file.getInputStream(), file.getContentType());

        return "https://firebasestorage.googleapis.com/v0/b/" +
                StorageClient.getInstance().bucket().getName() +
                "/o/" + filePath.replace("/", "%2F") +
                "?alt=media";
    }
}
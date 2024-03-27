package com.example.demo.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.stereotype.Component;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

@Component
public class CloudStorage {
    private static final String CONNECTION_STRING = "";

    public void saveToCloud(byte[] image, String imageName) {
        // Create a BlobServiceClient object using a connection string
        BlobServiceClient client = new BlobServiceClientBuilder().connectionString(CONNECTION_STRING).buildClient();

        // Create a unique name for the container
        String containerName = "images";

        // Create the container and return a container client object
        BlobContainerClient blobContainerClient = client.createBlobContainerIfNotExists(containerName);

        // Get a reference to a blob
        BlobClient blobClient = blobContainerClient.getBlobClient(imageName);

        // Upload the blob
        // blobClient.uploadFromFile(localPath + fileName);
        InputStream targetStream = new ByteArrayInputStream(image);
        blobClient.upload(targetStream);
    }
}

package com.example.demo.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.services.CloudStorage;
import com.example.demo.services.CustomVision;

@RestController
@CrossOrigin(origins = "${FRONTEND_HOST:*}")    // Devops best practice, don't hardcode

public class ImageController {

    @Autowired
    CloudStorage cloudStorage;

    @GetMapping("/")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello " + name;
    }
    
    @PostMapping("/images")
    public ResponseEntity<Object> uploadImage(@RequestBody String data) throws IOException {
        String base64 = data.replace("data:image/png;base64,", "");
        byte[] rawBytes = Base64.getDecoder().decode(base64);
        String imageName = UUID.randomUUID() + ".png";
        System.out.println(imageName);

        // saveImageToFile(rawBytes, imageName);
        // cloudStorage.saveToCloud(rawBytes, imageName);
        CustomVision.uploadImage("TianchengLuo", rawBytes);
        return new ResponseEntity<>("Successfully uploaded image", HttpStatus.OK);
    }

    private void saveImageToFile(byte[] image, String imageName) throws IOException {
        File path = new File("./images/");
        if (!path.exists()) {
            path.mkdir();
        }
        System.out.println(path.getAbsolutePath());
        Files.write(new File("./images/" + imageName).toPath(), image);
    }

    @PostMapping("/validate")
    public ResponseEntity<Object> validateImage(@RequestBody String data) throws IOException {
        String base64 = data.replace("data:image/png;base64,", "");
        byte[] rawBytes = Base64.getDecoder().decode(base64);
        ResponseEntity<String> res =  CustomVision.validate(rawBytes);
        System.out.println(res);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}


package com.example.demo.data;

public class Image {

    private String id;
    private byte[] image;


    public Image(String id, byte[] image) {
        this.id = id;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
package com.example.demo.data;

public interface Database {
    
    int saveUser(User user);

    int saveImage(Image image);

    User getUser(String userId);

    Image getImage(String userId);
    
}
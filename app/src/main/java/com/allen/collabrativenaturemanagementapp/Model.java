package com.allen.collabrativenaturemanagementapp;

public class Model {
    String ImageUrl;
    public Model(){

    }
    public Model(String ImageUrl){
        this.ImageUrl = ImageUrl;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}

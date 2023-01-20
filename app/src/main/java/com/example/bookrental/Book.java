package com.example.bookrental;

import java.io.Serializable;

public class Book implements Serializable {
    private int id;
    private String title;
    private String description;
    private String rentPerWeek;
    private String imageUri;
    private String username;
    private String phone;
    private String category;
    private String address;

    public Book(String title, String description, String rentPerWeek, String image, String username, String phone, String category, String address) {
        this.title = title;
        this.description = description;
        this.rentPerWeek = rentPerWeek;
        this.imageUri = image;
        this.username = username;
        this.phone = phone;
        this.category = category;
        this.address = address;
    }

    public Book(String imageUri, String title, String rent){
        this.imageUri = imageUri;
        this.title = title;
        this.rentPerWeek = rent;
    }

    public Book(int id, String title, String rentPerWeek, String description, String category, String username, String phone, String address, String imageUri) {
        this.id = id;
        this.title = title;
        this.rentPerWeek= rentPerWeek;
        this.description = description;
        this.category = category;
        this.username = username;
        this.phone = phone;
        this.address = address;
        this.imageUri = imageUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRentPerWeek() {
        return rentPerWeek;
    }

    public void setRentPerWeek(String rentPerWeek) {
        this.rentPerWeek = rentPerWeek;
    }

    public String getImage() {
        return imageUri;
    }

    public void setImage(String image) {
        this.imageUri = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}

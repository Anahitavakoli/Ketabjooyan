package com.anahitavakoli.ketabjooyan.model;

public class Users {

    private String userName, phoneNumber, password, image, address;

    public Users() {
    }

    public Users(String userName, String phoneNumber, String password, String image, String address) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.image = image;
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

/**
 This class represents a Customer object, which contains information about a user's name, email,address and so on.
 The constructor takes in parameters for each field, allowing for easy creation of a new user object.
 It also includes getter and setter methods for each field to allow for easy access and modification of the object's data.
 */
package com.example.kayshops.user;

import java.io.Serializable;

public class Users implements Serializable {
    public Users(int userId, String fullName, String dateOfBirth, String email, String phoneNumber, String userType) {
        this.userID = userId;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
    }
    public Users(int userId, String streetAddress, String cityAddress, String countyAddress, String postcode) {
        this.userID = userId;
        this.streetAddress = streetAddress;
        this.cityAddress = cityAddress;
        this.countyAddress = countyAddress;
        this.postcode = postcode;
    }


    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCityAddress() {
        return cityAddress;
    }

    public void setCityAddress(String cityAddress) {
        this.cityAddress = cityAddress;
    }

    public String getCountyAddress() {
        return countyAddress;
    }

    public void setCountyAddress(String countyAddress) {
        this.countyAddress = countyAddress;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Users() {
        this.userID = userID;
        this.fullName = fullName;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.streetAddress = streetAddress;
        this.cityAddress = cityAddress;
        this.countyAddress = countyAddress;
        this.postcode = postcode;
    }


    public Users(String fullName, String password, String dateOfBirth, String email, String phoneNumber, String streetAddress, String cityAddress, String countyAddress, String postcode) {
        this.fullName = fullName;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.streetAddress = streetAddress;
        this.cityAddress = cityAddress;
        this.countyAddress = countyAddress;
        this.postcode = postcode;
    }
    private int userID;

    public Users(String email, String password) {
        this.email = email;
        this.password = password;
    }

    private String fullName;
    private String password;
    private String dateOfBirth;
    private String email;
    private String phoneNumber;
    private String streetAddress;
    private String cityAddress;
    private String countyAddress;
    private String postcode;
    private String userType;
}

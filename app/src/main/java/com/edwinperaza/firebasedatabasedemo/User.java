package com.edwinperaza.firebasedatabasedemo;

public class User {

    private String name;
    private String LastName;
    private String email;

    public User() {
    }

    public User(String name, String lastName, String email) {
        this.name = name;
        LastName = lastName;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

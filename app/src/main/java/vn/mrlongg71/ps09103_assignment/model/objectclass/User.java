package vn.mrlongg71.ps09103_assignment.model.objectclass;

import java.util.List;

public class User {
    private String key,email,name,phone,date,image;


    public User(String key, String email, String name, String phone, String date,String image) {
        this.key = key;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.date = date;
        this.image = image;
    }

    public User() {

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

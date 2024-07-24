package com.ly;

import com.alibaba.fastjson.annotation.JSONField;

public class User{
    @JSONField
    private int age;
    @JSONField
    private String username;

    public User(int age, String username) {
        this.age = age;
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
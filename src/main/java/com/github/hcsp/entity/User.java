package com.github.hcsp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Instant;

public class User {
    private int id;
    private String username;
    private String avatar;
    private Instant createdAt;
    private Instant updatedAt;
    @JsonIgnore
    private String encryptedPassword;

    public User(int id, String username, String encryptedPassword) {
        this.id = id;
        this.username = username;
        this.avatar = "";
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.encryptedPassword = encryptedPassword;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }
}

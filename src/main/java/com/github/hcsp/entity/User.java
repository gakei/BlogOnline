package com.github.hcsp.entity;

import java.time.Instant;

public class User {
    private int id;
    private String username;
    private String avatar;
    private Instant createAt;
    private Instant updatedAt;

    public User(int id, String username) {
        this.id = id;
        this.username = username;
        this.avatar = "";
        this.createAt = Instant.now();
        this.updatedAt = Instant.now();
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

    public Instant getCreateAt() {
        return createAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}

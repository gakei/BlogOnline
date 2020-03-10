package com.github.hcsp.entity;

public class RegisterAndLoginSuccess {
    private String status;
    private String msg;
    private User data;

    protected RegisterAndLoginSuccess(String status, String msg, User data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static RegisterAndLoginSuccess success(String msg, User data) {
        return new RegisterAndLoginSuccess("ok", msg, data);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public User getData() {
        return data;
    }

    public void setData(User user) {
        this.data = user;
    }
}

package com.github.hcsp.entity;

public class RegisterAndLoginFailureOrLogoutStatus {
    private String msg;
    private String status;

    protected RegisterAndLoginFailureOrLogoutStatus(String status, String msg) {
        this.msg = msg;
        this.status = status;
    }

    public static RegisterAndLoginFailureOrLogoutStatus failure(String msg) {
        return new RegisterAndLoginFailureOrLogoutStatus("fail", msg);
    }

    public static RegisterAndLoginFailureOrLogoutStatus success(String msg) {
        return new RegisterAndLoginFailureOrLogoutStatus("fail", msg);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

package com.github.hcsp.entity;

public class LoginResult extends Result<User>{
    Boolean isLogin;


    public LoginResult(ResultStatus status, String msg, User user, Boolean isLogin) {
        super(status, msg, user);
        this.isLogin = isLogin;
    }

    public static LoginResult success(String msg, Boolean isLogin) {
        return new LoginResult(ResultStatus.OK, msg, null, isLogin);
    }

    public static LoginResult success(User user) {
        return new LoginResult(ResultStatus.OK, null, user, true);
    }

    public static LoginResult failure(String msg) {
        return new LoginResult(ResultStatus.FAIL, msg, null, false);
    }

    public static LoginResult success(String msg, User user) {
        return new LoginResult(ResultStatus.OK, msg, user, true);
    }

    /*public Boolean getIsLogin() {
        return isLogin;
    }*/

    public Boolean getIsLogin() {
        return isLogin;
    }
}

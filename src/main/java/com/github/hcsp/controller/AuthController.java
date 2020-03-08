package com.github.hcsp.controller;

import com.github.hcsp.entity.User;
import com.github.hcsp.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.Map;

@Controller
@ResponseBody
public class AuthController {
    AuthenticationManager authenticationManager;
    UserService userService;

    @Inject
    public AuthController(AuthenticationManager authenticationManager,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @GetMapping("/auth")
    public Result auth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User LoginUser = userService.getUserByUserName(authentication == null ? null : authentication.getName());
        if (LoginUser == null) {
            return new Result("ok", false, "用户没有登录");
        } else {
            return new Result("ok", true, null, LoginUser);
        }
    }

    @PostMapping("/auth/register")
    @ResponseBody
    public Result register(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        if (password.equals("") || username.equals("")) {
            return Result.fail("用户名或密码不能为空");
        }
        if (judgeInvalid(password)) {
            return Result.fail("invalid username/password");
        }

        if (judgeInvalid(username)) {
            return Result.fail("invalid username/password");
        }

        try {
            userService.save(username, password);
        }catch (DuplicateKeyException e) {
            e.printStackTrace();
            return Result.fail("用户名已经存在！");
        }
        return new Result("ok", false, "注册成功");
    }

    private boolean judgeInvalid(String data) {
        return (data.length() > 15 || data.length() <= 6);
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public Result logout() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User loggedUser = userService.getUserByUserName(username);

        if (loggedUser == null) {
            return Result.fail("用户没有登录");
        } else {
            SecurityContextHolder.clearContext();
            return new Result("ok", false, "登出成功");
        }
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public Result login(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");

        UserDetails userDetails = null;
        try{
            userDetails = userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return Result.fail("用户不存在");
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        try {
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);
            return new Result("ok", true, "登录成功", userService.getUserByUserName(username));
        } catch (BadCredentialsException e) {
            return Result.fail("密码不正确");
        }
    }
    private static class Result {
        String status;
        boolean isLogin;
        String msg;
        Object data;

        public Result(String status, boolean isLogin, String msg) {
            this(status, isLogin, msg, null);
        }

        public Result(String status, boolean isLogin, String msg, Object data) {
            this.status = status;
            this.isLogin = isLogin;
            this.msg = msg;
            this.data = data;
        }

        public static Result fail(String msg) {
            return new Result("fail", false, msg);
        }

        public String getStatus() {
            return status;
        }

        public boolean isLogin() {
            return isLogin;
        }

        public String getMsg() {
            return msg;
        }

        public Object getData() {
            return data;
        }
    }
}

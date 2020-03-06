package com.github.hcsp.controller;

import com.github.hcsp.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.plugin.liveconnect.SecurityContextHelper;

import javax.inject.Inject;
import java.util.Map;

@Controller
@ResponseBody
public class AuthController {
    private UserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager;

    @Inject
    public AuthController(UserDetailsService userDetailsService,
                          AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/auth")
    public Result auth() {
        return new Result("ok", false, "登录成功");
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public Result login(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");

        UserDetails userDetails = null;
        try{
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return new Result("fail", false, "用户不存在");
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        try {
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);
            User loginUser = new User(1, "张三");
            return new Result("ok", true, "登录成功", loginUser);
        } catch (BadCredentialsException e) {
            return new Result("fail", false, "密码不正确");
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

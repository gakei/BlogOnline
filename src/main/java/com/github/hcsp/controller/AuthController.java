package com.github.hcsp.controller;

import com.github.hcsp.entity.LoginResult;
import com.github.hcsp.entity.Result;
import com.github.hcsp.entity.User;
import com.github.hcsp.service.AuthService;
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
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final AuthService authService;

    @Inject
    public AuthController(AuthenticationManager authenticationManager,
                          UserService userService,
                          AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/auth")
    public LoginResult auth() {
        return authService.getCurrentUser()
                .map(LoginResult::success)
                .orElse(LoginResult.success("用户没有登录", false));
    }

    @PostMapping("/auth/register")
    @ResponseBody
    public LoginResult register(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");
        if (password.equals("") || username.equals("")) {
            return LoginResult.failure("用户名或密码不能为空");
        }
        if (judgeInvalid(password)) {
            return LoginResult.failure("invalid username/password");
        }

        if (judgeInvalid(username)) {
            return LoginResult.failure("invalid username/password");
        }

        try {
            userService.save(username, password);
        }catch (DuplicateKeyException e) {
            e.printStackTrace();
            return LoginResult.failure("用户名已经存在！");
        }
        return LoginResult.success("注册成功", userService.getUserByUserName(username));
    }

    private boolean judgeInvalid(String data) {
        return (data.length() > 15 || data.length() <= 6);
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public LoginResult logout() {
        SecurityContextHolder.clearContext();
        return authService.getCurrentUser()
                .map(user -> LoginResult.success("sucess", false))
                .orElse(LoginResult.failure("用户没有登录"));
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public LoginResult login(@RequestBody Map<String, String> usernameAndPassword) {
        String username = usernameAndPassword.get("username");
        String password = usernameAndPassword.get("password");

        UserDetails userDetails;
        try{
            userDetails = userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return LoginResult.failure("用户不存在");
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        try {
            authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(token);
            return LoginResult.success("登录成功", userService.getUserByUserName(username));
        } catch (BadCredentialsException e) {
            return LoginResult.failure("密码不正确");
        }
    }
}

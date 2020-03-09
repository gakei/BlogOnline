package com.github.hcsp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hcsp.service.AuthService;
import com.github.hcsp.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    private MockMvc mvc;
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setup() {
        AuthService authService = new AuthService(userService);
        mvc = MockMvcBuilders.standaloneSetup(new AuthController(authenticationManager, userService, authService)).build();
    }

    @Test
    void returnNotLoginByDefault() throws Exception {
        mvc.perform(get("/auth")).andExpect(status().isOk())
            .andExpect(mvcResult -> Assertions.assertTrue(mvcResult.getResponse()
                    .getContentAsString(Charset.defaultCharset()).contains("用户没有登录")));
    }

    @Test
    void testLogin() throws Exception {
        //未登录时，返回未登录wqwqquit

        mvc.perform(get("/auth")).andExpect(status().isOk())
                .andExpect(mvcResult -> Assertions.assertTrue(mvcResult.getResponse()
                        .getContentAsString(Charset.defaultCharset()).contains("用户没有登录")));

        //使用/auth/login登录
        Map<String, String> map = new HashMap<>();
        map.put("username", "MyUser");
        map.put("password", "MyPassword");

        Mockito.when(userService.loadUserByUsername("MyUser")).thenReturn(new User("MyUser", bCryptPasswordEncoder.encode("MyPassWord"), Collections.emptyList()));
        Mockito.when(userService.getUserByUserName("MyUser")).thenReturn(new com.github.hcsp.entity.User(123, "MyUser", bCryptPasswordEncoder.encode("MyPassWord")));

        MvcResult response = mvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(map)))
                .andExpect(status().isOk())
                .andExpect(mvcResult -> Assertions.assertTrue(mvcResult.getResponse()
                        .getContentAsString(Charset.defaultCharset()).contains("登录成功")))
                .andReturn();

        HttpSession session = response.getRequest().getSession();

        //再次检查/auth的返回值，处于登录状态
        mvc.perform(get("/auth").session((MockHttpSession) session)).andExpect(status().isOk())
                .andExpect(result -> {
                    System.out.println(result.getResponse().getContentAsString());
                    Assertions.assertTrue(result.getResponse().getContentAsString().contains("MyUser"));
                });
    }
}
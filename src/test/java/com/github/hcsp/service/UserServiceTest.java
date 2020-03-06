package com.github.hcsp.service;

import com.github.hcsp.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class UserServiceTest {
    @Mock
    UserMapper mockMapper;
    @Mock
    BCryptPasswordEncoder MockBCryptPasswordEncoder;
    @InjectMocks
    UserService userService;

    @Test
    public void testSave() {
        userService.save("myUser", "myPassword");
        Mockito.verify(mockMapper).save("myUser", "myPassword");
    }
}
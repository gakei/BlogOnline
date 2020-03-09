package com.github.hcsp.service;

import com.github.hcsp.entity.User;
import com.github.hcsp.dao.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserMapper mockMapper;
    @Mock
    BCryptPasswordEncoder mockEncoder;
    @InjectMocks
    UserService userService;

    @Test
    public void testSave() {
        //调用userService
        //验证userService 将请求转发给了userMapper
        Mockito.when(mockEncoder.encode("myPassword")).thenReturn("myEncodePassword");

        userService.save("myUser", "myPassword");

        Mockito.verify(mockMapper).save("myUser", "myEncodePassword");
    }

    @Test
    public void testGetUserByUsername() {
        userService.getUserByUserName("myUser");
        Mockito.verify(mockMapper).getUserByUsername("myUser");
    }

    @Test
    public void throwExceptionWhenUserNotFound() {
        Assertions.assertThrows(UsernameNotFoundException.class,
                ()->userService.loadUserByUsername("myUser"));
    }

    @Test
    public void returnUserDetailWhenUserFound() {
        Mockito.when(mockMapper.getUserByUsername("myUser"))
                .thenReturn(new User(123,"myUser", "myEncodedPassword"));
        UserDetails userDetails = userService.loadUserByUsername("myUser");

        Assertions.assertEquals("myUser", userDetails.getUsername());
        Assertions.assertEquals("myEncodedPassword", userDetails.getPassword());
    }
}
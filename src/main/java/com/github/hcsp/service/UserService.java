package com.github.hcsp.service;

import com.github.hcsp.entity.User;
import com.github.hcsp.dao.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {
    @Inject
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Inject
    private UserMapper userMapper;

    public void save(String username, String password) {
        userMapper.save(username, bCryptPasswordEncoder.encode(password));
    }

    public User getUserByUserName(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByUserName(username);

        if (user == null) {
            throw new UsernameNotFoundException(username + "不存在！");
        }

        return new org.springframework.security.core.userdetails.User(username,
                user.getEncryptedPassword(), Collections.emptyList());
    }
}

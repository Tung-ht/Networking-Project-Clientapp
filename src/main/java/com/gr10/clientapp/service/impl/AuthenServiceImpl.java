package com.gr10.clientapp.service.impl;

import com.gr10.clientapp.entity.User;
import com.gr10.clientapp.repo.UserRepo;
import com.gr10.clientapp.service.AuthenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenServiceImpl implements AuthenService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public String login(String username, String password) {
        String inform = "";
        if (doesUsernameExist(username)) {
            User user = userRepo.findUserByUsername(username);
            if (user.getPassword().equals(password) && !isLoggingIn(username)) {
                user.setIsLogin(1);
                userRepo.save(user);
                inform = "Login successfully";
            }
            else if (user.getPassword().equals(password) && isLoggingIn(username)) {
                inform = "Your account is already logged in from another device";
            }
            else {
                 inform = "Incorrect password!";
            }
        }
        else {
            inform = "Username is not found!";
        }
        return inform;
    }

    @Override
    public String logout(String username) {
        String inform = "";
        if (doesUsernameExist(username)) {
            User user = userRepo.findUserByUsername(username);
            user.setIsLogin(0);
            inform = "Logout successfully!";
        }
        else {
            inform = "Error!";
        }
        return inform;
    }

    @Override
    public String createNewAccount(String username, String password) {
        String inform = "";
        if (userRepo.existsUserByUsername(username)) {
            inform = "Username already existed!";
        }
        else {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            userRepo.save(user);
        }
        return null;
    }

    protected boolean isLoggingIn(String username) {
        User user = userRepo.findUserByUsername(username);
        if (user.getIsLogin() == 1) {
            return true;
        }
        else return false;
    }

    protected boolean doesUsernameExist(String username) {
        return userRepo.existsUserByUsername(username);
    }
}

package com.gr10.clientapp.service.impl;

import com.gr10.clientapp.entity.User;
import com.gr10.clientapp.repo.UserRepo;
import com.gr10.clientapp.service.AuthenService;
import com.gr10.clientapp.service.NetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthenServiceImpl implements AuthenService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private NetworkService networkService;

    @Override
    public String login(String username, String password) {
        String inform;
        if (!validateInput(username, password)) {
            inform = "Username and Password can not be empty!";
            return inform;
        }
        if (doesUsernameExist(username)) {
            User user = userRepo.findUserByUsername(username);
            if (user.getPassword().equals(password) && !isLoggingIn(username)) {
                user.setIsLogin(1);
                userRepo.save(user);
                inform = null;
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
    public String logout(String username) throws IOException {
        String inform;
        if (doesUsernameExist(username)) {
            User user = userRepo.findUserByUsername(username);
            user.setIsLogin(0);
            userRepo.save(user);
            networkService.logout();
            inform = "Logout successfully!";
        }
        else {
            inform = "Error!";
        }
        return inform;
    }

    @Override
    public String createNewAccount(String username, String password) {
        String inform;
        if (!validateInput(username, password)) {
            inform = "Username and Password can not be empty!";
            return inform;
        }
        if (userRepo.existsUserByUsername(username)) {
            inform = "Username already existed!";
        }
        else {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setIsLogin(0);
            userRepo.save(user);
            inform = null;
        }
        return inform;
    }

    protected boolean isLoggingIn(String username) {
        User user = userRepo.findUserByUsername(username);
        return user.getIsLogin() == 1;
    }

    protected boolean doesUsernameExist(String username) {
        return userRepo.existsUserByUsername(username);
    }

    protected boolean validateInput(String username, String password) {
        return !username.equals("") && !password.equals("");
    }
}

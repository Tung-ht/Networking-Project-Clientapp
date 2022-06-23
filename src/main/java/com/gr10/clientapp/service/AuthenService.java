package com.gr10.clientapp.service;

public interface AuthenService {
    // String to inform message whether success or error
    String login(String username, String password);
    String logout(String username);
    String createNewAccount(String username, String password);
}

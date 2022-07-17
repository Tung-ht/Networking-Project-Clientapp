package com.gr10.clientapp.service;

import java.io.IOException;

public interface AuthenService {
    /**
     *  Return a string message whether success or error
     *  String == null means success -> change scene
     */
    String login(String username, String password);

    String logout(String username) throws IOException;

    /**
     *  Return a string message whether success or error
     *  String == null means success -> change scene
     */
    String createNewAccount(String username, String password);
}

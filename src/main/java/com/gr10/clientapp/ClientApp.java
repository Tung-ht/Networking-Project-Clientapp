package com.gr10.clientapp;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import javafx.application.Application;

@SpringBootApplication
public class ClientApp {
	
	public static void main(String[] args) {
		Application.launch(FXApplication.class, args);
	}
	
}

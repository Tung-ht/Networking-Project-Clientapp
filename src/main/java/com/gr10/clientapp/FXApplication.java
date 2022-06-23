package com.gr10.clientapp;

import com.gr10.clientapp.controller.LoginController;
import com.gr10.clientapp.utils.StageManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class FXApplication extends Application {

	public static ConfigurableApplicationContext springContext;
	public static Stage stage;

	@Override
	public void init() throws Exception {
		String[] args = getParameters().getRaw().toArray(new String[0]);
        springContext = new SpringApplicationBuilder()
                .sources(ClientApp.class)
                .run(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		StageManager.changeScene(LoginController.class, "Login-Register");
	}

	@Override
	public void stop() throws Exception {
		springContext.close();
        Platform.exit();
	}
}

package com.gr10.clientapp.controller;

import com.gr10.clientapp.service.AuthenService;
import com.gr10.clientapp.utils.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@FxmlView("/fxml/sign-in.fxml")
public class SignInController {

    @FXML
    private Pane loginRegisterForm;

    @FXML
    private Button loginBtn;

    @FXML
    private Button signInBtn;

    @FXML
    private TextField usernameTf;

    @FXML
    private PasswordField passwordTf;

    @FXML
    private Label signInInfoLabel;

    @Autowired
    AuthenService authenService;

    @FXML
    void openLogInPage(ActionEvent event) throws IOException {
        StageManager.changeScene(LoginController.class, "Login-Register");
    }

    @FXML
    void userSignIn(ActionEvent event) {
        String username = usernameTf.getText();
        String password = passwordTf.getText();
        System.out.println(username + password);
        String inform = authenService.createNewAccount(username, password);
        signInInfoLabel.setText(inform);
    }

}


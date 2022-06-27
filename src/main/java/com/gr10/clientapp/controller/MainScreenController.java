package com.gr10.clientapp.controller;

import com.gr10.clientapp.FXApplication;
import com.gr10.clientapp.entity.FileInfo;
import com.gr10.clientapp.service.AuthenService;
import com.gr10.clientapp.service.impl.NetworkService;
import com.gr10.clientapp.utils.AlertUtils;
import com.gr10.clientapp.utils.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@FxmlView("/fxml/main-screen.fxml")
public class MainScreenController {

    @Autowired
    AuthenService authenService;

    @Autowired
    NetworkService clientService;

    @FXML
    private Button logoutBtn;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button uploadBtn;
    @FXML
    private Button downloadBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private TableView<FileInfo> fileTable;
    @FXML
    private TableColumn<?, ?> noCol;
    @FXML
    private TableColumn<?, ?> fileNameCol;
    @FXML
    private TableColumn<?, ?> fileSizeCol;

    @FXML
    public void initialize() {
        usernameLabel.setText(FXApplication.username);
        noCol.setCellValueFactory(new PropertyValueFactory<>("fileNo"));
        fileNameCol.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        fileSizeCol.setCellValueFactory(new PropertyValueFactory<>("fileSize"));
        fileTable.setItems(clientService.getFiles());
    }

    void refreshListFiles() {
        fileTable.setItems(clientService.getFiles());
    }

    @FXML
    void uploadFile(ActionEvent event) {
        File file = new FileChooser().showOpenDialog(FXApplication.stage);
        if (file != null) {
            System.out.println(file.getAbsoluteFile().getName());
        }
        AlertUtils.showError("Warning", "Could not upload file!", "File size is larger than 50 Mb!");
    }

    @FXML
    void downloadFile(ActionEvent event) {

    }

    @FXML
    void deleteFile(ActionEvent event) {
        FileInfo selectedFile = fileTable.getSelectionModel().getSelectedItem();
        if (selectedFile == null) return;

        boolean confirm = AlertUtils.showConfirmation("Delete File", "Are you sure to delete this file?",
                selectedFile.getFileName() + " " + selectedFile.getFileSize());
        if (confirm) {
            clientService.deleteFile(selectedFile);
            refreshListFiles();
        }
    }

    @FXML
    void userLogout(ActionEvent event) {
        String username = FXApplication.username;
        boolean confirm = AlertUtils.showConfirmation("Logout", "Are you sure to logout?", "Your account: " + username);
        if (confirm) {
            String inform = authenService.logout(username);
            System.out.println(inform);
            StageManager.changeScene(LoginController.class, "Login-Register");
        }
    }

}


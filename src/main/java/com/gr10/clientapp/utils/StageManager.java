package com.gr10.clientapp.utils;

import com.gr10.clientapp.FXApplication;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;

public class StageManager {

    public static void changeScene(Class<?> ControllerClass, String title) {
        Stage mainStage = FXApplication.stage;
        FxWeaver fxWeaver = FXApplication.springContext.getBean(FxWeaver.class);

        Parent root = fxWeaver.loadView(ControllerClass);
        Scene scene = new Scene(root);

        mainStage.setTitle(title);
        mainStage.setResizable(false);
        mainStage.setScene(scene);
        mainStage.show();
        mainStage.centerOnScreen();
    }

}

package com.gr10.clientapp.utils;

import com.gr10.clientapp.FXApplication;
import javafx.scene.Parent;
import javafx.scene.Scene;
import net.rgielen.fxweaver.core.FxWeaver;

import java.io.IOException;

public class StageManager {

    public static void changeScene(Class<?> ControllerClass, String title) throws IOException {
        FxWeaver fxWeaver = FXApplication.springContext.getBean(FxWeaver.class);

        Parent root = fxWeaver.loadView(ControllerClass);
        Scene scene = new Scene(root);

        FXApplication.stage.setTitle(title);
        FXApplication.stage.setResizable(false);
        FXApplication.stage.setScene(scene);
        FXApplication.stage.show();
    }

}

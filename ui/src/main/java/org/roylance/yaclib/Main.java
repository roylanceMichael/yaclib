package org.roylance.yaclib;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.InputStream;

public class Main extends Application {
    public static Stage stage;

    public static void main(final String[] args) {
        Application.launch(Main.class, args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setTitle("Yaclib UI");

        FXMLLoader loader = new FXMLLoader();
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource("/main.fxml"));

        TabPane page;
        try (InputStream in = Main.class.getResourceAsStream("/main.fxml")) {
            page = (TabPane) loader.load(in);
        }

        final Scene scene = new Scene(page);
        stage.setScene(scene);
        stage.sizeToScene();

        primaryStage.show();
    }
}

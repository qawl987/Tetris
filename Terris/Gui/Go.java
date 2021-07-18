package Terris.Gui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Go extends Application {
    @FXML
    public static Stage currentstage;
    public static Scene menuscene;
    @Override
    public void start(Stage stage) throws Exception {
        URL location =getClass().getResource("../resource/gameLayout.fxml");
        ResourceBundle resource = null;
        currentstage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(location,resource);
        Parent root = fxmlLoader.load();
        menuscene = new Scene(root);
        GuiController c = fxmlLoader.getController();
        GameController g =new GameController(c);
        //魔法的一行讓他拿到控制權
        menuscene.getRoot().requestFocus();
        stage.setTitle("Terris");
        stage.setScene(menuscene);
        stage.show();

    }
    public static void main(String[] args){
        launch();
    }

}

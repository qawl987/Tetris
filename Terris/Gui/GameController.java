package Terris.Gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    private final GuiController viewcontroller;
//    int[][] tmp = {{1,0}, {1,0}, {1,1}};
//    Tetris block = new Tetris(tmp, Color.RED);
    public GameController(GuiController c)
    {
        this.viewcontroller = c;
        this.viewcontroller.initGameView();
    }

//    public void addBlock()
//    {
//        viewcontroller.addBlockOnGamePanel(block);
//    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        Tetris t = new Tetris()

    }
}

/*package Terris.Gui;
import Terris.Gui.GameController;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.GridPane;

public class TimeController extends Service<GameController>
{
    private static final int sleepTime = 1000;
    public TimeController(GameController gameController) {

            setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    gameController.moveDown();
                    System.out.println("hello");
                }
            });

    }

    @Override
    protected Task<GameController> createTask() {
        return new Task<GameController>() {
            @Override
            protected GameController call() throws Exception {
                while(true){
                    Thread.sleep(sleepTime);
                }

                return null;
            }
        };
    }


}
*/
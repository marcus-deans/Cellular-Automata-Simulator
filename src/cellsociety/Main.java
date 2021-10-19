package cellsociety;
import cellsociety.view.GameView;
import cellsociety.view.LifeView;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class Main extends Application {

    protected static final String TITLE = "GameView";
    public static final int FRAME_WIDTH = 733;
    public static final int FRAME_HEIGHT = 680;
    public static final Paint BACKGROUND = Color.WHITE;

    /**
     * Organize display of game in a scene and start the game.
     */
    @Override
    public void start (Stage stage) {
        GameView program = new LifeView(FRAME_WIDTH, FRAME_HEIGHT, BACKGROUND);

        stage.setScene(program.setupGame());
        stage.setTitle(TITLE);
        stage.show();

        program.start();
    }
}

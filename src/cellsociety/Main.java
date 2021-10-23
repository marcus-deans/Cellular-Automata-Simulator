package cellsociety;
import cellsociety.controller.GameController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import cellsociety.view.GameView;

public class Main extends Application {

    public static final String MENU_TITLE = "Cell Society Menu";
    public static final String GAME_OF_LIFE_TITLE = "Game of Life";
    public static final String SIM_CONFIG_FILE = "data/game_of_life/blinkers.sim";
    //public static final String SIM_CONFIG_FILE = "data/game_of_life/penta_decathlon.sim";
    //public static final String SIM_CONFIG_FILE = "./cellsociety_team15/data/game_of_life/blinkers.sim";

    public static final int MENU_WIDTH = 450;
    public static final int MENU_HEIGHT = 100;
    public static final int FRAME_WIDTH = 733;
    public static final int FRAME_HEIGHT = 680;
    public static final Paint BACKGROUND = Color.web("#00539B");

    /**
     * Organize display of game in a scene and start the game.
     */
    @Override
    public void start (Stage stage) {
        stage.setScene(setupDisplay());
        stage.setTitle(MENU_TITLE);
        stage.show();
    }

    private Scene setupDisplay(){
        Scene myMenuScene = new Scene(setupMenuRoot(), MENU_WIDTH, MENU_HEIGHT);
        return myMenuScene;
    }

    public HBox setupMenuRoot() {
        HBox myMenuRoot = new HBox();

        // Create one button for each possible simulation
        Button startLogoIDEButton = makeButton(GAME_OF_LIFE_TITLE, value -> startGameOfLife());
        Button startLSystemButton = makeButton("Application2", value -> startGameOfLife());
        Button startDarwinButton = makeButton("Application3", value -> startGameOfLife());

        myMenuRoot.getChildren().addAll(startLogoIDEButton, startLSystemButton, startDarwinButton);
        myMenuRoot.setAlignment(Pos.CENTER);
        myMenuRoot.setSpacing(50);
        return myMenuRoot;
    }

    private Button makeButton(String property, EventHandler<ActionEvent> response) {
        Button result = new Button();
        result.setText(property);
        result.setOnAction(response);
        return result;
    }

    private void startGameOfLife(){
        GameView view = new GameView(FRAME_WIDTH, FRAME_HEIGHT, BACKGROUND, SIM_CONFIG_FILE);
        view.start(new Stage());
        //GameController controller = new GameController(SIM_CONFIG_FILE);
        //controller.setupProgram();
    }
}

package cellsociety;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import cellsociety.view.GameView;

public class Main extends Application {

    private static final String MAIN_WORDS_PATH ="cellsociety.resources.mainWords";
    private static final ResourceBundle gameTitleWords =ResourceBundle.getBundle(MAIN_WORDS_PATH);

//    public static final String SIM_CONFIG_FILE = "data/game_of_life/penta_decathlon.sim";
    public static final String SIM_CONFIG_FILE="data/spreading_of_fire/test.sim";

    public static final int MENU_WIDTH = 650;
    public static final int MENU_HEIGHT = 150;
    public static final int FRAME_WIDTH = 733;
    public static final int FRAME_HEIGHT = 680;
    private static final int BUTTON_WIDTH = 175;
    private static final int BUTTON_HEIGHT = 30;
    public static final Paint BACKGROUND = Color.web("#00539B");

    /**
     * Organize display of game in a scene and start the game.
     */
    @Override
    public void start (Stage stage) {
        stage.setScene(setupDisplay());
        stage.setTitle(getWord("menu_title"));
        stage.show();
    }

    private Scene setupDisplay(){
        Scene myMenuScene = new Scene(setupMenuRoot(), MENU_WIDTH, MENU_HEIGHT);
        myMenuScene.getStylesheets().add(GameView.class.getResource("MainFormatting.css").toExternalForm());
        return myMenuScene;
    }

    public VBox setupMenuRoot() {
        VBox myMenuRoot = new VBox();

        HBox myMenuTopRow = makeMenuRow();
        HBox myMenuBottomRow = makeMenuRow();

        Label programTitle = makeTitleLabel(getWord("welcomeText"));

        // Create one button for each possible simulation
        Button startApp1Button = makeButton(getWord("application1"), value -> startNewGame(getWord("defaultApp1File")));
        Button startApp2Button = makeButton(getWord("application2"), value -> startNewGame(getWord("defaultApp2File")));
        Button startApp3Button = makeButton(getWord("application3"), value -> startNewGame(getWord("defaultApp3File")));
        Button startApp4Button = makeButton(getWord("application4"), value -> startNewGame(getWord("defaultApp4File")));
        Button startApp5Button = makeButton(getWord("application5"), value -> startNewGame(getWord("defaultApp5File")));

        //Add to each box
        myMenuTopRow.getChildren().addAll(startApp1Button, startApp2Button, startApp3Button);
        myMenuBottomRow.getChildren().addAll(startApp4Button, startApp5Button);
        myMenuRoot.getChildren().addAll(programTitle, myMenuTopRow, myMenuBottomRow);
        myMenuRoot.setAlignment(Pos.CENTER);
        myMenuRoot.setSpacing(10);
        return myMenuRoot;
    }

    private Button makeButton(String property, EventHandler<ActionEvent> response) {
        Button gameSelectionButton = new Button();
        gameSelectionButton.setId("game-selection-button");
        gameSelectionButton.setText(property);
        gameSelectionButton.setPrefWidth(BUTTON_WIDTH);
        gameSelectionButton.setPrefHeight(BUTTON_HEIGHT);
        gameSelectionButton.setOnAction(response);
        return gameSelectionButton;
    }

    private void startNewGame(String appFileName){
        GameView newGameView = new GameView(FRAME_WIDTH, FRAME_HEIGHT, BACKGROUND, appFileName);
        newGameView.start(new Stage());
    }

    private Label makeTitleLabel(String words){
        Label newLabel = new Label(words);
        newLabel.setId("main-title-label");
        return newLabel;
    }

    private String getWord(String key) {
        String value = gameTitleWords.getString(key);
        return value;
    }

    private HBox makeMenuRow(){
        HBox newMenuRow = new HBox();
        newMenuRow.setId("menu-row");
        newMenuRow.setSpacing(40);
        newMenuRow.setAlignment(Pos.CENTER);
        return newMenuRow;
    }
}

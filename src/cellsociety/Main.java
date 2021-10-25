package cellsociety;

import cellsociety.view.GameView;
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

/**
 * Launches all applications by creating the appropriate game components via GameView
 * Depends on JavaFX working and appropriate resource files being configured appropriately
 * @author marcusdeans
 */
public class Main extends Application {

  public static final int MENU_WIDTH = 650;
  public static final int MENU_HEIGHT = 150;
  public static final int FRAME_WIDTH = 733;
  public static final int FRAME_HEIGHT = 680;
  public static final Paint BACKGROUND = Color.web("#00539B");
  private static final String MAIN_WORDS_PATH = "cellsociety.resources.mainWords";
  private static final ResourceBundle gameTitleWords = ResourceBundle.getBundle(MAIN_WORDS_PATH);
  private static final int BUTTON_WIDTH = 175;
  private static final int BUTTON_HEIGHT = 30;
  private static final int MENU_ROW_SPACING = 40;
  private static final int MENU_VERTICAL_SPACING = 10;

  /**
   * Organize display of game in a scene and start the game.
   */
  @Override
  public void start(Stage stage) {
    stage.setScene(setupDisplay());
    stage.setTitle(getWord("menu_title"));
    stage.show();
  }

  //setup the JavaFX Scene to hold all content as well as importing appropriate styling
  private Scene setupDisplay() {
    Scene myMenuScene = new Scene(setupMenuRoot(), MENU_WIDTH, MENU_HEIGHT);
    myMenuScene.getStylesheets()
        .add(GameView.class.getResource(getWord("stylingFile")).toExternalForm());
    return myMenuScene;
  }

  //create a JavaFX VBox to contain all of the elements present in the venu
  private VBox setupMenuRoot() {
    VBox myMenuRoot = new VBox();

    HBox myMenuTopRow = makeMenuRow();
    HBox myMenuBottomRow = makeMenuRow();

    Label programTitle = makeTitleLabel(getWord("welcomeText"));

    // Create one button for each possible simulation
    Button startApp1Button = makeButton(getWord("application1"),
        value -> startNewGame(getWord("defaultApp1File")));
    Button startApp2Button = makeButton(getWord("application2"),
        value -> startNewGame(getWord("defaultApp2File")));
    Button startApp3Button = makeButton(getWord("application3"),
        value -> startNewGame(getWord("defaultApp3File")));
    Button startApp4Button = makeButton(getWord("application4"),
        value -> startNewGame(getWord("defaultApp4File")));
    Button startApp5Button = makeButton(getWord("application5"),
        value -> startNewGame(getWord("defaultApp5File")));

    //Add to each box
    myMenuTopRow.getChildren().addAll(startApp1Button, startApp2Button, startApp3Button);
    myMenuBottomRow.getChildren().addAll(startApp4Button, startApp5Button);
    myMenuRoot.getChildren().addAll(programTitle, myMenuTopRow, myMenuBottomRow);
    myMenuRoot.setAlignment(Pos.CENTER);
    myMenuRoot.setSpacing(MENU_VERTICAL_SPACING);
    return myMenuRoot;
  }

  //creata a JavaFX Button with the appropriate text as well as provided EventHandler
  private Button makeButton(String property, EventHandler<ActionEvent> response) {
    Button gameSelectionButton = new Button();
    gameSelectionButton.setId("game-selection-button");
    gameSelectionButton.setText(property);
    gameSelectionButton.setPrefWidth(BUTTON_WIDTH);
    gameSelectionButton.setPrefHeight(BUTTON_HEIGHT);
    gameSelectionButton.setOnAction(response);
    return gameSelectionButton;
  }

  //create a new game animation based on the default app file provided
  private void startNewGame(String appFileName) {
    GameView newGameView = new GameView(FRAME_WIDTH, FRAME_HEIGHT, BACKGROUND, appFileName);
    newGameView.start(new Stage());
  }

  //create a JavaFX Label for the menu title
  private Label makeTitleLabel(String words) {
    Label newLabel = new Label(words);
    newLabel.setId("main-title-label");
    return newLabel;
  }

  //return the String  from the resource file based on the provieed string
  private String getWord(String key) {
    String value = gameTitleWords.getString(key);
    return value;
  }

  //create a new JavaFX HBox representing one row in the menu
  private HBox makeMenuRow() {
    HBox newMenuRow = new HBox();
    newMenuRow.setId("menu-row");
    newMenuRow.setSpacing(MENU_ROW_SPACING);
    newMenuRow.setAlignment(Pos.CENTER);
    return newMenuRow;
  }
}

package cellsociety;

import cellsociety.view.GameView;

import java.io.File;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Launches all applications by creating the appropriate game components via GameView
 * Depends on JavaFX working and appropriate resource files being configured appropriately
 * @author marcusdeans
 */
public class Main extends Application {

  public static final int MENU_WIDTH = 550;
  public static final int MENU_HEIGHT = 130;
  public static final int FRAME_WIDTH = 733;
  public static final int FRAME_HEIGHT = 680;
  public static final Paint BACKGROUND = Color.web("#00539B");
  private static final String MAIN_WORDS_PATH = "cellsociety.resources.mainWords";
  private static final ResourceBundle gameTitleWords = ResourceBundle.getBundle(MAIN_WORDS_PATH);
  private static final int BUTTON_WIDTH = 175;
  private static final int BUTTON_HEIGHT = 30;
  private static final int MENU_ROW_SPACING = 40;
  private static final int MENU_VERTICAL_SPACING = 20;

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

    Label programTitle = makeTitleLabel(getWord("welcomeText"));

    // Create a button to load new files
    Button startButton = makeButton("Load New File", value -> {
      File selectedCSVFile = makeFileChooser("SIM files (*.sim)", "*.sim");
      if(selectedCSVFile != null) {
        String filename = selectedCSVFile.getAbsolutePath();
        startNewGame(filename);
      }
    });

    //Add to each box
    myMenuRoot.getChildren().addAll(programTitle, startButton);
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

  private File makeFileChooser(String description, String extensions) {
    FileChooser myFileChooser = new FileChooser();
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(description, extensions);
    myFileChooser.getExtensionFilters().add(extFilter);
    File selectedFile = myFileChooser.showOpenDialog(null);
    return selectedFile;
  }
}

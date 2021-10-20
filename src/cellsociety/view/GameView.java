package cellsociety.view;

import cellsociety.controller.GameController;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import cellsociety.model.gamegrids.GameGrid;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * JavaFX View class
 */
public class GameView extends Application {

  public static final int COMMAND_HEIGHT = 130;

  private static final int FRAMES_PER_SECOND = 7;
  private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

  //Top Information View
  private int informationPanelX;
  private static final int GAME_TITLE_X = 10;
  private static final int GAME_TITLE_Y = 17;
  private static final int GAME_DROPDOWN_X = 100;
  private static final int GAME_DROPDOWN_Y = 0;
  private static final int LANGUAGES_TITLE_X = 160; //210
  private static final int LANGUAGES_TITLE_Y = 17;
  private static final int LANGUAGES_DROPDOWN_X = 230; //320
  private static final int LANGUAGES_DROPDOWN_Y  = 0;
  private static final int HISTORY_TITLE_X = 325; //445
  private static final int HISTORY_TITLE_Y = 17;
  private static final int HISTORY_DROPDOWN_X = 375; //495
  private static final int HISTORY_DROPDOWN_Y = 0;
  private static final int LANGUAGES_TITLE2_X = 440;
  private static final int LANGUAGES_TITLE2_Y = 17;
  private static final int LANGUAGES_DROPDOWN2_X = 520;
  private static final int LANGUAGES_DROPDOWN2_Y = 0;
  private static final int MAX_DROPDOWN_WIDTH = 50;
  private static final int OFFSET_X = 10;
  private static final int OFFSET_Y = 15;
  private static final int OFFSET_Y_TOP = 40;
  private static final int WIDTH_BUFFER = 200;

  //Control Panel on Right Side of Screen
  private int controlPanelX;
  private static final int CONTROL_PANEL_OFFSET = 175;
  private static final int ANIMATION_CONTROL_PANEL_Y = 300;
  private static final int LOAD_CONTROL_PANEL_Y = 500;
  private static final int VIEW_CONTROL_PANEL_Y = 100;
  private static final int BUTTON_WIDTH = 150;
  private static final int BUTTON_HEIGHT = 30;

  //Games
  private final List<String> gameTypes = new ArrayList<>(
      Arrays.asList("Life", "Fire", "Seg", "Wator"));
  //View types
  private final List<String> viewOptions = new ArrayList<>(
      Arrays.asList("Light", "Dark", "Duke", "UNC"));
  //Languages
  private final List<String> languageTypes = new ArrayList<>(
      Arrays.asList("English", "Spanish", "French"));

  private int frameWidth;
  private int frameHeight;
  private Paint frameBackground;
  public static int gridDisplayLength; //TODO: public to be accessed for computations, remove
  private String myTitle;
  private GameGrid myGridModel;
  private GridView myGridView;
  private GridPane myGameGridView;

  private Group root = new Group();
  private Timeline myAnimation;
  private Scene scene;

  private TextArea commandLine;
  private ComboBox savedPrograms;
  private ComboBox historyPrograms;
  private ComboBox languagesPrograms;
  private Locale langType;
  private FileInputStream fis;
  private Text gameSettingTitle;
  private Text savedTitle;
  private Text history;
  private Text languages;
  private Text animationSpeedText; //darwin

  private String runText;
  private GameController myGameController;
  private Button pauseGame;
  private boolean isPaused;

  public GameView(int width, int height, Paint background, String title){
    frameWidth = width;
    frameHeight = height;
    frameBackground = background;
    myTitle = title;

    gridDisplayLength = width - WIDTH_BUFFER;
    controlPanelX = width - CONTROL_PANEL_OFFSET;
  }

  @Override
  public void start(Stage primaryStage){
    scene = setupGame();
    primaryStage.setScene(scene);
    primaryStage.setTitle(myTitle);
    primaryStage.show();

    myAnimation = new Timeline();
    myAnimation.setCycleCount(Timeline.INDEFINITE);
    myAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(SECOND_DELAY), e -> step()));
    myAnimation.play();
  }


  public Scene setupGame() {
    myGameController = new GameController();
    root = new Group();
    createUIPanels();
    scene = new Scene(root, frameWidth, frameHeight, frameBackground);
    scene.getStylesheets().add(GameView.class.getResource("GameViewFormatting.css").toExternalForm());
    return scene;
  }

  private void createUIPanels() {

    // Information (top) panel:
    createInformationPanel();

    // Details (bottom) panel:
    createDetailsPanel();

    // Control (side) panel:
    createAnimationControlPane();
    createLoadControlPanel();
    createViewControlPanel();

    // Cosmetic lines defining the boundary of the actual grid display
    initializeBoundaries();

    initializeGrid();
  }

  private void createDetailsPanel(){
    HBox panel = new HBox();
    panel.setSpacing(15);

    Node gameTypeLabel;
  }

  private void createInformationPanel(){
    HBox panel = new HBox();
    panel.setSpacing(15);

    Node gameTypeLabel;
  }


  //<editor-fold desc="Create Control Pane and Buttons">
  private void createAnimationControlPane() {
    VBox panel = new VBox();
    panel.setSpacing(15);

    Node runGameButton = initializeRunAnimationButton();
    panel.getChildren().add(runGameButton);

    Node pauseGameButton = initializePauseButton();
    panel.getChildren().add(pauseGameButton);

    Node stepAnimationButton = initializeStepAnimationButton();
    panel.getChildren().add(stepAnimationButton);

    Node clearScreenButton = initializeClearScreenButton();
    panel.getChildren().add(clearScreenButton);

    panel.setLayoutX(controlPanelX);
    panel.setLayoutY(ANIMATION_CONTROL_PANEL_Y);
    panel.setId("animation-control-panel");

    root.getChildren().add(panel);
  }

  private void createLoadControlPanel() {
    VBox panel = new VBox();
    panel.setSpacing(15);

    Node loadFileButton = initializeLoadFileButton();
    panel.getChildren().add(loadFileButton);

    Node saveFileButton = initializeSaveFileButton();
    panel.getChildren().add(saveFileButton);

    panel.setLayoutX(controlPanelX);
    panel.setLayoutY(LOAD_CONTROL_PANEL_Y);
    panel.setId("load-control-panel");

    root.getChildren().add(panel);
  }

  private void createViewControlPanel() {
    VBox panel = new VBox();
    panel.setSpacing(15);

    Node viewControlDropdown = initializeViewControlDropdown();
    panel.getChildren().add(viewControlDropdown);

    Node languageControlDropdown = initializeLanguageControlDropdown();
    panel.getChildren().add(languageControlDropdown);

    panel.setLayoutX(controlPanelX);
    panel.setLayoutY(VIEW_CONTROL_PANEL_Y);
    panel.setId("view-control-panel");

    root.getChildren().add(panel);
  }

  private Node initializeViewControlDropdown() {
    ComboBox gameSetting = new ComboBox<>(FXCollections.observableList(viewOptions));
    gameSetting.setMaxWidth(BUTTON_WIDTH);
    gameSetting.setPrefWidth(BUTTON_WIDTH);
    gameSetting.setPrefHeight(BUTTON_HEIGHT);
    gameSetting.setPromptText(getWord("view_selection"));
    gameSetting.setOnAction((event) -> { //TODO: make sure this works to switch the game
      String game = gameSetting.getSelectionModel().getSelectedItem().toString();
      //TODO: set this up to select view
      if (game.equals(gameTypes.get(0))) {
        //LifeView myLifeView = new LifeView();
        //myLifeView.start(new Stage());
      }
//      else if(game.equals(gameTypes.get(1))){
//        FireView myFireView = new FireView();
//        myFireView.start(new Stage());
//      }
//      else if(game.equals(gameTypes.get(2))){
//        DarwinDisplay darwin = new DarwinDisplay();
//        darwin.start(new Stage());
//      }
    });
    gameSetting.setId("view-control-dropdown");
    return gameSetting;
  }

  //start and stop button in UI
  private Node initializePauseButton() {
    pauseGame = new Button(getWord("pause_game"));
    pauseGame.setOnAction(value -> togglePause());
    pauseGame.setPrefWidth(BUTTON_WIDTH);
    pauseGame.setPrefHeight(BUTTON_HEIGHT);
    return pauseGame;
  }

  // Start or stop searching animation as appropriate
  private void togglePause() {
    if (isPaused) {
      pauseGame.setText(getWord("pause_game"));
      myAnimation.play();
    } else {
      pauseGame.setText(getWord("resume_game"));
      myAnimation.pause();
    }
    isPaused = !isPaused;
  }

  //create button to step through animation
  private Node initializeStepAnimationButton() {
    Button stepAnimationButton = new Button(getWord("step_game"));
    stepAnimationButton.setOnAction(value -> step());
    stepAnimationButton.setPrefWidth(BUTTON_WIDTH);
    stepAnimationButton.setPrefHeight(BUTTON_HEIGHT);
    return stepAnimationButton;
  }

  //create button to run simulation
  private Node initializeRunAnimationButton() {
    //TODO: make it actually run simulation
    Button runAnimationButton = new Button(getWord("run_game"));
    runAnimationButton.setOnAction(value -> step());
    runAnimationButton.setPrefWidth(BUTTON_WIDTH);
    runAnimationButton.setPrefHeight(BUTTON_HEIGHT);
    return runAnimationButton;
  }

  //TODO: this is directly from OOLALA and does NOT work
  //create button to load file from source
  private Node initializeLoadFileButton() {
    Button saveCommands = new Button(getWord("load_text"));
    saveCommands.setPrefWidth(BUTTON_WIDTH);
    saveCommands.setPrefHeight(BUTTON_HEIGHT);
    saveCommands.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        String filename = getUserFileName(getWord("get_user_filename"));
//        if(myGameProcessor.saveCommand(commandLine.getText(), filename)){
//          updateSavedDropdown();
//        }else{
//          sendAlert("Error saving program!");
//        }
      }
    });
    //TODO: use the old runCommands button EventHandler to automatically execute upon load
//    runCommands.setOnAction(new EventHandler<ActionEvent>() {
//      @Override
//      public void handle(ActionEvent event) {
//        handleInputParsing(commandLine.getText());
//        //        myGameProcessor.inputParser(0, 0, 0, commandLine.getText());
//        validateCommandStream();
////        myGameProcessor.saveHistory(commandLine.getText());
//        updateHistoryDropdown();
//      }
//    });
    return saveCommands;
  }

  //TODO: this one works in OOLALA, fix to work here
  //create button to save current grid to file
  private Node initializeSaveFileButton() {
    Button saveCommands = new Button(getWord("save_text"));
    saveCommands.setPrefWidth(BUTTON_WIDTH);
    saveCommands.setPrefHeight(BUTTON_HEIGHT);
    saveCommands.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        String filename = getUserFileName(getWord("get_user_filename"));
        if (myGameController.saveCommand(commandLine.getText(), filename)) {
          updateSavedDropdown();
        } else {
          sendAlert("Error saving program!");
        }
      }
    });
    return saveCommands;
  }

  private Node initializeClearScreenButton() {
    Button clearScreen = new Button(getWord("clear_text"));
    clearScreen.setPrefWidth(BUTTON_WIDTH);
    clearScreen.setPrefHeight(BUTTON_HEIGHT);
    clearScreen.setOnAction(event -> {
      //TODO: update for this program
      commandLine.clear();
      historyPrograms.getItems().clear();
    });
    return clearScreen;
  }
  //</editor-fold>

  private void initializeBoundaries() {
    Line topLine = new Line(OFFSET_X, OFFSET_Y_TOP, OFFSET_X + gridDisplayLength, OFFSET_Y_TOP);
    topLine.setId("boundary-line");
    Line leftLine = new Line(OFFSET_X, OFFSET_Y_TOP, OFFSET_X, OFFSET_Y_TOP + gridDisplayLength);
    leftLine.setId("boundary-line");
    Line rightLine = new Line(OFFSET_X + gridDisplayLength, OFFSET_Y_TOP, OFFSET_X + gridDisplayLength, OFFSET_Y_TOP + gridDisplayLength);
    rightLine.setId("boundary-line");
    Line bottomLine = new Line(OFFSET_X, OFFSET_Y_TOP + gridDisplayLength, OFFSET_X + gridDisplayLength, OFFSET_Y_TOP + gridDisplayLength);
    bottomLine.setId("boundary-line");
    root.getChildren().add(topLine);
    root.getChildren().add(leftLine);
    root.getChildren().add(rightLine);
    root.getChildren().add(bottomLine);
  }

  private void initializeGrid(){
    myGridView = new GridView(40,40);
    myGameGridView = myGridView.getMyGameGrid();
    myGameGridView.setLayoutX(OFFSET_X+3);
    myGameGridView.setLayoutY(OFFSET_Y_TOP+3);
//    myGameGridView.set
    root.getChildren().add(myGameGridView);
  }

  private void updateSavedDropdown() {
    savedPrograms.getItems().clear();
    populateFileNames();
  }

  private void sendAlert(String alertMessage) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setContentText(alertMessage);
    alert.show();
  }

  private void populateFileNames() {
    File[] files = new File("data/examples/logo").listFiles();
    for (File file : files) {
      if (file.isFile()) {
        savedPrograms.getItems().add(file.getName());
      }
    }
  }

  //<editor-fold desc="Setup Languages, Conversion, and Update on Change">
  private Node initializeLanguageControlDropdown() {
    languagesPrograms = new ComboBox(FXCollections.observableList(languageTypes));
    languagesPrograms.setPrefWidth(BUTTON_WIDTH);
    languagesPrograms.setPrefHeight(BUTTON_HEIGHT);
    languagesPrograms.setPromptText(getWord("language_selection"));
    languagesPrograms.setOnAction((event) -> {
      String lang = (String) languagesPrograms.getValue();
      switch (lang) {
        case "English" -> {
          Locale.setDefault(new Locale("en"));
          updateLanguage();
        }
        case "Spanish" -> {
          Locale.setDefault(new Locale("es"));
          updateLanguage();
        }
        case "French" -> {
          Locale.setDefault(new Locale("fr"));
          updateLanguage();
        }
      }
    });
    return languagesPrograms;
  }

  private String getWord(String key) {
    ResourceBundle words = ResourceBundle.getBundle("words");
    String value = words.getString(key);
    return value;
  }

  private void updateLanguage() {
    clearText();
    runTitle();
  }
  //</editor-fold>

  private void clearText() {
    gameSettingTitle.setText("");
    savedTitle.setText("");
    history.setText("");
    languages.setText("");
    runText = "";
  }

  private void handleInputParsing(String text){

  }

  private String runTitle() {
    return runText = getWord("run_text");
  }

  private void validateCommandStream() {
//    Boolean valid = myGameProcessor.getValidCommand();
    Boolean valid = true;
    if (!valid) { //TODO: make sure popup works
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setContentText("Invalid command stream!");
      alert.show();
//      myGameProcessor.setValidCommand(true);
    }
  }

  private String getUserFileName(String message) {
    TextInputDialog getUserInput = new TextInputDialog();
    getUserInput.setHeaderText(message);
    String fileName = getUserInput.showAndWait().toString();
    if (myGameController.validateStringFilenameUsingIO(fileName)) {
      return fileName;
    }
    sendAlert("Invalid filename!");
    return getUserFileName(
        message); //TODO: test to make sure this gives users another chance if they submit an invalid filename
  }

  //Create method that passes in queue of commands to Logo
  private void step() {
  }

  private void updateHistoryDropdown() { //TODO: make sure history is specific to current game model
    historyPrograms.getItems().clear();
//    for (String element : myGameProcessor.getHistory()) {
//      historyPrograms.getItems().add(element);
//    }
  }
}
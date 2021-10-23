package cellsociety.view;

import static java.util.Map.entry;

import cellsociety.controller.GameController;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Map;

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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * JavaFX View class
 */
public class GameView extends Application {

  private static final int FRAMES_PER_SECOND = 7;
  private static final double SECOND_DELAY = 7.0 / FRAMES_PER_SECOND;
  private static final String GRID_COLORS_PATH ="cellsociety.resources.defaultColors";
  private static final ResourceBundle defaultGridColours =ResourceBundle.getBundle(GRID_COLORS_PATH);

  //Top Information View
  private HBox myInformationPanel;
  private static final int OFFSET_X = 10;
  private static final int OFFSET_Y = 15;
  private static final int OFFSET_Y_TOP = 40;
  private static final int WIDTH_BUFFER = 200;

  //Control Panel on Right Side of Screen
  private VBox myViewControlPanel;
  private int controlPanelX;
  private static final int CONTROL_PANEL_OFFSET = 175;
  private static final int ANIMATION_CONTROL_PANEL_Y = 300;
  private static final int LOAD_CONTROL_PANEL_Y = 500;
  private static final int VIEW_CONTROL_PANEL_Y = 100;
  private static final int BUTTON_WIDTH = 150;
  private static final int BUTTON_HEIGHT = 30;

  //Details panel on bottom of screen
  private HBox myDetailsPanel;
  private static final int CELL_STATE_SIZE = 15;

  //View types  private static final ResourceBundle
  private static final String DEFAULT_VIEW = "Duke";
  private static final String VIEW_OPTIONS = "ViewOptions";
  private static final String VIEW_COLORS_PATH ="cellsociety.resources.viewColours";
  private static final ResourceBundle viewColours =ResourceBundle.getBundle(VIEW_COLORS_PATH);
  private final List<String> viewOptions = Arrays.asList(viewColours.getString(VIEW_OPTIONS).split(","));

  //Games
  private final List<String> gameTypes=new ArrayList<>(Arrays.asList("GameOfLife", "SpreadingOfFire", "Segregation", "WatorWorld"));
//  private final List<String> gameTypes = new ArrayList<>(
//      Arrays.asList("Life", "Fire", "Seg", "Wator"));

  //Languages
  private final List<String> languageTypes = new ArrayList<>(
      Arrays.asList("English", "Spanish", "French"));

  private final Map<String, String[]> colourLabelNames = Map.ofEntries(
      entry("GameOfLife", new String[]{"Dead", "Alive"}),
      entry("SpreadingOfFire", new String[]{"Empty", "Tree", "Fire"}),
      entry("Segregation", new String[]{"Empty", "Alpha", "Beta"}),
      entry("WatorWorld", new String[]{"Water", "Fish", "Shark"})
  );


  String myType;

  private int frameWidth;
  private int frameHeight;
  private Paint frameBackground;
  private int gridDisplayLength;
  private String myTitle;
  private String myDescription;
  private String myAuthor;
  private String[] myGameParameters;
  private String[] myGridColours;
  private int[] gridSize;

  private Timeline myAnimation;
  private GridView myGridView;
  private GridPane myGameGridView;

  private Group root;
  private Scene scene;


  private TextArea commandLine;
  private ComboBox savedPrograms;
  private ComboBox historyPrograms;
  private ComboBox languagesPrograms;
  private Locale langType;
  private FileInputStream fis;

  private GameController myGameController;
  private Button pauseGame;
  private boolean isPaused;

  public GameView(int width, int height, Paint background, String filename) {
    frameWidth = width;
    frameHeight = height;
    frameBackground = background;
    setupController(filename);
    gridDisplayLength = width - WIDTH_BUFFER;
    controlPanelX = width - CONTROL_PANEL_OFFSET;
    root = new Group();
  }

  private void setupController(String filename){
    myGameController=new GameController(filename);
    myGameController.setupProgram();
    Map<String, String> parameters=myGameController.getConfigurationMap();
    myTitle=parameters.get("Title");
    myType=parameters.get("Type"); //work on translating from GameOfLife->life
    myDescription=parameters.get("Description");
    myAuthor =parameters.get("Author");
//    myGameParameters = parameters.get("GameParameters").split(",");
    if (parameters.get("StateColors")!=null) {
      myGridColours = parameters.get("StateColors").split(",");
    }
    else {
      myGridColours = defaultGridColours.getString(myType).split(",");
      //gridColors=defaultColors.getString(myType).split(",");
    }
    gridSize=myGameController.getGridSize();
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
    myDetailsPanel = new HBox();
    myDetailsPanel.setSpacing(40);

    myDetailsPanel.getChildren().add(createCellStatesPanel());
    myDetailsPanel.getChildren().add(createGameParametersPanel());

    myDetailsPanel.setLayoutX(OFFSET_X);
    myDetailsPanel.setLayoutY(OFFSET_Y + OFFSET_Y_TOP + gridDisplayLength);
    myDetailsPanel.setId("details-panel");

    root.getChildren().add(myDetailsPanel);
  }

  //method to create the HBox containing information on the simulation parameters
  private HBox createGameParametersPanel() {
    HBox gameParametersPanel = new HBox();
    gameParametersPanel.setSpacing(5);
    Node gameParametersText = makeText(getWord("game_parameters_text"));
    gameParametersPanel.getChildren().add(gameParametersText);

    Label firstGameParameterLabel = makeInformationLabel(getWord("game_parameters_label_alpha"));
    gameParametersPanel.getChildren().add(firstGameParameterLabel);
    return gameParametersPanel;
  }

  //method to create the HBox containing information on the colours corresponding to cell states
  private Node createCellStatesPanel() {
    HBox cellStatesPanel = new HBox();
    cellStatesPanel.setSpacing(5);
    Node gameTypeText = makeText(getWord("cell_state_text"));
    cellStatesPanel.getChildren().add(gameTypeText);

    for(int iterate = 0; iterate < myGridColours.length; iterate++){
      String colour = myGridColours[iterate];

      Label cellStateLabel = makeInformationLabel(colourLabelNames.get(myType)[iterate]);
      cellStatesPanel.getChildren().add(cellStateLabel);

      Rectangle cellStateRectangle = makeCellStateRectangle();
      cellStateRectangle.setId("cell-state-rectangle");
      cellStateRectangle.setFill(Paint.valueOf(colour));
      cellStatesPanel.getChildren().add(cellStateRectangle);
    }
    return cellStatesPanel;
  }

  //method to create small box for cell state colours
  private Rectangle makeCellStateRectangle() {
    Rectangle newCellState = new Rectangle();
    newCellState.setWidth(CELL_STATE_SIZE);
    newCellState.setHeight(CELL_STATE_SIZE);
    return newCellState;
  }


  //method to create individual text label
  private Text makeText(String text){
    Text newText = new Text(text);
    newText.setId("information-text");
    return newText;
  }

  //method to create individual progress label
  private Label makeInformationLabel(String text) {
    Label label = new Label(text);
    label.setId("information-label");
    return label;
  }

  private void createInformationPanel(){
    myInformationPanel = new HBox();
    myInformationPanel.setSpacing(20);

    HBox gameTypePanel = new HBox();
    gameTypePanel.setSpacing(5);
    Node gameTypeText = makeText(getWord("game_type_text"));
    gameTypePanel.getChildren().add(gameTypeText);
    Label myGameTypeLabel = makeInformationLabel(myType);
    gameTypePanel.getChildren().add(myGameTypeLabel);
    myInformationPanel.getChildren().add(gameTypePanel);

    HBox gameNamePanel = new HBox();
    gameNamePanel.setSpacing(5);
    Node gameNameText = makeText(getWord("game_name_text"));
    gameNamePanel.getChildren().add(gameNameText);
    Label myGameNameLabel = makeInformationLabel(myTitle);
    gameNamePanel.getChildren().add(myGameNameLabel);
    myInformationPanel.getChildren().add(gameNamePanel);

    HBox gameAuthorPanel = new HBox();
    gameAuthorPanel.setSpacing(5);
    Node gameAuthorText = makeText(getWord("game_author_text"));
    gameAuthorPanel.getChildren().add(gameAuthorText);
    Label myGameAuthorLabel = makeInformationLabel(myAuthor);
    gameAuthorPanel.getChildren().add(myGameAuthorLabel);
    myInformationPanel.getChildren().add(gameAuthorPanel);

    myInformationPanel.setLayoutX(OFFSET_X);
    myInformationPanel.setLayoutY(OFFSET_Y);
    myInformationPanel.setId("information-panel");

    root.getChildren().add(myInformationPanel);
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
    myViewControlPanel = new VBox();
    myViewControlPanel.setSpacing(15);

    Node viewControlDropdown = initializeViewControlDropdown();
    myViewControlPanel.getChildren().add(viewControlDropdown);

    Node languageControlDropdown = initializeLanguageControlDropdown();
    myViewControlPanel.getChildren().add(languageControlDropdown);

    myViewControlPanel.setLayoutX(controlPanelX);
    myViewControlPanel.setLayoutY(VIEW_CONTROL_PANEL_Y);
    myViewControlPanel.setId("view-control-panel");

    root.getChildren().add(myViewControlPanel);
  }

  private Node initializeViewControlDropdown() {
    ComboBox gameSetting = new ComboBox<>(FXCollections.observableList(viewOptions));
    gameSetting.setMaxWidth(BUTTON_WIDTH);
    gameSetting.setPrefWidth(BUTTON_WIDTH);
    gameSetting.setPrefHeight(BUTTON_HEIGHT);
    // Arrays.asList("Light", "Dark", "Duke", "UNC"));
    gameSetting.setPromptText(getWord("view_selection"));
    gameSetting.setOnAction((event) -> {
      String myViewOption = gameSetting.getSelectionModel().getSelectedItem().toString();
      //TODO: set this up to select view
      scene.setFill(Color.web(viewColours.getString(myViewOption)));
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
    //TODO: make it actually run simulation -> continuously as opposed to incrementing
    Button runAnimationButton = new Button(getWord("run_game"));
    runAnimationButton.setOnAction(value -> step());
    runAnimationButton.setPrefWidth(BUTTON_WIDTH);
    runAnimationButton.setPrefHeight(BUTTON_HEIGHT);
    return runAnimationButton;
  }

  //create button to load file from source
  private Node initializeLoadFileButton() {
    Button saveCommands = new Button(getWord("load_text"));
    saveCommands.setPrefWidth(BUTTON_WIDTH);
    saveCommands.setPrefHeight(BUTTON_HEIGHT);
    saveCommands.setOnAction(event -> {
      String filename = getUserLoadFileName(getWord("get_user_filename"));
      if(!myGameController.loadCommand(filename)){
        sendAlert("Error loading program!");
      }
      initializeGrid();
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
        String filename = getUserSaveFileName(getWord("get_user_filename"));
        if (myGameController.saveCommand(filename)) {
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
      clearPanels();
      createUIPanels();
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
    myGridView = new GridView(gridSize[0], gridSize[1], myGridColours, gridDisplayLength);
    myGameGridView = myGridView.getMyGameGrid();
    myGameGridView.setLayoutX(OFFSET_X+3);
    myGameGridView.setLayoutY(OFFSET_Y_TOP+3);
//    myGameGridView.set
    root.getChildren().add(myGameGridView);
    myGameController.setupListener(myGridView);
    myGameController.showInitialStates();
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
    clearPanels();
    createUIPanels();
  }
  //</editor-fold>

  private void handleInputParsing(String text){

  }

  private void clearPanels(){
    root.getChildren().remove(myDetailsPanel);
    root.getChildren().remove(myInformationPanel);
    root.getChildren().remove(myViewControlPanel);
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

  private String getUserLoadFileName(String message) {
    myAnimation.pause();
    TextInputDialog getUserInput = new TextInputDialog();
    getUserInput.setHeaderText(message);
    String fileName = getUserInput.showAndWait().toString();
    if (myGameController.validateLoadStringFilenameUsingIO(fileName)) {
      return fileName;
    }
    sendAlert("Invalid filename!");
    myAnimation.play();
    return getUserLoadFileName(
        message); //TODO: test to make sure this gives users another chance if they submit an invalid filename
  }

  private String getUserSaveFileName(String message) {
    myAnimation.pause();
    TextInputDialog getUserInput = new TextInputDialog();
    getUserInput.setHeaderText(message);
    String fileName = getUserInput.showAndWait().toString();
    if (myGameController.validateSaveStringFilenameUsingIO(fileName)) {
      return fileName;
    }
    sendAlert("Invalid filename!");
    myAnimation.play();
    return getUserSaveFileName(
        message); //TODO: test to make sure this gives users another chance if they submit an invalid filename
  }


  private void step() {
    myGameController.runSimulation();
  }
}
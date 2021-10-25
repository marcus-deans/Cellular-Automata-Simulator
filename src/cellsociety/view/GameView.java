package cellsociety.view;

import static java.util.Map.entry;

import cellsociety.controller.GameController;
import cellsociety.util.IncorrectCSVFormatException;
import cellsociety.util.IncorrectSimFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
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
 * JavaFX View for each game that creates the general UI; each instance for a single game application
 * Relies on appropriate resourcebundles being configured as well as JavaFX
 * Creates gameController
 * @author marcusdeans, drewpeterson
 */
public class GameView extends Application {

  private static final int FRAMES_PER_SECOND = 7;
  private static final double SECOND_DELAY = 7.0 / FRAMES_PER_SECOND;
  private static final String GRID_COLORS_PATH = "cellsociety.resources.defaultColors";
  private static final ResourceBundle defaultGridColours = ResourceBundle.getBundle(
      GRID_COLORS_PATH);
  private static final int OFFSET_X = 10;
  private static final int OFFSET_Y = 15;
  private static final int OFFSET_Y_TOP = 40;
  private static final int WIDTH_BUFFER = 200;
  private static final int CONTROL_PANEL_OFFSET = 175;
  private static final int ANIMATION_CONTROL_PANEL_Y = 300;
  private static final int LOAD_CONTROL_PANEL_Y = 500;
  private static final int VIEW_CONTROL_PANEL_Y = 100;
  private static final int BUTTON_WIDTH = 150;
  private static final int BUTTON_HEIGHT = 30;
  private static final int CELL_STATE_SIZE = 15;
  //View types  private static final ResourceBundle
  private static final String DEFAULT_VIEW = "Duke";
  private static final String VIEW_OPTIONS = "ViewOptions";
  private static final String VIEW_COLORS_PATH = "cellsociety.resources.viewColours";
  private static final ResourceBundle viewColours = ResourceBundle.getBundle(VIEW_COLORS_PATH);
  private final List<String> viewOptions = Arrays.asList(
      viewColours.getString(VIEW_OPTIONS).split(","));
  //Games
  private final List<String> gameTypes = new ArrayList<>(
      Arrays.asList("GameOfLife", "SpreadingOfFire", "Segregation", "WatorWorld", "Percolation"));
  //Languages
  private final List<String> languageTypes = new ArrayList<>(
      Arrays.asList("English", "Spanish", "French"));
  private final Map<String, String[]> colourLabelNames = Map.ofEntries(
      entry("GameOfLife", new String[]{"Dead", "Alive"}),
      entry("SpreadingOfFire", new String[]{"Empty", "Tree", "Fire"}),
      entry("Segregation", new String[]{"Empty", "Alpha", "Beta"}),
      entry("WatorWorld", new String[]{"Water", "Fish", "Shark"}),
      entry("Percolation", new String[]{"Empty", "Blocked", "Percolated"})
  );
  String myType;
  //Top Information View
  private HBox myInformationPanel;
//  private final List<String> gameTypes = new ArrayList<>(
//      Arrays.asList("Life", "Fire", "Seg", "Wator"));
  //Control Panel on Right Side of Screen
  private VBox myViewControlPanel;
  private int controlPanelX;
  //Details panel on bottom of screen
  private HBox myDetailsPanel;
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

  private Group myGameViewRoot;
  private Scene myGameViewScene;


  private TextArea commandLine;
  private ComboBox savedPrograms;
  private ComboBox historyPrograms;
  private ComboBox languagesPrograms;
  private Locale langType;
  private FileInputStream fis;

  private GameController myGameController;
  private Button pauseGame;
  private boolean isPaused;

  /**
   * Creates new GameView for each application
   * @param width of JavaFX display in pixels
   * @param height of JavaFX display in pixels
   * @param background colour of JavaFX background
   * @param filename Filename of the simulation file which GameController uses
   */
  public GameView(int width, int height, Paint background, String filename) {
    frameWidth = width;
    frameHeight = height;
    frameBackground = background;
    setupController(filename);
    gridDisplayLength = width - WIDTH_BUFFER;
    controlPanelX = width - CONTROL_PANEL_OFFSET;
    myGameViewRoot = new Group();
  }

  //setup the GameController for this specific simulation
  private void setupController(String filename) {
    myGameController = new GameController(filename);
    try {
      myGameController.setupProgram();
    } catch (IncorrectSimFormatException e) {

    } catch (IncorrectCSVFormatException e) {

    } catch (FileNotFoundException e) {

    }
    Map<String, String> parameters = myGameController.getConfigurationMap();
    myTitle = parameters.get("Title");
    myType = parameters.get("Type"); //work on translating from GameOfLife->life
    myDescription = parameters.get("Description");
    myAuthor = parameters.get("Author");
//    myGameParameters = parameters.get("GameParameters").split(",");
    if (parameters.get("StateColors") != null) {
      myGridColours = parameters.get("StateColors").split(",");
    } else {
      myGridColours = defaultGridColours.getString(myType).split(",");
    }
    gridSize = myGameController.getGridSize();
  }

  /**
   * Start the JavaFX simulation
   * @param primaryStage the Stage that is specific to this game instance
   */
  @Override
  public void start(Stage primaryStage) {
    myGameViewScene = setupGame();
    primaryStage.setScene(myGameViewScene);
    primaryStage.setTitle(myTitle);
    primaryStage.show();

    myAnimation = new Timeline();
    myAnimation.setCycleCount(Timeline.INDEFINITE);
    myAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(SECOND_DELAY), e -> step()));
  }

  //setup the game by creating the appropriate JavaFX components on the Scene
  private Scene setupGame() {
    myGameViewRoot = new Group();
    createUIPanels();
    myGameViewScene = new Scene(myGameViewRoot, frameWidth, frameHeight, frameBackground);
    myGameViewScene.getStylesheets()
        .add(GameView.class.getResource("GameViewFormatting.css").toExternalForm());
    return myGameViewScene;
  }

  //create all of the UI panels that will provide interactivity and information to the user
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

  //create the pJavaFX ane on the bottom of the screen; describes colours for cell states as well as simulation parameters
  private void createDetailsPanel() {
    myDetailsPanel = new HBox();
    myDetailsPanel.setSpacing(40);

    myDetailsPanel.getChildren().add(createCellStatesPanel());
    myDetailsPanel.getChildren().add(createGameParametersPanel());

    myDetailsPanel.setLayoutX(OFFSET_X);
    myDetailsPanel.setLayoutY(OFFSET_Y + OFFSET_Y_TOP + gridDisplayLength);
    myDetailsPanel.setId("details-panel");

    myGameViewRoot.getChildren().add(myDetailsPanel);
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

    for (int iterate = 0; iterate < myGridColours.length; iterate++) {
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
  private Text makeText(String text) {
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

  //create information panel on top of screen to display information like type, name, and author to user
  private void createInformationPanel() {
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

    myGameViewRoot.getChildren().add(myInformationPanel);
  }


  //<editor-fold desc="Create Control Pane and Buttons">
  //create the animation control pane allowing the user to run, pause/resume, clear, and step the simualtion
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

    myGameViewRoot.getChildren().add(panel);
  }

  //create the pane allowing user to load and save simulation files
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

    myGameViewRoot.getChildren().add(panel);
  }

  //create the view control panel allowing the user to select cosmetic aspects: colours and language
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

    myGameViewRoot.getChildren().add(myViewControlPanel);
  }

  //create the specific dropdown allowing the user to select which view mode they prefer
  private Node initializeViewControlDropdown() {
    ComboBox gameSetting = new ComboBox<>(FXCollections.observableList(viewOptions));
    gameSetting.setPrefWidth(BUTTON_WIDTH);
    gameSetting.setPrefHeight(BUTTON_HEIGHT);
    // Arrays.asList("Light", "Dark", "Duke", "UNC"));
    gameSetting.setPromptText(getWord("view_selection"));
    gameSetting.setOnAction((event) -> {
      String myViewOption = gameSetting.getSelectionModel().getSelectedItem().toString();
      //TODO: set this up to select view
      myGameViewScene.setFill(Color.web(viewColours.getString(myViewOption)));
    });
    gameSetting.setId("view-control-dropdown");
    return gameSetting;
  }

  //create the dropdown allowing user to select which language they prefer
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
    runAnimationButton.setOnAction(value -> myAnimation.play());
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
      try {
        if (!myGameController.loadCommand(filename)) {
          sendAlert("Error loading program!");
        }
      } catch (FileNotFoundException e) {
        //may not be necessary if file verification is elsewhere (could suppress this)
      } catch (IncorrectSimFormatException e) {
        //throw error of some sort
      } catch (IncorrectCSVFormatException e) {

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
//          updateSavedDropdown();
        } else {
          sendAlert("Error saving program!");
        }
      }
    });
    return saveCommands;
  }

  //create the clear screen button
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

  //create the cosmetic boundaries showing where the simulation takes place
  private void initializeBoundaries() {
    Line topLine = new Line(OFFSET_X, OFFSET_Y_TOP, OFFSET_X + gridDisplayLength, OFFSET_Y_TOP);
    topLine.setId("boundary-line");
    Line leftLine = new Line(OFFSET_X, OFFSET_Y_TOP, OFFSET_X, OFFSET_Y_TOP + gridDisplayLength);
    leftLine.setId("boundary-line");
    Line rightLine = new Line(OFFSET_X + gridDisplayLength, OFFSET_Y_TOP,
        OFFSET_X + gridDisplayLength, OFFSET_Y_TOP + gridDisplayLength);
    rightLine.setId("boundary-line");
    Line bottomLine = new Line(OFFSET_X, OFFSET_Y_TOP + gridDisplayLength,
        OFFSET_X + gridDisplayLength, OFFSET_Y_TOP + gridDisplayLength);
    bottomLine.setId("boundary-line");
    myGameViewRoot.getChildren().add(topLine);
    myGameViewRoot.getChildren().add(leftLine);
    myGameViewRoot.getChildren().add(rightLine);
    myGameViewRoot.getChildren().add(bottomLine);
  }

  //initialize the grid itself that appears on the scree
  private void initializeGrid() {
    myGridView = new GridView(gridSize[0], gridSize[1], myGridColours, gridDisplayLength);
    myGameGridView = myGridView.getMyGameGrid();
    myGameGridView.setLayoutX(OFFSET_X + 3);
    myGameGridView.setLayoutY(OFFSET_Y_TOP + 3);
    myGameViewRoot.getChildren().add(myGameGridView);
    myGameController.setupListener(myGridView);
    myGameController.showInitialStates();
  }

  //set an alert to the user indicating incorrect input
  private void sendAlert(String alertMessage) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setContentText(alertMessage);
    alert.show();
  }

  //<editor-fold desc="Setup Languages, Conversion, and Update on Change">

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

  //method to clear all extant JavaFX panels from the screen for refresh
  private void clearPanels() {
    myGameViewRoot.getChildren().remove(myDetailsPanel);
    myGameViewRoot.getChildren().remove(myInformationPanel);
    myGameViewRoot.getChildren().remove(myViewControlPanel);
  }


  //get the filename of the simulation file that the user wants to load
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

  //get the filename for the simulation file that the user wants to save the current simulation to
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

  //step the animation once
  private void step() {
    myGameController.runSimulation();
  }
}
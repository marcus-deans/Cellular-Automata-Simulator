package cellsociety.view;

import static java.util.Map.entry;

import cellsociety.controller.GameController;
import cellsociety.util.IncorrectCSVFormatException;
import cellsociety.util.IncorrectSimFormatException;
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
 * JavaFX View for each game that creates the general UI; each instance for a single game
 * application Relies on appropriate resourcebundles being configured as well as JavaFX Creates
 * gameController
 *
 * @author marcusdeans, drewpeterson
 */
public class GameView extends Application {
  //JavaFX Simulation Parameters:
  private static final int FRAMES_PER_SECOND = 7;
  private static final double SECOND_DELAY = 7.0 / FRAMES_PER_SECOND;

  //General resource file structure
  private static final String RESOURCE_FILE_PATH = "cellsociety.resources.gameView";
  private static final ResourceBundle gameViewResources = ResourceBundle.getBundle(RESOURCE_FILE_PATH);

  //Cosmetic features: colours and views
  private static final String GRID_COLORS_PATH = "cellsociety.resources.defaultColors";
  private static final ResourceBundle defaultGridColours = ResourceBundle.getBundle(
      GRID_COLORS_PATH);
  private static final String DEFAULT_VIEW = "Duke";
  private static final String VIEW_OPTIONS = "ViewOptions";
  private final List<String> viewOptions = Arrays.asList(
      gameViewResources.getString(VIEW_OPTIONS).split(","));

  //Cosmetic features: languages
  private static final String LANGUAGE_OPTIONS = "LanguageOptions";
  private final List<String> languageTypes = Arrays.asList(gameViewResources.getString(LANGUAGE_OPTIONS).split(","));
  private Locale langType;

  //Game options and parameters
  private static final String GAME_OPTIONS = "GameOptions";
  private final List<String> gameTypes = Arrays.asList(gameViewResources.getString(GAME_OPTIONS).split(","));
  private String myType;


  //Cosmetic features: JavaFX pixel positioning
  private int frameWidth;
  private int frameHeight;
  private Paint frameBackground;
  private int gridDisplayLength;
  private int[] gridSize;
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

  private final Map<String, String[]> colourLabelNames = Map.ofEntries(
      entry("GameOfLife", new String[]{"Dead", "Alive"}),
      entry("SpreadingOfFire", new String[]{"Empty", "Tree", "Fire"}),
      entry("Segregation", new String[]{"Empty", "Alpha", "Beta"}),
      entry("WatorWorld", new String[]{"Water", "Fish", "Shark"}),
      entry("Percolation", new String[]{"Empty", "Blocked", "Percolated"})
  );

  //Information panel on top of screen
  private String myTitle;
  private String myDescription;
  private String myAuthor;
  private HBox myInformationPanel;
  private static final int HORIZONTAL_PANEL_SPACING = 5;

  //Control Panel on Right Side of Screen
  private VBox myViewControlPanel;
  private int controlPanelX;
  private Button pauseGameButton;
  private boolean isPaused;
  private ComboBox languagesPrograms;
  private ComboBox viewSetting;

  //Details panel on bottom of screen
  private HBox myDetailsPanel;
  private static final int CELL_STATE_SIZE = 15;
  private String[] myGameParameters;
  private String[] myGridColours;

  //JavaFX setup elements
  private Timeline myAnimation;
  private Group myGameViewRoot;
  private Scene myGameViewScene;

  //Integral Game classes
  private GridView myGridView;
  private GridPane myGameGridView;
  private GameController myGameController;

  private FileInputStream fis;

  /**
   * Creates new GameView for each application
   *
   * @param width      of JavaFX display in pixels
   * @param height     of JavaFX display in pixels
   * @param background colour of JavaFX background
   * @param filename   Filename of the simulation file which GameController uses
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
   *
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

  //<editor-fold desc="Create Details Pane and Buttons">
  //create the JavaFX ane on the bottom of the screen; describes colours for cell states as well as simulation parameters
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
  //</editor-fold>

  //<editor-fold desc="Create General JavaFX Element Creators">
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

  //creata a JavaFX Button with the appropriate text as well as provided EventHandler
  private Button makeButton(String property, EventHandler<ActionEvent> response) {
    Button newButton = new Button();
    newButton.setText(property);
    newButton.setPrefWidth(BUTTON_WIDTH);
    newButton.setPrefHeight(BUTTON_HEIGHT);
    newButton.setOnAction(response);
    return newButton;
  }

  //create a JavaFX ComboBox (dropdown) with the appropriate title and provided options and Eventhandler
  private ComboBox makeComboBox(String title, List<String> boxOptions, EventHandler<ActionEvent> response){
    ComboBox newComboBox = new ComboBox<>(FXCollections.observableList(boxOptions));
    newComboBox.setPrefWidth(BUTTON_WIDTH);
    newComboBox.setPrefHeight(BUTTON_HEIGHT);
    newComboBox.setPromptText(title);
    newComboBox.setOnAction(response);
    return newComboBox;
  }

  //create a JavaFX HBox to serve as an individual panel consisting of text and label
  private HBox makeHorizontalPanel(Text text, Label label){
    HBox newHorizontalPanel = new HBox();
    newHorizontalPanel.setSpacing(HORIZONTAL_PANEL_SPACING);
    newHorizontalPanel.getChildren().addAll(text, label);
    return newHorizontalPanel;
  }
  //</editor-fold>


  //create information panel on top of screen to display information like type, name, and author to user
  private void createInformationPanel() {
    myInformationPanel = new HBox();
    myInformationPanel.setSpacing(20);

    HBox gameTypePanel = makeHorizontalPanel(makeText(getWord("game_type_text")), makeInformationLabel(myType));
    HBox gameNamePanel = makeHorizontalPanel(makeText(getWord("game_name_text")), makeInformationLabel(myTitle));
    HBox gameAuthorPanel = makeHorizontalPanel(makeText(getWord("game_author_text")), makeInformationLabel(myAuthor));

    myInformationPanel.getChildren().addAll(gameTypePanel, gameNamePanel, gameAuthorPanel);
    myInformationPanel.setLayoutX(OFFSET_X);
    myInformationPanel.setLayoutY(OFFSET_Y);
    myInformationPanel.setId("information-panel");

    myGameViewRoot.getChildren().add(myInformationPanel);
  }


  //<editor-fold desc="Create Control Pane and Buttons">
  //<editor-fold desc="Create Animation Control Pane and Buttons">
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

  //create button to run simulation
  private Node initializeRunAnimationButton() {
    Button runAnimationButton = makeButton(getWord("run_game"), value -> myAnimation.play());
    return runAnimationButton;
  }

  //start and stop button in UI
  private Node initializePauseButton() {
    pauseGameButton = makeButton(getWord("pause_game"), value -> togglePause());
    return pauseGameButton;
  }

  // Start or stop searching animation as appropriate
  private void togglePause() {
    if (isPaused) {
      pauseGameButton.setText(getWord("pause_game"));
      myAnimation.play();
    } else {
      pauseGameButton.setText(getWord("resume_game"));
      myAnimation.pause();
    }
    isPaused = !isPaused;
  }

  //create button to step through animation
  private Node initializeStepAnimationButton() {
    Button stepAnimationButton = makeButton(getWord("step_game"), value -> step());
    return stepAnimationButton;
  }

  //create the clear screen button
  private Node initializeClearScreenButton() {
    //TODO: update for this program
    Button clearScreen = makeButton(getWord("clear_text"), event -> {
      clearPanels();
      createUIPanels();
    });
    return clearScreen;
  }
  //</editor-fold>

  //<editor-fold desc="Create Load Control Pane and Button">
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


  //create button to load file from source
  private Node initializeLoadFileButton() {
    Button loadFileButton = makeButton(getWord("load_text"), event -> {
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
    return loadFileButton;
  }

  //TODO: this one works in OOLALA, fix to work here
  //create button to save current grid to file
  private Node initializeSaveFileButton() {
    Button saveFileButton = makeButton(getWord("save_text"), event -> {
      String filename = getUserSaveFileName(getWord("get_user_filename"));
      if (myGameController.saveCommand(filename)) {
//          updateSavedDropdown();
      } else {
        sendAlert("Error saving program!");
      }
    });
    return saveFileButton;
  }

  //</editor-fold>

  //<editor-fold desc="Create View Control Pane and Buttons">
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
    viewSetting = makeComboBox(getWord("view_selection"), viewOptions, (event) -> {
      String myViewOption = viewSetting.getSelectionModel().getSelectedItem().toString();
      myGameViewScene.setFill(Color.web(gameViewResources.getString(myViewOption)));
    });
    return viewSetting;
  }

  //create the dropdown allowing user to select which language they prefer
  private Node initializeLanguageControlDropdown() {
    languagesPrograms = makeComboBox(getWord("language_selection"), languageTypes, (event) -> {String lang = (String) languagesPrograms.getValue();
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
      }});
    return languagesPrograms;
  }
  //</editor-fold>
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
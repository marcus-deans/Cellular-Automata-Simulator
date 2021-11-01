package cellsociety.view;

import cellsociety.controller.GameController;
import cellsociety.util.IncorrectCSVFormatException;
import cellsociety.util.IncorrectSimFormatException;
import cellsociety.util.ReflectionException;
import cellsociety.view.ui.controlpanel.AnimationControlPanel;
import cellsociety.view.ui.DetailsPanel;
import cellsociety.view.ui.InformationPanel;
import cellsociety.view.ui.controlpanel.LoadControlPanel;
import cellsociety.view.ui.controlpanel.ViewControlPanel;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * JavaFX View for each game that creates the general UI; each instance for a single game
 * application Relies on appropriate resourcebundles being configured as well as JavaFX Creates
 * gameController
 *
 * @author marcusdeans, drewpeterson
 */
public class GameView extends Application implements PanelListener {
  //JavaFX Simulation Parameters:
  private static final int FRAMES_PER_SECOND = 7;
  private static final double SECOND_DELAY = 7.0 / FRAMES_PER_SECOND;

  //General resource file structure
  private static final String RESOURCE_FILE_PATH = "cellsociety.resources.view.viewControlResources";
  private static final ResourceBundle gameViewResources = ResourceBundle.getBundle(RESOURCE_FILE_PATH);

  //Cosmetic features: colours and views
  private static final String GRID_COLORS_PATH = "cellsociety.resources.defaultColors";
  private static final ResourceBundle defaultGridColours = ResourceBundle.getBundle(
      GRID_COLORS_PATH);

  //Languages
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
  protected static final int OFFSET_Y_TOP = 40;

  private static final int WIDTH_BUFFER = 200;
  private static final int CONTROL_PANEL_OFFSET = 175;

  private String myFilename;
  private String NO_CONTENT = "None";

  //Information panel on top of screen
  private String myTitle;
  private String myAuthor;
  private Node myInfoPanel;

  //Control Panel on Right Side of Screen
  private int controlPanelX;
  private Node myViewControlPanel;
  private Node myAnimationControlPanel;
  private Node myLoadControlPanel;

  //Details panel on bottom of screen
  private String[] myGameParameters;
  private String myDescription;
  private String[] myGridColours;
  private Node myDetailsPanel;

  //Grid display
  private Node myGridPanel;

  //JavaFX setup elements
  private Timeline myAnimation;
  private Group myGameViewRoot;
  private Scene myGameViewScene;

  //Integral Game classes
  private GridView myGridView;
  private GameController myGameController;

  private FileInputStream fis;

  private static final String REQUIRED_PARAMETERS = "cellsociety.resources.controller.requiredParameters";
  private static final ResourceBundle requiredParameters = ResourceBundle.getBundle(
      REQUIRED_PARAMETERS);

  private boolean successfulSetup;

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
    myFilename = filename;
    createController();
    gridDisplayLength = width - WIDTH_BUFFER;
    controlPanelX = width - CONTROL_PANEL_OFFSET;
    myGameViewRoot = new Group();
  }

  // Creates a new GameController for the specific simulation detailed in myFilename (.sim file)
  private void createController() {
    myGameController = new GameController(myFilename);
    setupController();
  }

  // Initializes the controller and retrieves relevant parameters
  //TODO: make sure exception stops everything from running (maybe pass it up another level?)
  private void setupController(){
    successfulSetup = false;
    try {
      myGameController.setupProgram();
      Map<String, String> parameters = myGameController.getConfigurationMap();
      myTitle = parameters.get("Title");
      myType = parameters.get("Type"); //work on translating from GameOfLife->life
      myDescription = parameters.get("Description");
      myAuthor = parameters.get("Author");
      String[] myAdditionalParameters = requiredParameters.getString(myType).split(",");
      myGameParameters = new String[myAdditionalParameters.length];
      for(int iterate = 0; iterate < myAdditionalParameters.length; iterate++){
        if(parameters.get(myAdditionalParameters[iterate]) != null){
          myGameParameters[iterate] = String.format("%s = %s",myAdditionalParameters[iterate], parameters.get(myAdditionalParameters[iterate]));
        }
        else {
          myGameParameters[iterate] = NO_CONTENT;
        }
      }

      if (parameters.get("StateColors") != null) {
        myGridColours = parameters.get("StateColors").split(",");
      } else {
        myGridColours = defaultGridColours.getString(myType).split(",");
      }
      gridSize = myGameController.getGridSize();
      successfulSetup = true;
    }
    catch (IncorrectSimFormatException e) {
      sendAlert(e.getMessage());
    } catch (IncorrectCSVFormatException e) {
      sendAlert(e.getMessage());
    }
    catch (ReflectionException e) {
      sendAlert("InternalError Cannot Make Object");
    }
  }

  /**
   * Start the JavaFX simulation by first setting up the scene and then initializing the animation
   *
   * @param primaryStage the Stage that will be used to display the game scene
   */
  @Override
  public void start(Stage primaryStage) {
    if (successfulSetup){
      myAnimation = new Timeline();
      myAnimation.setCycleCount(Timeline.INDEFINITE);

      setupScene();
      primaryStage.setScene(myGameViewScene);
      primaryStage.setTitle(myTitle);
      primaryStage.show();

      myAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(SECOND_DELAY), e -> step()));
    }
  }

  // creates the scene and initializes all of its components
  private void setupScene() {
    myGameViewRoot = new Group();
    myGameViewScene = new Scene(myGameViewRoot, frameWidth, frameHeight, frameBackground);

    createUIPanels();
    initializeBoundaries();
    myGridPanel = createGrid();
    myGameViewRoot.getChildren().addAll(myInfoPanel, myDetailsPanel, myAnimationControlPanel, myLoadControlPanel, myViewControlPanel, myGridPanel);

    myGameViewScene.getStylesheets().add(GameView.class.getResource("GameViewFormatting.css").toExternalForm());
  }

  //create the UI panels that will provide interactivity and information to the user
  private void createUIPanels() {
    // Information (top) panel:
    myInfoPanel = createInformationPanel();

    // Details (bottom) panel:
    myDetailsPanel = createDetailsPanel();

    // Control (side) panel:
    myAnimationControlPanel = createAnimationControlPane();
    myLoadControlPanel = createLoadControlPanel();
    myViewControlPanel = createViewControlPanel();
  }

  //<editor-fold desc="Create Details Pane and Buttons">
  //create the JavaFX ane on the bottom of the screen; describes colours for cell states as well as simulation parameters
  private Node createDetailsPanel() {
    DetailsPanel myDetailsPanel = new DetailsPanel(gridDisplayLength, myGridColours, myType, myGameParameters);
    return myDetailsPanel.createDetailsPanel();
  }
  //</editor-fold>

  //create information panel on top of screen to display information like type, name, and author to user
  private Node createInformationPanel() {
    InformationPanel myInformationPanel = new InformationPanel(myType, myTitle, myAuthor);
    return myInformationPanel.createInformationPanel();
  }

  //<editor-fold desc="Create Control Pane and Buttons">
  //<editor-fold desc="Create Animation Control Pane and Buttons">
  //create the animation control pane allowing the user to run, pause/resume, clear, and step the simualtion
  private Node createAnimationControlPane() {
    AnimationControlPanel myAnimationControlPanel = new AnimationControlPanel(myAnimation, myGameController,controlPanelX);
    myAnimationControlPanel.setPanelListener(this);
    return myAnimationControlPanel.createAnimationControlPanel();
  }
  //</editor-fold>

  //<editor-fold desc="Create Load Control Pane and Button">
  //create the pane allowing user to load and save simulation files
  private Node createLoadControlPanel() {
    LoadControlPanel myLoadControlPanel = new LoadControlPanel(myAnimation, controlPanelX);
    myLoadControlPanel.setPanelListener(this);
    return myLoadControlPanel.createLoadControlPanel();
  }

  //</editor-fold>

  //<editor-fold desc="Create View Control Pane and Buttons">
  //create the view control panel allowing the user to select cosmetic aspects: colours and language
  private Node createViewControlPanel() {
    ViewControlPanel myViewControlPanel = new ViewControlPanel(controlPanelX);
    myViewControlPanel.setPanelListener(this);
    return myViewControlPanel.createViewControlPanel();
  }
  //</editor-fold>
  //</editor-fold>

  //initialize the grid itself that appears on the scree
  private Node createGrid() {
    myGridView = new GridView(gridSize[0], gridSize[1], myGridColours, gridDisplayLength);
    GridPane myGameGridView = myGridView.getMyGameGrid();
    myGameGridView.setOnMouseClicked(click->updateGrid(click.getX(), click.getY()));
    myGameGridView.setLayoutX(OFFSET_X + 3);
    myGameGridView.setLayoutY(OFFSET_Y_TOP + 3);
    myGameController.setupListener(myGridView);
    myGameController.showInitialStates();
    return myGameGridView;
  }

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

  private void updateGrid(double x, double y) {
    myGameController.calculateIndexesAndUpdateModel(x, y, myGridView.getMyCellHeight(), myGridView.getMyCellWidth());
  }

  //<editor-fold desc="Setup Languages, Conversion, and Update on Change">
  //</editor-fold>

  // runs one step of the simulation
  private void step() {
    myGameController.runSimulation();
  }

  // displays alert/error message to the user - currently duplicated in SharedUIComponents
  protected void sendAlert(String alertMessage) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setContentText(alertMessage);
    alert.show();
  }

  // retrieves relevant word from the "words" ResourceBundle - currently duplicated in SharedUIComponents
  protected String getWord(String key) {
    ResourceBundle words = ResourceBundle.getBundle("words");
    String value = words.getString(key);
    return value;
  }

  // refreshes the UI panels by removing them from the scene before creating new panels and adding them back
  private void refreshUIPanels(){
    myGameViewRoot.getChildren().removeAll(myInfoPanel, myDetailsPanel, myAnimationControlPanel, myLoadControlPanel, myViewControlPanel);
    createUIPanels();
    myGameViewRoot.getChildren().addAll(myInfoPanel, myDetailsPanel, myAnimationControlPanel, myLoadControlPanel, myViewControlPanel);
  }

  /**
   * Updates the language displayed on the UI panels by first switching the default value of the Locale used to represent
   * the selected language and then refreshing the panels
   * @param newLanguage the desired language
   */
  @Override
  public void updateLanguage(String newLanguage) {
    switch (newLanguage) {
      case "English" -> {
        Locale.setDefault(new Locale("en"));
      }
      case "Spanish" -> {
        Locale.setDefault(new Locale("es"));
      }
      case "French" -> {
        Locale.setDefault(new Locale("fr"));
      }
    }

    refreshUIPanels();
  }

  /**
   * Resets the simulation on the screen by simply reloading the current file
   */
  @Override
  public void resetScreen() {
    loadNewFile(myFilename);
  }

  /**
   * Updates the color scheme by simply setting the fill of the background
   * @param newColor desired color scheme
   */
  @Override
  public void updateColorScheme(Color newColor) {
    myGameViewScene.setFill(newColor);
  }

  /**
   * Loads a new file by changing myFilename before resetting the controller and refreshing the grid view/UI panels
   * @param filename name of the file to load
   */
  @Override
  public void loadNewFile(String filename) {
    myFilename = filename;

    myGameController.loadNewFile(myFilename);
    setupController();
    gridSize = myGameController.getGridSize();
    myGameViewRoot.getChildren().remove(myGridPanel);
    myGridPanel = createGrid();
    myGameViewRoot.getChildren().addAll(myGridPanel);
    myGameController.setupListener(myGridView);
    myGameController.showInitialStates();
    refreshUIPanels();
  }

  /**
   * Saves the current simulation and its parameters to a .sim and .csv file
   */
  @Override
  public void saveCurrentFile(){
    // TODO: does NOT currently work
    String filename = getUserSaveFileName(getWord("get_user_filename"));
    if (myGameController.saveCommand(filename)) {
//          updateSavedDropdown();
    }
    else {
      sendAlert("Error Saving Program");
    }
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
    sendAlert("Invalid Filename");
    myAnimation.play();
    return getUserSaveFileName(
        message); //TODO: test to make sure this gives users another chance if they submit an invalid filename
  }
}
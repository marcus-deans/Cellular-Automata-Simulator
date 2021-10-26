package cellsociety.view;

import cellsociety.controller.GameController;
import cellsociety.util.IncorrectCSVFormatException;
import cellsociety.util.IncorrectSimFormatException;
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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

  //Information panel on top of screen
  private String myTitle;
  private String myDescription;
  private String myAuthor;
  private HBox myInformationPanel;

  //Control Panel on Right Side of Screen
  private VBox myViewControlPanel;
  private int controlPanelX;
  private Button pauseGameButton;
  private boolean isPaused;

  //Details panel on bottom of screen
  private HBox myDetailsPanel;
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
      //TODO error checking
    } catch (IncorrectCSVFormatException e) {
      //TODO error checking
    } catch (FileNotFoundException e) {
      //TODO error checking (but also this exception could be skipped if its checked elsewhere)
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
    myAnimation = new Timeline();
    myAnimation.setCycleCount(Timeline.INDEFINITE);

    myGameViewScene = setupGame();
    primaryStage.setScene(myGameViewScene);
    primaryStage.setTitle(myTitle);
    primaryStage.show();


    myAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(SECOND_DELAY), e -> step()));
  }

  //setup the game by creating the appropriate JavaFX components on the Scene
  private Scene setupGame() {
    myGameViewRoot = new Group();
    myGameViewScene = new Scene(myGameViewRoot, frameWidth, frameHeight, frameBackground);
    createUIPanels();
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
    DetailsPanel newDetailsPanel = new DetailsPanel(myGameViewRoot, gridDisplayLength, myGridColours, myType);
  }
  //</editor-fold>

  //create information panel on top of screen to display information like type, name, and author to user
  private void createInformationPanel() {
    InformationPanel newInformationPanel = new InformationPanel(myGameViewRoot, myType, myTitle, myAuthor);
  }

  //<editor-fold desc="Create Control Pane and Buttons">
  //<editor-fold desc="Create Animation Control Pane and Buttons">
  //create the animation control pane allowing the user to run, pause/resume, clear, and step the simualtion
  private void createAnimationControlPane() {
    AnimationControlPanel newAnimationControlPanel = new AnimationControlPanel(myGameViewRoot, myAnimation, myGameController,controlPanelX);
  }
  //</editor-fold>

  //<editor-fold desc="Create Load Control Pane and Button">
  //create the pane allowing user to load and save simulation files
  private void createLoadControlPanel() {
    LoadControlPanel newLoadControlPanel = new LoadControlPanel(myGameViewRoot, myGameController, myAnimation, controlPanelX);
  }

  //</editor-fold>

  //<editor-fold desc="Create View Control Pane and Buttons">
  //create the view control panel allowing the user to select cosmetic aspects: colours and language
  private void createViewControlPanel() {
    ViewControlPanel newViewControlPanel = new ViewControlPanel(myGameViewRoot, myGameViewScene, controlPanelX);
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

  //<editor-fold desc="Setup Languages, Conversion, and Update on Change">

  //</editor-fold>

  //step the animation once
  private void step() {
    myGameController.runSimulation();
  }

}
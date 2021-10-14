package cellsociety.view;

import cellsociety.controller.GameController;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

/**
 * JavaFX View class
 */
public class LifeView extends GameView{

  //  public static final String TITLE = R.string.program_name;
  public static final String TITLE = "LifeView";

  private ComboBox creatureDropdown;

  @Override
  protected Scene setupGame(int width, int height, Paint background) {
    //Initialize the view classes
    myGameController = new GameController();
    performInitialSetup(); //setup everything common between all 3 displays
//    myGameProcessor = new Logo();
    initializeCreatureDropdown(); //dropdown of all turtles and current running turtle
    creaturesTitle(getWord("creatures_text_logo"));
    //Set the scene
    Scene scene = new Scene(root, width, height, background);
    scene.getStylesheets().add(LifeView.class.getResource("GameViewFormatting.css").toExternalForm());
    return scene;
  }

  @Override
  protected File[] getFilesFromPath() {
    return new File("data/examples/logo").listFiles();
  }

  @Override
  protected void updateLanguage() {
    clearText();
    gameTitle();
    savedTitle();
    historyTitle();
    languagesTitle();
    creaturesTitle(getWord(""));
    runTitle();
  }


  @Override
  protected void populateFileNames() {
//    File[] files = new File("data/examples/logo").listFiles();
//    for (File file : files) {
//      if (file.isFile()) {
//        savedPrograms.getItems().add(file.getName());
//      }
//    }
  }

  @Override
  protected void handleInputParsing(String text) {
    myGameController.parseInput(text);
  }

  @Override
  protected void initializeCreatureDropdown() {
    creatureDropdown = new ComboBox();
//    creatureDropdown.setOnAction((event) -> {
//      //TODO: switch to this turtle
//      int id = Integer.parseInt(historyPrograms.getSelectionModel().getSelectedItem().toString());
////      for (ModelTurtle modelTurtle : allModelTurtles) {
////        if (modelTurtle.myTurtleId == id) {
////          myModelTurtle = modelTurtle;
////        }
////      }
//      for (TurtleLinkage turtleLinkage : allTurtleLinkages) {
//        if (turtleLinkage.myID == id) {
//          switchTurtleLinkage(turtleLinkage);
//        }
//      }
//      //TODO: clear previous lines?
//    });
    //TODO: add all the existing turtles to the dropdown
    creatureDropdown.setLayoutX(CREATURES_DROPDOWN_X);
    creatureDropdown.setLayoutY(CREATURES_DROPDOWN_Y);
    creatureDropdown.setMaxWidth(MAX_DROPDOWN_WIDTH);
    root.getChildren().add(creatureDropdown);
  }

  @Override
  protected void clearSpecificGameDropdowns() {
    //TODO: choose which dropdowns to clear
  }
}
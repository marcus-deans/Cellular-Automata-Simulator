package cellsociety.view;

import cellsociety.controller.GameController;
import java.io.File;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Paint;

/**
 * JavaFX View class
 */
public class LifeView extends GameView {

  //  public static final String TITLE = R.string.program_name;
  public static final String TITLE = "LifeView";
  @Override
  protected Scene setupGame(int width, int height, Paint background) {
    //Initialize the view classes
    myGameController = new GameController();
    performInitialSetup(); //setup everything common between all 3 displays
    //Set the scene
    Scene scene = new Scene(root, width, height, background);
    scene.getStylesheets()
        .add(LifeView.class.getResource("GameViewFormatting.css").toExternalForm());
    return scene;
  }

  @Override
  protected void updateLanguage() {
    clearText();
    gameTitle();
    savedTitle();
    historyTitle();
    languagesTitle();
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
  protected void clearSpecificGameDropdowns() {
    //TODO: choose which dropdowns to clear
  }
}
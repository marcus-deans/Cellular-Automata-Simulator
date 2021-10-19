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

  public LifeView(int width, int height, Paint background) {
    super(width, height, background);
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
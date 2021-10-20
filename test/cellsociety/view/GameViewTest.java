package cellsociety.view;

import static org.junit.jupiter.api.Assertions.*;

import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

class GameViewTest extends DukeApplicationTest  {
  // keep only if needed to call application methods in tests
  private GameView myLifeView;
  // keep GUI components used in multiple tests
  private Labeled myLabel;
  private TextInputControl myTextField;


  protected static final String TITLE = "GameView";
  public static final int FRAME_WIDTH = 733;
  public static final int FRAME_HEIGHT = 680;
  public static final Paint BACKGROUND = Color.web("#00539B");


  // this method is run BEFORE EACH test to set up application in a fresh state
  @Override
  public void start (Stage stage) {
    // create application and add scene for testing to given stage
    myLifeView = new GameView(FRAME_WIDTH, FRAME_HEIGHT, BACKGROUND, "Game of Life", 5, 5);
    stage.setScene(myLifeView.setupGame());
    stage.setTitle(TITLE);
    stage.show();

    myLifeView.start(stage);

    // components that will be reused in different tests
    myLabel = lookup("#Label").query();
    myTextField = lookup("#Pane #InputField").query();
    // clear text field, just in case
    myTextField.clear();
  }

  // tests for different kinds of UI components
  @Test
  void testTextFieldAction () {
    String expected = "ENTER test!";
    // GIVEN, app first starts up
    // WHEN, text is typed and action is activated with ENTER key
    clickOn(myTextField).write(expected).write(KeyCode.ENTER.getChar());
    // THEN, check label text has been updated to match input
    assertLabelText(expected);
  }

  @Test
  void testLoadActionBadFilename () {
    final String BAD_FILE_NAME = "bad_file.txt";
    String expected = "Could not load " + BAD_FILE_NAME;
    // GIVEN, app first starts up
    // WHEN, bad file name is entered to create an error dialog box
    // Note, RUN needed because new JFX component (DialogBox) is created by loadData call
//    runAsJFXAction(() -> myLifeView.load(BAD_FILE_NAME));
    // THEN, check proper error is displayed
    assertEquals(expected, getDialogMessage());
  }

  // everything tests text of the label
  private void assertLabelText (String expected) {
    assertEquals(expected, myLabel.getText());
  }


}
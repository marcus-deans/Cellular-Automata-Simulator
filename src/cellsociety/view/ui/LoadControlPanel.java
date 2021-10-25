package cellsociety.view.ui;

import cellsociety.controller.GameController;
import cellsociety.util.IncorrectCSVFormatException;
import cellsociety.util.IncorrectSimFormatException;
import java.io.FileNotFoundException;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;

public class LoadControlPanel extends SharedUIComponents{
  private static final int LOAD_CONTROL_PANEL_Y = 500;
  private GameController myGameController;
  private Timeline myAnimation;
  private int myControlPanelX;


  public LoadControlPanel(Group root, GameController gameController, Timeline animation, int controlPanelX){
    super(root);
    myGameController = gameController;
    myAnimation = animation;
    myControlPanelX = controlPanelX;
    createLoadControlPanel();
  }

  private void createLoadControlPanel(){
    VBox panel = new VBox();
    panel.setSpacing(15);

    Node loadFileButton = initializeLoadFileButton();
    panel.getChildren().add(loadFileButton);

    Node saveFileButton = initializeSaveFileButton();
    panel.getChildren().add(saveFileButton);

    panel.setLayoutX(myControlPanelX);
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
//      initializeGrid();
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
}

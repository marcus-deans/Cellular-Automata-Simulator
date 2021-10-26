package cellsociety.view.ui.controlpanel;

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

public class LoadControlPanel extends ControlPanel {
  private GameController myGameController;
  private Timeline myAnimation;

  public LoadControlPanel(GameController gameController, Timeline animation, int controlPanelX){
    super(controlPanelX);
    myGameController = gameController;
    myAnimation = animation;
    createLoadControlPanel();
  }

  public Node createLoadControlPanel(){
    VBox panel = new VBox();
    panel.setSpacing(getInt("control_panel_spacing"));

    Node loadFileButton = initializeLoadFileButton();
    panel.getChildren().add(loadFileButton);

    Node saveFileButton = initializeSaveFileButton();
    panel.getChildren().add(saveFileButton);

    panel.setLayoutX(myControlPanelX);
    panel.setLayoutY(getInt("load_control_panel_y"));
    panel.setId("load-control-panel");

    return panel;
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

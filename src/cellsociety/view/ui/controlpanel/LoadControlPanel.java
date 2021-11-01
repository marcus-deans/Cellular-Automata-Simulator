package cellsociety.view.ui.controlpanel;

import java.io.File;

import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

/**
 * JavaFX View for each game that creates the general UI; each instance for a single game
 * application Relies on appropriate resourcebundles being configured as well as JavaFX Creates
 * gameController
 *
 * @author marcusdeans, drewpeterson
 */
public class LoadControlPanel extends ControlPanel {
  private Timeline myAnimation;

  public LoadControlPanel(Timeline animation, int controlPanelX){
    super(controlPanelX);
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
      File selectedCSVFile = makeFileChooser("SIM files (*.sim)", "*.sim");
      String filename = selectedCSVFile.getAbsolutePath();
      if(this.getMyPanelListener() != null) {
        this.getMyPanelListener().loadNewFile(filename);
      }
    });
    return loadFileButton;
  }

  private File makeFileChooser(String description, String extensions) {
    FileChooser myFileChooser = new FileChooser();
    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(description, extensions);
    myFileChooser.getExtensionFilters().add(extFilter);
    File selectedFile = myFileChooser.showOpenDialog(null);
    return selectedFile;
  }


  //TODO: this one works in OOLALA, fix to work here
  //create button to save current grid to file
  private Node initializeSaveFileButton() {
    Button saveFileButton = makeButton(getWord("save_text"), event -> {
      if(this.getMyPanelListener() != null) {
        this.getMyPanelListener().saveCurrentFile();
      }
    });
    return saveFileButton;
  }

}

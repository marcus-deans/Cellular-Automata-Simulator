package cellsociety.view.ui;

import cellsociety.view.PanelListener;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * JavaFX superclass that facilitates the creation of the individual panels and buttons on the
 * screen and includes all shared resources and methods. Relies on appropriate resourcebundles being
 * configured as well as JavaFX
 *
 * @author marcusdeans, drewpeterson
 */
public abstract class SharedUIComponents {

  //General resource file structure
  private static final String UI_FILE_PATH = "cellsociety.resources.view.uiLocation";
  private static final ResourceBundle uiLocationResources = ResourceBundle.getBundle(UI_FILE_PATH);

  private PanelListener myPanelListener;

  /**
   * Set the internal panel listener to the PanelListener provided
   *
   * @param panelListener the listener that should be set
   */
  public void addListener(PanelListener panelListener) {
    myPanelListener = panelListener;
  }

  /**
   * Return the panel listener for usage elsewhere so that updates can be carried over
   *
   * @return the PanelListener
   */
  public PanelListener getMyPanelListener() {
    return myPanelListener;
  }

  //<editor-fold desc="Create General JavaFX Element Creators">
  //method to create individual text label
  protected Text makeText(String text) {
    Text newText = new Text(text);
    newText.setId("information-text");
    return newText;
  }

  //method to create individual progress label
  protected Label makeInformationLabel(String text) {
    Label label = new Label(text);
    label.setId("information-label");
    return label;
  }

  //create a JavaFX Button with the appropriate text as well as provided EventHandler
  protected Button makeButton(String property, EventHandler<ActionEvent> response) {
    Button newButton = new Button();
    newButton.setText(property);
    newButton.setPrefWidth(getInt("button_width"));
    newButton.setPrefHeight(getInt("button_height"));
    newButton.setOnAction(response);
    return newButton;
  }

  //create a JavaFX ComboBox (dropdown) with the appropriate title and provided options and Eventhandler
  protected ComboBox makeComboBox(String title, List<String> boxOptions,
      EventHandler<ActionEvent> response) {
    ComboBox newComboBox = new ComboBox<>(FXCollections.observableList(boxOptions));
    newComboBox.setPrefWidth(getInt("button_width"));
    newComboBox.setPrefHeight(getInt("button_height"));
    newComboBox.setPromptText(title);
    newComboBox.setOnAction(response);
    return newComboBox;
  }

  //create a JavaFX HBox to serve as an individual panel consisting of text and label
  protected HBox makeHorizontalPanel(Text text, Label label) {
    HBox newHorizontalPanel = new HBox();
    newHorizontalPanel.setSpacing(getInt("horizontal_panel_spacing"));
    newHorizontalPanel.getChildren().addAll(text, label);
    return newHorizontalPanel;
  }
  //</editor-fold>

  //get a String resource from the resource file
  protected String getWord(String key) {
    ResourceBundle words = ResourceBundle.getBundle("words");
    String value = "error";
    try {
      value = words.getString(key);
    } catch (Exception exception) {
      sendAlert(String.format("%s string was not found in Resource File %s", key, UI_FILE_PATH));
    }
    return value;
  }

  //get an Integer resource from the resource file
  protected int getInt(String key) {
    int value = -1;
    try {
      value = Integer.parseInt(uiLocationResources.getString(key));
    } catch (Exception exception) {
      sendAlert(String.format("%s string was not found in Resource File %s", key, UI_FILE_PATH));
    }
    return value;
  }

  //set an alert to the user indicating incorrect input
  protected void sendAlert(String alertMessage) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setContentText(alertMessage);
    alert.show();
  }
}

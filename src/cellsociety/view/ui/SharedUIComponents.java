package cellsociety.view.ui;

import cellsociety.model.gamegrids.GameGrid;
import cellsociety.view.GridView;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class SharedUIComponents {
  private static final int BUTTON_WIDTH = 150;
  private static final int BUTTON_HEIGHT = 30;
  protected static final int HORIZONTAL_PANEL_SPACING = 5;
  protected static final int CONTROL_PANEL_SPACING = 15;

  protected static final int OFFSET_X = 10;
  protected static final int OFFSET_Y = 15;
  protected static final int OFFSET_Y_TOP = 40;
  protected Group myGameViewRoot;

  private static final int WIDTH_BUFFER = 200;
  private static final int CONTROL_PANEL_OFFSET = 175;

  public SharedUIComponents(Group root){
    myGameViewRoot = root;

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

  //creata a JavaFX Button with the appropriate text as well as provided EventHandler
  protected Button makeButton(String property, EventHandler<ActionEvent> response) {
    Button newButton = new Button();
    newButton.setText(property);
    newButton.setPrefWidth(BUTTON_WIDTH);
    newButton.setPrefHeight(BUTTON_HEIGHT);
    newButton.setOnAction(response);
    return newButton;
  }

  //create a JavaFX ComboBox (dropdown) with the appropriate title and provided options and Eventhandler
  protected ComboBox makeComboBox(String title, List<String> boxOptions, EventHandler<ActionEvent> response){
    ComboBox newComboBox = new ComboBox<>(FXCollections.observableList(boxOptions));
    newComboBox.setPrefWidth(BUTTON_WIDTH);
    newComboBox.setPrefHeight(BUTTON_HEIGHT);
    newComboBox.setPromptText(title);
    newComboBox.setOnAction(response);
    return newComboBox;
  }

  //create a JavaFX HBox to serve as an individual panel consisting of text and label
  protected HBox makeHorizontalPanel(Text text, Label label){
    HBox newHorizontalPanel = new HBox();
    newHorizontalPanel.setSpacing(HORIZONTAL_PANEL_SPACING);
    newHorizontalPanel.getChildren().addAll(text, label);
    return newHorizontalPanel;
  }
  //</editor-fold>

  protected String getWord(String key) {
    ResourceBundle words = ResourceBundle.getBundle("words");
    String value = words.getString(key);
    return value;
  }

  protected void updateLanguage() {
//    clearPanels();
//    createUIPanels();
  }

  //method to clear all extant JavaFX panels from the screen for refresh
  protected void clearPanels() {
//    myGameViewRoot.getChildren().remove(myDetailsPanel);
//    myGameViewRoot.getChildren().remove(myInformationPanel);
//    myGameViewRoot.getChildren().remove(myViewControlPanel);
  }

  //set an alert to the user indicating incorrect input
  protected void sendAlert(String alertMessage) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setContentText(alertMessage);
    alert.show();
  }
}

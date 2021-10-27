package cellsociety.view.ui;

import cellsociety.model.gamegrids.GameGrid;
import cellsociety.view.GridView;
import java.util.List;
import java.util.ResourceBundle;

import cellsociety.view.PanelListener;
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
  //General resource file structure
  private static final String UI_FILE_PATH = "cellsociety.resources.view.uiLocation";
  private static final ResourceBundle uiLocationResources = ResourceBundle.getBundle(UI_FILE_PATH);

  protected static final int OFFSET_X = 10;
  protected static final int OFFSET_Y = 15;
  protected static final int OFFSET_Y_TOP = 40;
  //protected Group myGameViewRoot;

  private static final int WIDTH_BUFFER = 200;
  private static final int CONTROL_PANEL_OFFSET = 175;
  protected PanelListener listener;

  public SharedUIComponents(){
  }

  public void addListener(PanelListener pl) {
    listener = pl;
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
    newButton.setPrefWidth(getInt("button_width"));
    newButton.setPrefHeight(getInt("button_height"));
    newButton.setOnAction(response);
    return newButton;
  }

  //create a JavaFX ComboBox (dropdown) with the appropriate title and provided options and Eventhandler
  protected ComboBox makeComboBox(String title, List<String> boxOptions, EventHandler<ActionEvent> response){
    ComboBox newComboBox = new ComboBox<>(FXCollections.observableList(boxOptions));
    newComboBox.setPrefWidth(getInt("button_width"));
    newComboBox.setPrefHeight(getInt("button_height"));
    newComboBox.setPromptText(title);
    newComboBox.setOnAction(response);
    return newComboBox;
  }

  //create a JavaFX HBox to serve as an individual panel consisting of text and label
  protected HBox makeHorizontalPanel(Text text, Label label){
    HBox newHorizontalPanel = new HBox();
    newHorizontalPanel.setSpacing(getInt("horizontal_panel_spacing"));
    newHorizontalPanel.getChildren().addAll(text, label);
    return newHorizontalPanel;
  }
  //</editor-fold>

  protected String getWord(String key) {
    ResourceBundle words = ResourceBundle.getBundle("words");
    String value = words.getString(key);
    return value;
  }

  protected int getInt(String key) {
    int value = Integer.parseInt(uiLocationResources.getString(key));
    return value;
  }

  //set an alert to the user indicating incorrect input
  protected void sendAlert(String alertMessage) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setContentText(alertMessage);
    alert.show();
  }
}

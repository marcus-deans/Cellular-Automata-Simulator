package cellsociety.view.ui;

import cellsociety.view.ui.SharedUIComponents;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ViewControlPanel extends SharedUIComponents {
  private Scene myGameViewScene;
  private ComboBox languagesPrograms;
  private ComboBox viewSetting;


  private static final int VIEW_CONTROL_PANEL_Y = 100;
  private int myControlPanelX;

  //General resource file structure
  private static final String RESOURCE_FILE_PATH = "cellsociety.resources.gameView";
  private static final ResourceBundle gameViewResources = ResourceBundle.getBundle(RESOURCE_FILE_PATH);

  private static final String DEFAULT_VIEW = "Duke";
  private static final String VIEW_OPTIONS = "ViewOptions";
  private final List<String> viewOptions = Arrays.asList(
      gameViewResources.getString(VIEW_OPTIONS).split(","));

  //Cosmetic features: languages
  private static final String LANGUAGE_OPTIONS = "LanguageOptions";
  private final List<String> languageTypes = Arrays.asList(gameViewResources.getString(LANGUAGE_OPTIONS).split(","));

  public ViewControlPanel(Group root, Scene gameViewScene, int controlPanelX){
    super(root);
    myGameViewScene = gameViewScene;
    myControlPanelX = controlPanelX;
    createViewControlPanel();
  }


  private void createViewControlPanel(){
    VBox myViewControlPanel = new VBox();
    myViewControlPanel.setSpacing(CONTROL_PANEL_SPACING);

    Node viewControlDropdown = initializeViewControlDropdown();
    myViewControlPanel.getChildren().add(viewControlDropdown);

    Node languageControlDropdown = initializeLanguageControlDropdown();
    myViewControlPanel.getChildren().add(languageControlDropdown);

    myViewControlPanel.setLayoutX(myControlPanelX);
    myViewControlPanel.setLayoutY(VIEW_CONTROL_PANEL_Y);
    myViewControlPanel.setId("view-control-panel");

    myGameViewRoot.getChildren().add(myViewControlPanel);
  }


  //create the specific dropdown allowing the user to select which view mode they prefer
  private Node initializeViewControlDropdown() {
     viewSetting = makeComboBox(getWord("view_selection"), viewOptions, (event) -> {
      String myViewOption = viewSetting.getSelectionModel().getSelectedItem().toString();
      myGameViewScene.setFill(Color.web(gameViewResources.getString(myViewOption)));
    });
    return viewSetting;
  }

  //create the dropdown allowing user to select which language they prefer
  private Node initializeLanguageControlDropdown() {
     languagesPrograms = makeComboBox(getWord("language_selection"), languageTypes, (event) -> {String lang = (String) languagesPrograms.getValue();
      switch (lang) {
        case "English" -> {
          Locale.setDefault(new Locale("en"));
          updateLanguage();
        }
        case "Spanish" -> {
          Locale.setDefault(new Locale("es"));
          updateLanguage();
        }
        case "French" -> {
          Locale.setDefault(new Locale("fr"));
          updateLanguage();
        }
      }});
    return languagesPrograms;
  }
}

package cellsociety.view.ui;

import cellsociety.view.ui.SharedUIComponents;
import java.util.Locale;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ViewControlPanel extends SharedUIComponents {
  private static final int VIEW_CONTROL_PANEL_Y = 100;

  public ViewControlPanel(Group root){
    super(root);
    createViewControlPanel();
  }


  private void createViewControlPanel(){
    VBox myViewControlPanel = new VBox();
    myViewControlPanel.setSpacing(15);

    Node viewControlDropdown = initializeViewControlDropdown();
    myViewControlPanel.getChildren().add(viewControlDropdown);

    Node languageControlDropdown = initializeLanguageControlDropdown();
    myViewControlPanel.getChildren().add(languageControlDropdown);

    myViewControlPanel.setLayoutX(controlPanelX);
    myViewControlPanel.setLayoutY(VIEW_CONTROL_PANEL_Y);
    myViewControlPanel.setId("view-control-panel");

    myGameViewRoot.getChildren().add(myViewControlPanel);
  }


  //create the specific dropdown allowing the user to select which view mode they prefer
  private Node initializeViewControlDropdown() {
    Node viewSetting = makeComboBox(getWord("view_selection"), viewOptions, (event) -> {
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

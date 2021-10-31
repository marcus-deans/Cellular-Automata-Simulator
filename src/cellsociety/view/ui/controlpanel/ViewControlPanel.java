package cellsociety.view.ui.controlpanel;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ViewControlPanel extends ControlPanel {
  private ComboBox languagesPrograms;
  private ComboBox viewSetting;

  private static final int VIEW_CONTROL_PANEL_Y = 100;

  //General resource file structure
  private static final String RESOURCE_FILE_PATH = "cellsociety.resources.view.viewControlResources";
  private static final ResourceBundle gameViewResources = ResourceBundle.getBundle(RESOURCE_FILE_PATH);

  private static final String DEFAULT_VIEW = "Duke";
  private static final String VIEW_OPTIONS = "ViewOptions";
  private final List<String> viewOptions = Arrays.asList(
      gameViewResources.getString(VIEW_OPTIONS).split(","));

  //Cosmetic features: languages
  private static final String LANGUAGE_OPTIONS = "LanguageOptions";
  private final List<String> languageTypes = Arrays.asList(gameViewResources.getString(LANGUAGE_OPTIONS).split(","));

  public ViewControlPanel(int controlPanelX){
    super(controlPanelX);
    createViewControlPanel();
  }

  public Node createViewControlPanel(){
    VBox myViewControlPanel = new VBox();
    myViewControlPanel.setSpacing(getInt("control_panel_spacing"));

    Node viewControlDropdown = initializeViewControlDropdown();
    myViewControlPanel.getChildren().add(viewControlDropdown);

    Node languageControlDropdown = initializeLanguageControlDropdown();
    myViewControlPanel.getChildren().add(languageControlDropdown);

    myViewControlPanel.setLayoutX(myControlPanelX);
    myViewControlPanel.setLayoutY(getInt("view_control_panel_y"));
    myViewControlPanel.setId("view-control-panel");

    return myViewControlPanel;
  }


  //create the specific dropdown allowing the user to select which view mode they prefer
  private Node initializeViewControlDropdown() {
     viewSetting = makeComboBox(getWord("view_selection"), viewOptions, (event) -> {
      String myViewOption = viewSetting.getSelectionModel().getSelectedItem().toString();
      if(this.getListener() != null){
        this.getListener().updateColorScheme(Color.web(gameViewResources.getString(myViewOption)));
      }
    });
    return viewSetting;
  }

  //create the dropdown allowing user to select which language they prefer
  private Node initializeLanguageControlDropdown() {
     languagesPrograms = makeComboBox(getWord("language_selection"), languageTypes, (event) -> {
       String lang = (String) languagesPrograms.getValue();
      switch (lang) {
        // TODO: does the language Locale need to be set here or in GameView???
        case "English" -> {
          Locale.setDefault(new Locale("en"));
          if(this.getListener() != null){
            this.getListener().updateLanguage("en");
          }
        }
        case "Spanish" -> {
          Locale.setDefault(new Locale("es"));
          if(this.getListener() != null){
            this.getListener().updateLanguage("es");
          }
        }
        case "French" -> {
          Locale.setDefault(new Locale("fr"));
          if(this.getListener() != null){
            this.getListener().updateLanguage("fr");
          }
        }
      }});
    return languagesPrograms;
  }
}

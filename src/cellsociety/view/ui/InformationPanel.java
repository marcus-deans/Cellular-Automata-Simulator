package cellsociety.view.ui;

import javafx.scene.Node;
import javafx.scene.layout.HBox;

/**
 * JavaFX panel that creates the information panel display the game type, author, and name.
 * Relies on appropriate resourcebundles being configured, SharedUIComponents, and JavaFX
 *
 * @author marcusdeans, drewpeterson
 */
public class InformationPanel extends SharedUIComponents{
  private String myType;
  private String myTitle;
  private String myAuthor;

  /**
   * Initialize the information panel creator
   * @param type String type of simulation
   * @param title String title of simulation
   * @param author String author of simulation
   */
  public InformationPanel(String type, String title, String author){
    myType = type;
    myTitle = title;
    myAuthor = author;
  }

  /**
   * Create the information panel that displays type, name, and author of the simulation on-screen
   * @return the JavaFX HBox that constitutes the information panel
   */
  public Node createInformationPanel(){
    HBox myInformationPanel = new HBox();
    myInformationPanel.setSpacing(getInt("information_panel_spacing"));

    HBox gameTypePanel = makeHorizontalPanel(makeText(getWord("game_type_text")), makeInformationLabel(myType));
    HBox gameNamePanel = makeHorizontalPanel(makeText(getWord("game_name_text")), makeInformationLabel(myTitle));
    HBox gameAuthorPanel = makeHorizontalPanel(makeText(getWord("game_author_text")), makeInformationLabel(myAuthor));

    myInformationPanel.getChildren().addAll(gameTypePanel, gameNamePanel, gameAuthorPanel);
    myInformationPanel.setLayoutX(getInt("offset_x"));
    myInformationPanel.setLayoutY(getInt("offset_y"));
    myInformationPanel.setId("information-panel");

    return myInformationPanel;
  }
}

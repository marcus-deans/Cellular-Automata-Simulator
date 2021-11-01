package cellsociety.view.ui;

import javafx.scene.Node;
import javafx.scene.layout.HBox;

/**
 * JavaFX View for each game that creates the general UI; each instance for a single game
 * application Relies on appropriate resourcebundles being configured as well as JavaFX Creates
 * gameController
 *
 * @author marcusdeans, drewpeterson
 */
public class InformationPanel extends SharedUIComponents{
  private String myType;
  private String myTitle;
  private String myAuthor;

  /**
   *
   * @param type
   * @param title
   * @param author
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

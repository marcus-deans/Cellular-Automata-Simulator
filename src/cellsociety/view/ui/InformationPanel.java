package cellsociety.view.ui;

import javafx.scene.Node;
import javafx.scene.layout.HBox;

public class InformationPanel extends SharedUIComponents{
  private String myType;
  private String myTitle;
  private String myAuthor;

  public InformationPanel(String type, String title, String author){
    myType = type;
    myTitle = title;
    myAuthor = author;
  }

  public Node createInformationPanel(){
    HBox myInformationPanel = new HBox();
    myInformationPanel.setSpacing(getInt("information_panel_spacing"));

    HBox gameTypePanel = makeHorizontalPanel(makeText(getWord("game_type_text")), makeInformationLabel(myType));
    HBox gameNamePanel = makeHorizontalPanel(makeText(getWord("game_name_text")), makeInformationLabel(myTitle));
    HBox gameAuthorPanel = makeHorizontalPanel(makeText(getWord("game_author_text")), makeInformationLabel(myAuthor));

    myInformationPanel.getChildren().addAll(gameTypePanel, gameNamePanel, gameAuthorPanel);
    myInformationPanel.setLayoutX(OFFSET_X);
    myInformationPanel.setLayoutY(OFFSET_Y);
    myInformationPanel.setId("information-panel");

    return myInformationPanel;
  }
}

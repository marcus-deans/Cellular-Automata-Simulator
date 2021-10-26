package cellsociety.view.ui;

import javafx.scene.Group;
import javafx.scene.layout.HBox;

public class InformationPanel extends SharedUIComponents{
  private String myType;
  private String myTitle;
  private String myAuthor;

  public InformationPanel(Group root, String type, String title, String author){
    super(root);
    myType = type;
    myTitle = title;
    myAuthor = author;
    createInformationPanel();
  }

  private void createInformationPanel(){
    HBox myInformationPanel = new HBox();
    myInformationPanel.setSpacing(20);

    HBox gameTypePanel = makeHorizontalPanel(makeText(getWord("game_type_text")), makeInformationLabel(myType));
    HBox gameNamePanel = makeHorizontalPanel(makeText(getWord("game_name_text")), makeInformationLabel(myTitle));
    HBox gameAuthorPanel = makeHorizontalPanel(makeText(getWord("game_author_text")), makeInformationLabel(myAuthor));

    myInformationPanel.getChildren().addAll(gameTypePanel, gameNamePanel, gameAuthorPanel);
    myInformationPanel.setLayoutX(OFFSET_X);
    myInformationPanel.setLayoutY(OFFSET_Y);
    myInformationPanel.setId("information-panel");

    myGameViewRoot.getChildren().add(myInformationPanel);
  }
}

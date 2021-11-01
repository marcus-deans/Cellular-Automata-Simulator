package cellsociety.view.ui;

import static java.util.Map.entry;

import java.util.Map;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * JavaFX View for each game that creates the general UI; each instance for a single game
 * application Relies on appropriate resourcebundles being configured as well as JavaFX Creates
 * gameController
 *
 * @author marcusdeans, drewpeterson
 */
public class DetailsPanel extends SharedUIComponents{
  private int myGridDisplayLength;
  private String[] myGridColours;
  private String[] myGameParameters;
  private String myType;

  private final Map<String, String[]> colourLabelNames = Map.ofEntries(
      entry("GameOfLife", new String[]{"Dead", "Alive"}),
      entry("SpreadingOfFire", new String[]{"Empty", "Tree", "Fire"}),
      entry("Segregation", new String[]{"Empty", "Alpha", "Beta"}),
      entry("WatorWorld", new String[]{"Water", "Fish", "Shark"}),
      entry("Percolation", new String[]{"Empty", "Blocked", "Percolated"})
  );

  /**
   *
   * @param gridDisplayLength
   * @param gridColours
   * @param type
   * @param gameParameters
   */
  public DetailsPanel(int gridDisplayLength, String[] gridColours, String type, String[] gameParameters){
    myGridDisplayLength = gridDisplayLength;
    myGridColours = gridColours;
    myGameParameters = gameParameters;
    myType = type;
    createDetailsPanel();
  }

  /**
   *
   * @return
   */
  public Node createDetailsPanel(){
    HBox myDetailsPanel = new HBox();
    myDetailsPanel.setSpacing(getInt("details_panel_spacing"));

    myDetailsPanel.getChildren().add(createCellStatesPanel());
    myDetailsPanel.getChildren().add(createGameParametersPanel());

    myDetailsPanel.setLayoutX(getInt("offset_x"));
    myDetailsPanel.setLayoutY(getInt("offset_y") + getInt("offset_y_top") + myGridDisplayLength);
    myDetailsPanel.setId("details-panel");

    return myDetailsPanel;
  }

  //method to create the HBox containing information on the simulation parameters
  private HBox createGameParametersPanel() {
    HBox gameParametersPanel = new HBox();
    gameParametersPanel.setSpacing(getInt("horizontal_panel_spacing"));
    Node gameParametersText = makeText(getWord("game_parameters_text"));
    gameParametersPanel.getChildren().add(gameParametersText);

    if(myGameParameters[0].equals("None")){
      myGameParameters = new String[]{getWord("no_game_parameters_text")};
    }
    for (String parameter : myGameParameters){
      Label newGameParameterLabel = makeInformationLabel(parameter);
      gameParametersPanel.getChildren().add(newGameParameterLabel);

    }
    return gameParametersPanel;
  }


  //method to create the HBox containing information on the colours corresponding to cell states
  private Node createCellStatesPanel() {
    HBox cellStatesPanel = new HBox();
    cellStatesPanel.setSpacing(getInt("horizontal_panel_spacing"));
    Node gameTypeText = makeText(getWord("cell_state_text"));
    cellStatesPanel.getChildren().add(gameTypeText);

    for (int iterate = 0; iterate < myGridColours.length; iterate++) {
      String colour = myGridColours[iterate];

      Label cellStateLabel = makeInformationLabel(colourLabelNames.get(myType)[iterate]);
      cellStatesPanel.getChildren().add(cellStateLabel);

      Rectangle cellStateRectangle = makeCellStateRectangle();
      cellStateRectangle.setId("cell-state-rectangle");
      cellStateRectangle.setFill(Paint.valueOf(colour));
      cellStatesPanel.getChildren().add(cellStateRectangle);
    }
    return cellStatesPanel;
  }

  //method to create small box for cell state colours
  private Rectangle makeCellStateRectangle() {
    Rectangle newCellState = new Rectangle();
    newCellState.setWidth(getInt("cell_state_size"));
    newCellState.setHeight(getInt("cell_state_size"));
    return newCellState;
  }
  //</editor-fold>



}

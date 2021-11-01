package cellsociety.view.ui;

import static java.util.Map.entry;

import java.util.Map;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * JavaFX panel that creates the details panel that displays game colours and parameters Relies on
 * appropriate resourcebundles being configured, SharedUIComponents, and JavaFX
 *
 * @author marcusdeans, drewpeterson
 */
public class DetailsPanel extends SharedUIComponents {

  private final Map<String, String[]> colourLabelNames = Map.ofEntries(
      entry("GameOfLife",
          new String[]{getWord("gameoflife_cellstate1"), getWord("gameoflife_cellstate2")}),
      entry("SpreadingOfFire", new String[]{getWord("fire_cellstate1"), getWord("fire_cellstate2"),
          getWord("fire_cellstate3")}),
      entry("Segregation", new String[]{getWord("segregation_cellstate1"), getWord("segregation_cellstate2"),
          getWord("segregation_cellstate3")}),
      entry("WatorWorld", new String[]{getWord("wator_cellstate1"), getWord("wator_cellstate2"),
          getWord("wator_cellstate3")}),
      entry("Percolation", new String[]{getWord("percolation_cellstate1"), getWord("percolation_cellstate2"),
          getWord("percolation_cellstate3")})
  );
  private int myGridDisplayLength;
  private String[] myGridColours;
  private String[] myGameParameters;
  private String myType;

  /**
   * Initialize the details panel creator
   *
   * @param gridDisplayLength the size of the display that can be occupied
   * @param gridColours       the colours of the grid that should be labeled in the legend
   * @param type              String type of simulation
   * @param gameParameters    the parameters of the game if pertinent, e.g., fire probability
   */
  public DetailsPanel(int gridDisplayLength, String[] gridColours, String type,
      String[] gameParameters) {
    myGridDisplayLength = gridDisplayLength;
    myGridColours = gridColours;
    myGameParameters = gameParameters;
    myType = type;
    createDetailsPanel();
  }

  /**
   * Create the details panel that displays colour legend and parameters of the simulation
   *
   * @return the JavaFX HBox that constitutes the details panel
   */
  public Node createDetailsPanel() {
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

    if (myGameParameters[0].equals("None")) {
      myGameParameters = new String[]{getWord("no_game_parameters_text")};
    }
    for (String parameter : myGameParameters) {
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

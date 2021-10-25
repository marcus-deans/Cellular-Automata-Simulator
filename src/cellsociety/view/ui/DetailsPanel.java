package cellsociety.view.ui;

import static java.util.Map.entry;

import java.util.Map;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class DetailsPanel extends SharedUIComponents{
  private int myGridDisplayLength;
  private static final int CELL_STATE_SIZE = 15;
  private String[] myGridColours;
  private String myType;

  private final Map<String, String[]> colourLabelNames = Map.ofEntries(
      entry("GameOfLife", new String[]{"Dead", "Alive"}),
      entry("SpreadingOfFire", new String[]{"Empty", "Tree", "Fire"}),
      entry("Segregation", new String[]{"Empty", "Alpha", "Beta"}),
      entry("WatorWorld", new String[]{"Water", "Fish", "Shark"}),
      entry("Percolation", new String[]{"Empty", "Blocked", "Percolated"})
  );

  public DetailsPanel(Group root, int gridDisplayLength, String[] gridColours, String type){
    super(root);
    myGridDisplayLength = gridDisplayLength;
    myGridColours = gridColours;
    myType = type;
    createDetailsPanel();
  }

  private void createDetailsPanel(){
    HBox myDetailsPanel = new HBox();
    myDetailsPanel.setSpacing(40);

    myDetailsPanel.getChildren().add(createCellStatesPanel());
    myDetailsPanel.getChildren().add(createGameParametersPanel());

    myDetailsPanel.setLayoutX(OFFSET_X);
    myDetailsPanel.setLayoutY(OFFSET_Y + OFFSET_Y_TOP + myGridDisplayLength);
    myDetailsPanel.setId("details-panel");

    myGameViewRoot.getChildren().add(myDetailsPanel);
  }

  //method to create the HBox containing information on the simulation parameters
  private HBox createGameParametersPanel() {
    HBox gameParametersPanel = new HBox();
    gameParametersPanel.setSpacing(5);
    Node gameParametersText = makeText(getWord("game_parameters_text"));
    gameParametersPanel.getChildren().add(gameParametersText);

    Label firstGameParameterLabel = makeInformationLabel(getWord("game_parameters_label_alpha"));
    gameParametersPanel.getChildren().add(firstGameParameterLabel);
    return gameParametersPanel;
  }


  //method to create the HBox containing information on the colours corresponding to cell states
  private Node createCellStatesPanel() {
    HBox cellStatesPanel = new HBox();
    cellStatesPanel.setSpacing(5);
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
    newCellState.setWidth(CELL_STATE_SIZE);
    newCellState.setHeight(CELL_STATE_SIZE);
    return newCellState;
  }
  //</editor-fold>



}

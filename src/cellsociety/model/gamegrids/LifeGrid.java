package cellsociety.model.gamegrids;

import cellsociety.model.cells.Cell;
import cellsociety.model.cells.LifeCell.LIFE_STATES;
import java.util.Map;
import java.util.ResourceBundle;

/*
 * Rules of Game of Life:
 * Any live cell with two or three live neighbours survives
 * Dead cell with exactly three live neighbours becomes alive
 * All other live cells die, and all other dead cells stay dead
 */

/**
 * Create new LifeGrid that creates the grid of LifeCells that is used for the game
 * Depends on accurate input from the GameController
 * @author marcusdeans
 */
public class LifeGrid extends GameGrid {

  private static final String LIFE_RESOURCE_PATH = "cellsociety.resources.model.gameRuleControl";
  private static final ResourceBundle lifeRuleResources = ResourceBundle.getBundle(LIFE_RESOURCE_PATH);

  private final int makeAliveNumber = Integer.parseInt(lifeRuleResources.getString("makeDeadAlive"));
  private final int keepAliveNumber = Integer.parseInt(lifeRuleResources.getString("keepAliveAlive"));
  /**
   * Create the new LifeGrid
   * @param gameGrid array of all of the individual Cells that make up the gird
   * @param type the type of program being created -> necessary due to reflection
   * @param configurationMap of the different parameters used specifically in this game (none)
   */
  public LifeGrid(Cell[][] gameGrid, String type, Map<String, String> configurationMap) {
    super(gameGrid, type);
  }

  /**
   * Run the game as GameOfLife
   */
  @Override
  public void runGame() {
    computeNeighborsAndRules();
  }

  //apply the rules of the Game of Life -> go through neighbours and check which conditions satisfied
  //store new value for given cell in futureGrid
  protected void applyGameRules(Cell computingCell, int col, int row) {
    int newValue = -1;
    int liveliness = computingCell.getMyCellState();
    int liveCount = 0; //alive neighbors
    for (Cell neighbouringCell : this.getCheckingCellNeighbours()) {
      if (neighbouringCell != null) {
        if (neighbouringCell.getMyCellState() == LIFE_STATES.LIVE.getValue()) {
          liveCount++;
        }
      }
    }

    newValue = determineNewCellValue(liveliness, liveCount);
    this.setFutureCellValue(row, col, newValue);
    //futureGrid[row][col].setMyCellState(newValue);
  }

  //based on the infomration gathered, determine the new value that the Cell should be set to
  private int determineNewCellValue(int liveliness, int liveCount) {
    int newValue;
    if ((liveliness == 1) && (liveCount == keepAliveNumber || liveCount == makeAliveNumber)) {
      newValue = 1;
    } else if ((liveliness == 0) && (liveCount == makeAliveNumber)) {
      newValue = 1;
    } else {
      newValue = 0;
    }
    return newValue;
  }

  //convert the integer value of a cell into the appropriate state for ease
  private LIFE_STATES determineCellState(int newValue) {
    switch (newValue) {
      case 0 -> {
        return LIFE_STATES.DEAD;
      }
      case 1 -> {
        return LIFE_STATES.LIVE;
      }
    }
    return LIFE_STATES.ERROR;
  }
}

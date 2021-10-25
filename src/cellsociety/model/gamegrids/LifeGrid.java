package cellsociety.model.gamegrids;

import cellsociety.model.cells.Cell;
import cellsociety.model.cells.LifeCell.LIFE_STATES;
import java.util.Map;

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
    for (Cell neighbouringCell : checkingCellNeighbours) {
      if (neighbouringCell != null) {
        if (neighbouringCell.getMyCellState() == LIFE_STATES.LIVE.getValue()) {
          liveCount++;
        }
      }
    }

    if ((liveliness == 1) && (liveCount == 2 || liveCount == 3)) {
      newValue = 1;
    } else if ((liveliness == 0) && (liveCount == 3)) {
      newValue = 1;
    } else {
      newValue = 0;
    }

    futureGrid[row][col].setMyCellState(newValue);
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

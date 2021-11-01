package cellsociety.model.gamegrids;

import cellsociety.model.cells.Cell;
import cellsociety.model.cells.PercCell.PERC_STATES;
import java.util.Map;

/**
 * Create new PercGrid that creates the grid of PercCells that is used for the game
 * Depends on accurate input from the GameController
 * @author morganfeist, marcusdeans */
public class PercGrid extends GameGrid {

  /**
   * Create the new PercGrid
   * @param gameGrid array of all of the individual Cells that make up the gird
   * @param type the type of program being created -> necessary due to reflection
   * @param configurationMap of the different parameters used specifically in this game (none)
   */
  public PercGrid(Cell[][] gameGrid, String type, Map<String, String> configurationMap) {
    super(gameGrid, type);
  }

  /**
   * Run the game as Percolation
   */
  @Override
  public void runGame() {
    computeNeighborsAndRules();
  }

  //convert the integer value of a cell into the appropriate state for ease
  private PERC_STATES determineCellState(int newValue) {
    switch (newValue) {
      case 0 -> {
        return PERC_STATES.EMPTY;
      }
      case 1 -> {
        return PERC_STATES.PERCOLATED;
      }
      case 2 -> {
        return PERC_STATES.BLOCKED;
      }
    }
    return PERC_STATES.ERROR;
  }

  //apply the rules of Percolation  -> go through neighbours and check which conditions satisfied
  //also check cases for self and store new value for given cell in futureGrid
  protected void applyGameRules(Cell computingCell, int col, int row) {
    int currentCellState = computingCell.getMyCellState();
    int newValue = currentCellState;

    switch (determineCellState(currentCellState)) {
      case EMPTY -> {
        for (Cell neighbouringCell : this.getCheckingCellNeighbours()) {
          if (neighbouringCell != null) {
            if (neighbouringCell.getMyCellState() == PERC_STATES.PERCOLATED.getValue()) {
              newValue = PERC_STATES.PERCOLATED.getValue();
              break;
            }
          }
        }
      }
      case BLOCKED -> {
        newValue = PERC_STATES.BLOCKED.getValue();
      }
      case PERCOLATED -> {
        newValue = PERC_STATES.PERCOLATED.getValue();
      }
      default -> {
        newValue = PERC_STATES.ERROR.getValue();
      }
    }
    this.setFutureCellValue(row, col, newValue);
    //futureGrid[row][col].setMyCellState(newValue);
  }

}

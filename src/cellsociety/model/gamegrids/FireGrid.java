package cellsociety.model.gamegrids;

import cellsociety.model.cells.Cell;
import cellsociety.model.cells.FireCell.FIRE_STATES;
import java.util.Map;
import java.util.Random;

/*
 * Rules of Spreading of Fire:
 * A burning cell turns into an empty cell
 * A tree will burn if at least one neighbor is burning
 * A tree ignites with probability f even if no neighbor is burning
 * An empty space fills with a tree with probability p
 */

/**
 * Create new FireGrid that creates the grid of FireCells that is used for the game
 * Depends on accurate input from the GameController and error-checked configurationMap
 * @author marcusdeans
 */
public class FireGrid extends GameGrid {

  private float myFireProb;
  private float myTreeProb;

  /**
   * Create the new FireGird
   * @param gameGrid array of all of the individual Cells that make up the gird
   * @param type the type of program being created -> necessary due to reflection
   * @param configurationMap of the different parameters used specifically in this game
   */
  public FireGrid(Cell[][] gameGrid, String type, Map<String, String> configurationMap) {
    super(gameGrid, type);
    myFireProb = Float.parseFloat(configurationMap.get("probCatch"));
    myTreeProb = Float.parseFloat(configurationMap.get("fillTree"));
  }

  /**
   * Run the game as Spreading of Fire
   */
  @Override
  public void runGame() {
    computeNeighborsAndRules();
  }

  //fire only looks at 4 neighbors as opposed to 9 -> overwrite GameGrid (superclass) method
  @Override
  protected void computeNeighbours(int cellX, int cellY) {
    this.setCheckingCellNeighbours(new Cell[4]);
    //checkingCellNeighbours = new Cell[4];
    int iterator = 0;
    int[] x = {-1, 1, 0, 0};
    int[] y = {0, 0, 1, -1};
    for (int i = 0; i < x.length; i++) {
      int checkCol = cellX + x[i];
      int checkRow = cellY + y[i];
      if (checkCol < 0 || checkCol >= this.getGameGrid()[0].length || checkRow < 0
          || checkRow >= this.getGameGrid().length) {
        continue;
      }
      this.setOneNeighborValueFromGameGrid(iterator, checkRow, checkCol);
      //checkingCellNeighbours[iterator] = this.getCellArray()[checkRow][checkCol];
      iterator++;
    }
  }

  //convert the integer value of a cell into the appropriate state for ease
  private FIRE_STATES determineCellState(int newValue) {
    switch (newValue) {
      case 0 -> {
        return FIRE_STATES.EMPTY;
      }
      case 1 -> {
        return FIRE_STATES.TREE;
      }
      case 2 -> {
        return FIRE_STATES.FIRE;
      }
    }
    return FIRE_STATES.ERROR;
  }

  //apply the rules of Fire  -> go through neighbours and check which conditions satisfied
  //also check cases for self and store new value for given cell in futureGrid
  protected void applyGameRules(Cell computingCell, int col, int row) {
    int currentCellState = computingCell.getMyCellState();
    int newValue = currentCellState;

    switch (determineCellState(currentCellState)) {
      case EMPTY -> {
        if (new Random().nextFloat() < myTreeProb) {
          newValue = FIRE_STATES.TREE.getValue();
        }
      }
      case FIRE -> {
        newValue = FIRE_STATES.EMPTY.getValue();
      }
      case TREE -> {
        if (new Random().nextFloat() < myFireProb) {
          newValue = FIRE_STATES.FIRE.getValue();
        } else {
          for (Cell neighbouringCell : this.getCheckingCellNeighbours()) {
            if (neighbouringCell != null) {
              if (neighbouringCell.getMyCellState() == FIRE_STATES.FIRE.getValue()) {
                newValue = FIRE_STATES.FIRE.getValue();
                break;
              }
            }
          }
        }
      }
      default -> {
      }
    }
    this.setFutureCellValue(row, col, newValue);
    //futureGrid[row][col].setMyCellState(newValue);
  }
}

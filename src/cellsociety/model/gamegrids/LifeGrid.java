package cellsociety.model.gamegrids;

import cellsociety.model.cells.Cell;
import cellsociety.model.cells.FireCell.FIRE_STATES;
import cellsociety.model.cells.LifeCell.LIFE_STATES;
import java.util.Map;

public class LifeGrid extends GameGrid {

  public LifeGrid(Cell[][] gameGrid, String type, Map<String, String> configurationMap) {
    super(gameGrid, type);
  }

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

    //TODO: he probably wants these to be in resource file to not magic values and are changeable
    //any live cell with two or three live neighbours survives
    if ((liveliness == 1) && (liveCount == 2 || liveCount == 3)) {
      newValue = 1;
    } else if ((liveliness == 0) && (liveCount
        == 3)) { //dead cell with exactly three live neighbours becomes alive
      newValue = 1;
    } else {
      newValue = 0; //all other live cells die, and all other dead cells stay dead
    }
    this.setFutureCellValue(row, col, newValue);
    //futureGrid[row][col].setMyCellState(newValue);
  }

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

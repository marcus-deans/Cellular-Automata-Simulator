package cellsociety.model.gamegrids;

import cellsociety.model.cells.Cell;
import cellsociety.model.cells.FireCell.FIRE_STATES;
import cellsociety.model.cells.PercCell.PERC_STATES;
import java.util.Random;

public class PercGrid extends GameGrid{
  public PercGrid(Cell[][] gameGrid, String type){
    super(gameGrid, type);
  }

  @Override
  public void runGame() {
    computeNeighborsAndRules();
  }

  private PERC_STATES determineCellState(int newValue){
    switch(newValue){
      case 0 -> {
        return PERC_STATES.EMPTY;
      }
      case 1 -> {
        return PERC_STATES.BLOCKED;
      }
      case 2 -> {
        return PERC_STATES.PERCOLATED;
      }
    }
    return PERC_STATES.ERROR;
  }

  //apply the rules of Percolation  -> go through neighbours and check which conditions satisfied
  //also check cases for self
  //store new value for given cell in futureGrid
  protected void applyGameRules(Cell computingCell, int col, int row){
    int currentCellState = computingCell.getMyCellState();
    int newValue = currentCellState;

    switch(determineCellState(currentCellState)){
      case EMPTY -> {
        for(Cell neighbouringCell : checkingCellNeighbours) {
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

    futureGrid[row][col].setMyCellState(newValue);
  }

}

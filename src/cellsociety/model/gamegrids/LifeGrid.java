package cellsociety.model.gamegrids;

import cellsociety.model.cells.Cell;

public class LifeGrid extends GameGrid{
  public LifeGrid(Cell[][] gameGrid){
    super(gameGrid);
  }

  @Override
  public void runGame() {
    computeNeighborsAndRules();
  }

  //apply the rules of the Game of Life -> go through neighbours and check which conditions satisfied
  //store new value for given cell in futureGrid
  protected void applyGameRules(Cell computingCell, int col, int row){
    int newValue = -1;
    int liveliness = computingCell.getMyCellState();
    int liveCount = 0; //alive neighbors
    for(Cell neighbouringCell : checkingCellNeighbours){
      if (neighbouringCell!=null) {
        if (neighbouringCell.getMyCellState() == 1) {
          liveCount++;
        }
      }
    }

    //any live cell with two or three live neighbours survives
    if ((liveliness == 1) && (liveCount == 2 || liveCount == 3)) {
      newValue = 1;
    }
    else if ((liveliness == 0)&&(liveCount == 3)){ //dead cell with exactly three live neighbours becomes alive
      newValue = 1;
    }
    else{
      newValue=0; //all other live cells die, and all other dead cells stay dead
    }

    futureGrid[row][col].setMyCellState(newValue);
  }
}

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

  //iterate through the grid and for each cell: identify neighbours and apply game rules, then replace values
  private void computeNeighborsAndRules(){
    for(int x = 0; x < myGameWidth; x++){
      for(int y = 0; y<myGameHeight; y++){
        computeNeighbours(myGameGrid[x][y]);
        applyGameRules(myGameGrid[x][y], x, y);
      }
    }
    updateCellValues();
  }

  //apply the rules of the Game of Life -> go through neighbours and check which conditions satisfied
  //store new value for given cell in futureGrid
  private void applyGameRules(Cell computingCell, int x, int y){
    int newValue = -1;
    int liveliness = computingCell.getMyCellState();
    int liveCount = 0;
    for(Cell neighbouringCell : checkingCellNeighbours){
      if(neighbouringCell.getMyCellState() == 1){
        liveCount++;
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

    futureGrid[x][y].setMyCellState(newValue);
  }
}

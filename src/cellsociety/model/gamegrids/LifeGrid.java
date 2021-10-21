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
    // TODO: x and y are backwards here
    for(int col = 0; col < myGameWidth; col++){
      for(int row = 0; row<myGameHeight; row++){
        computeNeighbours(col, row);
        //applyGameRules(myGameGrid[x][y], x, y);
        applyGameRules(myGameGrid[row][col], col, row);
        sendViewUpdate("Row", row-1, row);
        sendViewUpdate("Column", col-1, col);
        //sendViewUpdate("State", myGameGrid[x][y].getMyCellState(), futureGrid[x][y].getMyCellState());
        sendViewUpdate("State", -1, futureGrid[row][col].getMyCellState());
        //boolean helper = (myGameGrid[x][y] == futureGrid[x][y]);
        //the arrays are pointing at each other-> false in the beginning then true
        boolean helper = (myGameGrid[row][col].getMyCellState() == futureGrid[row][col].getMyCellState());
        System.out.println(helper);
      }
    }
    updateCellValues();
  }

  //apply the rules of the Game of Life -> go through neighbours and check which conditions satisfied
  //store new value for given cell in futureGrid
  private void applyGameRules(Cell computingCell, int col, int row){
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

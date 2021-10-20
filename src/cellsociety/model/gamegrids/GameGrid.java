package cellsociety.model.gamegrids;

import cellsociety.model.cells.Cell;

public abstract class GameGrid {
  //TODO make these private
    Cell[][] myGameGrid;
    int myGameWidth;
    int myGameHeight;
    Cell[] checkingCellNeighbours;
    Cell[][] futureGrid;
    private int myNewValue;

    public GameGrid(Cell[][] gameGrid){
      myGameGrid = gameGrid;
      myGameWidth = gameGrid.length;
      myGameHeight = gameGrid[0].length;
      myNewValue = 0;
      futureGrid=gameGrid;
    }
    public void runGame(){};

    protected void updateCellValues(){
      myGameGrid = futureGrid;
    }

    //populates Cell[] of the possible neighbours of given cell (max 9)
    protected void computeNeighbours(int cellX, int cellY){
      checkingCellNeighbours=new Cell[9];
      //not changing for some reason
      int iterator = 0;
      for(int x = -1; x < 2; x++){
        int checkX=cellX+x;
        //int checkX = checkingCell.getMyX() + x;
        if(checkX < 0 || checkX >= myGameWidth){
          continue;
        }
        for(int y = -1; y<2; y++){
          int checkY=cellY+y;
          //int checkY = checkingCell.getMyY() + y;
          if(checkY < 0 || checkY >= myGameHeight || (x==0&&y==0)){
            continue;
          }
          checkingCellNeighbours[iterator] = myGameGrid[checkX][checkY]; //out of bounds now?
          iterator++;
        }
      }
    }

    private int newValue(){
      myNewValue = 0;
      return myNewValue;
    }
    public Cell[][] getCellArray() {
      return myGameGrid;
    }
}

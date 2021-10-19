package cellsociety.model.gamegrids;

import cellsociety.model.cells.Cell;

public abstract class GameGrid {
    Cell[][] myGameGrid;
    int myGameWidth;
    int myGameHeight;
    Cell[] checkingCellNeighbours;
    Cell[][] futureGrid;
    int myNewValue;

    public GameGrid(Cell[][] gameGrid){
      myGameGrid = gameGrid;
      myGameWidth = gameGrid.length;
      myGameHeight = gameGrid[0].length;
      myNewValue = 0;
    }

    protected void updateCellValues(){
      myGameGrid = futureGrid;
    }

    //populates Cell[] of the possible neighbours of given cell (max 9)
    protected void computeNeighbours(Cell checkingCell){
      checkingCellNeighbours = new Cell[9];
      int iterator = 0;
      for(int x = -1; x < 2; x++){
        int checkX = checkingCell.getMyX() + x;
        if(checkX < 0 || checkX > myGameWidth){
          continue;
        }
        for(int y = -1; y<2; y++){
          int checkY = checkingCell.getMyY() + y;
          if(checkY < 0 || checkY > myGameHeight){
            continue;
          }
          checkingCellNeighbours[iterator] = myGameGrid[checkX][checkY];
          iterator++;
        }
      }
    }

    private int newValue(){
      myNewValue = 0;
      return myNewValue;
    }
}

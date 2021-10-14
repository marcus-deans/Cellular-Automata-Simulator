package cellsociety.model.gamegrids;

import cellsociety.model.cells.Cell;

public abstract class GameGrid {
    Cell[][] myGameGrid;
    int myNewValue;

    public GameGrid(Cell[][] gameGrid){
      myGameGrid = gameGrid;
      myNewValue = 0;
    }

    protected void updateCellValues(Cell[][] gameGrid){
    }

    private void updateGame(){

    }

    protected void computeNeighbours(Cell checkingCell){

    }

    private int newValue(){
      myNewValue = 0;
      return myNewValue;
    }
}

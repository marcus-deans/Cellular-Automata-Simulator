package cellsociety.model.cells;

public abstract class Cell {
  int myCellState;
  int myX;
  int myY;

  public Cell(int cellState) {
    myCellState = cellState;
  }

  public int getMyY() {
    return myY;
  }

  public int getMyX() {
    return myX;
  }
}

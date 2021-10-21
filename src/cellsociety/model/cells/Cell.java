package cellsociety.model.cells;

public abstract class Cell {
  int myCellState;
  int myX;
  int myY;

  public Cell(int cellState) {
    myCellState = cellState;
  }

  public Cell (Cell copy) {
    myCellState=copy.getMyCellState();
  }

  public int getMyY() {
    return myY;
  }

  public int getMyX() {
    return myX;
  }

  public int getMyCellState() {
    return myCellState;
  }

  public void setMyCellState(int myCellState) {
    this.myCellState = myCellState;
  }
}

package cellsociety.model.edgePolicy;

public abstract class Edge {
  //contructor with boundaries and current row and column?
  private int height;
  private int width;
  private int row;
  private int col;

  public Edge(int row, int col, int height, int width) {
    this.row=row;
    this.col=col;
    this.height=height;
    this.width=width;
  }
  public int[] validateCoordinates(int checkRow, int checkCol) {
    if (checkRow==row && col==checkCol) {
      return null;
    }
    else if (checkRow<0 || checkCol<0 || checkRow>=height || checkCol>=width) {
      int[] ret=checkBoundary(checkRow, checkCol);
      return ret;
    }
    else {
      return new int[] {checkRow, checkCol};
    }
  }
  protected int getHeight() {
    return height;
  }
  protected int getWidth() {
    return width;
  }
  public abstract int[] checkBoundary(int row, int col);

}

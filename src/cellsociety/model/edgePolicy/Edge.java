package cellsociety.model.edgePolicy;


/**
 *Class to represent the Edge Policy of a Simulation
 * @author morganfeist
 */
public abstract class Edge {
  private int height;
  private int width;
  private int row;
  private int col;

  /**
   * Edge constructor to set values based on current position and grid values
   * @param row current row to find neighbors for
   * @param col current column to find neighbors for
   * @param height total grid height
   * @param width total grid width
   */
  public Edge(int row, int col, int height, int width) {
    this.row=row;
    this.col=col;
    this.height=height;
    this.width=width;
  }

  /**
   * Used to find neighbor coordinates based on the edge type
   * @param checkRow the row to verify if it is a neighbor within the
   * @param checkCol the col to verify if it is a neighbor within bounds
   * @return coordinates in the form of {row, col} of where the neighbor is or null if there is none
   */
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

  /**
   * Determine what to do with coordinates on the boundary of the grid
   * @param row current row in possible neighbor
   * @param col current col in possible neighbor
   * @return coordinates in form of {row, col} at a neighbor boundary or null if there should be none
   */
  public abstract int[] checkBoundary(int row, int col);

}

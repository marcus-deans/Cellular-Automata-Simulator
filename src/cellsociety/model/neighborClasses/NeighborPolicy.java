package cellsociety.model.neighborClasses;
import cellsociety.model.edgePolicy.Edge;
import cellsociety.model.shapes.Shape;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Neighbor Policy for a simulation
 * @author morganfeist
 * Depends on edge policy and chosen shape
 */
public abstract class NeighborPolicy {
  Edge edgePolicy;
  Shape shape;
  public NeighborPolicy(Shape s, Edge e) {
    edgePolicy=e;
    shape=s;
  }
  protected abstract int[][] computeNeighborCoordinates(int row, int col);

  /**
   * Determines all the neighbor coordinates based on the edge rules, chosen neighbor class and shape
   * @param row the current row in the cell array to find its neighbors
   * @param col the current column in the cell array to find its neighbors
   * @return 2d int array of coordinates in form {row, col}
   */
  public int[][] determineCoordinates(int row, int col) {
    int[][] possibleNeighbors=computeNeighborCoordinates(row, col);
    List<int[]> ret=new ArrayList<>();
    for (int[] possibleNeighbor : possibleNeighbors) {

      int[] check = edgePolicy.validateCoordinates(possibleNeighbor[0]+row, possibleNeighbor[1]+col);
      if (check != null) {
        ret.add(check);
      }
    }
    int[][] finalCoordinates=new int[ret.size()][2];
    for (int coord=0; coord<ret.size(); coord++) {
      finalCoordinates[coord]=ret.get(coord);
    }
    return finalCoordinates;
  }

  protected Shape getShape() {
    return shape;
  }

  protected int[][] append(int[][] a, int[][] b) {
    int[][] result = new int[a.length + b.length][];
    System.arraycopy(a, 0, result, 0, a.length);
    System.arraycopy(b, 0, result, a.length, b.length);
    return result;
  }
}

package cellsociety.model.neighborClasses;
import cellsociety.model.shapes.Shape;
import cellsociety.model.edgePolicy.Edge;

/**
 * Class to find all the possible neighbors of a shape
 * @author morganfeist
 */
public class CompleteNeighbors extends NeighborPolicy {
  public CompleteNeighbors(Shape s, Edge e) {
    super(s, e);
  }

  @Override
  public int[][] computeNeighborCoordinates(int row, int col) {
    int[][] coords1 = this.getShape().determineEdges();
    int[][] coords2 = this.getShape().determineOtherNeighbors();
    int[][] coords = append(coords1, coords2);
    return coords;
  }

}

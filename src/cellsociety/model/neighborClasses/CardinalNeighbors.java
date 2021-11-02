package cellsociety.model.neighborClasses;
import cellsociety.model.edgePolicy.Edge;
import cellsociety.model.shapes.Shape;

/**
 * Represents neighbors only at the edges of a shape
 * @author morganfeist
 */
public class CardinalNeighbors extends NeighborPolicy {
  public CardinalNeighbors(Shape s, Edge e) {
    super(s, e);
  }


  @Override
  public int[][] computeNeighborCoordinates(int row, int col) {
    int[][] coords = this.getShape().determineEdges();
    return coords;
  }

}

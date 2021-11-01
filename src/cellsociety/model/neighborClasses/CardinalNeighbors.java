package cellsociety.model.neighborClasses;
import cellsociety.model.edgePolicy.Edge;
import cellsociety.model.shapes.Shape;

public class CardinalNeighbors extends NeighborPolicy {
  public CardinalNeighbors(Shape s, Edge e) {
    super(s, e);
  }

  //compute neighbors should probably just return an int array of neighbors coordinates?
  //main function loops through and makes cells?
  @Override
  public int[][] computeNeighborCoordinates(int row, int col) {
    int[][] coords = this.getShape().determineEdges();
    return coords;
  }

}

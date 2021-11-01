package cellsociety.model.edgePolicy;

public class FiniteEdge extends Edge {
  public FiniteEdge(int row, int col, int height, int width) {
    super(row, col, height, width);
  }
  @Override
  public int[] checkBoundary(int row, int col) {
    return null;
  }
}

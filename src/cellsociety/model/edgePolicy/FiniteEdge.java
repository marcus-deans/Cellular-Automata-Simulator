package cellsociety.model.edgePolicy;

public class FiniteEdge extends Edge {
  public FiniteEdge(int row, int col, int height, int width) {
    super(row, col, height, width);
  }

  /**
   * There are no neighbors outside of the boundary
   * @param row current row in possible neighbor
   * @param col current col in possible neighbor
   * @return null
   */
  @Override
  public int[] checkBoundary(int row, int col) {
    return null;
  }
}

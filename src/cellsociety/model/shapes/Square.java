package cellsociety.model.shapes;

public class Square implements Shape {
  public Square() {

  }

  @Override
  public int[][] determineEdges() {
    return new int[][]{{-1,0},{1,0},{0,1},{0,-1}};
  }

  @Override
  public int[][] determineOtherNeighbors() {
    return new int[][]{{-1,-1}, {-1,1}, {1,-1}, {1,1}};
  }
}

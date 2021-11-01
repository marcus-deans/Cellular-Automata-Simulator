package cellsociety.model.shapes;

public class Hexagon implements Shape{
  public Hexagon() {}

  public int[][] determineEdges() {
    //assume hexagon forms downward zigzag
    return new int[][] {{1,0}, {-1,0}, {0,1}, {0,-1}, {-1,-1}, {1,-1}};
  }
  public int[][] determineOtherNeighbors() {return new int[][]{};}


}

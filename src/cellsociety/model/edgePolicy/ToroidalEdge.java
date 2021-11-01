package cellsociety.model.edgePolicy;

public class ToroidalEdge extends Edge {
  public ToroidalEdge(int row, int col, int height, int width) {
    super(row, col, height, width);
  }
  @Override
  public int[] checkBoundary(int row, int col) {
    int height=this.getHeight();
    int width=this.getWidth();
    int[] coord= {row, col};
    if (row<0) {
      coord[0]=height-1;
    }
    else if (row==height) {
      coord[0]=0;
    }
    if (col<0) {
      coord[1]=width-1;
    }
    else if (col==width) {
      coord[1]=0;
    }
    return coord;
  }
}

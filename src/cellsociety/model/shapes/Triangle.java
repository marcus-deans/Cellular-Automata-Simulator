package cellsociety.model.shapes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Triangle implements Shape {
  boolean facingUp;
  public Triangle() {
    facingUp=true;
  }

  @Override
  //TODO: verify this array concatenation works
  public int[][] determineEdges() {
    int[][] edges={{0,1},{0,-1}};
    int[] possibleEdges;
    if (!facingUp) {
      possibleEdges=new int[]{1,0};
    }
    else {
      possibleEdges=new int[]{-1,0};
    }
    int[][] result = new int[edges.length+1][];
    System.arraycopy(edges, 0, result, 0, edges.length);
    System.arraycopy(possibleEdges, 0, result, edges.length, 1);
    facingUp=!facingUp; //assumes that cells are travelled in order
    return result;
  }

  @Override
  public int[][] determineOtherNeighbors() {
    int[][] notEdges={{}};
    return notEdges;
  }
  //somehow needs to take into account triangle direction

}

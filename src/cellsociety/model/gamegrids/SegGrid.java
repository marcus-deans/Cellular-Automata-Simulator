package cellsociety.model.gamegrids;

import cellsociety.model.cells.Cell;
import cellsociety.model.cells.SegCell.SEG_STATES;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class SegGrid extends GameGrid {

  private float mySimilarProportion;
  private ArrayList<int[]> emptyCells;
  private ArrayList<int[]> unEmptyCells;

  public SegGrid(Cell[][] gameGrid, String type, Map<String, String> configurationMap) {
    super(gameGrid, type);
    mySimilarProportion = Float.parseFloat(configurationMap.get("similarProportion"));
  }

  /*
  Agents are split into two groups and occupy the spaces of the grid and only one agent
  can occupy a space at a time. Agents desire a fraction B_a of their neighborhood
  (in this case defined to be the eight adjacent agents around them) to be from the same group.
  Increasing B_a corresponds to increasing the agent's intolerance of outsiders.

  Each round consists of agents checking their neighborhood to see if the fraction of neighbors
  B that matches their group?ignoring empty spaces?is greater than or equal B_a.
  If B < B_a then the agent will choose to relocate to a vacant spot where B ? B
  This continues until every agent is satisfied. Every agent is not guaranteed to be satisfied and
  in these cases it is of interest to study the patterns (if any) of the agent dynamics.
   */


  //apply the rules of Schelling's Segregation -> go through neighbours and check which conditions satisfied
  //store new value for given cell in futureGrid
  protected void applyGameRules(Cell computingCell, int col, int row) {
    int newValue = -1;
    int[] coord={row, col};
    int computingCellState = computingCell.getMyCellState();
    int similarCount = 0; //similar neighbors
    int neighbourCount = 0; //extant neighbours
    //accounts for case where cell was previously empty but now filled
    if (computingCellState==0 && unEmptyCells.contains(coord)) {
      return;
    }
    for (Cell neighbouringCell : checkingCellNeighbours) {
      if (neighbouringCell != null) {
        neighbourCount ++;
        if (neighbouringCell.getMyCellState() == computingCellState) {
          similarCount++;
        }
      }
    }
    //TODO: probably make one pass and determine which cells stay in position
    if(similarCount/neighbourCount < mySimilarProportion){
      int[] loc=findNewLocation();
      futureGrid[loc[0]][loc[1]].setMyCellState(newValue);
      newValue=0;
    }
    else {
      newValue=computingCellState;
    }
    futureGrid[row][col].setMyCellState(newValue);
  }
  //this needs to be called in gamegrid method or something because we don't want it called every time
  private void findEmptyCells() {
    Cell[][] currentArray=this.getCellArray();
    for (int row=0; row<currentArray.length; row++) {
      for (int col=0; col<currentArray[0].length; col++) {
        if (currentArray[row][col].getMyCellState()==0) {
          emptyCells.add(new int[]{row, col});
        }
      }
    }
  }


  private int[] findNewLocation(){
    Random r = new Random();
    int index=r.nextInt(emptyCells.size());
    int[] coord=emptyCells.get(index);
    emptyCells.remove(index);
    unEmptyCells.add(coord);
    //TODO: determine how to find new location to move to
    //compile list of all open spots in future grid (first pass means stationary cells already in that grid)
    //then select a random spot and set hold of that spot in futureGrid
    return coord;
  }

  private SEG_STATES determineCellState(int newValue) {
    switch (newValue) {
      case 0 -> {
        return SEG_STATES.EMPTY;
      }
      case 1 -> {
        return SEG_STATES.ALPHA;
      }
      case 2 -> {
        return SEG_STATES.BETA;
      }
    }
    return SEG_STATES.ERROR;
  }
}
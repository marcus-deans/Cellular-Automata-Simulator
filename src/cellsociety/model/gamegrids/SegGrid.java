package cellsociety.model.gamegrids;

import cellsociety.model.cells.Cell;
import cellsociety.model.cells.SegCell.SEG_STATES;

public class SegGrid extends GameGrid {

  private int mySimilarProportion;

  public SegGrid(Cell[][] gameGrid, int similarProportion) {
    super(gameGrid);
    mySimilarProportion = similarProportion;
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
    int computingCellState = computingCell.getMyCellState();
    int similarCount = 0; //similar neighbors
    int neighbourCount = 0; //extant neighbours
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
      findNewLocation();
    }
    futureGrid[row][col].setMyCellState(newValue);
  }

  private void findNewLocation(){
    //TODO: determine how to find new location to move to
    //compile list of all open spots in future grid (first pass means stationary cells already in that grid)
    //then select a random spot and set hold of that spot in futureGrid
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
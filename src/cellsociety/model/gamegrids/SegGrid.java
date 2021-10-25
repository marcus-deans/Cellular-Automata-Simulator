package cellsociety.model.gamegrids;

import cellsociety.model.cells.Cell;
import cellsociety.model.cells.SegCell.SEG_STATES;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Create new Seggrid that creates the grid of SegCells that is used for the game
 * Depends on accurate input from the GameController and error-checked configurationMap
 * @author morganfeist, marcusdeans
 */
public class SegGrid extends GameGrid {

  private float mySimilarProportion;
  private ArrayList<int[]> emptyCells;
  private ArrayList<int[]> unEmptyCells;

  /**
   * Create the new SegGrid
   * @param gameGrid array of all of the individual Cells that make up the gird
   * @param type the type of program being created -> necessary due to reflection
   * @param configurationMap of the different parameters used specifically in this game
   */
  public SegGrid(Cell[][] gameGrid, String type, Map<String, String> configurationMap) {
    super(gameGrid, type);
    mySimilarProportion = Float.parseFloat(configurationMap.get("similarProportion"));
    emptyCells = new ArrayList<>();
    unEmptyCells = new ArrayList<>();
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


  /**
   * Run the game as Schelling's Segregation
   */
  @Override
  public void runGame() {
    findEmptyCells();
    computeNeighborsAndRules();
  }

  //apply the rules of Schelling's Segregation -> go through neighbours and check which conditions satisfied
  //store new value for given cell in futureGrid
  protected void applyGameRules(Cell computingCell, int col, int row) {
    int newValue = -1;
    int[] coord = {row, col};
    int computingCellState = computingCell.getMyCellState();
    int similarCount = 0; //similar neighbors
    int neighbourCount = 0; //extant neighbours
    //accounts for case where cell was previously empty but now filled
    if (computingCellState == 0 && containsArray(unEmptyCells, coord)) {
      return;
    }
    if (computingCellState==0) {
      setFutureCellValue(row, col, 0);
      //futureGrid[row][col].setMyCellState(0);
      return;
    }
    for (Cell neighbouringCell : this.getCheckingCellNeighbours()) {
      if (neighbouringCell != null && neighbouringCell.getMyCellState()!=0) {
        neighbourCount++;
        if (neighbouringCell.getMyCellState() == computingCellState) {
          similarCount++;
        }
      }
    }
    //TODO: probably make one pass and determine which cells stay in position
    float proportion=(float)similarCount/neighbourCount;
    newValue = determineIfCellLeaves(newValue, computingCellState, proportion);
    this.setFutureCellValue(row, col, newValue);
    //futureGrid[row][col].setMyCellState(newValue);
  }

  private int determineIfCellLeaves(int newValue, int computingCellState, float proportion) {
    if (proportion < mySimilarProportion) {
      //this is always 0
      try {
        int[] loc = findNewLocation();
        this.setFutureCellValue(loc[0], loc[1], computingCellState);
        //futureGrid[loc[0]][loc[1]].setMyCellState(computingCellState);
        newValue = 0;
      }
      catch(Exception e) {
        e.printStackTrace();
      }
    } else {
      newValue = computingCellState;
    }
    return newValue;
  }

  private boolean containsArray(List<int[]> l, int[] compare) {
    for (int[] coordinates: l) {
      if (Arrays.equals(coordinates, compare)) {
        return true;
      }
    }
    return false;
  }
  //this needs to be called in gamegrid method or something because we don't want it called every time
  private void findEmptyCells() {
    emptyCells.clear();
    unEmptyCells.clear();
    Cell[][] currentArray = this.getGameGrid();
    for (int row = 0; row < currentArray.length; row++) {
      for (int col = 0; col < currentArray[0].length; col++) {
        if (currentArray[row][col].getMyCellState() == 0) {
          emptyCells.add(new int[]{row, col});
        }
      }
    }
  }

  //find a new location that the current cell should move to
  private int[] findNewLocation() throws Exception {
    Random r = new Random();
    if (emptyCells.size()<=0) {
      throw new Exception("need more empty cells in simulation");
      //we're always hitting this idk why
    }
    int index = r.nextInt(emptyCells.size());
    int[] coord = emptyCells.get(index);
    emptyCells.remove(index);
    unEmptyCells.add(coord);
    //TODO: determine how to find new location to move to
    //compile list of all open spots in future grid (first pass means stationary cells already in that grid)
    //then select a random spot and set hold of that spot in futureGrid
    return coord;
  }

  //convert the integer value of a cell into the appropriate state for ease
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
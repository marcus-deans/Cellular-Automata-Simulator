package cellsociety.model.gamegrids;

import cellsociety.model.cells.Cell;
import cellsociety.model.cells.WatorCell;
import cellsociety.model.cells.WatorCell.WATOR_STATES;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Create new WatorGrid that creates the grid of WatorCells that is used for the game
 * Depends on accurate input from the GameController and error-checked configurationMap
 * @author marcusdeans
 */
public class WatorGrid extends GameGrid {
  private int myFishLifespanThreshold;
  private int mySharkLifespanThreshold;
  private int mySharkEnergyThreshold;
  private ArrayList<int[]> emptyCells;
  private ArrayList<int[]> unEmptyCells;
  private ArrayList<int[]> modifiedCells;
  private int myFishEnergyValue;

  /**
   * Create the new WatorGrid
   * @param gameGrid array of all of the individual Cells that make up the gird
   * @param type the type of program being created -> necessary due to reflection
   * @param configurationMap of the different parameters used specifically in this game
   */
  public WatorGrid(Cell[][] gameGrid, String type, Map<String, String> configurationMap) {
    super(gameGrid, type);
    myFishLifespanThreshold = Integer.parseInt(configurationMap.get("fish_lifespan"));
    mySharkLifespanThreshold = Integer.parseInt(configurationMap.get("shark_lifespan"));
    mySharkEnergyThreshold = Integer.parseInt(configurationMap.get("shark_energy"));
    myFishEnergyValue=2;
    emptyCells = new ArrayList<>();
    unEmptyCells = new ArrayList<>();
    modifiedCells=new ArrayList<>();
  }

  /*
      At each chronon, a fish moves randomly to one of the adjacent unoccupied squares. If there
      are no free squares, no movement takes place.
    Once a fish has survived a certain number of chronons it may reproduce. This is done as it moves
    to a neighbouring square, leaving behind a new fish in its old position. Its reproduction time
    is also reset to zero.
   */


  /*
   At each chronon, a shark moves randomly to an adjacent square occupied by a fish. If there is
   none, the shark moves to a random adjacent unoccupied square. If there are no free squares,
   no movement takes place.
    At each chronon, each shark is deprived of a unit of energy.
    Upon reaching zero energy, a shark dies.
    If a shark moves to a square occupied by a fish, it eats the fish and earns a certain amount of energy.
    Once a shark has survived a certain number of chronons it may reproduce in exactly the same way as the fish.
   */

  /**
   * Run the game as Wa-Tor World
   */
  @Override
  public void runGame() {
//    findEmptyCells();
    modifiedCells.clear();
    computeNeighborsAndRules();
  }

  //apply the rules of Wa-Tor World -> go through neighbours and check which conditions satisfied
  //store new value for given cell in futureGrid
  protected void applyGameRules(Cell computingCell, int col, int row) {
    int newValue = -1;
    int[] coord = {row, col};
    if (containsArray(modifiedCells, coord)) {
      return;
    }
    int computingCellState = computingCell.getMyCellState();
    computingCell.incrementLifespan();
    int similarCount = 0; //similar neighbors
    int neighbourCount = 0; //extant neighbours

    if(computingCellState == WATOR_STATES.FISH.getValue()){
      //TODO: do fish things, determine if empty neighbour then move; if not then stay
      fishNewValue(computingCell);
    }
    else if(computingCellState == WATOR_STATES.SHARK.getValue()){
      computingCell.incrementEnergy();
      sharkNewValue(computingCell);
      //TODO: do shark things
    }
//    futureGrid[row][col].setMyCellState(newValue); //performed inside calculations
  }

  //check all of the neighbouring cells to see which are empty, then select one randomly
  private Cell selectRandomEmpty(Cell checkCell){
    ArrayList<Cell> emptyCellOptions = new ArrayList<>(); //alive neighbors
    for (Cell neighbouringCell : this.getCheckingCellNeighbours()) {
      if (neighbouringCell != null) {
        if (containsArray(modifiedCells, new int[]{neighbouringCell.getMyY(), neighbouringCell.getMyX()})) {
          continue;
        }
        if (neighbouringCell.getMyCellState() == WATOR_STATES.WATER.getValue()) {
          emptyCellOptions.add(neighbouringCell);
        }
      }
    }
    if(emptyCellOptions.size() != 0){
      int emptyCellIndex = (int)(Math.random() * emptyCellOptions.size());
      return emptyCellOptions.get(emptyCellIndex);
    }
    return new WatorCell(WATOR_STATES.ERROR.getValue());
  }

  //determine the new value of the fish based on whether it moves or not
  private void fishNewValue(Cell checkCell){
    //TODO: check if fish's lifespan exceeds threshold -> then reproduce
    Cell newCellLocation = selectRandomEmpty(checkCell);
    if(newCellLocation.getMyCellState() != WATOR_STATES.ERROR.getValue()){
      setFutureLocation(newCellLocation, WATOR_STATES.FISH.getValue());
      setFutureLocation(checkCell, WATOR_STATES.WATER.getValue());
      modifiedCells.add(new int[] {newCellLocation.getMyY(), newCellLocation.getMyX()});
    }
    else{ //no neighbours were empty
      setFutureLocation(checkCell, WATOR_STATES.FISH.getValue());
    }
  }

  //determine if there is food (that is, a fish) in a neighbouring cell
  private Cell determineNearbyFood(Cell checkCell){
    ArrayList<Cell> confirmedFoodOptions = new ArrayList<>(); //alive neighbors
    for (Cell neighbouringCell : this.getCheckingCellNeighbours()) {
      if (neighbouringCell != null) {
        if (containsArray(modifiedCells, new int[]{neighbouringCell.getMyY(), neighbouringCell.getMyX()})) {
          continue;
        }
        if (neighbouringCell.getMyCellState() == WATOR_STATES.FISH.getValue()) {
          confirmedFoodOptions.add(neighbouringCell);
        }
      }
    }
    if(confirmedFoodOptions.size() != 0){
      int emptyCellIndex = (int)(Math.random() * confirmedFoodOptions.size());
      return confirmedFoodOptions.get(emptyCellIndex);
    }
    return new WatorCell(WATOR_STATES.ERROR.getValue());
  }

  //determine the new value of the shark's cells depending on which conditions satisfied
  private void sharkNewValue(Cell checkCell){
    //TODO: check if shark's lifespan exceeds threshold -> then reproduce
    //TODO: check if shark's energy exceeds threshold -> then dies
    Cell newEmptyLocation = selectRandomEmpty(checkCell);
    Cell newPossibleFoodLocation = determineNearbyFood(checkCell);
    if(newPossibleFoodLocation.getMyCellState() != WATOR_STATES.ERROR.getValue()){ //there is something edible in neighbours
      setFutureLocation(newPossibleFoodLocation, WATOR_STATES.SHARK.getValue());
      setFutureLocation(checkCell, WATOR_STATES.WATER.getValue());
      modifiedCells.add(new int[] {newPossibleFoodLocation.getMyY(), newPossibleFoodLocation.getMyX()});
      //TODO: set the eaten fish's cell to water (it has been eaten)
    }
    else if(newEmptyLocation.getMyCellState() != WATOR_STATES.ERROR.getValue()){ //check for nearby empty and go there
      setFutureLocation(newEmptyLocation, WATOR_STATES.SHARK.getValue());
      modifiedCells.add(new int[] {newEmptyLocation.getMyY(), newEmptyLocation.getMyX()});
      setFutureLocation(checkCell, WATOR_STATES.WATER.getValue());
    }
    else{ //no neighbours were empty AND nothing edible nearby -> stay in place
      setFutureLocation(checkCell, WATOR_STATES.SHARK.getValue());
    }
  }
  //set the determined future location within futuregrid
  private void setFutureLocation(Cell setCell, int newCellState){
    //TODO verify that x and y are right
    //we really want a cell setter method not a value setter method
    this.setFutureCellValue(setCell.getMyY(), setCell.getMyX(), newCellState);
    //futureGrid[setCell.getMyX()][setCell.getMyY()].setMyCellState(newCellState);
  }


  //convert the integer value of a cell into the appropriate state for ease
  private WATOR_STATES determineCellState(int newValue) {
    switch (newValue) {
      case 0 -> {
        return WATOR_STATES.WATER;
      }
      case 1 -> {
        return WATOR_STATES.FISH;
      }
      case 2 -> {
        return WATOR_STATES.SHARK;
      }
    }
    return WATOR_STATES.ERROR;
  }

  private boolean containsArray(List<int[]> l, int[] compare) {
    for (int[] coordinates: l) {
      if (Arrays.equals(coordinates, compare)) {
        return true;
      }
    }
    return false;
  }
}

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
public class WatorGrid2 extends GameGrid {
  private int myFishLifespanThreshold;
  private int mySharkLifespanThreshold;
  private int mySharkEnergyThreshold;
  private int myFishEnergyValue;
  private ArrayList<int[]> emptyCells;
  private ArrayList<int[]> unEmptyCells;
  private ArrayList<int[]> modifiedCells;
  private int[][] currentNeighborCoords;

  /**
   * Create the new WatorGrid
   * @param gameGrid array of all of the individual Cells that make up the gird
   * @param type the type of program being created -> necessary due to reflection
   * @param configurationMap of the different parameters used specifically in this game
   */
  public WatorGrid2(Cell[][] gameGrid, String type, Map<String, String> configurationMap) {
    super(gameGrid, type);
    try {
      myFishLifespanThreshold = Integer.parseInt(configurationMap.get("fish_lifespan"));
      mySharkLifespanThreshold = Integer.parseInt(configurationMap.get("shark_lifespan"));
      mySharkEnergyThreshold = Integer.parseInt(configurationMap.get("shark_energy"));
      myFishEnergyValue=2;
      //also need energy per fish parameter
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    emptyCells = new ArrayList<>();
    unEmptyCells = new ArrayList<>();
    modifiedCells= new ArrayList<>();
    currentNeighborCoords=new int[8][2];
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
    clearArrays();
    computeNeighborsAndRules();
  }
  private void clearArrays() {
    modifiedCells.clear();
  }

  //computes neighbors toroidal and 8 neighbors
  @Override
  protected void computeNeighbours(int cellX, int cellY) {
    int iterator = 0;
    setCheckingCellNeighbours(new Cell[8]);
    currentNeighborCoords=new int[8][2];
    for (int x = -1; x < 2; x++) {
      int checkCol = cellX + x;
      checkCol = checkColOutsideBoundary(checkCol);
      for (int y = -1; y < 2; y++) {
        int checkRow = cellY + y;
        if (x==0 && y==0) {
          continue;
        }
        checkRow = checkRowOutsideBoundary(checkRow);
        setOneNeighborValueFromGameGrid(iterator, checkRow, checkCol);
        currentNeighborCoords[iterator]=new int[]{checkRow, checkCol};
        iterator++;
      }
    }
  }

  private int checkColOutsideBoundary(int checkCol) {
    if (checkCol <0) {
      checkCol =this.getGameGrid()[0].length-1;
    }
    if (checkCol >= this.getGameGrid()[0].length) {
      checkCol =0;
    }
    return checkCol;
  }

  private int checkRowOutsideBoundary(int checkRow) {
    if (checkRow <0) {
      checkRow =this.getGameGrid().length-1;
    }
    if (checkRow >=this.getGameGrid().length) {
      checkRow =0;
    }
    return checkRow;
  }

  //apply the rules of Wa-Tor World -> go through neighbours and check which conditions satisfied
  //store new value for given cell in futureGrid
  protected void applyGameRules(Cell computingCell, int col, int row) {
    int newValue = -1;
    int[] coord = {row, col};
    int computingCellState = computingCell.getMyCellState();
    computingCell.incrementLifespan();
    int similarCount = 0; //similar neighbors
    int neighbourCount = 0; //extant neighbours
    if (containsArray(modifiedCells, coord)) {
      System.out.println("skip");
      return;
    }
    if(computingCellState == WATOR_STATES.FISH.getValue()){
      //TODO: do fish things, determine if empty neighbour then move; if not then stay
      //fishNewValue(computingCell);
      fishNewValue(row, col);
    }
    else if(computingCellState == WATOR_STATES.SHARK.getValue()){
      computingCell.incrementEnergy();
      //sharkNewValue(computingCell);
      sharkNewValue(row, col);
      //TODO: do shark things
    }
    else {
      this.setFutureCellValue(row, col, WATOR_STATES.WATER.getValue());
    }
//    futureGrid[row][col].setMyCellState(newValue); //performed inside calculations
  }

  //check all of the neighbouring cells to see which are empty, then select one randomly
  //private int[] selectRandomEmpty(Cell checkCell){
  private int[] selectRandomEmpty(){
    //ArrayList<Cell> emptyCellOptions = new ArrayList<>(); //alive neighbors
    ArrayList<int[]> emptyCellCoords= new ArrayList<>();
    Cell[] checkingCellNeighbors=this.getCheckingCellNeighbours();
    for (int iterator=0; iterator<this.getCheckingCellNeighbours().length; iterator++) {
      int value=checkingCellNeighbors[iterator].getMyCellState();
      int[] coords= currentNeighborCoords[iterator];
      if (containsArray(modifiedCells, coords)) {
        continue;
      }
      if (value==WATOR_STATES.WATER.getValue()) {
        emptyCellCoords.add(coords);
      }
    }
//    for (Cell neighbouringCell : this.getCheckingCellNeighbours()) {
//      if (neighbouringCell != null) {
//        if (neighbouringCell.getMyCellState() == WATOR_STATES.WATER.getValue()) {
//          emptyCellOptions.add(neighbouringCell);
//        }
//      }
//    }
    if(emptyCellCoords.size() != 0){
      int emptyCellIndex = (int)(Math.random() * emptyCellCoords.size());
      //return emptyCellOptions.get(emptyCellIndex);
      return emptyCellCoords.get(emptyCellIndex);
    }
    return null;
    //return new WatorCell(WATOR_STATES.ERROR.getValue());
  }

  //determine the new value of the fish based on whether it moves or not
  private void fishNewValue(int row, int col){
    //TODO: check if fish's lifespan exceeds threshold -> then reproduce
    //Cell newCellLocation = selectRandomEmpty(checkCell);
    int[] newCellLocation=selectRandomEmpty();
    if (newCellLocation!=null) {
      setFutureCellValue(newCellLocation[0], newCellLocation[1], WATOR_STATES.FISH.getValue());
      //reproduce here:
      //issue: lifespan numbers don't move with fish so nothing works
      setFutureCellValue(row, col, WATOR_STATES.WATER.getValue());
      modifiedCells.add(newCellLocation);
    }
//    if(newCellLocation.getMyCellState() != WATOR_STATES.ERROR.getValue()){
//      setFutureLocation(newCellLocation, WATOR_STATES.FISH.getValue());
//      setFutureLocation(checkCell, WATOR_STATES.WATER.getValue());
//    }
    else{ //no neighbours were empty
      setFutureCellValue(row, col, WATOR_STATES.FISH.getValue());
      //setFutureLocation(checkCell, WATOR_STATES.FISH.getValue());
    }
  }

//  ArrayList<int[]> emptyCellCoords= new ArrayList<>();
//  Cell[] checkingCellNeighbors=this.getCheckingCellNeighbours();
//    for (int iterator=0; iterator<this.getCheckingCellNeighbours().length; iterator++) {
//    int value=checkingCellNeighbors[iterator].getMyCellState();
//    int[] coords= currentNeighborCoords[iterator];
//    if (value==WATOR_STATES.WATER.getValue()) {
//      emptyCellCoords.add(coords);
//    }
//  }

  //determine if there is food (that is, a fish) in a neighbouring cell
  private int[] determineNearbyFood(){
    //ArrayList<Cell> confirmedFoodOptions = new ArrayList<>(); //alive neighbors
    ArrayList<int[]> confirmedFoodCoords= new ArrayList<>();
    Cell[] checkingCellNeighbors=this.getCheckingCellNeighbours();
    for (int iterator=0; iterator<this.getCheckingCellNeighbours().length; iterator++) {
      int value=checkingCellNeighbors[iterator].getMyCellState();
      int[] coords= currentNeighborCoords[iterator];
      if (containsArray(modifiedCells, coords)) {
        continue;
      }
      if (value==WATOR_STATES.FISH.getValue()) {
        confirmedFoodCoords.add(coords);
      }
    }
//    for (Cell neighbouringCell : this.getCheckingCellNeighbours()) {
//      if (neighbouringCell != null) {
//        if (neighbouringCell.getMyCellState() == WATOR_STATES.FISH.getValue()) {
//          confirmedFoodOptions.add(neighbouringCell);
//        }
//      }
//    }
    if(confirmedFoodCoords.size() != 0){
      int emptyCellIndex = (int)(Math.random() * confirmedFoodCoords.size());
      return confirmedFoodCoords.get(emptyCellIndex);
    }
    //return new WatorCell(WATOR_STATES.ERROR.getValue());
    return null;
  }

  //determine the new value of the shark's cells depending on which conditions satisfied
  private void sharkNewValue(int row, int col){
    //TODO: check if shark's lifespan exceeds threshold -> then reproduce
    //TODO: check if shark's energy exceeds threshold -> then dies
    //Cell newEmptyLocation = selectRandomEmpty(checkCell);
    int[] newEmptyLocation=selectRandomEmpty();
    //Cell newPossibleFoodLocation = determineNearbyFood(checkCell);
    int[] newPossibleFoodLocation=determineNearbyFood();
    if (newPossibleFoodLocation!=null) {
      setFutureCellValue(newPossibleFoodLocation[0], newPossibleFoodLocation[1], WATOR_STATES.SHARK.getValue());
      setFutureCellValue(row, col, WATOR_STATES.WATER.getValue());
      //TODO: set the eaten fish's cell to water (it has been eaten?--i feel like it's fine)
      modifiedCells.add(newPossibleFoodLocation);
    }
    else if(newEmptyLocation!=null) {
      setFutureCellValue(newEmptyLocation[0], newEmptyLocation[1], WATOR_STATES.SHARK.getValue());
      setFutureCellValue(row, col, WATOR_STATES.WATER.getValue());
      modifiedCells.add(newEmptyLocation);
    }
//    if(newPossibleFoodLocation.getMyCellState() != WATOR_STATES.ERROR.getValue()){ //there is something edible in neighbours
//      setFutureLocation(newPossibleFoodLocation, WATOR_STATES.SHARK.getValue());
//      setFutureLocation(checkCell, WATOR_STATES.WATER.getValue());
//      //TODO: set the eaten fish's cell to water (it has been eaten)
//    }
//    else if(newEmptyLocation.getMyCellState() != WATOR_STATES.ERROR.getValue()){ //check for nearby empty and go there
//      setFutureLocation(newEmptyLocation, WATOR_STATES.SHARK.getValue());
//      setFutureLocation(checkCell, WATOR_STATES.WATER.getValue());
//    }
    else{ //no neighbours were empty AND nothing edible nearby -> stay in place
      setFutureCellValue(row, col, WATOR_STATES.SHARK.getValue());
      //setFutureLocation(checkCell, WATOR_STATES.SHARK.getValue());
    }
  }
  //set the determined future location within futuregrid
  private void setFutureLocation(Cell setCell, int newCellState){
    //TODO verify that x and y are right
    //x and y are never set
    this.setFutureCellValue(setCell.getMyX(), setCell.getMyY(), newCellState);
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

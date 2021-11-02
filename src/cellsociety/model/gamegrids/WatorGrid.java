package cellsociety.model.gamegrids;

import cellsociety.model.cells.Cell;
import cellsociety.model.cells.WatorCell;
import cellsociety.model.cells.WatorCell.WATOR_STATES;
import cellsociety.util.ReflectionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Create new WatorGrid that creates the grid of WatorCells that is used for the game
 * Depends on accurate input from the GameController and error-checked configurationMap
 * @author morganfeist, marcusdeans */
public class WatorGrid extends GameGrid {
  private int myFishLifespanThreshold;
  private int mySharkLifespanThreshold;
  private int mySharkEnergyThreshold;
  private ArrayList<int[]> modifiedCells;

  /**
   * Create the new WatorGrid
   * @param type the type of program being created -> necessary due to reflection
   * @param configurationMap of the different parameters used specifically in this game
   */
  public WatorGrid(Cell[][] gameGrid, String type, Map<String, String> configurationMap) {
    super(gameGrid, type);
    myFishLifespanThreshold = Integer.parseInt(configurationMap.get("fishLifespan"));
    mySharkLifespanThreshold = Integer.parseInt(configurationMap.get("sharkLifespan"));
    mySharkEnergyThreshold = Integer.parseInt(configurationMap.get("sharkEnergy"));
    modifiedCells=new ArrayList<>();
  }

  /**
   * Run the game as Wa-Tor World
   */
  @Override
  public void runGame() throws ReflectionException {
//    findEmptyCells();
    modifiedCells.clear();
    computeNeighborsAndRules();
  }

//  @Override
//  public void computeNeighbours(int cellX, int cellY) {
//    this.setCheckingCellNeighbours(new Cell[4]);
//    //checkingCellNeighbours = new Cell[4];
//    int iterator = 0;
//    int[] x = {-1, 1, 0, 0};
//    int[] y = {0, 0, 1, -1};
//    for (int i = 0; i < x.length; i++) {
//      int checkCol = cellX + x[i];
//      checkCol = checkColOutsideBoundary(checkCol);
//      int checkRow = cellY + y[i];
//      checkRow = checkRowOutsideBoundary(checkRow);
//      if (checkCol < 0 || checkCol >= this.getGameGrid()[0].length || checkRow < 0
//          || checkRow >= this.getGameGrid().length) {
//        continue;
//      }
//      this.setOneNeighborValueFromGameGrid(iterator, checkRow, checkCol);
//      //checkingCellNeighbours[iterator] = this.getCellArray()[checkRow][checkCol];
//      iterator++;
//    }
//  }

//  private int checkColOutsideBoundary(int checkCol) {
//    if (checkCol <0) {
//      checkCol =this.getGameGrid()[0].length-1;
//    }
//    if (checkCol >= this.getGameGrid()[0].length) {
//      checkCol =0;
//    }
//    return checkCol;
//  }
//
//  private int checkRowOutsideBoundary(int checkRow) {
//    if (checkRow <0) {
//      checkRow =this.getGameGrid().length-1;
//    }
//    if (checkRow >=this.getGameGrid().length) {
//      checkRow =0;
//    }
//    return checkRow;
//  }

  //apply the rules of Wa-Tor World -> go through neighbours and check which conditions satisfied
  //store new value for given cell in futureGrid
  protected void applyGameRules(Cell computingCell, int col, int row) {
    int[] coord = {row, col};
    if (containsArray(modifiedCells, coord)) {
      return;
    }
    int computingCellState = computingCell.getMyCellState();
    ((WatorCell)computingCell).incrementLifespan();
    if(computingCellState == WATOR_STATES.FISH.getValue()){
      fishNewValue(computingCell);
    }
    else if(computingCellState == WATOR_STATES.SHARK.getValue()){
      ((WatorCell)computingCell).incrementEnergy();
      sharkNewValue(computingCell);
    }
    else {
      this.setFutureCell(computingCell, row, col);
    }
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
      Random generator=new Random();
      int emptyCellIndex=generator.nextInt(emptyCellOptions.size());
      return emptyCellOptions.get(emptyCellIndex);
    }
    return new WatorCell(WATOR_STATES.ERROR.getValue());
  }


  //determine the new value of the fish based on whether it moves or not
  private void fishNewValue(Cell checkCell){
    //TODO: check if fish's lifespan exceeds threshold -> then reproduce
    Cell newCellLocation = selectRandomEmpty(checkCell);
    int[] coords = {checkCell.getMyY(), checkCell.getMyX()};

    if(newCellLocation.getMyCellState() != WATOR_STATES.ERROR.getValue()){
      moveFishIfPossible(checkCell, newCellLocation, coords);
    }
    else{ //no neighbours were empty
      setFutureLocation(checkCell);
    }
  }

  private void moveFishIfPossible(Cell checkCell, Cell newCellLocation, int[] coords) {
    checkCell.setMyX(newCellLocation.getMyX());
    checkCell.setMyY(newCellLocation.getMyY());
    setFutureLocation(checkCell);
    if (((WatorCell) checkCell).getMyLifeChronons()>=myFishLifespanThreshold) {
      ((WatorCell) checkCell).resetLifespan();
      makeNewCell(coords[0], coords[1], WATOR_STATES.FISH.getValue());
    }
    else {
      makeNewCell(coords[0], coords[1], WATOR_STATES.WATER.getValue());
    }
    modifiedCells.add(new int[] {newCellLocation.getMyY(), newCellLocation.getMyX()});
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
    int[] currentCoords={checkCell.getMyY(), checkCell.getMyX()};
    Cell newEmptyLocation = selectRandomEmpty(checkCell);
    Cell newPossibleFoodLocation = determineNearbyFood(checkCell);
    //shark dies
    if (((WatorCell)checkCell).getMyEnergyChronons()>=mySharkEnergyThreshold) {
      ((WatorCell)checkCell).resetEnergy();
      makeNewCell(currentCoords[0], currentCoords[1], WATOR_STATES.WATER.getValue());
      return;
    }
    if(newPossibleFoodLocation.getMyCellState() != WATOR_STATES.ERROR.getValue()){ //there is something edible in neighbours
      moveSharkToFoodSpot(checkCell, currentCoords, newPossibleFoodLocation);
    }
    else if(newEmptyLocation.getMyCellState() != WATOR_STATES.ERROR.getValue()){ //check for nearby empty and go there
      moveSharkToEmptySpot(checkCell, currentCoords, newEmptyLocation);
    }
    setFutureLocation(checkCell);
  }

  private void moveSharkToFoodSpot(Cell checkCell, int[] currentCoords, Cell newPossibleFoodLocation) {
    checkCell.setMyX(newPossibleFoodLocation.getMyX());
    checkCell.setMyY(newPossibleFoodLocation.getMyY());
    setFutureLocation(checkCell);
    ((WatorCell) checkCell).incrementEnergyAteFish();
    //setFutureLocation(newPossibleFoodLocation, WATOR_STATES.SHARK.getValue());
    if (((WatorCell) checkCell).getMyLifeChronons()>=mySharkLifespanThreshold) {
      makeNewCell(currentCoords[0], currentCoords[1], WATOR_STATES.SHARK.getValue());
    }
    else {
      makeNewCell(currentCoords[0], currentCoords[1], WATOR_STATES.WATER.getValue());
    }
    //setFutureLocation(checkCell);
    //setFutureLocation(checkCell, WATOR_STATES.WATER.getValue());
    modifiedCells.add(new int[] {newPossibleFoodLocation.getMyY(), newPossibleFoodLocation.getMyX()});
  }

  private void moveSharkToEmptySpot(Cell checkCell, int[] currentCoords, Cell newEmptyLocation) {
    checkCell.setMyY(newEmptyLocation.getMyY());
    checkCell.setMyX(newEmptyLocation.getMyX());
    //setFutureLocation(checkCell);
    modifiedCells.add(new int[] {newEmptyLocation.getMyY(), newEmptyLocation.getMyX()});
    //setFutureLocation(checkCell, WATOR_STATES.WATER.getValue());
    makeNewCell(currentCoords[0], currentCoords[1], WATOR_STATES.WATER.getValue());
  }

  private void makeNewCell(int row, int col, int cellState){
    Cell c= new WatorCell(cellState, col, row);
    this.setFutureCell(c, row, col);
  };
  //set the determined future location within futuregrid
  private void setFutureLocation(Cell setCell){
    this.setFutureCell(setCell, setCell.getMyY(), setCell.getMyX());
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

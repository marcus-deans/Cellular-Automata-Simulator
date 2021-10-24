package cellsociety.model.gamegrids;

import cellsociety.model.cells.Cell;
import cellsociety.model.cells.WatorCell.WATOR_STATES;
import java.util.Map;

public class WatorGrid extends GameGrid {

  public WatorGrid(Cell[][] gameGrid, String type, Map<String, String> configurationMap) {
    super(gameGrid, type);
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

  @Override
  public void runGame() {

  }

  //apply the rules of Wa-Tor World -> go through neighbours and check which conditions satisfied
  //store new value for given cell in futureGrid
  protected void applyGameRules(Cell computingCell, int col, int row) {
    int newValue = -1;
    int computingCellState = computingCell.getMyCellState();
    if(computingCellState == WATOR_STATES.FISH.getValue()){
      //TODO: do fish things
    }
    else if(computingCellState == WATOR_STATES.SHARK.getValue()){
      //TODO: do shark things
    }
    int liveCount = 0; //alive neighbors
    for (Cell neighbouringCell : checkingCellNeighbours) {
      if (neighbouringCell != null) {
        if (neighbouringCell.getMyCellState() == 1) {
          liveCount++;
        }
      }
    }

    futureGrid[row][col].setMyCellState(newValue);
  }

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
}

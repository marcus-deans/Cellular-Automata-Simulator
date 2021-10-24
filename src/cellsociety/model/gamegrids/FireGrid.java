package cellsociety.model.gamegrids;

import cellsociety.model.cells.Cell;
import cellsociety.model.cells.FireCell.FIRE_STATES;
import java.util.Random;

/**
 *     A burning cell turns into an empty cell
 *     A tree will burn if at least one neighbor is burning
 *     A tree ignites with probability f even if no neighbor is burning
 *     An empty space fills with a tree with probability p
 */



public class FireGrid extends GameGrid{
  private float myFireProb;
  private float myTreeProb;

  public FireGrid(Cell[][] gameGrid, float fireProb, float treeProb){
    super(gameGrid);
    myFireProb = fireProb;
    myTreeProb = treeProb;
  }

  @Override
  public void runGame() {
    computeNeighborsAndRules();
  }

  private FIRE_STATES determineCellState(int newValue){
    switch(newValue){
      case 0 -> {
        return FIRE_STATES.EMPTY;
      }
      case 1 -> {
        return FIRE_STATES.TREE;
      }
      case 2 -> {
        return FIRE_STATES.FIRE;
      }
    }
    return FIRE_STATES.ERROR;
  }

  //apply the rules of Fire  -> go through neighbours and check which conditions satisfied
  //also check cases for self
  //store new value for given cell in futureGrid
  protected void applyGameRules(Cell computingCell, int col, int row){
    int currentCellState = computingCell.getMyCellState();
    int newValue = currentCellState;

    switch(determineCellState(currentCellState)){
      case EMPTY -> {
        if( new Random().nextFloat() < myTreeProb){
          newValue = FIRE_STATES.TREE.getValue();
        }
      }
      case FIRE -> {
        newValue = FIRE_STATES.EMPTY.getValue();
      }
      case TREE -> {
        if( new Random().nextFloat() < myFireProb){
          newValue = FIRE_STATES.FIRE.getValue();
        }
        else{
          for(Cell neighbouringCell : checkingCellNeighbours) {
            if (neighbouringCell != null) {
              if (neighbouringCell.getMyCellState() == FIRE_STATES.FIRE.getValue()) {
                newValue = FIRE_STATES.FIRE.getValue();
                break;
              }
            }
          }
        }
      }
      default -> {
      }
    }

    futureGrid[row][col].setMyCellState(newValue);
  }
}

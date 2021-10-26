package cellsociety.model.gamegrids;

import cellsociety.view.GridListener;
import cellsociety.model.cells.Cell;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public abstract class GameGrid {

  private List<GridListener> listeners = new ArrayList<>();
  private Cell[] checkingCellNeighbours;
  private Cell[][] futureGrid;
  private Cell[][] myGameGrid;
  private int myGameWidth;
  private int myGameHeight;
  private int myNewValue;
  private String type;

  public GameGrid(Cell[][] gameGrid, String type) {
    myGameGrid = gameGrid;
    myGameWidth = gameGrid[0].length;
    myGameHeight = gameGrid.length;
    myNewValue = 0;
    this.type = type;
    setupFutureGrid(); //may not be necessary but tests currently depend on it?
    //updateInitialFutureGrid();
  }

  protected void setFutureCellValue(int row, int col, int value) {
    futureGrid[row][col].setMyCellState(value);
  }

  private void setupFutureGrid() {
    futureGrid = new Cell[myGameHeight][myGameWidth];
    for (int i = 0; i < myGameWidth; i++) {
      for (int j = 0; j < myGameHeight; j++) {
        futureGrid[j][i] = makeNewCell(0);
      }
    }
  }
  protected void setCheckingCellNeighbours(Cell[] neighbors) {
    checkingCellNeighbours=neighbors;
  }
  protected Cell[] getCheckingCellNeighbours() {
    return checkingCellNeighbours;
  }
  protected void setOneNeighborValueFromGameGrid(int index, int row, int col) {
    checkingCellNeighbours[index]=myGameGrid[row][col];
  }


  public void updateInitialFutureGrid() {
    futureGrid = new Cell[myGameHeight][myGameWidth];
    for (int i = 0; i < myGameWidth; i++) {
      for (int j = 0; j < myGameHeight; j++) {
        //futureGrid[i][j]=new LifeCell(0);
        //futureGrid[j][i].setMyCellState(myGameGrid[j][i].getMyCellState());
        futureGrid[j][i] = makeNewCell(myGameGrid[j][i].getMyCellState());
        //futureGrid[j][i]=new LifeCell(myGameGrid[j][i].getMyCellState());
        sendViewUpdate(j, i, futureGrid[j][i].getMyCellState());
      }
    }
  }

  public abstract void runGame();

  protected void updateCellValues() {
    //myGameGrid = futureGrid;
    for (int row = 0; row < futureGrid.length; row++) {
      for (int col = 0; col < futureGrid[0].length; col++) {
        sendViewUpdate(row, col, futureGrid[row][col].getMyCellState());
        myGameGrid[row][col].setMyCellState(futureGrid[row][col].getMyCellState());
      }
    }
  }

  //populates Cell[] of the possible neighbours of given cell (max 9)
  //only 4 neighbors for fire
  protected void computeNeighbours(int cellX, int cellY) {
    checkingCellNeighbours = new Cell[9]; //cell 8?
    //not changing for some reason
    int iterator = 0;
    for (int x = -1; x < 2; x++) {
      int checkCol = cellX + x;
      if (checkCol < 0 || checkCol >= myGameWidth) {
        continue;
      }
      for (int y = -1; y < 2; y++) {
        int checkRow = cellY + y;
        if (checkRow < 0 || checkRow >= myGameHeight || (x == 0 && y == 0)) {
          continue;
        }
        checkingCellNeighbours[iterator] = myGameGrid[checkRow][checkCol];
        iterator++;
      }
    }
  }

  private int newValue() {
    myNewValue = 0;
    return myNewValue;
  }

  //iterate through the grid and for each cell: identify neighbours and apply game rules, then replace values
  protected void computeNeighborsAndRules() {
    // TODO: x and y could be backwards here
    //for segregation initial pass should go here
    for (int col = 0; col < myGameWidth; col++) {
      for (int row = 0; row < myGameHeight; row++) {
        computeNeighbours(col, row);
        //applyGameRules(myGameGrid[x][y], x, y);
        applyGameRules(myGameGrid[row][col], col, row);
      }
    }
    updateCellValues();
  }

  public Cell[][] getGameGrid() {
    return myGameGrid;
  }

  public void addListener(GridListener gl) {
    listeners.add(gl);
  }

  protected void sendViewUpdate(int row, int column, int state) {
    for(GridListener listener : listeners){
      listener.update(row, column, state);
    }
  }

  protected abstract void applyGameRules(Cell computingCell, int col, int row);

  //TODO: accomodate Wotor which has different CELL parameters as opposed to rest
  private Cell makeNewCell(int value) {
    Cell cell = null;
    try {
      Class<?> clazz = Class.forName("cellsociety.model.cells." + type + "Cell");
      Constructor<?> c = clazz.getConstructor(int.class);
      cell = (Cell) c.newInstance(value);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return cell;
  }
}

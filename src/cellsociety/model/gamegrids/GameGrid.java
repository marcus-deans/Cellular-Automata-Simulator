package cellsociety.model.gamegrids;

import cellsociety.model.cells.Cell;
import cellsociety.view.GridListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ResourceBundle;

/**
 * Abstract GameGrid that will allow for a grid of cells to be created for any given game selected
 * Depends on accurate input from the GameController and error-checked configurationMap
 *
 * @author morganfeist, marcusdeans
 */
public abstract class GameGrid {

  private static final String RESOURCE_FILE_PATH = "cellsociety.resources.numCellStates";
  private static final ResourceBundle numCellStates = ResourceBundle.getBundle(RESOURCE_FILE_PATH);
  private GridListener listener;
  private Cell[] checkingCellNeighbours;
  private Cell[][] futureGrid;
  private Cell[][] myGameGrid;
  private int myGameWidth;
  private int myGameHeight;
  private int myNewValue;
  private String type;

  /**
   * Create a new abstract GameGrid that will encompass all of the cells in the simulation
   *
   * @param gameGrid the multidimensional array of Cells that is of appropriate size
   * @param type     the type of simulation to be created
   */
  public GameGrid(Cell[][] gameGrid, String type) {
    myGameGrid = gameGrid;
    myGameWidth = gameGrid[0].length;
    myGameHeight = gameGrid.length;
    myNewValue = 0;
    this.type = type;
    setupFutureGrid(); //may not be necessary but tests currently depend on it?
    //updateInitialFutureGrid();
  }

  /**
   * Sets the listener object that will be notified/called upon whenever the state of a cell
   * changes
   *
   * @param gl the GridListener instance
   */
  public void addListener(GridListener gl) {
    listener = gl;
  }

  //set the value of a cell in futureGrid, i.e., the new value for a given cel
  protected void setFutureCellValue(int row, int col, int value) {
    futureGrid[row][col].setMyCellState(value);
  }

  //replace the cell in FutureGrid with the specific cell, i.e., replace a present cell
  protected void setFutureCell(Cell cell, int row, int col) {
    futureGrid[row][col] = cell;
    //cell.setMyX(col);
    //cell.setMyY(row);
  }

  //setup the future grid that represents the grid after all of the changes take place
  private void setupFutureGrid() {
    futureGrid = new Cell[myGameHeight][myGameWidth];
    for (int i = 0; i < myGameWidth; i++) {
      for (int j = 0; j < myGameHeight; j++) {
        futureGrid[j][i] = makeNewCell(0, j, i);
      }
    }
  }

  //obtain the array of Cell that neighbour the given cell
  protected Cell[] getCheckingCellNeighbours() {
    return checkingCellNeighbours;
  }

  //set the array of Cells that neighbour the given cell
  protected void setCheckingCellNeighbours(Cell[] neighbors) {
    checkingCellNeighbours = neighbors;
  }

  //set the value of a neighboring cell by obtaining that cell's state on the current grid
  protected void setOneNeighborValueFromGameGrid(int index, int row, int col) {
    checkingCellNeighbours[index] = myGameGrid[row][col];
  }

  //update the states of the futureGrid that represents the grid after changes are made
  public void updateInitialFutureGrid() {
    futureGrid = new Cell[myGameHeight][myGameWidth];
    for (int i = 0; i < myGameWidth; i++) {
      for (int j = 0; j < myGameHeight; j++) {
        //futureGrid[j][i].setMyCellState(myGameGrid[j][i].getMyCellState());
        futureGrid[j][i] = makeCopyCell(myGameGrid[j][i]);
        //futureGrid[j][i] = makeNewCell(myGameGrid[j][i].getMyCellState(), j, i);
        if (listener != null) {
          listener.update(j, i, futureGrid[j][i].getMyCellState());
        }
      }
    }
  }

  /**
   * Run the game animation, implemented somewhat different depending on simulation
   */
  public abstract void runGame();

  //update teh cells values by replacing all of the cells with their equivalent in FutureGrid
  protected void updateCellValues() {
    //myGameGrid = futureGrid;
    //this needs to be updated to make a copy of cells
    for (int row = 0; row < futureGrid.length; row++) {
      for (int col = 0; col < futureGrid[0].length; col++) {
        if (listener != null) {
          listener.update(row, col, futureGrid[row][col].getMyCellState());
        }
        myGameGrid[row][col] = makeCopyCell(futureGrid[row][col]);
        //myGameGrid[row][col].setMyCellState(futureGrid[row][col].getMyCellState());
      }
    }
  }

  //populates Cell[] of the possible neighbours of given cell (max 9), but only 4 neighbors for fire (and wator--modify)
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

  //when a cell is clicked by user, updates its state accordingly
  public void updateOneCell(int row, int col) {
    try {
      int val = myGameGrid[row][col].getMyCellState();
      if (val < Integer.parseInt(numCellStates.getString(type)) - 1) {
        val++;
      } else {
        val = 0;
      }
      //we want to increment val here
      myGameGrid[row][col] = makeNewCell(val, row, col);
      //myGameGrid[row][col].setMyCellState(val);
      if (listener != null) {
        listener.update(row, col, val);
      }
    } catch (ArrayIndexOutOfBoundsException e) {

    }
  }

  //iterate through the grid and for each cell: identify neighbours and apply game rules, then replace values
  //TODO make this random??
  protected void computeNeighborsAndRules() {
    for (int col = 0; col < myGameWidth; col++) {
      for (int row = 0; row < myGameHeight; row++) {
        computeNeighbours(col, row);
        applyGameRules(myGameGrid[row][col], col, row);
      }
    }
    updateCellValues();
  }

  //return the GameGrid for the present simulation
  protected Cell[][] getGameGrid() {
    return myGameGrid;
  }

  //get the state of a particular cell in the grid
  public int getCellValue(int row, int col) {
    return myGameGrid[row][col].getMyCellState();
  }

  //Apply all of the rules for the relevant simulation and store the outcomes
  protected abstract void applyGameRules(Cell computingCell, int col, int row);

  //Make a copy of the given cell as appropriate (make of the correct subclass using reflection)
  private Cell makeCopyCell(Cell cell) {
    Cell copy = null;
    Constructor<?> c = null;
    Class<?> clazz = null;
    try {
      clazz = Class.forName("cellsociety.model.cells." + type + "Cell");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    try {
      c = clazz.getConstructor(Cell.class);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
    try {
      copy = (Cell) c.newInstance(cell);
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return copy;
  }

  //TODO: accomodate Wotor which has different CELL parameters as opposed to rest
  //create a new cell in the grid which is of the appropriate subclass corresponding to simulation type
  private Cell makeNewCell(int value, int row, int col) {
    Cell cell = null;
    try {
      Class<?> clazz = Class.forName("cellsociety.model.cells." + type + "Cell");
      Constructor<?> c = clazz.getConstructor(int.class, int.class, int.class);
      cell = (Cell) c.newInstance(value, col, row);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return cell;
  }
}

package cellsociety.model.gamegrids;

import cellsociety.model.cells.Cell;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Constructor;

public abstract class GameGrid {

  protected PropertyChangeSupport support;
  Cell[] checkingCellNeighbours;
  Cell[][] futureGrid;
  //TODO make these private
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
    //futureGrid=gameGrid;
    support = new PropertyChangeSupport(this);
    setupFutureGrid();
  }

  private void setupFutureGrid() {
    futureGrid = new Cell[myGameHeight][myGameWidth];
    for (int i = 0; i < myGameWidth; i++) {
      for (int j = 0; j < myGameHeight; j++) {
        futureGrid[j][i] = makeNewCell(0);
        //futureGrid[j][i]=new LifeCell(0);
      }
    }
  }

  public void updateInitialFutureGrid() {
    futureGrid = new Cell[myGameHeight][myGameWidth];
    for (int i = 0; i < myGameWidth; i++) {
      for (int j = 0; j < myGameHeight; j++) {
        //futureGrid[i][j]=new LifeCell(0);
        //futureGrid[j][i].setMyCellState(myGameGrid[j][i].getMyCellState());
        futureGrid[j][i] = makeNewCell(myGameGrid[j][i].getMyCellState());
        //futureGrid[j][i]=new LifeCell(myGameGrid[j][i].getMyCellState());
        sendViewUpdate("Row", j - 1, j);
        sendViewUpdate("Column", i - 1, i);
        sendViewUpdate("State", -1, futureGrid[j][i].getMyCellState());
      }
    }
  }

  public abstract void runGame();

  protected void updateCellValues() {
    //myGameGrid = futureGrid;
    for (int i = 0; i < futureGrid.length; i++) {
      for (int j = 0; j < futureGrid[0].length; j++) {
        myGameGrid[i][j].setMyCellState(futureGrid[i][j].getMyCellState());
      }
    }
    //boolean unequal=(myGameGrid==futureGrid);
    //always false so not the issue
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
        sendViewUpdate("Row", row - 1, row);
        sendViewUpdate("Column", col - 1, col);
        sendViewUpdate("State", myGameGrid[row][col].getMyCellState(),
            futureGrid[row][col].getMyCellState());
        //sendViewUpdate("State", -1, futureGrid[row][col].getMyCellState());
        //boolean helper = (myGameGrid[x][y] == futureGrid[x][y]);
        //the arrays are pointing at each other-> false in the beginning then true
//        boolean helper = (myGameGrid[row][col].getMyCellState() == futureGrid[row][col].getMyCellState());
//        System.out.println(helper);
      }
    }
    updateCellValues();
  }

  public Cell[][] getCellArray() {
    return myGameGrid;
  }

  /**
   * Add an observer/listener to GameGrid's instance of PropertyChangeSupport, which enables
   * GameGrid to notify that observer of changes in its state
   *
   * @param pcl the PropertyChangeListener to add
   */
  public void addPropertyChangeListener(PropertyChangeListener pcl) {
    support.addPropertyChangeListener(pcl);
  }

  protected void sendViewUpdate(String propertyName, Object oldValue, Object newValue) {
    support.firePropertyChange(propertyName, oldValue, newValue);
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

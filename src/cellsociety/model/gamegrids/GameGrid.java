package cellsociety.model.gamegrids;

import cellsociety.model.edgePolicy.Edge;
import cellsociety.model.edgePolicy.FiniteEdge;
import cellsociety.model.edgePolicy.ToroidalEdge;
import cellsociety.model.neighborClasses.CardinalNeighbors;
import cellsociety.model.neighborClasses.CompleteNeighbors;
import cellsociety.model.neighborClasses.NeighborPolicy;
import cellsociety.model.shapes.Hexagon;
import cellsociety.model.shapes.Shape;
import cellsociety.model.shapes.Square;
import cellsociety.view.GridListener;
import cellsociety.model.cells.Cell;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ResourceBundle;

/**
 *  * @author morganfeist, marcusdeans
 */
public abstract class GameGrid {

  private GridListener listener;
  private Cell[] checkingCellNeighbours;
  private Cell[][] futureGrid;
  private Cell[][] myGameGrid;
  private int myGameWidth;
  private int myGameHeight;
  private int myNewValue;
  private String type;
  private static final String NUM_STATES_FILE_PATH = "cellsociety.resources.model.numCellStates";

  private static final ResourceBundle numCellStates = ResourceBundle.getBundle(NUM_STATES_FILE_PATH);
  private static final String NEIGHBOR_RULES_FILE_PATH = "cellsociety.resources.model.neighborRules";
  private static final ResourceBundle neighborRules = ResourceBundle.getBundle(NEIGHBOR_RULES_FILE_PATH);

  public GameGrid(Cell[][] gameGrid, String type) {
    myGameGrid = gameGrid;
    myGameWidth = gameGrid[0].length;
    myGameHeight = gameGrid.length;
    myNewValue = 0;
    this.type = type;
    setupFutureGrid(); //may not be necessary but tests currently depend on it?
    //updateInitialFutureGrid();
  }

  public void addListener(GridListener gl) {
    listener = gl;
  }

  protected void setFutureCellValue(int row, int col, int value) {
    futureGrid[row][col].setMyCellState(value);
  }

  protected void setFutureCell(Cell cell, int row, int col) {
    futureGrid[row][col]=cell;
    //cell.setMyX(col);
    //cell.setMyY(row);
  }

  private void setupFutureGrid() {
    futureGrid = new Cell[myGameHeight][myGameWidth];
    for (int i = 0; i < myGameWidth; i++) {
      for (int j = 0; j < myGameHeight; j++) {
        futureGrid[j][i] = makeNewCell(0, j, i);
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
        //futureGrid[j][i].setMyCellState(myGameGrid[j][i].getMyCellState());
        futureGrid[j][i]=makeCopyCell(myGameGrid[j][i]);
        //futureGrid[j][i] = makeNewCell(myGameGrid[j][i].getMyCellState(), j, i);
        if (listener!=null) {listener.update(j, i, futureGrid[j][i].getMyCellState());}
      }
    }
  }

  public abstract void runGame();

  protected void updateCellValues() {
    //myGameGrid = futureGrid;
    //this needs to be updated to make a copy of cells
    for (int row = 0; row < futureGrid.length; row++) {
      for (int col = 0; col < futureGrid[0].length; col++) {
        if (listener!=null) {listener.update(row, col, futureGrid[row][col].getMyCellState());}
        myGameGrid[row][col]=makeCopyCell(futureGrid[row][col]);
        //myGameGrid[row][col].setMyCellState(futureGrid[row][col].getMyCellState());
      }
    }
  }
  //TODO fully implement this method to allow for differerent shapes and edge policies
  //add ways to set the neighbor policy, edge policy, and shape
  protected void computeNeighbours(int cellX, int cellY) {
    Shape s = new Square();
    Edge e = makeEdge(cellY, cellX, myGameHeight, myGameWidth);
    //Edge e = new ToroidalEdge(cellY, cellX, myGameHeight, myGameWidth);
    //NeighborPolicy n = new CompleteNeighbors(s, e);
    NeighborPolicy n = makeNeighbor(s, e);
    int[][] neighborCoords=n.determineCoordinates(cellY, cellX);
    checkingCellNeighbours=new Cell[neighborCoords.length];
    for (int i=0; i<neighborCoords.length; i++) {
      checkingCellNeighbours[i]=myGameGrid[neighborCoords[i][0]][neighborCoords[i][1]];
    }
  }

  private NeighborPolicy makeNeighbor(Shape shape, Edge edge) {
    Constructor<?> c = null;
    Class<?> clazz = null;
    NeighborPolicy neighbor=null;
    String name=neighborRules.getString(type+"Neighbor");
    try {
      clazz = Class.forName("cellsociety.model.neighborClasses." + name+"Neighbors");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    try {
      c = clazz.getConstructor(Shape.class, Edge.class);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
    try {
      neighbor = (NeighborPolicy) c.newInstance(shape, edge);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return neighbor;
  }

  private Edge makeEdge(int row, int col, int height, int width) {
    Constructor<?> c = null;
    Class<?> clazz = null;
    Edge edge=null;
    String name=neighborRules.getString(type+"Edge");
    try {
      clazz = Class.forName("cellsociety.model.edgePolicy." + name+"Edge");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    try {
      c = clazz.getConstructor(int.class, int.class, int.class, int.class);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
    try {
      edge = (Edge) c.newInstance(row, col, height, width);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return edge;
  }

  //populates Cell[] of the possible neighbours of given cell (max 9)
  //only 4 neighbors for fire and wator
  protected void computeNeighbors(int cellX, int cellY) {
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
  //use when cell is clicked
  public void updateOneCell(int row, int col) {
    try {
      int val = myGameGrid[row][col].getMyCellState();
      if (val < Integer.parseInt(numCellStates.getString(type))-1) {
        val++;
      } else {
        val = 0;
      }
      //we want to increment val here
      myGameGrid[row][col]=makeNewCell(val, row, col);
      //myGameGrid[row][col].setMyCellState(val);
      if (listener!=null) {listener.update(row, col, val);}
    }
    catch (ArrayIndexOutOfBoundsException e) {

    }
  }

  private int newValue() {
    myNewValue = 0;
    return myNewValue;
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

  protected Cell[][] getGameGrid() {
    return myGameGrid;
  }

  public int getCellValue(int row, int col) {
    return myGameGrid[row][col].getMyCellState();
  }

  protected abstract void applyGameRules(Cell computingCell, int col, int row);

  private Cell makeCopyCell (Cell cell) {
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

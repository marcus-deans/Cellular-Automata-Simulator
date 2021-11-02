package cellsociety.model.gamegrids;

import cellsociety.model.edgePolicy.Edge;
import cellsociety.model.neighborClasses.NeighborPolicy;
import cellsociety.model.shapes.Shape;
import cellsociety.model.shapes.Square;
import cellsociety.util.ReflectionException;
import cellsociety.view.GridListener;
import cellsociety.model.cells.Cell;
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

  private static final String RESOURCE_FILE_PATH = "cellsociety.resources.model.numCellStates";
  private static final ResourceBundle numCellStates = ResourceBundle.getBundle(RESOURCE_FILE_PATH);
  private GridListener listener;
  private Cell[] checkingCellNeighbours;
  private Cell[][] futureGrid;
  private Cell[][] myGameGrid;
  private int myGameWidth;
  private int myGameHeight;
  private String type;
  private static final String NEIGHBOR_RULES_FILE_PATH = "cellsociety.resources.model.neighborRules";
  private static final ResourceBundle neighborRules = ResourceBundle.getBundle(NEIGHBOR_RULES_FILE_PATH);

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
    this.type = type;
    setupFutureGrid();
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
  }


  //setup the future grid that represents the grid after all of the changes take place
  private void setupFutureGrid() {
    futureGrid = new Cell[myGameHeight][myGameWidth];
    for (int i = 0; i < myGameWidth; i++) {
      for (int j = 0; j < myGameHeight; j++) {
        try {
          futureGrid[j][i] = makeNewCell(0, j, i);
        }
        catch (ReflectionException e) {

        }
      }
    }
  }

  //obtain the array of Cell that neighbour the given cell
  protected Cell[] getCheckingCellNeighbours() {
    return checkingCellNeighbours;
  }

  /**
   * updatesTheFutureGrid so that on file load it reflects the initial Cell states
   * @throws ReflectionException if unable to reflect correct cells
   */
  //update the states of the futureGrid that represents the grid after changes are made
  public void updateInitialFutureGrid() throws ReflectionException {
    futureGrid = new Cell[myGameHeight][myGameWidth];
    for (int i = 0; i < myGameWidth; i++) {
      for (int j = 0; j < myGameHeight; j++) {
        futureGrid[j][i] = makeCopyCell(myGameGrid[j][i]);
        if (listener != null) {
          listener.update(j, i, futureGrid[j][i].getMyCellState());
        }
      }
    }
  }

  /**
   * Run the game animation, implemented somewhat differently depending on simulation
   * @throws ReflectionException if cannot create correct cells
   */
  public abstract void runGame() throws ReflectionException;

  //update teh cells values by replacing all of the cells with their equivalent in FutureGrid
  protected void updateCellValues() throws ReflectionException {
    //myGameGrid = futureGrid;
    for (int row = 0; row < futureGrid.length; row++) {
      for (int col = 0; col < futureGrid[0].length; col++) {
        if (listener != null) {
          listener.update(row, col, futureGrid[row][col].getMyCellState());
        }
        myGameGrid[row][col] = makeCopyCell(futureGrid[row][col]);
      }
    }
  }

  //add ways to set the neighbor policy, edge policy, and shape
  protected void computeNeighbours(int cellX, int cellY) throws ReflectionException {
    Shape s = new Square();
    Edge e = makeEdge(cellY, cellX, myGameHeight, myGameWidth);
    NeighborPolicy n = makeNeighbor(s, e);
    int[][] neighborCoords=n.determineCoordinates(cellY, cellX);
    checkingCellNeighbours=new Cell[neighborCoords.length];
    for (int i=0; i<neighborCoords.length; i++) {
      checkingCellNeighbours[i]=myGameGrid[neighborCoords[i][0]][neighborCoords[i][1]];
    }
  }

  private NeighborPolicy makeNeighbor(Shape shape, Edge edge) throws ReflectionException {
    Constructor<?> c;
    Class<?> clazz;
    NeighborPolicy neighbor;
    String name=neighborRules.getString(type+"Neighbor");
    try {
      clazz = Class.forName("cellsociety.model.neighborClasses." + name + "Neighbors");
      c = clazz.getConstructor(Shape.class, Edge.class);
      neighbor = (NeighborPolicy) c.newInstance(shape, edge);
    }
    catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
      throw new ReflectionException();
    }

    return neighbor;
  }

  private Edge makeEdge(int row, int col, int height, int width) throws ReflectionException {
    Constructor<?> c;
    Class<?> clazz;
    Edge edge;
    String name=neighborRules.getString(type+"Edge");
    try {
      clazz = Class.forName("cellsociety.model.edgePolicy." + name + "Edge");
      c = clazz.getConstructor(int.class, int.class, int.class, int.class);
      edge = (Edge) c.newInstance(row, col, height, width);
    }
    catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
      throw new ReflectionException();
    }
    return edge;
  }

  //when a cell is clicked by user, updates its state accordingly
  public void updateOneCell(int row, int col){
    try {
      int val = myGameGrid[row][col].getMyCellState();
      if (val < Integer.parseInt(numCellStates.getString(type)) - 1) {
        val++;
      } else {
        val = 0;
      }
      myGameGrid[row][col] = makeNewCell(val, row, col);
      if (listener != null) {
        listener.update(row, col, val);
      }
    } catch (ArrayIndexOutOfBoundsException | ReflectionException e) {

    }
  }

  //iterate through the grid and for each cell: identify neighbours and apply game rules, then replace values
  protected void computeNeighborsAndRules() throws ReflectionException {
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
  private Cell makeCopyCell(Cell cell) throws ReflectionException {
    Cell copy;
    try {
      Class<?> clazz = Class.forName("cellsociety.model.cells." + type + "Cell");
      Constructor<?> c;
      c = clazz.getConstructor(Cell.class);
      copy = (Cell) c.newInstance(cell);
    }
    catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
      throw new ReflectionException();
    }
    return copy;
  }

  //TODO: accomodate Wotor which has different CELL parameters as opposed to rest
  //create a new cell in the grid which is of the appropriate subclass corresponding to simulation type
  private Cell makeNewCell(int value, int row, int col) throws ReflectionException {
    Cell cell;
    try {
      Class<?> clazz = Class.forName("cellsociety.model.cells." + type + "Cell");
      Constructor<?> c = clazz.getConstructor(int.class, int.class, int.class);
      cell = (Cell) c.newInstance(value, col, row);
    } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
      throw new ReflectionException();
    }
    return cell;
  }
}

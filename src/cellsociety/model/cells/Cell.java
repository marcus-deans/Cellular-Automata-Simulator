package cellsociety.model.cells;

/**
 * Abstract format for a Cell to be created for any of the given cellular automata games
 * Does not depend on any other classes and acts as a superclass for others
 * @author marcusdeans
 */
public abstract class Cell {

  private int myCellState;
 private  int myX;
  private int myY;

  /**
   * Create a new abstract cell
   * @param cellState the state of the cell, i.e., its main parameter
   */
  public Cell(int cellState) {
    myCellState = cellState;
  }

  /**
   * Create a new abstract cell based on another one
   * @param copy the Cell that hte stqte should be copied from
   */
  public Cell(Cell copy) {
    myCellState = copy.getMyCellState();
  }

  /**
   * Obtain the y position of the cell
   * @return integer y coordinate
   */
  public int getMyY() {
    return myY;
  }

  /**
   * Obtain the x position of the cell
   * @return integer x coordinate
   */
  public int getMyX() {
    return myX;
  }

  /**
   * Obtain the cell state of the given cell
   * @return the integer cell state
   */
  public int getMyCellState() {
    return myCellState;
  }

  /**
   * Set a new cell state for the given cel
   * @param myCellState integer state that Cell should be set to
   */
  public void setMyCellState (int myCellState){
    this.myCellState = myCellState;
  }

  /**
   * Increment the lifespan of shark/fish, implemented in WatorCell
   */
  public void incrementLifespan(){};

  /**
   * Increment the energy count of a shark, implemented in WatorCell
   */
  public void incrementEnergy(){};
}

package cellsociety.model.cells;

/**
 * Structure for any given cell in Spreading of Fire game
 * Depends on superclass Cell and allows for easy specification of cell characteristics
 * @author morganfeist, marcusdeans */
public class FireCell extends Cell {

  /**
   * Create a new FireCell
   * @param cellState the integer state that the new cell should be set to
   */
  public FireCell(int cellState) {
    super(cellState);
  }
  public FireCell(Cell c) {
    super(c);
  }
  public FireCell(int cellState, int col, int row) {
    super(cellState, col, row);
  }



  /**
   * Enum to easy identification of the different cell states
   */
  public enum FIRE_STATES {
    ERROR(-1), EMPTY(0), TREE(1), FIRE(2);

    private int numVal;

    FIRE_STATES(int numVal) {
      this.numVal = numVal;
    }

    /**
     * Obtain the integer value for the given state
     * @return the integer state
     */
    public int getValue() {
      return numVal;
    }
  }
}

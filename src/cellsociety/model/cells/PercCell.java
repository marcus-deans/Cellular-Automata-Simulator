package cellsociety.model.cells;

/**
 * Structure for any given cell in Percolation
 * Depends on superclass Cell and allows for easy specification of cell characteristics
 * @author morganfeist, marcusdeans */
public class PercCell extends Cell {

  /**
   * Create a new PercCell
   * @param cellState the integer state that the new cell should be set to
   */
  public PercCell(int cellState) {
    super(cellState);
  }
  public PercCell(Cell c) {
    super(c);
  }
  public PercCell(int cellState, int col, int row) {
    super(cellState, col, row);
  }


  /**
   * Enum to easy identification of the different cell states
   */
  public enum PERC_STATES {
    ERROR(-1), EMPTY(0), BLOCKED(2), PERCOLATED(1);

    private int numVal;

    PERC_STATES(int numVal) {
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

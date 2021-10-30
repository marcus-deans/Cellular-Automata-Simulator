package cellsociety.model.cells;

/**
 * Structure for any given cell in Schelling's Segregation
 * Depends on superclass Cell and allows for easy specification of cell characteristics
 * @author marcusdeans
 */
public class SegCell extends Cell{

  /**
   * Create a new SegCell
   * @param cellState the integer state that the new cell should be set to
   */
  public SegCell(int cellState) {
    super(cellState);
  }

  public SegCell(int cellState, int x, int y) {
    super(cellState, x, y);
  }

  /**
   * Enum to easy identification of the different cell states
   */
  public enum SEG_STATES {
    ERROR(-1), EMPTY(0), ALPHA(1), BETA(2);

    private int numVal;

    SEG_STATES(int numVal) {
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

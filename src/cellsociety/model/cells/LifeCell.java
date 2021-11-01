package cellsociety.model.cells;

/**
 * Structure for any given cell in Game of Life
 * Depends on superclass Cell and allows for easy specification of cell characteristics
 * @author morganfeist, marcusdeans */
public class LifeCell extends Cell{

  /**
   * Create a new LifeCell
   * @param cellState the integer state that the new cell should be set to
   */
  public LifeCell(int cellState){
    super(cellState);
  }
  public LifeCell(Cell copy) {
    super(copy);
  }
  public LifeCell(int cellState, int col, int row) {
    super(cellState, col, row);
  }

  /**
   * Enum to easy identification of the different cell states
   */
  public enum LIFE_STATES {
    ERROR(-1), DEAD(0), LIVE(1);

    private int numVal;

    LIFE_STATES(int numVal) {
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

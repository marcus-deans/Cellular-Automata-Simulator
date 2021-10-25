package cellsociety.model.cells;

/**
 * Structure for any given cell in Wator World
 * Depends on superclass Cell and allows for easy specification of cell characteristics
 * @author marcusdeans
 */
public class WatorCell extends Cell {

  private int myLifeChronons;
  private int myEnergyChronons;

  /**
   * Create a new WatorCell with appropriate characteristics
   * @param cellState the integer state that the new cell should be set to
   */
  public WatorCell(int cellState) {
    super(cellState);
    myLifeChronons = 0;
    myEnergyChronons = 0;
  }

  /**
   * Enum to easy identification of the different cell states
   */
  public enum WATOR_STATES {
    ERROR(-1), WATER(0), FISH(1), SHARK(2);

    private int numVal;

    WATOR_STATES(int numVal) {
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

  @Override
  public void incrementLifespan(){
    myLifeChronons++;
  }

  @Override
  public void incrementEnergy(){
    myEnergyChronons++;
  }
}

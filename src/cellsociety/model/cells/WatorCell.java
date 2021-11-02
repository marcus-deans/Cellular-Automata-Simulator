package cellsociety.model.cells;

/**
 * Structure for any given cell in Wator World
 * Depends on superclass Cell and allows for easy specification of cell characteristics
 * @author morganfeist, marcusdeans */
public class WatorCell extends Cell {
  //energy chronons->shark only
  //life chronons->both (time to reproduce)
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

  public WatorCell(int cellState, int x, int y) {
    super(cellState, x, y);
    myLifeChronons = 0;
    myEnergyChronons = 0;
  }
  public WatorCell(Cell copy) {
    super(copy);
    myLifeChronons=((WatorCell) copy).getMyLifeChronons();
    myEnergyChronons=((WatorCell) copy).getMyEnergyChronons();
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

  public int getMyEnergyChronons() {
    return myEnergyChronons;
  }

  public int getMyLifeChronons() {
    return myLifeChronons;
  }

  /**
   * Increment the lifespan of shark/fish after one chronon
   */
  public void incrementLifespan(){
    myLifeChronons++;
  }

  public void resetLifespan() {
    myLifeChronons=0;
  }


  /**
   * Subtract from energy because a Shark ate a fish and therefore should live longer
   */
  public void incrementEnergyAteFish(){
    myEnergyChronons--;
  }

  /**
   * Increment the energy count of a shark after one chronon
   */
  public void incrementEnergy(){
    myEnergyChronons++;
  }

  public void resetEnergy() {
    myEnergyChronons=0;
  }

}

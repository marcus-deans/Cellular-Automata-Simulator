package cellsociety.model.cells;

public class WatorCell extends Cell {

  private int myLifeChronons;
  private int myEnergyChronons;

  public WatorCell(int cellState) {
    super(cellState);
    myLifeChronons = 0;
    myEnergyChronons = 0;
  }

  public enum WATOR_STATES {
    ERROR(-1), WATER(0), FISH(1), SHARK(2);

    private int numVal;

    WATOR_STATES(int numVal) {
      this.numVal = numVal;
    }

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

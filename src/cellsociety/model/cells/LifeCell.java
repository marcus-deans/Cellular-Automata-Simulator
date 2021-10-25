package cellsociety.model.cells;

public class LifeCell extends Cell{
  //dead = 0
  //live = 1

  public LifeCell(int cellState){
    super(cellState);
  }

  public enum LIFE_STATES {
    ERROR(-1), DEAD(0), LIVE(1);

    private int numVal;

    LIFE_STATES(int numVal) {
      this.numVal = numVal;
    }

    public int getValue() {
      return numVal;
    }
  }
}

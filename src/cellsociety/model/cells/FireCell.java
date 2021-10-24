package cellsociety.model.cells;

public class FireCell extends Cell {

  public FireCell(int cellState) {
    super(cellState);
  }

  public enum FIRE_STATES {
    ERROR(-1), EMPTY(0), TREE(1), FIRE(2);

    private int numVal;

    FIRE_STATES(int numVal) {
      this.numVal = numVal;
    }

    public int getValue() {
      return numVal;
    }
  }
}

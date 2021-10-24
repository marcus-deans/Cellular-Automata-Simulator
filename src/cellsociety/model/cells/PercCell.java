package cellsociety.model.cells;

public class PercCell extends Cell{
  public PercCell(int cellState){
    super(cellState);
  }

  public enum PERC_STATES {
    ERROR(-1), EMPTY(0), BLOCKED(1), PERCOLATED(2);

    private int numVal;

    PERC_STATES(int numVal) {
      this.numVal = numVal;
    }

    public int getValue() {
      return numVal;
    }
  }

}

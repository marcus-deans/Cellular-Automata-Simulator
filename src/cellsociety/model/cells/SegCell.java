package cellsociety.model.cells;

public class SegCell extends Cell{
  public SegCell(int cellState){
    super(cellState);
  }

  public enum SEG_STATES {
    ERROR(-1), EMPTY(0), ALPHA(1), BETA(2);

    private int numVal;

    SEG_STATES(int numVal) {
      this.numVal = numVal;
    }

    public int getValue() {
      return numVal;
    }
  }
}

package cellsociety.model.cells;

public class LifeCell extends Cell{
  //dead = 0
  //live = 1

  public LifeCell(int cellState){
    super(cellState);
  }
  public LifeCell(Cell copy) {super(copy);}
}

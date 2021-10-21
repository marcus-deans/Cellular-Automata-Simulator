package cellsociety.model.gamegrids;

import cellsociety.model.cells.Cell;
import cellsociety.model.cells.LifeCell;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class GameGrid {
  //TODO make these private
    Cell[][] myGameGrid;
    int myGameWidth;
    int myGameHeight;
    Cell[] checkingCellNeighbours;
    Cell[][] futureGrid;
    private int myNewValue;

    protected PropertyChangeSupport support;

    public GameGrid(Cell[][] gameGrid){
      myGameGrid = gameGrid;
      myGameWidth = gameGrid.length;
      myGameHeight = gameGrid[0].length;
      myNewValue = 0;
      //futureGrid=gameGrid;

      support = new PropertyChangeSupport(this);
      setupFutureGrid();
    }

    private void setupFutureGrid() {
      futureGrid=new Cell[myGameWidth][myGameHeight];
      for (int i=0; i<myGameWidth; i++) {
        for (int j=0; j<myGameHeight; j++) {
          futureGrid[i][j]=new LifeCell(0);
//          sendViewUpdate("Row", j-1, j);
//          sendViewUpdate("Column", i-1, i);
//          //sendViewUpdate("State", myGameGrid[x][y].getMyCellState(), futureGrid[x][y].getMyCellState());
//          sendViewUpdate("State", -1, futureGrid[i][j].getMyCellState());
        }
      }
    }

    public void runGame(){};

    protected void updateCellValues(){
      myGameGrid = futureGrid;
    }

    //populates Cell[] of the possible neighbours of given cell (max 9)
    protected void computeNeighbours(int cellX, int cellY){
      checkingCellNeighbours=new Cell[9];
      //not changing for some reason
      int iterator = 0;
      for(int x = -1; x < 2; x++){
        int checkX=cellX+x;
        //int checkX = checkingCell.getMyX() + x;
        if(checkX < 0 || checkX >= myGameWidth){
          continue;
        }
        for(int y = -1; y<2; y++){
          int checkY=cellY+y;
          //int checkY = checkingCell.getMyY() + y;
          if(checkY < 0 || checkY >= myGameHeight || (x==0&&y==0)){
            continue;
          }
          checkingCellNeighbours[iterator] = myGameGrid[checkX][checkY]; //out of bounds now?
          iterator++;
        }
      }
    }

    private int newValue(){
      myNewValue = 0;
      return myNewValue;
    }

    public Cell[][] getCellArray() {
      return myGameGrid;
    }

  /**
   * Add an observer/listener to GameGrid's instance of PropertyChangeSupport, which enables GameGrid to notify that observer
   * of changes in its state
   * @param pcl the PropertyChangeListener to add
   */
  public void addPropertyChangeListener(PropertyChangeListener pcl){
    support.addPropertyChangeListener(pcl);
  }

  protected void sendViewUpdate(String propertyName, Object oldValue, Object newValue){
    support.firePropertyChange(propertyName, oldValue, newValue);
  }
}

package cellsociety.model.gamegrids;

import cellsociety.model.cells.Cell;
import cellsociety.model.cells.LifeCell;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;

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
      myGameWidth = gameGrid[0].length;
      myGameHeight = gameGrid.length;
      System.out.println("height"+ myGameHeight);
      System.out.println("width"+myGameWidth);
      myNewValue = 0;
      //futureGrid=gameGrid;
      support = new PropertyChangeSupport(this);
      setupFutureGrid();
    }

    private void setupFutureGrid() {
      futureGrid=new Cell[myGameHeight][myGameWidth];
      for (int i=0; i<myGameWidth; i++) {
        for (int j=0; j<myGameHeight; j++) {
          futureGrid[j][i]=new LifeCell(0);
        }
      }
    }

  public void updateInitialFutureGrid() {
    futureGrid=new Cell[myGameHeight][myGameWidth];
    for (int i=0; i<myGameWidth; i++) {
      for (int j=0; j<myGameHeight; j++) {
        //futureGrid[i][j]=new LifeCell(0);
        //getting out of bounds when not a square
        System.out.println("does this print"+myGameGrid[j][i]);
        System.out.println(j);
        futureGrid[j][i]=new LifeCell(myGameGrid[j][i]);
        sendViewUpdate("Row", j-1, j);
        sendViewUpdate("Column", i-1, i);
        //sendViewUpdate("State", myGameGrid[x][y].getMyCellState(), futureGrid[x][y].getMyCellState());
        sendViewUpdate("State", -1, futureGrid[j][i].getMyCellState());
        System.out.println("sent");
        System.out.println(futureGrid[j][i].getMyCellState());
      }
    }
  }

    public void runGame(){};

    protected void updateCellValues(){
      //myGameGrid = futureGrid;
      for (int i=0; i< futureGrid.length; i++) {
        for (int j=0; j< futureGrid[0].length; j++) {
          myGameGrid[i][j]=new LifeCell(futureGrid[i][j]);
        }
      }
      //boolean unequal=(myGameGrid==futureGrid);
      //always false so not the issue
    }

    //populates Cell[] of the possible neighbours of given cell (max 9)
    protected void computeNeighbours(int cellX, int cellY){
      checkingCellNeighbours=new Cell[9];
      //not changing for some reason
      int iterator = 0;
      for(int x = -1; x < 2; x++){
        int checkCol =cellX+x;
        //int checkX = checkingCell.getMyX() + x;
        if(checkCol < 0 || checkCol >= myGameWidth){
          continue;
        }
        for(int y = -1; y<2; y++){
          int checkRow=cellY+y;
          //int checkY = checkingCell.getMyY() + y;
          if(checkRow < 0 || checkRow >= myGameHeight || (x==0&&y==0)){
            continue;
          }
          checkingCellNeighbours[iterator] = myGameGrid[checkRow][checkCol]; //out of bounds now?
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

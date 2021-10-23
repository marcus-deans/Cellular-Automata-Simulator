package cellsociety.view;

import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


/**
 * JavaFX View class
 */
public class GridView implements PropertyChangeListener {
  private GridPane myGameGrid;
  private int myWidthNumber;
  private int myHeightNumber;
  private int myGridDimensions = GameView.gridDisplayLength; //TODO: get from controller instead
  private int myCellWidth;
  private int myCellHeight;

  private int currentRow;
  private int currentColumn;
  private int currentState;


  public GridView(int height, int width, String[] colors){
    myGameGrid = new GridPane();
    myWidthNumber = width;
    myHeightNumber = height;

    determineCellDimensions();
    populateNewGrid();
  }

  private void determineCellDimensions(){
    //TODO: fix computation
    myCellWidth = (myGridDimensions-30)/myWidthNumber;
    myCellHeight = (myGridDimensions-30)/myHeightNumber;
  }

  private Rectangle createNewCellView(int state){
    Rectangle newCell = new Rectangle();
    newCell.setWidth(myCellWidth);
    newCell.setHeight(myCellHeight);
    newCell.setStroke(Color.WHITE);
    if(state == 0){
      //newCell.setId("dead-cell-view");
      newCell.setFill(Color.web("blue"));
    }
    if(state == 1){
      //newCell.setId("live-cell-view");
      newCell.setFill(Color.web("purple"));
    }
    return newCell;
  }

  private void populateNewGrid(){
    for(int column = 0; column<myWidthNumber; column++){
      for(int row =0; row<myHeightNumber; row++){
        myGameGrid.add(createNewCellView(0), column, row);
      }
    }
  }

  public GridPane getMyGameGrid(){
    myGameGrid.setGridLinesVisible(true);
    return myGameGrid;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    String propertyName = evt.getPropertyName();
    if(propertyName.equals("Row")){
      currentRow = (int) evt.getNewValue();
    }
    if(propertyName.equals("Column")){
      currentColumn = (int) evt.getNewValue();
    }
    if(propertyName.equals("State")){
      currentState = (int) evt.getNewValue();
      myGameGrid.add(createNewCellView(currentState), currentColumn, currentRow);
    }
  }
}
package cellsociety.view;

import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


/**
 * JavaFX View class
 */
public class GridView {
  private GridPane myGameGrid;
  private int myWidthNumber;
  private int myHeightNumber;
  private int myGridDimensions = GameView.gridDisplayLength; //TODO: get from controller instead
  private int myCellWidth;
  private int myCellHeight;
//  private Paint DEAD_CELL = Color.BLACK;


  public GridView(int width, int height){
    myGameGrid = new GridPane();
    myWidthNumber = width;
    myHeightNumber = height;

    determineCellDimensions();
    populateNewGrid();
//    myGameGrid.setVgap(0.5);
//    myGameGrid.setHgap(0.5);
  }

  private void determineCellDimensions(){
    //TODO: fix computation
    myCellWidth = (myGridDimensions-30)/myWidthNumber;
    myCellHeight = (myGridDimensions-30)/myHeightNumber;
  }

  private Rectangle createNewCellView(){
    Rectangle newCell = new Rectangle();
    newCell.setWidth(myCellWidth);
    newCell.setHeight(myCellHeight);
//    newCell.setFill(DEAD_CELL);
    newCell.setId("dead-cell-view");
    return newCell;
  }

  private void populateNewGrid(){
    for(int x = 0; x<myWidthNumber; x++){
      for(int y =0; y<myHeightNumber; y++){
        //gridpane.add(Node, column, row)
        myGameGrid.add(createNewCellView(), x, y);
//        Rectangle newCellView = createNewCellView();
//        GridPane.setColumnIndex(newCellView, x);
//        GridPane.setRowIndex(newCellView, y);
      }
    }
  }

  public GridPane getMyGameGrid(){
    myGameGrid.setGridLinesVisible(true);
    return myGameGrid;
  }
}
package cellsociety.view;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;


/**
 * JavaFX View class
 */
public class GridView implements GridListener {

  private String[] myGridColours;
  private GridPane myGameGrid;
  private int myWidthNumber;
  private int myHeightNumber;
  private int myGridDimensions;
  private int myCellWidth;
  private int myCellHeight;
  private static final int LINE_SIZE = 6;

  public GridView(int height, int width, String[] gridColours, int gridDisplayLength) {
    myGameGrid = new GridPane();
    myWidthNumber = width;
    myHeightNumber = height;
    myGridColours = gridColours;
    myGridDimensions = gridDisplayLength;

    determineCellDimensions();
    populateNewGrid();
  }

  private void determineCellDimensions() {
    //TODO: fix computation
    myCellWidth = (myGridDimensions - LINE_SIZE) / myWidthNumber;
    myCellHeight = (myGridDimensions - LINE_SIZE) / myHeightNumber;
  }

  private Rectangle createNewCellView(int state) {
    Rectangle newCell = new Rectangle();
    newCell.setWidth(myCellWidth);
    newCell.setHeight(myCellHeight);

    newCell.setId("cell-view");
    newCell.setFill(Paint.valueOf(myGridColours[state]));
    return newCell;
  }

  private void populateNewGrid() {
    for (int column = 0; column < myWidthNumber; column++) {
      for (int row = 0; row < myHeightNumber; row++) {
        myGameGrid.add(createNewCellView(0), column, row);
      }
    }
  }

  public GridPane getMyGameGrid() {
    myGameGrid.setGridLinesVisible(true);
    return myGameGrid;
  }
  public int getMyCellWidth() {
    return myCellWidth;
  }
  public int getMyCellHeight() {
    return myCellHeight;
  }

  public int[] updateCellOnClick(double x, double y) {

    return new int[]{0,0};
  }

  @Override
  public void update(int row, int column, int state) {
    myGameGrid.add(createNewCellView(state), column, row);
  }
}
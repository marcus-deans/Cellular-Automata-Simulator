package cellsociety.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;


/**
 * JavaFX View class
 */
public class GridView implements PropertyChangeListener {

  private String[] myGridColours;
  private GridPane myGameGrid;
  private int myWidthNumber;
  private int myHeightNumber;
  private int myGridDimensions;
  private int myCellWidth;
  private int myCellHeight;

  private int currentRow;
  private int currentColumn;
  private int currentState;


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
    myCellWidth = (myGridDimensions - 30) / myWidthNumber;
    myCellHeight = (myGridDimensions - 30) / myHeightNumber;
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

  public int[] updateCellOnClick(double x, double y) {

    return new int[]{0,0};
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    String propertyName = evt.getPropertyName();
    if (propertyName.equals("Row")) {
      currentRow = (int) evt.getNewValue();
    }
    if (propertyName.equals("Column")) {
      currentColumn = (int) evt.getNewValue();
    }
    if (propertyName.equals("State")) {
      currentState = (int) evt.getNewValue();
      myGameGrid.add(createNewCellView(currentState), currentColumn, currentRow);
    }
  }
}
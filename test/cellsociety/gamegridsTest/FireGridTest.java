package cellsociety.gamegridsTest;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import cellsociety.model.cells.Cell;
import cellsociety.model.cells.LifeCell;
import cellsociety.model.gamegrids.FireGrid;
import cellsociety.model.gamegrids.GameGrid;
import org.junit.jupiter.api.Test;

public class FireGridTest {

  @Test
  void SmallFireArray() {
    Cell[][] array={{new LifeCell(0), new LifeCell(0), new LifeCell(0)}, {new LifeCell(1), new LifeCell(2), new LifeCell(1)}, {new LifeCell(0), new LifeCell(0), new LifeCell(0)}};
    GameGrid grid=new FireGrid(array, "Fire", 0, 0);
    grid.runGame();
    Cell[][] a=grid.getCellArray();
    int[] cellRow={0,0,0};
    int[] compareRow = createComparisonRow(cellRow, a, 0);
    assertArrayEquals(cellRow, compareRow);
    int[] cellRow2={2,0,2};
    int[] compareRow2 = createComparisonRow(cellRow2, a, 1);
    assertArrayEquals(cellRow2, compareRow2);
    int[] cellRow3={0,0,0};
    int[] compareRow3 = createComparisonRow(cellRow3, a, 2);
    assertArrayEquals(cellRow3, compareRow3);
  }

  @Test
  void SmallTreeArray() {
    Cell[][] array={{new LifeCell(0), new LifeCell(0), new LifeCell(0)}, {new LifeCell(1), new LifeCell(2), new LifeCell(1)}, {new LifeCell(0), new LifeCell(0), new LifeCell(0)}};
    GameGrid grid=new FireGrid(array, "Fire", 0, 1);
    grid.runGame();
    Cell[][] a=grid.getCellArray();
    int[] cellRow={1,1,1};
    int[] compareRow = createComparisonRow(cellRow, a, 0);
    assertArrayEquals(cellRow, compareRow);
    int[] cellRow2={2,0,2};
    int[] compareRow2 = createComparisonRow(cellRow2, a, 1);
    assertArrayEquals(cellRow2, compareRow2);
    int[] cellRow3={1,1,1};
    int[] compareRow3 = createComparisonRow(cellRow3, a, 2);
    assertArrayEquals(cellRow3, compareRow3);
  }
  //fire still spreads with probablity of 0

  private int[] createComparisonRow(int[] cellRow, Cell[][] a, int i2) {
    int[] compareRow = new int[cellRow.length];
    for (int i = 0; i < cellRow.length; i++) {
      compareRow[i] = a[i2][i].getMyCellState();
    }
    return compareRow;
  }

}

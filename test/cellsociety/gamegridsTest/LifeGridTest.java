package cellsociety.gamegridsTest;
import cellsociety.model.cells.Cell;
import cellsociety.model.cells.LifeCell;
import cellsociety.model.gamegrids.GameGrid;
import cellsociety.model.gamegrids.LifeGrid;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class LifeGridTest {

  @Test
  void Test3x3SampleCellArray() {
    Cell[][] array={{new LifeCell(0), new LifeCell(1), new LifeCell(0)}, {new LifeCell(0), new LifeCell(1), new LifeCell(1)}, {new LifeCell(1), new LifeCell(0), new LifeCell(1)}};
    GameGrid grid = new LifeGrid(array);
    grid.runGame();
    Cell[][] a=grid.getCellArray();
    int[] cellRow={0,1,1};
    int[] compareRow = createComparisonRow(cellRow, a, 0);
    assertArrayEquals(cellRow, compareRow);
    int[] cellRow2={1,0,1};
    int[] compareRow2 = createComparisonRow(cellRow2, a, 1);
    assertArrayEquals(cellRow2, compareRow2);
    int[] cellRow3={0,1,1};
    int[] compareRow3 = createComparisonRow(cellRow3, a, 2);
    assertArrayEquals(cellRow3, compareRow3);
  }

  @Test
  void Test2x2SampleCellArray() {
    Cell[][] array={{new LifeCell(0), new LifeCell(1)}, {new LifeCell(1), new LifeCell(1)}};
    GameGrid grid = new LifeGrid(array);
    grid.runGame();
    Cell[][] a=grid.getCellArray();
    int[] cellRow={1,1};
    int[] compareRow = createComparisonRow(cellRow, a, 0);
    assertArrayEquals(cellRow, compareRow);
    int[] cellRow2={1,1};
    int[] compareRow2 = createComparisonRow(cellRow2, a, 1);
    assertArrayEquals(cellRow2, compareRow2);
  }

  private int[] createComparisonRow(int[] cellRow, Cell[][] a, int i2) {
    int[] compareRow = new int[cellRow.length];
    for (int i = 0; i < cellRow.length; i++) {
      compareRow[i] = a[i2][i].getMyCellState();
    }
    return compareRow;
  }
}


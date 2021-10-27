package cellsociety.gamegridsTest;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cellsociety.model.cells.Cell;
import cellsociety.model.cells.SegCell;
import cellsociety.model.gamegrids.GameGrid;
import cellsociety.model.gamegrids.SegGrid;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class SegGridTest {

  @Test
  void simpleArray() {
    Map<String, String> configurationMap=new HashMap<>();
    configurationMap.put("similarProportion", ".5");
    Cell[][] array = createCellArray(new int[][]{{1,2,1}, {1,0,0}, {0,0,1}});
    //cells to move: 2, 1
    GameGrid grid = new SegGrid(array, "Seg", configurationMap);
    grid.runGame();
    Cell[][] a = grid.getGameGrid();
    assertEquals(1, a[0][0].getMyCellState());
    assertEquals(0, a[0][1].getMyCellState());
    assertEquals(1, a[1][0].getMyCellState());
    assertEquals(1, a[2][2].getMyCellState());
    assertTrue(a[1][1].getMyCellState()==2 || a[1][2].getMyCellState()==2 ||a[2][0].getMyCellState()==2 || a[2][1].getMyCellState()==2);
    assertTrue(a[1][1].getMyCellState()==1 || a[1][2].getMyCellState()==1 ||a[2][0].getMyCellState()==1|| a[2][1].getMyCellState()==2);
  }

  @Test
  void moreComplicatedArray() {
    Cell[][] array = createCellArray(new int[][]{{1,2,1}, {1,0,0}, {0,0,1}});
  }

  @Test
  void noMovement() {
    Map<String, String> configurationMap=new HashMap<>();
    configurationMap.put("similarProportion", ".5");
    int[][] start=new int[][]{{1,1,1}, {0,0,0}, {2,2,2}};
    Cell[][] array = createCellArray(start);
    GameGrid grid = new SegGrid(array, "Seg", configurationMap);
    grid.runGame();
    Cell[][] a = grid.getGameGrid();
    assertArrayEquals(start, new int[][]{createComparisonRow(a[0]), createComparisonRow(a[1]), createComparisonRow(a[2])});
  }

  private Cell[][] createCellArray(int[][] a) {
    Cell[][] ret = new Cell[a.length][a[0].length];
    for (int i=0; i<a.length; i++) {
      for (int j=0; j<a[0].length; j++) {
        ret[i][j]=new SegCell(a[i][j]);
      }
    }
    return ret;
  }
  private int[] createComparisonRow(Cell[] row) {
    int[] compareRow = new int[row.length];
    for (int i = 0; i < row.length; i++) {
      compareRow[i] = row[i].getMyCellState();
    }
    return compareRow;
  }

}

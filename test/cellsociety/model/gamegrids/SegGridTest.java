package cellsociety.model.gamegrids;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cellsociety.model.cells.Cell;
import cellsociety.model.cells.SegCell;
import cellsociety.model.gamegrids.GameGrid;
import cellsociety.model.gamegrids.SegGrid;
import cellsociety.util.ReflectionException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class SegGridTest {

  @Test
  void simpleArray() throws ReflectionException {
    Map<String, String> configurationMap=new HashMap<>();
    configurationMap.put("similarProportion", ".5");
    Cell[][] array = createCellArray(new int[][]{{1,2,1}, {1,0,0}, {0,0,1}});
    GameGrid grid = new SegGrid(array, "Seg", configurationMap);
    grid.runGame();
    int[][] end=new int[3][3];
    for (int i=0; i<3; i++) {
      for (int j=0; j<3; j++) {
        end[j][i]=grid.getCellValue(j, i);
      }
    }
    assertEquals(1, end[0][0]);
    assertEquals(0, end[0][1]);
    assertEquals(1, end[1][0]);
    assertEquals(1, end[2][2]);
    assertTrue(end[1][1]==2 || end[1][2]==2 ||end[2][0]==2 || end[2][1]==2);
    assertTrue(end[1][1]==1 || end[1][2]==1 ||end[2][0]==1|| end[2][1]==1);
  }

  @Test
  void forcedMovement() throws ReflectionException {
    Cell[][] array = createCellArray(new int[][]{{2,2,2}, {2,1,2}, {0,2,2}});
    Map<String, String> configurationMap=new HashMap<>();
    configurationMap.put("similarProportion", ".5");
    GameGrid grid = new SegGrid(array, "Seg", configurationMap);
    grid.runGame();
    int[][] end=createIntArray(3,3,grid);
    int[][] expected={{2,2,2},{2,0,2},{1,2,2}};
    assertArrayEquals(expected, end);
  }

  @Test
  void forcedMovementRound2Alternating() throws ReflectionException {
    Cell[][] array = createCellArray(new int[][]{{2,2,2}, {2,1,2}, {0,2,2}});
    Map<String, String> configurationMap=new HashMap<>();
    configurationMap.put("similarProportion", ".5");
    GameGrid grid = new SegGrid(array, "Seg", configurationMap);
    grid.runGame();
    grid.runGame();
    int[][] end=createIntArray(3,3,grid);
    int[][] expected={{2,2,2},{2,1,2},{0,2,2}};
    assertArrayEquals(expected, end);
  }

  @Test
  void noMovement() throws ReflectionException {
    Map<String, String> configurationMap=new HashMap<>();
    configurationMap.put("similarProportion", ".5");
    int[][] start=new int[][]{{1,1,1}, {0,0,0}, {2,2,2}};
    Cell[][] array = createCellArray(start);
    GameGrid grid = new SegGrid(array, "Seg", configurationMap);
    grid.runGame();
    int[][] end=new int[3][3];
    for (int i=0; i<3; i++) {
      for (int j=0; j<3; j++) {
        end[j][i]=grid.getCellValue(j, i);
      }
    }
    assertArrayEquals(start, end);
  }

  private int[][] createIntArray(int rowSize, int colSize, GameGrid g) {
    int[][] ret = new int[rowSize][colSize];
    for (int row=0; row<rowSize; row++) {
      for (int col=0; col<colSize; col++) {
        ret[row][col]= g.getCellValue(row, col);
      }
    }
    return ret;
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

}

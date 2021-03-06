package cellsociety.model.gamegrids;

import cellsociety.model.cells.Cell;
import cellsociety.model.cells.SegCell;
import cellsociety.model.gamegrids.GameGrid;
import cellsociety.model.gamegrids.PercGrid;
import cellsociety.util.ReflectionException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class PercGridTest {
  @Test
  void TestSimplePercGrid() throws ReflectionException {
    int[][] startArray={{2,1,2}, {2,0,2}, {2,0,2}};
    Cell[][] startCells=createCellArray(startArray);
    Map<String, String> configurationMap=new HashMap<>();
    GameGrid g=new PercGrid(startCells, "Perc", configurationMap);
    g.runGame();
    int[][] end = new int[3][3];
    for (int i=0; i<3; i++) {
      for (int j=0; j<3;j++) {
        end[j][i]=g.getCellValue(j, i);
      }
    }
    int[][] expected={{2,1,2}, {2, 1,2}, {2, 0, 2}};
    assertArrayEquals(end, expected);
  }

  @Test
  void TestSimplePercGridRound2() throws ReflectionException {
    int[][] startArray={{2,1,2}, {2,0,2}, {2,0,2}};
    Cell[][] startCells=createCellArray(startArray);
    Map<String, String> configurationMap=new HashMap<>();
    GameGrid g=new PercGrid(startCells, "Perc", configurationMap);
    g.runGame();
    g.runGame();
    int[][] end=new int[3][3];
    for (int i=0; i<3; i++) {
      for (int j=0; j<3; j++) {
        end[j][i]=g.getCellValue(j, i);
      }
    }
    int[][] expected={{2,1,2}, {2, 1,2}, {2, 1, 2}};
    assertArrayEquals(expected, end);
  }

  @Test
  void TestNoMovementPercGrid() throws ReflectionException {
    int[][] startArray={{2,1,2}, {2,2,2}, {2,0,2}};
    Cell[][] startCells=createCellArray(startArray);
    Map<String, String> configurationMap=new HashMap<>();
    GameGrid g=new PercGrid(startCells, "Perc", configurationMap);
    g.runGame();
    int[][] end=new int[3][3];
    for (int i=0; i<3; i++) {
      for (int j=0; j<3; j++) {
        end[j][i]=g.getCellValue(j, i);
      }
    }
    int[][] expected={{2,1,2}, {2, 2,2}, {2, 0, 2}};
    assertArrayEquals(expected, end);
  }

  @Test
  void TestLessSimplePercGrid() throws ReflectionException {
    int[][] startArray={{2,2,0}, {2,1,2}, {0,2,2}};
    Cell[][] startCells=createCellArray(startArray);
    Map<String, String> configurationMap=new HashMap<>();
    GameGrid g=new PercGrid(startCells, "Perc", configurationMap);
    g.runGame();
    int[][] end=new int[3][3];
    for (int i=0; i<3; i++) {
      for (int j=0; j<3; j++) {
        end[j][i]=g.getCellValue(j, i);
      }
    }
    int[][] expected={{2,2,1}, {2,1,2}, {1, 2, 2}};
    assertArrayEquals(expected, end);
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

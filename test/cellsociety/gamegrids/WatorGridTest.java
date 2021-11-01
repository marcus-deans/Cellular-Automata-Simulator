package cellsociety.gamegrids;

import cellsociety.model.cells.Cell;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import cellsociety.model.cells.WatorCell;
import cellsociety.model.gamegrids.GameGrid;
import cellsociety.model.gamegrids.WatorGrid;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WatorGridTest {
  @Test
  void simpleShark() {
    Map<String, String> configurationMap=new HashMap<>();
    configurationMap.put("fish_lifespan", "2");
    configurationMap.put("shark_lifespan", "2");
    configurationMap.put("shark_energy", "2");
    int[][] start=new int[][]{{2,1}, {0,0}};
    Cell[][] array = createCellArray(start);
    GameGrid g = new WatorGrid(array, "Wator", configurationMap);
    g.runGame();
    int[][] end=createIntArray(2, 2, g);
    int[][] expected = {{0,2}, {0,0}};
    assertArrayEquals(expected, end);
  }

  @Test
  void sharkWrapAround() {
    Map<String, String> configurationMap=new HashMap<>();
    configurationMap.put("fish_lifespan", "2");
    configurationMap.put("shark_lifespan", "2");
    configurationMap.put("shark_energy", "2");
    int[][] start=new int[][]{{2,0,0},{0,0,0}, {1,0,0}};
    Cell[][] array = createCellArray(start);
    GameGrid g = new WatorGrid(array, "Wator", configurationMap);
    g.runGame();
    int[][] end = createIntArray(3, 3, g);
    int[][] expected = {{0,0,0}, {0,0,0}, {2,0,0}};
    assertArrayEquals(expected, end);
  }

  @Test
  void fishUnreachable() {
    Map<String, String> configurationMap=new HashMap<>();
    configurationMap.put("fish_lifespan", "2");
    configurationMap.put("shark_lifespan", "2");
    configurationMap.put("shark_energy", "2");
    int[][] start=new int[][]{{0,0,0},{0,2,0}, {0,0,1}};
    Cell[][] array = createCellArray(start);
    GameGrid g = new WatorGrid(array, "Wator", configurationMap);
    g.runGame();
    int[][] end = createIntArray(3, 3, g);
    assertNotEquals(2, end[2][2]);
  }

  @Test
  void simpleFish() {
    Map<String, String> configurationMap=new HashMap<>();
    configurationMap.put("fish_lifespan", "2");
    configurationMap.put("shark_lifespan", "2");
    configurationMap.put("shark_energy", "2");
    int[][] start=new int[][]{{1,0,0},{0,0,0}, {0,0,0}};
    Cell[][] array = createCellArray(start);
    GameGrid g = new WatorGrid(array, "Wator", configurationMap);
    g.runGame();
    int[][] end = createIntArray(3, 3, g);
    assertEquals(0, end[0][0]);
    assertTrue(end[0][1]==1 || end[1][0]==1 || end[2][0]==1 || end[0][2]==1);
    int fishCount = getFishCount(end);
    assertEquals(1, fishCount);
  }

  private int getFishCount(int[][] end) {
    int fishCount=0;
    for (int i=0; i< end.length; i++) {
      for (int j=0; j< end.length; j++) {
        if (end[i][j]==1) {
          fishCount++;
        }
      }
    }
    return fishCount;
  }

  @Test
  void reproduceFish() {
    Map<String, String> configurationMap=new HashMap<>();
    configurationMap.put("fish_lifespan", "2");
    configurationMap.put("shark_lifespan", "1");
    configurationMap.put("shark_energy", "1");
    int[][] start=new int[][]{{1,0,0},{0,0,0}, {0,0,0}};
    Cell[][] array = createCellArray(start);
    GameGrid g = new WatorGrid(array, "Wator", configurationMap);
    g.runGame();
    int[][] end = createIntArray(3, 3, g);
    int fishCount=getFishCount(end);
    assertEquals(fishCount, 1);
    g.runGame();
    end = createIntArray(3, 3, g);
    fishCount=getFishCount(end);
    assertEquals(fishCount, 2);
    g.runGame();
    end = createIntArray(3, 3, g);
    fishCount=getFishCount(end);
    assertEquals(fishCount, 2);
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
    for (int row=0; row<a.length; row++) {
      for (int col=0; col<a[0].length; col++) {
        ret[row][col]=new WatorCell(a[row][col], col, row);
      }
    }
    return ret;
  }

}

package cellsociety.gamegridsTest;

import cellsociety.model.cells.Cell;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import cellsociety.model.cells.WatorCell;
import cellsociety.model.gamegrids.GameGrid;
import cellsociety.model.gamegrids.WatorGrid;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class WatorGridTest {
  @Test
  void simpleShark() {
    Map<String, String> configurationMap=new HashMap<>();
    configurationMap.put("fish_lifespan", "1");
    configurationMap.put("shark_lifespan", "1");
    configurationMap.put("shark_energy", "1");
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
    configurationMap.put("fish_lifespan", "1");
    configurationMap.put("shark_lifespan", "1");
    configurationMap.put("shark_energy", "1");
    int[][] start=new int[][]{{2,0,0},{0,0,0}, {1,0,0}};
    Cell[][] array = createCellArray(start);
    GameGrid g = new WatorGrid(array, "Wator", configurationMap);
    g.runGame();
    int[][] end = createIntArray(3, 3, g);
    int[][] expected = {{0,0,0}, {0,0,0}, {2,0,0}};
    assertArrayEquals(expected, end);
  }

  @Test
  void simpleFish() {
    Map<String, String> configurationMap=new HashMap<>();
    configurationMap.put("fish_lifespan", "1");
    configurationMap.put("shark_lifespan", "1");
    configurationMap.put("shark_energy", "1");
    int[][] start=new int[][]{{2,0,0},{0,0,0}, {1,0,0}};

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

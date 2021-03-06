package cellsociety.model.gamegrids;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import cellsociety.model.cells.Cell;
import cellsociety.model.cells.FireCell;
import cellsociety.model.cells.LifeCell;
import cellsociety.model.gamegrids.FireGrid;
import cellsociety.model.gamegrids.GameGrid;
import cellsociety.util.ReflectionException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class FireGridTest {

  @Test
  void SmallFireArray() throws ReflectionException {
    Map<String, String> configurationMap;
    Cell[][] array={{new FireCell(0), new FireCell(0), new FireCell(0)}, {new FireCell(1), new FireCell(2), new FireCell(1)}, {new FireCell(0), new FireCell(0), new FireCell(0)}};
    configurationMap = new HashMap<>();
    configurationMap.put("probCatch", "0");
    configurationMap.put("fillTree", "0");
    GameGrid grid=new FireGrid(array, "Fire", configurationMap);
    grid.runGame();
    int[][] end=new int[3][3];
    for (int i=0; i<3; i++) {
      for (int j=0; j<3; j++) {
        end[j][i]=grid.getCellValue(j, i);
      }
    }
    int[] cellRow={0,0,0};
    int[] compareRow = end[0];
    assertArrayEquals(cellRow, compareRow);
    int[] cellRow2={2,0,2};
    int[] compareRow2 = end[1];
    assertArrayEquals(cellRow2, compareRow2);
    int[] cellRow3={0,0,0};
    int[] compareRow3 = end[2];
    assertArrayEquals(cellRow3, compareRow3);
  }

  @Test
  void LargerFireCorrectNeighbors() throws ReflectionException {
    Map<String, String> configurationMap;
    Cell[][] array={{new FireCell(0), new FireCell(1), new FireCell(1)}, {new FireCell(1), new FireCell(2), new FireCell(1)}, {new FireCell(0), new FireCell(1), new FireCell(0)}};
    configurationMap = new HashMap<>();
    configurationMap.put("probCatch", "0");
    configurationMap.put("fillTree", "0");
    GameGrid grid=new FireGrid(array, "Fire", configurationMap);
    grid.runGame();
    int[][] end=new int[3][3];
    for (int i=0; i<3; i++) {
      for (int j=0; j<3; j++) {
        end[j][i]=grid.getCellValue(j, i);
      }
    }
    int[] cellRow={0,2,1};
    int[] compareRow = end[0];
    assertArrayEquals(cellRow, compareRow);
    int[] cellRow2={2,0,2};
    int[] compareRow2 = end[1];
    assertArrayEquals(cellRow2, compareRow2);
    int[] cellRow3={0,2,0};
    int[] compareRow3 = end[2];
    assertArrayEquals(cellRow3, compareRow3);
  }

  @Test
  void SmallTreeArray() throws ReflectionException {
    Map<String, String> configurationMap;
    Cell[][] array={{new LifeCell(0), new LifeCell(0), new LifeCell(0)}, {new LifeCell(1), new LifeCell(2), new LifeCell(1)}, {new LifeCell(0), new LifeCell(0), new LifeCell(0)}};
    configurationMap = new HashMap<String, String>();
    configurationMap.put("probCatch", "0");
    configurationMap.put("fillTree", "1");
    GameGrid grid=new FireGrid(array, "Fire", configurationMap);
    grid.runGame();
    int[][] end=new int[3][3];
    for (int i=0; i<3; i++) {
      for (int j=0; j<3; j++) {
        end[j][i]=grid.getCellValue(j, i);
      }
    }
    int[] cellRow={1,1,1};
    int[] compareRow = end[0];
    assertArrayEquals(cellRow, compareRow);
    int[] cellRow2={2,0,2};
    int[] compareRow2 =end[1];
    assertArrayEquals(cellRow2, compareRow2);
    int[] cellRow3={1,1,1};
    int[] compareRow3 = end[2];
    assertArrayEquals(cellRow3, compareRow3);
  }

  @Test
  void probCatch1() throws ReflectionException {
    Map<String, String> configurationMap;
    Cell[][] array = {{new FireCell(1), new FireCell(1), new FireCell(1)},
        {new FireCell(1), new FireCell(1), new FireCell(1)},
        {new FireCell(1), new FireCell(1), new FireCell(1)}};
    configurationMap = new HashMap<>();
    configurationMap.put("probCatch", "1");
    configurationMap.put("fillTree", "0");
    GameGrid grid = new FireGrid(array, "Fire", configurationMap);
    grid.runGame();
    int[][] end = createIntArray(3,3, grid);
    int[][] expected = {{2, 2, 2}, {2, 2, 2}, {2, 2, 2}};
    assertArrayEquals(expected, end);
    grid.runGame();
    end=createIntArray(3,3,grid);
    expected = new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
    assertArrayEquals(expected, end);

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
}

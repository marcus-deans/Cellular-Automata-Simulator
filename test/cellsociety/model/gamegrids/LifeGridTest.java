package cellsociety.model.gamegrids;
import cellsociety.model.cells.Cell;
import cellsociety.model.cells.LifeCell;
import cellsociety.model.gamegrids.GameGrid;
import cellsociety.model.gamegrids.LifeGrid;
import cellsociety.util.ReflectionException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class LifeGridTest {
  private Map<String, String> configurationMap;

  @Test
  void Test3x3SampleCellArray() throws ReflectionException {
    Cell[][] array={{new LifeCell(0), new LifeCell(1), new LifeCell(0)}, {new LifeCell(0), new LifeCell(1), new LifeCell(1)}, {new LifeCell(1), new LifeCell(0), new LifeCell(1)}};
    configurationMap = new HashMap<>();
    GameGrid grid = new LifeGrid(array, "Life", configurationMap);
    grid.runGame();
    //Cell[][] a=grid.getGameGrid();
    int[][] end=new int[3][3];
    for (int i=0; i<3; i++) {
      for (int j=0; j<3; j++) {
        end[j][i]=grid.getCellValue(j, i);
      }
    }
    int[] cellRow={0,1,1};
    assertArrayEquals(cellRow, end[0]);
    int[] cellRow2={1,0,1};
    assertArrayEquals(cellRow2, end[1]);
    int[] cellRow3={0,0,1};
    assertArrayEquals(cellRow3, end[2]);
  }

  @Test
  void Test3x3SampleCellArrayRound2() throws ReflectionException {
    Cell[][] array={{new LifeCell(0), new LifeCell(1), new LifeCell(0)}, {new LifeCell(0), new LifeCell(1), new LifeCell(1)}, {new LifeCell(1), new LifeCell(0), new LifeCell(1)}};
    configurationMap = new HashMap<>();
    GameGrid grid = new LifeGrid(array, "Life", configurationMap);
    grid.runGame();
    grid.runGame();
    int[][] end=new int[3][3];
    for (int i=0; i<3; i++) {
      for (int j=0; j<3; j++) {
        end[j][i]=grid.getCellValue(j, i);
      }
    }
    int[] cellRow={0,1,1};
    assertArrayEquals(cellRow, end[0]);
    int[] cellRow2={0,0,1};
    assertArrayEquals(cellRow2, end[1]);
    int[] cellRow3={0,1,0};
    assertArrayEquals(cellRow3, end[2]);
  }

  @Test
  void Test2x2SampleCellArray() throws ReflectionException {
    Cell[][] array={{new LifeCell(0), new LifeCell(1)}, {new LifeCell(1), new LifeCell(1)}};
    configurationMap = new HashMap<>();
    GameGrid grid = new LifeGrid(array, "Life", configurationMap);
    grid.runGame();
    int[][] end=new int[2][2];
    for (int i=0; i<2; i++) {
      for (int j=0; j<2; j++) {
        end[j][i]=grid.getCellValue(j, i);
      }
    }
    int[] cellRow={1,1};
    assertArrayEquals(cellRow, end[0]);
    int[] cellRow2={1,1};
    assertArrayEquals(cellRow2, end[1]);
  }

}


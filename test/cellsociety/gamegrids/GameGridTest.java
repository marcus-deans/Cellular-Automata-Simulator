package cellsociety.gamegrids;

import cellsociety.model.cells.Cell;
import cellsociety.model.cells.PercCell;
import cellsociety.model.cells.WatorCell;
import cellsociety.model.gamegrids.GameGrid;
import cellsociety.model.gamegrids.PercGrid;
import cellsociety.model.gamegrids.WatorGrid;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameGridTest {

  @Test
  void updateOneCell() {
    Cell[][] array={{new PercCell(0), new PercCell(1), new PercCell(2)}};
    GameGrid g = new PercGrid(array, "Perc", new HashMap<>());
    g.updateOneCell(0,0);
    assertEquals(1, g.getCellValue(0,0));
    g.updateOneCell(0, 1);
    assertEquals(2, g.getCellValue(0, 1));
    g.updateOneCell(0, 2);
    assertEquals(0, g.getCellValue(0, 2));
  }

  @Test
  void updateOneCellCheckRunGame() {
    Cell[][] array={{new WatorCell(0), new WatorCell(2), new WatorCell(0)}};
    GameGrid g = new WatorGrid(array, "Wator", Map.of("fish_lifespan", "2", "shark_lifespan", "2", "shark_energy", "2"));
    g.updateOneCell(0, 1);
    assertEquals(0, g.getCellValue(0, 1));
    g.runGame();
    assertEquals(0, g.getCellValue(0,0));
    assertEquals(0, g.getCellValue(0,1));
    assertEquals(0, g.getCellValue(0,2));
  }

}

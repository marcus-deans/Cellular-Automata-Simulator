package cellsociety.gamegridsTest;

import cellsociety.model.cells.Cell;
import cellsociety.model.cells.LifeCell;
import cellsociety.model.cells.PercCell;
import cellsociety.model.gamegrids.GameGrid;
import cellsociety.model.gamegrids.LifeGrid;
import cellsociety.model.gamegrids.PercGrid;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameGridTest {

  @Test
  void updateOneCell() {
    Cell[][] array={{new PercCell(0), new PercCell(1), new PercCell(2)}};
    GameGrid g = new PercGrid(array, "Perc", new HashMap<String, String>());
    g.updateOneCell(0,0);
    assertEquals(1, g.getGameGrid()[0][0].getMyCellState());
    g.updateOneCell(0, 1);
    assertEquals(2, g.getGameGrid()[0][1].getMyCellState());
    g.updateOneCell(0, 2);
    assertEquals(0, g.getGameGrid()[0][2].getMyCellState());
  }

  @Test
  void updateInitialFutureGrid() {

  }

}

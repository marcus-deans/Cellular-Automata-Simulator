package cellsociety.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cellsociety.util.IncorrectCSVFormatException;
import cellsociety.util.IncorrectSimFormatException;
import cellsociety.util.ReflectionException;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class ControllerTest {

  @Test
  void testSetupSimFile()
      throws ReflectionException, IncorrectSimFormatException, IncorrectCSVFormatException {
    GameController g = new GameController("data/game_of_life/blinkers.sim");
    g.setupProgram();
    Map<String, String> map=g.getConfigurationMap();
    assertEquals("GameOfLife",map.get("Type"));
    assertEquals("game_of_life/blinkers.csv", map.get("InitialStates"));
    assertEquals("John Conway", map.get("Author"));
  }

  @Test
  void testSimFileWithErrors() {
    GameController g = new GameController("data/incorrect_files/missingsimparameter.sim");
    assertThrows(IncorrectSimFormatException.class, ()->g.setupProgram());
  }

  @Test
  void testCSVFileWithErrors() {
    GameController g = new GameController("data/incorrect_files/no_integers.sim");
    assertThrows(IncorrectCSVFormatException.class, ()->g.setupProgram());
  }

  @Test
  void getGridSize()
      throws ReflectionException, IncorrectSimFormatException, IncorrectCSVFormatException {
    GameController g = new GameController("data/game_of_life/penta_decathlon.sim");
    g.setupProgram();
    int[] size =g.getGridSize();
    assertEquals(18, size[0]);
    assertEquals(11, size[1]);
  }

  @Test
  void validateSave() {
    GameController g = new GameController("data/game_of_life/penta_decathlon.sim");
    boolean a =g.validateSaveStringFilenameUsingIO("thisisavalidfilename.csv");
    assertTrue(a);
  }
}

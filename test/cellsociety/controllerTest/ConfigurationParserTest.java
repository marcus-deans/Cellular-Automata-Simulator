package cellsociety.controllerTest;

import cellsociety.controller.ConfigurationParser;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import cellsociety.controller.InputParser;
import cellsociety.model.cells.Cell;
import cellsociety.util.IncorrectCSVFormatException;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;

public class ConfigurationParserTest {

  @Test
  void testParse() throws IOException {
    ConfigurationParser parser=new ConfigurationParser("data/game_of_life/glider.sim");
    Map<String, String> map=parser.parseSim();
    assertEquals("Glider", map.get("Title"));
    assertEquals("GameOfLife", map.get("Type"));
    assertEquals("Richard K. Guy", map.get("Author"));
    assertEquals("game_of_life/glider.csv", map.get("InitialStates"));
    assertEquals("00FF00,FF00FF", map.get("StateColors"));
  }

  @Test
  void testParseWeirdKeys() throws IOException {
    ConfigurationParser parser=new ConfigurationParser("data/percolation/simple_pipe.sim");
    Map<String, String> map=parser.parseSim();
    assertEquals("Simple Pipe", map.get("Title"));
    assertEquals("Percolation", map.get("Type"));
    assertEquals("Unknown", map.get("Author"));
    assertEquals("percolation/simple_pipe.csv", map.get("InitialStates"));
    assertEquals(null, map.get("StateColors"));
  }

}

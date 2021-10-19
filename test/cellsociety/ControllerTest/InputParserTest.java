package cellsociety.ControllerTest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import cellsociety.controller.InputParser;
import cellsociety.model.cells.Cell;
import cellsociety.model.cells.LifeCell;
import cellsociety.util.IncorrectCSVFormat;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class InputParserTest {

  @Test
  void testWellFormattedFile() throws CsvValidationException, IOException, IncorrectCSVFormat {
    InputParser i= new InputParser( "/data/game_of_life/");
    Cell[][] expectedGrid=i.parseFile();
    Cell[] cellRow={new LifeCell(0),new LifeCell(0),new LifeCell(0),new LifeCell(0),new LifeCell(0),new LifeCell(0),new LifeCell(0),new LifeCell(1),new LifeCell(1), new LifeCell(1)};
    assertEquals(cellRow, expectedGrid[0]);
  }

}

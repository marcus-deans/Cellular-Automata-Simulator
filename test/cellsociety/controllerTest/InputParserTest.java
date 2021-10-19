package cellsociety.controllerTest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import cellsociety.controller.InputParser;
import cellsociety.model.cells.Cell;
import cellsociety.util.IncorrectCSVFormatException;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;

public class InputParserTest {

  @Test
  void testWellFormattedFile() throws CsvValidationException, IOException, IncorrectCSVFormatException {
    InputParser parser= new InputParser( "data/game_of_life/blinkers.csv");
    Cell[][] expectedGrid=parser.parseFile();
    int[] cellRow={0,0,0,0,0,0,0,1,1,1};
    for (int i=0; i<cellRow.length; i++) {
      assertEquals(cellRow[i], expectedGrid[0][i].getMyCellState());
    }
    int[] cellRow2={0,0,0,0,1,1,1,0,0,0};
    for (int i=0; i<cellRow2.length; i++) {
      assertEquals(cellRow2[i], expectedGrid[9][i].getMyCellState());
    }
  }

}

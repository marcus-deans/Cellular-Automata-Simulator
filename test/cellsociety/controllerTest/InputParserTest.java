package cellsociety.controllerTest;

import cellsociety.util.ReflectionException;
import java.io.FileNotFoundException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import cellsociety.controller.InputParser;
import cellsociety.model.cells.Cell;
import cellsociety.util.IncorrectCSVFormatException;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;

public class InputParserTest {

  @Test
  void testWellFormattedFile() throws ReflectionException, FileNotFoundException, IncorrectCSVFormatException {
    InputParser parser= new InputParser( "data/game_of_life/blinkers.csv", "Life");
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

  @Test
  void noIntegers() {
    InputParser parser= new InputParser( "data/game_of_life/no_integers.csv", "Life");
    Exception e=assertThrows(IncorrectCSVFormatException.class, ()->parser.parseFile());
    assertTrue(e.getMessage().contains("int"));
  }

  @Test
  void wrongDimensions() {
    //InputParser parser= new InputParser( "data/game_of_life/no_integers.csv", "Life");
    //assertThrows(IncorrectCSVFormatException.class, ()->parser.parseFile());
  }


}

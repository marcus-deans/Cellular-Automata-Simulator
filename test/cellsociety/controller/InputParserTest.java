package cellsociety.controller;

import cellsociety.util.ReflectionException;
import java.io.FileNotFoundException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import cellsociety.model.cells.Cell;
import cellsociety.util.IncorrectCSVFormatException;

public class InputParserTest {

  @Test
  void testWellFormattedFile() throws ReflectionException, FileNotFoundException, IncorrectCSVFormatException {
    InputParser parser= new InputParser( "data/game_of_life/blinkers.csv", "Life");
    Cell[][] actualGrid=parser.parseFile();
    int[] cellRow={0,0,0,0,0,0,0,1,1,1};
    int[] actualRow1=createIntArray(actualGrid[0]);
    assertArrayEquals(cellRow, actualRow1);
    int[] cellRow2={1,0,0,0,1,0,0,0,0,0};
    int[] actualRow2=createIntArray(actualGrid[3]);
    assertArrayEquals(cellRow2, actualRow2);
  }
  private int[] createIntArray(Cell[] c) {
    int[] ret=new int[c.length];
    for (int i=0; i<c.length; i++) {
      ret[i]=c[i].getMyCellState();
    }
    return ret;
  }

  @Test
  void noIntegers() {
    InputParser parser= new InputParser( "data/incorrect_files/no_integers.csv", "Life");
    Exception e=assertThrows(IncorrectCSVFormatException.class, ()->parser.parseFile());
    assertTrue(e.getMessage().contains("int"));
  }

  @Test
  void wrongDimensions() {
    InputParser parser= new InputParser( "data/incorrect_files/wrongDimensions.csv", "Seg");
    Exception e=assertThrows(IncorrectCSVFormatException.class, ()->parser.parseFile());
    assertTrue(e.getMessage().contains("x"));
  }

  @Test
  void notAllowedValue() {
    InputParser parser = new InputParser("data/percolation/long_pipe.csv", "Life");
    Exception e = assertThrows(IncorrectCSVFormatException.class, ()->parser.parseFile());
    assertTrue(e.getMessage().contains("out of bounds"));
  }

  @Test
  void decimalGreaterThan1() {
    InputParser parser = new InputParser("data/percolation/long_pipe.csv", "Life");
    Exception e = assertThrows(IncorrectCSVFormatException.class, ()->parser.parseFile());
    assertTrue(e.getMessage().contains("out of bounds"));
  }
  @Test
  void fileNotFound() {
    InputParser parser= new InputParser("data/madeUp.csv", "Life");
    Exception e = assertThrows(IncorrectCSVFormatException.class, ()->parser.parseFile());
    assertTrue(e.getMessage().contains("file not found"));
  }


  @Test
  void TooManyDimensions() {
    InputParser parser= new InputParser("data/incorrect_files/tooManyDimensions.csv", "Life");
    Exception e = assertThrows(IncorrectCSVFormatException.class, ()->parser.parseFile());
    assertTrue(e.getMessage().contains("Dimensions"));
  }


}

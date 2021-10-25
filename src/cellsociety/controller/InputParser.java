package cellsociety.controller;

import cellsociety.model.cells.Cell;
import cellsociety.util.IncorrectCSVFormatException;
import cellsociety.util.ReflectionException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;

//input parser needs to know what kind of cells to create and what values are acceptable
public class InputParser {

  private String myText;
  private Cell[][] parsedArray;
  private String type;
  private int gridRows;
  private int gridColumns;

  public InputParser(String text, String type) {
    myText = text;
    this.type = type;
  }

  public Cell[][] parseFile()
      throws IncorrectCSVFormatException, ReflectionException, FileNotFoundException {
    FileReader fileReader = new FileReader(myText);
    CSVReader csvReader = new CSVReader(fileReader);
    String[] next;
    try {
      next = csvReader.readNext();
    } catch (CsvValidationException e) {
      throw new IncorrectCSVFormatException("csv file can't be read");
    } catch (IOException e) {
      throw new IncorrectCSVFormatException("IO exception");
    }
    //could throw exceptions so might want to handle io and csvvalidation internally
    if (next.length != 2) {
      throw new IncorrectCSVFormatException("Need Exactly 2 Dimensions");
    }
    try {
      gridRows = Integer.parseInt(next[1]);
      gridColumns = Integer.parseInt(next[0]);
    } catch (NumberFormatException e) {
      throw new IncorrectCSVFormatException("All inputs must be integers");
    }
    parsedArray = new Cell[gridRows][gridColumns];
    addCellsToArray(csvReader);
    return parsedArray;
  }

  //TODO verify that columns and rows are not switched
  private void addCellsToArray(CSVReader csvReader)
      throws IncorrectCSVFormatException, ReflectionException {
    String[] next;
    int xIndex;
    int yIndex = 0;
    while (true) {
      try {
        if (!((next = csvReader.readNext()) != null && yIndex < gridRows)) {
          break;
        }
      } catch (IOException e) {
        throw new IncorrectCSVFormatException("IO issue");
      } catch (CsvValidationException e) {
        throw new IncorrectCSVFormatException("csv file can't be parsed");
      }
      xIndex = 0;
      for (String cell : next) {
        if (xIndex >= gridColumns) {
          throw new IncorrectCSVFormatException(
              String.format("row %d has too many x values", yIndex));
        }
        Object[] param;
        Constructor<?> c;
        try {
          Class<?> clazz;
          clazz = Class.forName("cellsociety.model.cells." + type + "Cell");
          c = clazz.getConstructor(int.class);
        } catch (ClassNotFoundException e) {
          throw new ReflectionException("class not found");
        } catch (NoSuchMethodException e) {
          throw new ReflectionException("no method exists");
        }
        try {
          param = new Object[]{Integer.parseInt(cell)};
        } catch (NumberFormatException e) {
          throw new IncorrectCSVFormatException("All values need to be ints");
        }
        //o = c.newInstance(param);
        try {
          parsedArray[yIndex][xIndex] = (Cell) c.newInstance(param);
        } catch (Exception e) {
          throw new ReflectionException("can't create new instance");
        }
        xIndex++;
      }
//        parsedArray[yIndex][xIndex] = new LifeCell(
//            Integer.parseInt(cell)); //should account for different cell types
//        xIndex++;
      yIndex++;
    }

    if (yIndex > gridRows) {
      throw new IncorrectCSVFormatException(String.format("too many rows in csv"));
    }
  }

}


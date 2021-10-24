package cellsociety.controller;

import cellsociety.model.cells.Cell;
import cellsociety.model.cells.LifeCell;
import cellsociety.util.IncorrectCSVFormatException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.NoSuchElementException;

//input parser needs to know what kind of cells to create and what values are acceptable
public class InputParser {

  String myText;
  private int gridRows;
  private int gridColumns;
  Cell[][] parsedArray;
  String type;

  public InputParser(String text, String type) {
    myText = text;
    this.type=type;
  }

  public Cell[][] parseFile()
      throws IncorrectCSVFormatException, CsvValidationException, IOException {
    FileReader fileReader = new FileReader(myText);
    CSVReader csvReader = new CSVReader(fileReader);
    String[] next;
    next = csvReader.readNext();
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
      throws IncorrectCSVFormatException, CsvValidationException, IOException {
    String[] next;
    int xIndex = 0;
    int yIndex = 0;
    while ((next = csvReader.readNext()) != null && yIndex < gridRows) {
      xIndex = 0;
      for (String cell : next) {
        if (xIndex >= gridColumns) {
          throw new IncorrectCSVFormatException(
              String.format("row %d has too many x values", yIndex));
        }
        Class<?> clazz;
        try {
          clazz = Class.forName("cellsociety.model.cells." + type + "Cell");
          Constructor<?> c = clazz.getConstructor(int.class);
          Object[] param=null;
          try {
            param = new Object[]{Integer.parseInt(cell)};
          }
          catch (NumberFormatException e) {
            throw new IncorrectCSVFormatException("All values need to be ints");
          }
          //o = c.newInstance(param);
          parsedArray[yIndex][xIndex] = (Cell)c.newInstance(param);
          xIndex++;
        }
        catch (Exception e) {
          //bad as always
          e.printStackTrace();
        }
//        parsedArray[yIndex][xIndex] = new LifeCell(
//            Integer.parseInt(cell)); //should account for different cell types
//        xIndex++;
      }
      yIndex++;
    }
    if (yIndex > gridRows) {
      throw new IncorrectCSVFormatException(String.format("too many rows in csv"));
    }
  }

}


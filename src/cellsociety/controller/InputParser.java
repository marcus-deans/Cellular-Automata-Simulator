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
import java.lang.reflect.InvocationTargetException;
import java.util.ResourceBundle;


//input parser needs to know what kind of cells to create and what values are acceptable
public class InputParser {

  private String myText;
  private Cell[][] parsedArray;
  private String type;
  private int gridRows;
  private int gridColumns;
  private static final String RESOURCE_FILE_PATH = "cellsociety.resources.model.numCellStates";
  private static final ResourceBundle numCellStates = ResourceBundle.getBundle(RESOURCE_FILE_PATH);

  public InputParser(String text, String type) {
    myText = text;
    this.type = type;
  }

  public Cell[][] parseFile()
      throws IncorrectCSVFormatException, ReflectionException, FileNotFoundException {
    FileReader fileReader = new FileReader(myText);
    CSVReader csvReader = new CSVReader(fileReader);
    String[] row;
    row = readFirstLine(csvReader);
    //could throw exceptions so might want to handle io and csvvalidation internally
    checkFirstRowFormatting(row);
    parsedArray = new Cell[gridRows][gridColumns];
    addCellsToArray(csvReader);
    return parsedArray;
  }

  private void checkFirstRowFormatting(String[] row) throws IncorrectCSVFormatException {
    if (row.length != 2) {
      throw new IncorrectCSVFormatException("Need Exactly 2 Dimensions");
    }
    try {
      gridRows = Integer.parseInt(row[1]);
      gridColumns = Integer.parseInt(row[0]);
    } catch (NumberFormatException e) {
      throw new IncorrectCSVFormatException("All inputs must be integers");
    }
  }

  private String[] readFirstLine(CSVReader csvReader) throws IncorrectCSVFormatException {
    String[] row;
    try {
      row = csvReader.readNext();
    } catch (CsvValidationException e) {
      throw new IncorrectCSVFormatException("csv file can't be read");
    } catch (IOException e) {
      throw new IncorrectCSVFormatException("IO exception");
    }
    return row;
  }

  //TODO verify that columns and rows are not switched
  private void addCellsToArray(CSVReader csvReader)
      throws IncorrectCSVFormatException, ReflectionException {
    String[] next;
    int rowIndex = 0;
    while (true) {
      next = getNextLine(csvReader, rowIndex);
      if (next == null) {
        break;
      }
      iterateThroughRowCreateAndAddCells(next, rowIndex);
      rowIndex++;
    }
    if (rowIndex > gridRows) {
      throw new IncorrectCSVFormatException(String.format("too many rows in csv"));
    }
  }

  private String[] getNextLine(CSVReader csvReader, int yIndex) throws IncorrectCSVFormatException {
    String[] next;
    try {
      if (!((next = csvReader.readNext()) != null && yIndex < gridRows)) {
        return null;
      }
    } catch (IOException e) {
      throw new IncorrectCSVFormatException("IO issue");
    } catch (CsvValidationException e) {
      throw new IncorrectCSVFormatException("csv file can't be parsed");
    }
    return next;
  }

  private void iterateThroughRowCreateAndAddCells(String[] next, int yIndex)
      throws IncorrectCSVFormatException, ReflectionException {
    int xIndex = 0;
    for (String cell : next) {
      if (xIndex >= gridColumns) {
        throw new IncorrectCSVFormatException(
            String.format("row %d has too many x values", yIndex));
      }

        Constructor<?> c = getConstructor();
      //making them with x and y locations
      Object[] param = new Object[]{parseCellValue(cell), xIndex, yIndex};
        addNewCellInstanceToArray(xIndex, yIndex, c, param);

      xIndex++;
    }
  }

  private void addNewCellInstanceToArray(int xIndex, int yIndex, Constructor<?> c, Object[] param)
      throws ReflectionException {
    try {
      parsedArray[yIndex][xIndex] = (Cell) c.newInstance(param);
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
//    try {
//      parsedArray[yIndex][xIndex] = (Cell) c.newInstance(param);
//    } catch (Exception e) {
//      throw new ReflectionException("can't create new instance");
//    }
  }

  private int parseCellValue(String cell) throws IncorrectCSVFormatException {
    int param;
    try {
      param = Integer.parseInt(cell);
    } catch (NumberFormatException e) {
      throw new IncorrectCSVFormatException("All values need to be ints");
    }
    if (Integer.parseInt(cell)>=Integer.parseInt(numCellStates.getString(type))) {
      throw new IncorrectCSVFormatException(String.format("value out of bounds", cell));
    }
    return param;
  }

  private Constructor<?> getConstructor() throws ReflectionException {
    Constructor<?> c;
    try {
      Class<?> clazz;
      clazz = Class.forName("cellsociety.model.cells." + type + "Cell");
      //c = clazz.getConstructor(int.class);
      c=clazz.getConstructor(int.class, int.class, int.class);
    } catch (ClassNotFoundException e) {
      throw new ReflectionException("class not found");
    } catch (NoSuchMethodException e) {
      throw new ReflectionException("no method exists");
    }
    return c;
  }
}


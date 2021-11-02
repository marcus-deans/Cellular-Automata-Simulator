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

/**
 * Parser to interpret CSV files and create Cell Grid
 * Relies on cell type and resource file with number of cell states
 * @author morganfeist

 */
public class InputParser {

  private String filename;
  private Cell[][] parsedArray;
  private String type;
  private int gridRows;
  private int gridColumns;
  private static final String RESOURCE_FILE_PATH = "cellsociety.resources.model.numCellStates";
  private static final ResourceBundle numCellStates = ResourceBundle.getBundle(RESOURCE_FILE_PATH);

  /**
   * Creates InputParser based on file and simulation type
   * @param text filepath of CSV
   * @param type simulation Type (i.e Life)
   */
  public InputParser(String text, String type) {
    filename = text;
    this.type = type;
  }

  /**
   * @return Cell array based on simulation type and csv contents
   * @throws IncorrectCSVFormatException if there are errors in the construction of the CSV
   * @throws ReflectionException
   */
  public Cell[][] parseFile()
      throws IncorrectCSVFormatException, ReflectionException{
    FileReader fileReader=null;
    try {
      fileReader = new FileReader(filename);
    }catch (FileNotFoundException e) {
      throw new IncorrectCSVFormatException("CSV file not found");
    }
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
    } catch (CsvValidationException | IOException e) {
      throw new IncorrectCSVFormatException("CSV file can't be read");
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
    } catch (IOException | CsvValidationException e) {
      throw new IncorrectCSVFormatException("Cannot read csv");
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
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new ReflectionException("Unable to make cell instance");
    }
  }

  private int parseCellValue(String cell) throws IncorrectCSVFormatException {
    int param;
    try {
      param = Integer.parseInt(cell);
    } catch (NumberFormatException e) {
      throw new IncorrectCSVFormatException("All values need to be ints");
    }
    if (Integer.parseInt(cell)>=Integer.parseInt(numCellStates.getString(type))) {
      throw new IncorrectCSVFormatException(String.format("Cell value out of bounds: %s", cell));
    }
    return param;
  }

  private Constructor<?> getConstructor() throws ReflectionException {
    Constructor<?> c;
    try {
      Class<?> clazz;
      clazz = Class.forName("cellsociety.model.cells." + type + "Cell");
      c=clazz.getConstructor(int.class, int.class, int.class);
    } catch (ClassNotFoundException e) {
      throw new ReflectionException("class not found");
    } catch (NoSuchMethodException e) {
      throw new ReflectionException("no method exists");
    }
    return c;
  }
}


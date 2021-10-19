package cellsociety.controller;

import cellsociety.model.cells.Cell;
import cellsociety.model.cells.LifeCell;
import cellsociety.util.IncorrectCSVFormat;
import com.opencsv.CSVReader; //confusing
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class InputParser {
  String myText;
  private int cellX;
  private int cellY;
  Cell[][] parsedArray;

  public InputParser(String text){
    myText = text;
  }

  public void parseFile() throws IOException, IncorrectCSVFormat, CsvValidationException {
    FileReader fileReader=new FileReader(myText);
    //file not found exception?
    CSVReader csvReader = new CSVReader(fileReader);
    String[] next;
    next = csvReader.readNext();
    //could throw exceptions so might want to handle io and csvvalidation internally
      if (next.length!=2) {
        throw new IncorrectCSVFormat("Need Exactly 2 Dimensions");
      }
      try {
        cellX = Integer.parseInt(next[0]);
        cellY = Integer.parseInt(next[1]);
      }
      catch(NumberFormatException e) {
        throw new IncorrectCSVFormat("All inputs must be integers");
      }
      parsedArray= new Cell[cellX][cellY];
      addCellsToArray(csvReader);
  }

  private void addCellsToArray(CSVReader csvReader)
      throws IncorrectCSVFormat, CsvValidationException, IOException {
    String[] next;
    int xIndex=0;
    int yIndex=0;
      while ((next = csvReader.readNext()) != null && yIndex < cellY) {
        for (String cell : next) {
          if (xIndex >= cellX) {
            throw new IncorrectCSVFormat(String.format("row %d has too many x values", yIndex));
          }
          parsedArray[xIndex][yIndex] = new LifeCell(
              Integer.parseInt(cell)); //should account for different cell types
          xIndex++;
        }
        yIndex++;
      }
      if (yIndex == cellY) {
        throw new IncorrectCSVFormat(String.format("too many rows in csv"));
      }
    }
  }

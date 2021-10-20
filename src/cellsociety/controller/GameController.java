package cellsociety.controller;


import cellsociety.model.cells.Cell;
import cellsociety.model.gamegrids.GameGrid;
import cellsociety.model.gamegrids.LifeGrid;
import cellsociety.view.GameView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public class GameController {

  //where should these live?
  //public static final String dataConfigurationFile = "data/game_of_life/blinkers.csv";
  //public static final String simulationConfigurationFile = "data/game_of_life/blinkers.sim";
  int myGameType;
  String gameType;
  GameGrid myGrid; //this is the model not to be confused with the array contained in the grid
  GameView myView;
  Map<String, String> configuration;
  //should GameController also hold view?

  public GameController() {
    myGameType = 0;
  }

  public void setup() {

  }
  public void readSimFile(String filename) {
    ConfigurationParser configParser=new ConfigurationParser(filename);
    try {
      configuration = configParser.parseSim();
    }
    catch (IOException e) {

    }
    gameType=configuration.get("Type");
    parseInput(configuration.get("InitialStates"));

  }
  public void parseInput(String text) {
    InputParser myInputParser = new InputParser(text);
    Cell[][] grid;
    try {
      grid = myInputParser.parseFile();
    } catch (Exception e) {
      grid = null;
      //there are so many exceptions what do I do with them
    }
    myGrid = new LifeGrid(grid); //obviously we'll use reflection here in the future
  }

  //pass type, description, title into view
  //pass type into model too


  /**
   * Save the input to a text file
   *
   * @param inputStream command line input from Display
   * @param filename    String filename of file to be saved
   * @return confirmation that files was saved
   */
  public boolean saveCommand(String inputStream, String filename) {
    StringBuilder sb = new StringBuilder();
    sb.append("data/game_of_live/output/");
    sb.append(filename);
    sb.append(".csv");
    return saveCommandGivenPath(inputStream, sb.toString());
  }

  /**
   * Save user input commands to file
   *
   * @param inputStream command line user input
   * @param path        path that file should be saved to
   * @return return whether command saved succesfully
   */
  private boolean saveCommandGivenPath(String inputStream, String path) {
    File newProgram = new File(path);
    try {
      if (newProgram.createNewFile()) {
        FileWriter writeToFile = new FileWriter(newProgram.getAbsolutePath());
        writeToFile.write(inputStream);
        writeToFile.close();
      } else {
        return false; //on front end, user will be informed of error
      }
    } catch (IOException e) {
      return false; //on front end, user will be informed of error
    }
    return true;
  }

  public boolean getContentFromFilename(String filename) {
    File[] files = getFilesFromPath();
    for (File file : files) {
      if (file.isFile() && file.getName().equals(filename)) {
        try {
          Scanner scanner = new Scanner(file);
          String input;
          StringBuffer contents = new StringBuffer();
          while (scanner.hasNextLine()) {
            input = scanner.nextLine();
            if (!Arrays.asList(input.split("")).contains("#")) {
              contents.append(input + " ");
            }
          }
//          commandLine.clear();
//          commandLine.setText(contents.toString());
        } catch (FileNotFoundException e) {
          return false;
        }
      }
    }
    return true;
  }

  private File[] getFilesFromPath() {
    return new File("data/game_of_life").listFiles();
  }

  public boolean validateStringFilenameUsingIO(String filename) {
    File file = new File(filename);
    boolean created = false;
    try {
      created = file.createNewFile();
      return created;
    } catch (IOException e) {

    } finally {
      if (created) {
        file.delete();
      }
    }
    return false;
  }
}

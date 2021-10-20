package cellsociety.controller;


import cellsociety.model.cells.Cell;
import cellsociety.model.gamegrids.GameGrid;
import cellsociety.model.gamegrids.LifeGrid;
import cellsociety.view.GameView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GameController {

  public static final int FRAME_WIDTH = 733;
  public static final int FRAME_HEIGHT = 680;
  public static final Paint BACKGROUND = Color.web("#00539B");

  private String mySimFilename;
  private String gameType;
  private Cell[][] myInitialStates;
  private GameGrid myGridModel; //this is the model not to be confused with the array contained in the grid
  private GameView myProgramView;
  private Map<String, String> configuration;

  public GameController(String simFilename) {
    mySimFilename = simFilename;
    configuration = new HashMap<>();
  }

  public void setupProgram() {
    readSimFile();
    myGridModel = new LifeGrid(myInitialStates); //obviously we'll use reflection here in the future
    myProgramView = new GameView(FRAME_WIDTH, FRAME_HEIGHT, BACKGROUND, gameType);
    myProgramView.start(new Stage());
  }

  public void runSimulation(){
    myGridModel.runGame();
  }

  public void readSimFile() {
    ConfigurationParser configParser = new ConfigurationParser(mySimFilename);
    try {
      configuration = configParser.parseSim();
    }
    catch (IOException e) {

    }
    gameType = configuration.get("Type");
    parseCSVFile(configuration.get("InitialStates"));

  }

  public void parseCSVFile(String CSVFile) {
    InputParser myInputParser = new InputParser(CSVFile);
    try {
      myInitialStates = myInputParser.parseFile();
    } catch (Exception e) {
      myInitialStates = null;
      // TODO: currently this exception always fires so myInitialStates is always null which always throws an error when initializing a new GameGrid
    }
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

  public GameGrid getMyGrid() {
    return myGridModel;
  }
}

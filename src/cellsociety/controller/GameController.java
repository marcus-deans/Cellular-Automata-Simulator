package cellsociety.controller;

import cellsociety.model.cells.Cell;
import cellsociety.model.gamegrids.GameGrid;
import cellsociety.util.IncorrectCSVFormatException;
import cellsociety.util.IncorrectSimFormatException;
import cellsociety.util.ReflectionException;
import cellsociety.view.GridView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Controller class to act as go between for model and view
 * Creates model, relies on creation by view
 * @author morganfeist
 */
public class GameController {

  private final Map<String, String> typeAbbreviations = Map.of("GameOfLife", "Life", "WatorWorld",
      "Wator", "Segregation", "Seg", "SpreadingOfFire", "Fire", "Percolation", "Perc");
  private String mySimFilename;
  private String abbreviatedType;
  private Cell[][] myInitialStates;
  private GameGrid myGridModel;
  private Map<String, String> configurationMap;

  /**
   * Creates new GameController with filename and map to hold parameters from file
   *
   * @param simFilename .sim filepath
   */
  public GameController(String simFilename) {
    mySimFilename = simFilename;
    configurationMap = new HashMap<>();
  }

  public void setupProgram()
      throws IncorrectCSVFormatException, IncorrectSimFormatException, ReflectionException {
    readSimFile();
    createGridModel();
  }

  private void createGridModel() throws ReflectionException {
    Object o;
    try {
      Class<?> clazz = Class.forName("cellsociety.model.gamegrids." + abbreviatedType + "Grid");
      Constructor<?> c = clazz.getConstructor(Cell[][].class, String.class, Map.class);
      Object[] param = {myInitialStates, abbreviatedType, configurationMap};
      o = c.newInstance(param);
    } catch (InstantiationException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
      throw new ReflectionException();
    }
    myGridModel = (GameGrid) o;
  }

  public void setupListener(GridView view) {
    myGridModel.addListener(view);
  }

  public void showInitialStates() throws ReflectionException {
    myGridModel.updateInitialFutureGrid();
  }

  public void runSimulation() throws ReflectionException {
    myGridModel.runGame();
  }

  private void readSimFile() throws IncorrectCSVFormatException, IncorrectSimFormatException {
    ConfigurationParser configParser = new ConfigurationParser(mySimFilename);
    configurationMap = configParser.parseSim();
    abbreviatedType = typeAbbreviations.get(configurationMap.get("Type"));
    parseCSVFile(configurationMap.get("InitialStates"));
  }

  public Map<String, String> getConfigurationMap() {
    return configurationMap;
  }

  public int[] getGridSize() {
    int[] dimensions = {myInitialStates.length, myInitialStates[0].length};
    return dimensions;
  }
  //use this method when grid is clicked
  public void calculateIndexesAndUpdateModel(double x, double y, int height, int width) {
    int row=(int)(y/height);
    int col=(int)(x/width);
    myGridModel.updateOneCell(row, col);
  }

  private void parseCSVFile(String CSVFile)
      throws IncorrectCSVFormatException {
    InputParser myInputParser = new InputParser("./data/"+CSVFile, typeAbbreviations.get(configurationMap.get("Type")));
    //InputParser myInputParser = new InputParser("data/" + CSVFile, typeAbbreviations.get(configurationMap.get("Type")));
    try {
      myInitialStates = myInputParser.parseFile();
    } catch (ReflectionException e) {
      myInitialStates = null;
      e.printStackTrace();
    }
  }

  /**
   * Save current grid to a text file
   *
   * @param filename String filename of file to be saved
   * @return confirmation that files was saved
   */
  public boolean saveCommand(String filename) {
    StringBuilder sb = new StringBuilder();
    sb.append("data/output/");
    sb.append(filename);
    sb.append(".csv");
    return saveCommandGivenPath(sb.toString());
  }

  /**
   * Save current grid to provided file
   *
   * @param path path that file should be saved to
   * @return return whether command saved succesfully
   */
  private boolean saveCommandGivenPath(String path) {
    String inputStream = "FIX_THIS"; //TODO: convert the GameGrid into an InputStream
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

  public void loadNewFile(String filename){
    mySimFilename = filename;
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

  public boolean validateSaveStringFilenameUsingIO(String filename) {
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

  public boolean validateLoadStringFilenameUsingIO(String filename) {
    return !validateSaveStringFilenameUsingIO(filename);
  }
}

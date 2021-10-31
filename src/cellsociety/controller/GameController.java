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

public class GameController {

  private final Map<String, String> typeAbbreviations = Map.of("GameOfLife", "Life", "WatorWorld",
      "Wator", "Segregation", "Seg", "SpreadingOfFire", "Fire", "Percolation", "Perc");
  private String mySimFilename;
  private String abbreviatedType;
  private Cell[][] myInitialStates;
  private GameGrid myGridModel; //this is the model not to be confused with the array contained in the grid
  //private GameView myProgramView;
  private Map<String, String> configurationMap;
  private int numGridRows;
  private int numGridColumns;

  //private Timeline myAnimation;

  public GameController(String simFilename) {
    mySimFilename = simFilename;
    configurationMap = new HashMap<>();
  }

  public void setupProgram()
      throws IncorrectCSVFormatException, IncorrectSimFormatException, FileNotFoundException, ReflectionException {
    readSimFile();
    //TODO: figure out how to use reflection if the parameters are different
    //issue: they don't all need the same parameters
    //myGridModel=new FireGrid(myInitialStates, Float.parseFloat(configurationMap.get("fillTree")),Float.parseFloat(configurationMap.get("probCatch")));
    Object o = null;
    try {
      Class<?> clazz = Class.forName("cellsociety.model.gamegrids." + abbreviatedType + "Grid");
      Constructor<?> c = clazz.getConstructor(Cell[][].class, String.class, Map.class);
      Object[] param = {myInitialStates, abbreviatedType, configurationMap};
      o = c.newInstance(param);
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    myGridModel = (GameGrid) o;
  }

  public void setupListener(GridView view) {
    myGridModel.addListener(view);
  }

  public void showInitialStates() {
    myGridModel.updateInitialFutureGrid();
  }

  public void runSimulation() {
    myGridModel.runGame();
  }

  private void readSimFile() throws IncorrectCSVFormatException, IncorrectSimFormatException, FileNotFoundException {
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
      throws IncorrectCSVFormatException, FileNotFoundException {
    //InputParser myInputParser = new InputParser("./cellsociety_team15/data/"+CSVFile, typeAbbreviations.get(configurationMap.get("Type")));
    InputParser myInputParser = new InputParser("data/" + CSVFile, typeAbbreviations.get(configurationMap.get("Type")));
    try {
      myInitialStates = myInputParser.parseFile();
    } catch (ReflectionException e) {
      myInitialStates = null;
      e.printStackTrace();
      // TODO: currently this exception always fires so myInitialStates is always null which always throws an error when initializing a new GameGrid
    }
  }

  //pass type, description, title into view
  //pass type into model too


  /**
   * Save current grid to a text file
   *
   * @param filename String filename of file to be saved
   * @return confirmation that files was saved
   */
  public boolean saveCommand(String filename) {
    StringBuilder sb = new StringBuilder();
    sb.append("data/game_of_live/output/");
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

  public boolean loadNewFile(String filename)
      throws FileNotFoundException, IncorrectSimFormatException, IncorrectCSVFormatException {
    mySimFilename = filename;
    try {
      setupProgram();
    }
    catch(ReflectionException e) {
      System.out.println("reflection exception");
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

  public GameGrid getMyGrid() {
    return myGridModel;
  }
}

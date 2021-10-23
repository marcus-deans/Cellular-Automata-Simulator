package cellsociety.controller;


import cellsociety.model.cells.Cell;
import cellsociety.model.gamegrids.GameGrid;
import cellsociety.model.gamegrids.LifeGrid;
import cellsociety.util.IncorrectSimFormatException;
import cellsociety.view.GameView;
import cellsociety.view.GridView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GameController {

  private String mySimFilename;
  private String myGameType;
  private Cell[][] myInitialStates;
  private GameGrid myGridModel; //this is the model not to be confused with the array contained in the grid
  private GameView myProgramView;
  private Map<String, String> configurationMap;
  private int numGridRows;
  private int numGridColumns;
  private final Map<String, String> typeAbbreviations=Map.of("GameOfLife", "Life", "WatorWorld", "Wator", "Segregation", "Seg", "SpreadingOfFire", "Fire");

  //private Timeline myAnimation;

  public GameController(String simFilename) {
    mySimFilename = simFilename;
    configurationMap = new HashMap<>();
  }

  public void setupProgram() {
    readSimFile();
    //TODO: use reflection to create appropriate grid
//    try {
//      Class<?> gridClazz = Class.forName("cellsociety.model.gamegrids.GameGrid");
//      Constructor<? extends GameGrid> gridConstructor = gridClazz.getConstructor(new Object[] {String.class} );
//      myGridModel = gridConstructor.newInstance( new Object["your_string"] )
//    } catch (Exception e){
//      e.printStackTrace();
//    }


    //REFERENCE: https://www.heimetli.ch/java/create-new-instance.html
    // Other links: https://stackoverflow.com/questions/12538761/how-to-create-instances-of-all-subclasses
    // https://stackoverflow.com/questions/35884572/how-to-create-instance-of-subclass-with-constructor-from-super-class
    // https://stackoverflow.com/questions/7421913/java-method-to-instantiate-a-particular-sub-class-of-an-abstract-class
    // public Fox( boolean flag, Field field, Position position )
    // public LifeGrid(Cell[][] gameGrid){
    //   public FireGrid(Cell[][] gameGrid, int fireProb, int treeProb){
    //public PercGrid(Cell[][] gameGrid){
    // public GameGrid(Cell[][] gameGrid){

//    try {
//      Constructor<? extends Animal> constructor = getClass().getDeclaredConstructor( boolean.class, Field.class, Position.class ) ;
//      Animal animal = constructor.newInstance( true, new Field(), new Position() ) ;
//    } catch( Exception e ) {
//      System.out.println( e ) ;
//    }

    myGridModel = new LifeGrid(myInitialStates); //obviously we'll use reflection here in the future
  }

  public void setupListener(GridView view) {
    myGridModel.addPropertyChangeListener(view);
    System.out.println("property");
  }

  public void showInitialStates() {
    myGridModel.updateInitialFutureGrid();
  }

  public void runSimulation(){
    myGridModel.runGame();
  }

  public void readSimFile() {
    ConfigurationParser configParser = new ConfigurationParser(mySimFilename);
    try {
      configurationMap = configParser.parseSim();
    }
    catch (IncorrectSimFormatException e) {

    }
    catch (FileNotFoundException e) {

    }
    myGameType = configurationMap.get("Type");
    parseCSVFile(configurationMap.get("InitialStates"));

  }

  public Map<String, String> getConfigurationMap() {
    return configurationMap;
  }

  public int[] getGridSize() {
    int[] dimensions={myInitialStates.length, myInitialStates[0].length};
    return dimensions;
  }

  private void parseCSVFile(String CSVFile) {
    //InputParser myInputParser = new InputParser("./cellsociety_team15/data/"+CSVFile);
    InputParser myInputParser = new InputParser("data/"+CSVFile);
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
   * Save current grid to a text file
   *
   * @param filename    String filename of file to be saved
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
   * @param path        path that file should be saved to
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

  public boolean loadCommand(String filename){
    mySimFilename = filename;
    setupProgram();
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

  public boolean validateLoadStringFilenameUsingIO(String filename){
    return !validateSaveStringFilenameUsingIO(filename);
  }

  public GameGrid getMyGrid() {
    return myGridModel;
  }
}

package cellsociety.controller;

import cellsociety.util.IncorrectSimFormatException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
//TODO with some refactoring, check for required parameters based on given type
//probably need to check type earlier than the rest of the parameters for this to work

/**
 * Parser that interprets .sim file to determine the parameter values for the simulations and
 * whether the file is formatted correctly
 * Relies on correctly configured resourceBundles
 * @author morganfeist
 */
public class ConfigurationParser {

  private static final String REQUIRED_PARAMETERS = "cellsociety.resources.controller.requiredParameters";
  private static final ResourceBundle requiredParameters = ResourceBundle.getBundle(
      REQUIRED_PARAMETERS);
  private static final String VALID_PARAMETERS = "cellsociety.resources.controller.validParameters";
  private static final ResourceBundle validParameters = ResourceBundle.getBundle(
      VALID_PARAMETERS);

  private Map<String, String> returnedValues;
  private String filename;

  /**
   * @param filename the file path of the csv file
   */
  public ConfigurationParser(String filename) {
    this.filename = filename;
    returnedValues = new HashMap<>();
  }

  public Map<String, String> parseSim()
      throws IncorrectSimFormatException {
    //move to properties file
    List<String> requiredParams = new ArrayList<>(
        Arrays.asList(requiredParameters.getString("RequiredParameters").split(",")));
    FileReader reader=null;
    try {
      reader = new FileReader(filename);
    }catch (FileNotFoundException e) {
      //this has already been verified
    }
    Properties properties = new Properties();
    Set<String> keys = getKeysFromProperties(reader, properties);
    addCorrectlyFormattedKeysToMap(requiredParams, properties, keys);
    checkForMissingParameters(requiredParams);
    return returnedValues;
  }

  private Set<String> getKeysFromProperties(FileReader reader, Properties properties)
      throws IncorrectSimFormatException {
    try {
      properties.load(reader);
    } catch (IOException e) {
      throw new IncorrectSimFormatException("Sim file not readable");
    }
    Set<String> keys = properties.stringPropertyNames();
    return keys;
  }

  private void checkForMissingParameters(List<String> requiredParams)
      throws IncorrectSimFormatException {
    if (requiredParams.size() > 0) {
      throw new IncorrectSimFormatException(
          String.format("Missing parameter in .sim: %s", requiredParams.get(0)));
    } else {
      checkForAdditionalParameters();
    }
  }

  private void checkForAdditionalParameters() throws IncorrectSimFormatException {
    String[] l = requiredParameters.getString(returnedValues.get("Type")).split(",");
    if (l[0].equals("")) {
      return;
    }
    for (String s : l) {
      if (returnedValues.get(s) == null) {
        throw new IncorrectSimFormatException(
            String.format("Missing parameter in .sim: %s", s));
      }
      else if (requiredParameters.getString(s+"Type").equals("positive")) {
        validateInt(s);
      }
      else if (requiredParameters.getString(s+"Type").equals("decimal")) {
        validateDecimal(s);
      }
    }
  }

  private void validateInt(String i) throws IncorrectSimFormatException {
    int num;
    System.out.println("validate");
    try {
      num=Integer.parseInt(returnedValues.get(i));
    }
    catch (NumberFormatException e) {
      throw new IncorrectSimFormatException(String.format("Need number for parameter: %s", i));
    }
    if (num<1) {
      throw new IncorrectSimFormatException(String.format("Need integer greater than 1 for parameter: %s", i));
    }
  }

  private void validateDecimal(String f) throws IncorrectSimFormatException {
    float decimal;
    try {
      decimal=Float.parseFloat(returnedValues.get(f));
    }
    catch (NumberFormatException e) {
      throw new IncorrectSimFormatException(String.format("Need decimal for parameter: %s", f));
    }
    if (decimal<0 || decimal>1) {
      throw new IncorrectSimFormatException(String.format("Parameter not between 0 and 1: %s", f));
    }
  }

  private void addCorrectlyFormattedKeysToMap(List<String> requiredParams, Properties p,
      Set<String> keys) throws IncorrectSimFormatException {
    for (String s : keys) {
      String remove = "";
      for (String parameters : requiredParams) {
        if (addedToMapIgnoreCase(p, parameters, s)) {
          remove = parameters;
          break;
        }
      }
      returnedValues.putIfAbsent(s, p.getProperty(s));
      requiredParams.remove(remove);
    }
  }

  private boolean addedToMapIgnoreCase(Properties p, String compare, String key)
      throws IncorrectSimFormatException {
    String[] split=validParameters.getString("Type").split(",");
    if (key.toLowerCase().contains(compare.toLowerCase())) {
      if (compare.equals("Type")) {
        if (!Arrays.asList(split).contains(p.getProperty(key))) {
          throw new IncorrectSimFormatException(String.format("Not a Valid Type: %s", p.getProperty(key)));
        }
      }
      returnedValues.put(compare, p.getProperty(key));
      return true;
    }
    return false;
  }


}

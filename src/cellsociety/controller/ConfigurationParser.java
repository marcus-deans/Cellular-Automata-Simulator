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
public class ConfigurationParser {
  private static final String REQUIRED_PARAMETERS = "cellsociety.resources.requiredParameters";
  private static final ResourceBundle requiredParameters = ResourceBundle.getBundle(
      REQUIRED_PARAMETERS);

  private Map<String, String> returnedValues;
  private String filename;

  public ConfigurationParser(String filename) {
    this.filename = filename;
    returnedValues = new HashMap<>();
  }

  public Map<String, String> parseSim() throws IncorrectSimFormatException, FileNotFoundException {
    //move to properties file
    List<String> requiredParams = new ArrayList<>(Arrays.asList(requiredParameters.getString("RequiredParameters").split(",")));
    FileReader reader = new FileReader(filename);
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
    }
    catch(IOException e) {
      throw new IncorrectSimFormatException("sim file not readable");
    }
    Set<String> keys = properties.stringPropertyNames();
    return keys;
  }

  private void checkForMissingParameters(List<String> requiredParams) throws IncorrectSimFormatException {
    if (requiredParams.size() > 0) {
      throw new IncorrectSimFormatException(
          String.format("Missing parameter in .sim: %s", requiredParams.get(0)));
    }
    else {
      String[] l=requiredParameters.getString(returnedValues.get("Type")).split(",");
      if (l[0].equals("")) {return;}
      for (String s:l) {
        if (returnedValues.get(s)==null) {
          throw new IncorrectSimFormatException(
              String.format("Missing parameter in .sim: %s", s));
        }
      }
    }
  }

  private void addCorrectlyFormattedKeysToMap(List<String> requiredParams, Properties p,
      Set<String> keys) {
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

  private boolean addedToMapIgnoreCase(Properties p, String compare, String key) {
    if (key.toLowerCase().contains(compare.toLowerCase())) {
      returnedValues.put(compare, p.getProperty(key));
      System.out.println("added" +compare);
      return true;
    }
    return false;
  }


}

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
import java.util.Set;

public class ConfigurationParser {

  Map<String, String> returnedValues;
  //  String description;
//  String[] colors;
//  String title;
//  String author;
  String filename;

  //  String dataFilename;
//  String type;
  public ConfigurationParser(String filename) {
    this.filename = filename;
    returnedValues = new HashMap<>();
  }

  //  public void parseSim() {
//    Properties p = System.getProperties();
//    Set set = p.entrySet();
//
//    Iterator itr = set.iterator();
//    while (itr.hasNext()) {
//      System.out.println(itr);
//    }
//    //Map.Entry entry = (Map.Entry) itr.next();
//  }
  public Map<String, String> parseSim() throws IncorrectSimFormatException, FileNotFoundException {
    List<String> requiredParams = new ArrayList<>(Arrays.asList("Title", "Author", "Type",
        "Description", "InitialStates"));
    try {
      FileReader reader = new FileReader(filename);
      Properties properties = new Properties();
      properties.load(reader);
      Set<String> keys = properties.stringPropertyNames();
      addCorrectlyFormattedKeysToMap(requiredParams, properties, keys);
      if (requiredParams.size() > 0) {
        throw new IncorrectSimFormatException(
            String.format("Missing argument in .sim: %s", requiredParams.get(0)));
      }
    } catch (FileNotFoundException e) {
      throw new FileNotFoundException("missing file");
    } catch (IOException e) {
      throw new IncorrectSimFormatException("Sim format has errors, cannot be parsed");
    }
    return returnedValues;
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
      return true;
    }
    return false;
  }

//        if (s.toLowerCase().contains("title")) {
//    returnedValues.put("Title", p.getProperty(s));
//    continue;
//  }
//      if (s.toLowerCase().contains("author")) {
//    returnedValues.put("Author", p.getProperty(s));
//    continue;
//  }
//      if (s.toLowerCase().contains("type")) {
//    returnedValues.put("Type", p.getProperty(s));
//    continue;
//  }
//      if (s.toLowerCase().contains("initialStates")) {
//    returnedValues.put("Type", p.getProperty(s));
//    continue;
//  }
//  addedIgnoreCase(p, s, "Description");
//      returnedValues.put(s, p.getProperty(s));
//}

  //  public void parseSims() throws IOException {
//
//    Properties p = System.getProperties();
//    Set set = p.entrySet();
//
//    Iterator itr = set.iterator();
//    while (itr.hasNext()) {
//      Map.Entry entry = (Map.Entry) itr.next();
//      if (entry.getKey().toString().toLowerCase().contains("title")) {
//        map.put("title");
//      }
//      if (entry.getKey().toString().toLowerCase().contains("author")) {
//        author = entry.getValue().toString();
//      }
//      if (entry.getKey().toString().toLowerCase().contains("description")) {
//        description = entry.getValue().toString();
//      }
//      if (entry.getKey().toString().toLowerCase().contains("initialstates")) {
//        filename = entry.getValue().toString();
//      }
//      if (entry.getKey().toString().toLowerCase().contains("statecolors")) {
//        colors=findColors(entry.getValue().toString());
//      }
//      //this does not account for additional properties and is therefore wrong
//    }
//  }
  private String[] findColors(String value) {
    String[] ret = value.split(",");
    return ret;
  }

}

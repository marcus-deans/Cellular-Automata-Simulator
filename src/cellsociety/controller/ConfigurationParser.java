package cellsociety.controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
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
  public Map<String, String> parseSim() throws IOException {
    FileReader reader = new FileReader(filename);

    Properties p = new Properties();
    p.load(reader);
    Set<String> keys = p.stringPropertyNames();
    String[] requiredParams = {"Title", "Author", "Type", "Description", "InitialStates",
        "StateColors"};
    for (String s : keys) {
      for (String parameters : requiredParams) {
        if (addedToMapIgnoreCase(p, parameters, s)) {
          continue;
        } else
          returnedValues.put(s, p.getProperty(s));
      }
    }
    return returnedValues;
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

package cellsociety.controller;

public class GameController {
  int myGameType;

  public GameController(){
    myGameType = 0;
  }
    public void parseInput(String text){
      InputParser myInputParser = new InputParser(text);
    }
}

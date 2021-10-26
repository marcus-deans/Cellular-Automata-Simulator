package cellsociety.view.ui.controlpanel;

import cellsociety.view.ui.SharedUIComponents;
import javafx.scene.Group;

public class ControlPanel extends SharedUIComponents {
  protected int myControlPanelX;

  public ControlPanel(Group root, int controlPanelX){
    super(root);
    myControlPanelX = controlPanelX;
  }
}

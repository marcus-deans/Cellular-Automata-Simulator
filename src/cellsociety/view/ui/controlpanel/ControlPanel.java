package cellsociety.view.ui.controlpanel;

import cellsociety.view.ui.SharedUIComponents;
import javafx.scene.Group;

/**
 * JavaFX View for each game that creates the general UI; each instance for a single game
 * application Relies on appropriate resourcebundles being configured as well as JavaFX Creates
 * gameController
 *
 * @author marcusdeans, drewpeterson
 */
public class ControlPanel extends SharedUIComponents {
  //TODO make private
  protected int myControlPanelX;

  public ControlPanel(int controlPanelX){
    myControlPanelX = controlPanelX;
  }
}

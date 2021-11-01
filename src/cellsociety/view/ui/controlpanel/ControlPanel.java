package cellsociety.view.ui.controlpanel;

import cellsociety.view.ui.SharedUIComponents;
import javafx.scene.Group;

/**
 * JavaFX superclass that holds the necessary shared information for the control panel displays.
 * Relies on appropriate resourcebundles being configured, SharedUIComponents, and JavaFX
 *
 * @author marcusdeans, drewpeterson
 */
public abstract class ControlPanel extends SharedUIComponents {
  protected int myControlPanelX;

  /**
   * Create the general control panel constructor
   * @param controlPanelX the location on the UI that the control panel should be located at
   */
  public ControlPanel(int controlPanelX){
    myControlPanelX = controlPanelX;
  }
}

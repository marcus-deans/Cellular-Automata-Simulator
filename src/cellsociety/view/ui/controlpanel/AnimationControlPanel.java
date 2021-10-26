package cellsociety.view.ui.controlpanel;

import cellsociety.controller.GameController;
import cellsociety.view.ui.SharedUIComponents;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class AnimationControlPanel extends ControlPanel {
  private Button pauseGameButton;
  private boolean isPaused;
  private Timeline myAnimation;
  private GameController myGameController;

  public AnimationControlPanel(Group root, Timeline animation, GameController gameController, int controlPanelX){
    super(root, controlPanelX);
    myAnimation = animation;
    myGameController = gameController;
    createAnimationControlPanel();
  }


  private void createAnimationControlPanel(){
    VBox panel = new VBox();
    panel.setSpacing(getInt("control_panel_spacing"));

    Node runGameButton = initializeRunAnimationButton();
    panel.getChildren().add(runGameButton);

    Node pauseGameButton = initializePauseButton();
    panel.getChildren().add(pauseGameButton);

    Node stepAnimationButton = initializeStepAnimationButton();
    panel.getChildren().add(stepAnimationButton);

    Node clearScreenButton = initializeClearScreenButton();
    panel.getChildren().add(clearScreenButton);

    panel.setLayoutX(myControlPanelX);
    panel.setLayoutY(getInt("animation_control_panel_y"));
    panel.setId("animation-control-panel");

    myGameViewRoot.getChildren().add(panel);
  }


  //create button to run simulation
  private Node initializeRunAnimationButton() {
    Button runAnimationButton = makeButton(getWord("run_game"), value -> myAnimation.play());
    return runAnimationButton;
  }

  //start and stop button in UI
  private Node initializePauseButton() {
    pauseGameButton = makeButton(getWord("pause_game"), value -> togglePause());
    return pauseGameButton;
  }

  // Start or stop searching animation as appropriate
  private void togglePause() {
    if (isPaused) {
      pauseGameButton.setText(getWord("pause_game"));
      myAnimation.play();
    } else {
      pauseGameButton.setText(getWord("resume_game"));
      myAnimation.pause();
    }
    isPaused = !isPaused;
  }

  //create button to step through animation
  private Node initializeStepAnimationButton() {
    Button stepAnimationButton = makeButton(getWord("step_game"), value -> step());
    return stepAnimationButton;
  }

  //create the clear screen button
  private Node initializeClearScreenButton() {
    //TODO: update for this program
    Button clearScreen = makeButton(getWord("clear_text"), event -> {
      clearPanels();
//      createUIPanels();
    });
    return clearScreen;
  }


  //step the animation once
  private void step() {
    myGameController.runSimulation();
  }
}

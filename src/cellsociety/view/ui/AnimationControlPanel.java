package cellsociety.view.ui;

import cellsociety.view.ui.SharedUIComponents;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class AnimationControlPanel extends SharedUIComponents {
  private Button pauseGameButton;
  private boolean isPaused;
  private Timeline myAnimation;

  private static final int ANIMATION_CONTROL_PANEL_Y = 300;

  public AnimationControlPanel(Group root, Timeline animation){
    super(root);
    myAnimation = animation;
    createAnimationControlPanel();
  }


  private void createAnimationControlPanel(){
    VBox panel = new VBox();
    panel.setSpacing(15);

    Node runGameButton = initializeRunAnimationButton();
    panel.getChildren().add(runGameButton);

    Node pauseGameButton = initializePauseButton();
    panel.getChildren().add(pauseGameButton);

    Node stepAnimationButton = initializeStepAnimationButton();
    panel.getChildren().add(stepAnimationButton);

    Node clearScreenButton = initializeClearScreenButton();
    panel.getChildren().add(clearScreenButton);

    panel.setLayoutX(controlPanelX);
    panel.setLayoutY(ANIMATION_CONTROL_PANEL_Y);
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
      createUIPanels();
    });
    return clearScreen;
  }
}

package util;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;


/**
 * This class acts as an intermediary between an OpenJFX application and TestFX.
 *
 * It adds support for some extra UI components and employs a hack to work around an existing bug in
 * the current version to get them all to work properly.
 *
 * @author Robert C. Duvall
 */
public class DukeApplicationTest extends ApplicationTest {
    // typical dialog box submit button text, perhaps should be a parameter?
    private static final String SUBMIT = "OK";


    // standard steps for all test applications so factor out here
    @BeforeAll
    public static void setUpClass () {
        // explicitly use the most stable robot implementation to avoid some older versions
        //   https://stackoverflow.com/questions/52605298/simple-testfx-example-fails
        System.setProperty("testfx.robot", "glass");
    }

    // standard steps for all test applications so factor out here
    @AfterEach
    public void tearDown () throws Exception {
        // remove stage of running app
        FxToolkit.cleanupStages();
        // clear any key or mouse presses left unreleased
        release(new KeyCode[] {});
        release(new MouseButton[] {});
    }


    // utility methods for different UI components

    /**
     * Simulate a click on any kind of button
     */
    protected void clickOn (ButtonBase b) {
        simulateAction(b, () -> b.fire());
    }

    /**
     * Simulate a click within given GridPane
     */
    protected void clickOn (GridPane g, Node item) {
        Point2D offset = point(item).atPosition(Pos.CENTER).query();
        simulateAction(offset.getX(), offset.getY(),
                       () -> g.getOnMouseClicked().handle(new MouseEvent(MouseEvent.MOUSE_CLICKED, offset.getX(), offset.getY(), offset.getX(), offset.getY(), MouseButton.PRIMARY, 1,
                                                 false, false, false, false, true, false, false, true, false, false,
                                                          new PickResult(item, offset.getX(), offset.getY()))));
    }

    /**
     * Simulate a click within a non-button node
     */
    protected void clickOn (Node n, int x, int y) {
        Point2D offset = point(n).atPosition(Pos.TOP_LEFT).atOffset(new Point2D(x, y)).query();
        simulateAction(offset.getX(), offset.getY(),
                       () -> n.getOnMouseClicked().handle(new MouseEvent(MouseEvent.MOUSE_CLICKED, offset.getX(), offset.getY(), offset.getX(), offset.getY(), MouseButton.PRIMARY, 1,
                                                 false, false, false, false, true, false, false, true, false, false,
                                                          new PickResult(n, x, y))));
    }

    /**
     * Simulate typing given text in any text component
     */
    protected void writeInputTo (TextInputControl t, String text) {
        simulateAction(t, () -> {
            t.clear();  // NOTE: not always a good assumption
            t.requestFocus();
            write(text);
        });
    }

    /**
     * Simulate writing given text(s) within the fields of a DialogBox
     */
    protected void writeInputsToDialog (String ... textInput) {
        int k = 0;
        for (Node field : lookup(".dialog-pane .text-field").queryAll()) {
            if (k < textInput.length) {
                writeInputTo((TextInputControl)field, textInput[k]);
                k += 1;
            }
        }
        // look up resulting dialog's button based on its label
        clickOn(lookup(SUBMIT).query());
    }


    /**
     * Simulate setting a Slider to given value
     */
    protected void setValue (Slider s, double value) {
        simulateAction(s, () -> s.setValue(value));
    }

    /**
     * Simulate setting a ColorPicker to given value
     */
    protected void setValue (ColorPicker cp, Color value) {
        simulateAction(cp, () -> { cp.setValue(value); cp.fireEvent(new ActionEvent()); });
    }


    /**
     * Simulate selecting given value from a ComboBox
     */
    protected void select (ComboBox<String> cb, String value) {
        // FIXME: duplicated code - but no common ancestor defines getSelectionModel()
        simulateAction(cb, () -> cb.getSelectionModel().select(value));
    }

    /**
     * Simulate selecting given value from a ChoiceBox
     */
    protected void select (ChoiceBox<String> cb, String value) {
        // FIXME: duplicated code - but no common ancestor defines getSelectionModel()
        simulateAction(cb, () -> cb.getSelectionModel().select(value));
    }

    /**
     * Simulate selecting given value from a ListView
     */
    protected void select (ListView<String> lv, String value) {
        // FIXME: duplicated code - but no common ancestor defines getSelectionModel()
        simulateAction(lv, () -> lv.getSelectionModel().select(value));
    }

    /**
     * Returns message displayed in currently displayed DialogBox
     */
    protected String getDialogMessage () {
        String message = ((Label)lookup(".dialog-pane .content").query()).getText();
        clickOn(lookup(SUBMIT).query());
        return message;
    }

    /**
     * Run given view method as a JFX action, allowing you to add/remove components from the Scene or Stage.
     */
    protected void runAsJFXAction (Runnable action) {
        // fire event using given action on the given node
        Platform.runLater(action);
        // make it "later" so the requested event has time to run
        WaitForAsyncUtils.waitForFxEvents();
    }


    // HACKs: needed to get simulating a UI action working :(
    private void simulateAction (Node n, Runnable action) {
        // simulate robot motion, not strictly necessary but helps show what test is being run
        moveTo(n);
        // fire event using given action on the given node
        runAsJFXAction(action);
    }

    private void simulateAction (double x, double y, Runnable action) {
        // simulate robot motion, not strictly necessary but helps show what test is being run
        moveTo(x, y);
        // fire event using given action on the given node
        runAsJFXAction(action);
    }
}

package cellsociety.view.populationchart;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Create new pie chart to view population, depends on JavaFX and GridView
 *
 * @author marcusdeans
 */
public class PopulationChartView extends Application {
  private Map<Integer, Integer> myStateCount;
  private static final Color BACKGROUND_COLOUR = Color.web("#DEE4E7");

  //General resource file structure
  private static final String RESOURCE_FILE_PATH = "cellsociety.resources.view.populationChart";
  private static final ResourceBundle populationChartResources = ResourceBundle.getBundle(RESOURCE_FILE_PATH);

  /**
   * Create the new population chart view
   * @param stateCount the values for the different simulation states
   */
  public PopulationChartView(Map<Integer, Integer> stateCount){
    myStateCount = stateCount;
  }

  /**
   * Start the JavaFX animation of the chart
   * @param stage the stage that elements should be put on
   */
    @Override public void start(Stage stage) {
      Scene myPopulationChartScene = new Scene(new Group());
      myPopulationChartScene.getStylesheets().add(PopulationChartView.class.getResource("PopulationChartViewFormatting.css").toExternalForm());
      myPopulationChartScene.setFill(BACKGROUND_COLOUR);
      stage.setTitle(getString("population_chart_title"));
      stage.setWidth(getInt("population_chart_width"));
      stage.setHeight(getInt("population_chart_height"));

      int keyNumber = myStateCount.size();
      ArrayList<Data> populationChartData = new ArrayList<>();
      for(int counter = 0; counter < keyNumber; counter++){
        populationChartData.add(new PieChart.Data(String.valueOf(counter), myStateCount.get(counter)));
      }
      ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(populationChartData);

      final PieChart chart = new PieChart(pieChartData);
      chart.setTitle(getString("population_chart_title"));

      final Label caption = new Label("");
      caption.setId("population-chart-caption");

      for (final PieChart.Data data : chart.getData()) {
        data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
            e -> {
              double total = 0;
              for (PieChart.Data d : chart.getData()) {
                total += d.getPieValue();
              }
              caption.setTranslateX(e.getSceneX());
              caption.setTranslateY(e.getSceneY());
              String text = String.format("%.1f%%", 100*data.getPieValue()/total) ;
              caption.setText(text);
            }
        );
      }


      ((Group) myPopulationChartScene.getRoot()).getChildren().add(chart);
      stage.setScene(myPopulationChartScene);
      stage.show();
    }

    public static void main(String[] args) {
      launch(args);
    }

  //get a String resource from the resource file
  private String getString(String key) {
    ResourceBundle words = ResourceBundle.getBundle("words");
    String value = "error";
    try {
      value = words.getString(key);
    } catch (Exception exception) {
      sendAlert(String.format("%s string was not found in Resource File %s", key, RESOURCE_FILE_PATH));
    }
    return value;
  }

  //set an alert to the user indicating incorrect input
  private void sendAlert(String alertMessage) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setContentText(alertMessage);
    alert.show();
  }

  //get an Integer resource from the resource file
  private int getInt(String key) {
    int value = -1;
    try {
      value = Integer.parseInt(populationChartResources.getString(key));
    } catch (Exception exception) {
      sendAlert(String.format("%s string was not found in Resource File %s", key, RESOURCE_FILE_PATH));
    }
    return value;
  }


  }

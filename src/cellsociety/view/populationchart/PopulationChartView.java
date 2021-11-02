package cellsociety.view.populationchart;
import cellsociety.view.GameView;
import java.util.ArrayList;
import java.util.Map;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
  import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;


public class PopulationChartView extends Application {
  private Map<Integer, Integer> myStateCount;
  private static final Color BACKGROUND_COLOUR = Color.web("#012169");
  public PopulationChartView(Map<Integer, Integer> stateCount){
    myStateCount = stateCount;
  }

    @Override public void start(Stage stage) {
      Scene myPopulationChartScene = new Scene(new Group());
      myPopulationChartScene.getStylesheets().add(PopulationChartView.class.getResource("PopulationChartViewFormatting.css").toExternalForm());
      myPopulationChartScene.setFill(BACKGROUND_COLOUR);
      stage.setTitle("Population Pie Chart");
      stage.setWidth(500);
      stage.setHeight(500);

      int keyNumber = myStateCount.size();
      ArrayList<Data> populationChartData = new ArrayList<>();
      for(int counter = 0; counter < keyNumber; counter++){
        populationChartData.add(new PieChart.Data(String.valueOf(counter), myStateCount.get(counter)));
      }
      ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(populationChartData);

      final PieChart chart = new PieChart(pieChartData);
      chart.setTitle("Population Pie Chart");

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


  }

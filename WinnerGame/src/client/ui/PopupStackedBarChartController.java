package Client.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PopupStackedBarChartController {

	@FXML
	public AnchorPane							anchorPane;

	private Stage								dialogStage;

	@FXML
	private StackedBarChart<String, Double>		stackedBarChart;
	@FXML
	private CategoryAxis						stackedBarChartX;
	@FXML
	private NumberAxis							stackedBarChartY;

	public void setGraphData(ArrayList<HashMap<String, Double>> data, String[] categories) {
		ClientGameUIController.buildXYChart(data, categories, stackedBarChartX, stackedBarChartY, stackedBarChart);
	}

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void close() {
		this.dialogStage.close();
	}

	public static <T> void buildXYChart(ArrayList<HashMap<String, T>> data, String[] categories, CategoryAxis xAxis,
			NumberAxis yAxis, XYChart<String, T> barChart) {

		HashMap<String, XYChart.Series<String, T>> seriesMaps = new HashMap<String, XYChart.Series<String, T>>();

		for (int i = 0; i < data.size(); i++) {
			HashMap<String, T> roundMap = data.get(i);
			for (Map.Entry<String, T> entry : roundMap.entrySet()) {
				XYChart.Series<String, T> roundSeries = seriesMaps.get(entry.getKey());
				if (roundSeries == null) {
					XYChart.Series<String, T> newSerie = new XYChart.Series<>();
					newSerie.setName(entry.getKey());
					newSerie.getData().add(new XYChart.Data<String, T>(categories[i], entry.getValue()));
					seriesMaps.put(entry.getKey(), newSerie);
					barChart.getData().add(newSerie);
				} else {
					roundSeries.getData().add(new XYChart.Data<String, T>(categories[i], entry.getValue()));
				}
			}
		}
		
		xAxis.setCategories(FXCollections.observableArrayList(categories));
	}
}

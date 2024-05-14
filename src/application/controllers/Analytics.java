package application.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import application.utils.DBUtility;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class Analytics implements Initializable {

	@FXML
	private AnchorPane anchorContainerTotalAvailableMovies;

	@FXML
	private AnchorPane anchorContainerTotalRevenue;

	@FXML
	private AnchorPane anchorContainerTotalSoldTicket;

	@FXML
	private PieChart pieChart;

	@FXML
	private LineChart<String, Number> lineChart;

	@FXML
	private Label totalRevenueAmountLabel, availableMoviesLabel, totalTicketsSoldLabel;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		addDataToPieChart();
		addDataToLineChart();

		// total ticket sold
		int totalTicketsSoldCount = DBUtility.totalTicketsSold();
		String totalTicketSoldString = String.valueOf(totalTicketsSoldCount);
		totalTicketsSoldLabel.setText(totalTicketSoldString);

		// Total Revenue
		double totalRevenueAmount = DBUtility.calculateTotalPrice();
		int totalPriceInt = (int) totalRevenueAmount;
		String totalPriceString = String.valueOf(totalPriceInt);
		totalRevenueAmountLabel.setText(totalPriceString);

		// Total Movies
		int totalMoviesCount = DBUtility.countMovies();
		String totalMoviesCountString = String.valueOf(totalMoviesCount);
		availableMoviesLabel.setText(totalMoviesCountString);
	}

	// Method to add data to the pie chart
	public void addDataToPieChart() {
		int[] ticketCounts = DBUtility.countTicketsByType();

		// Get the data list from the pie chart
		PieChart.Data data1 = new PieChart.Data("Standard", ticketCounts[2]);
		PieChart.Data data2 = new PieChart.Data("Premium", ticketCounts[0]);
		PieChart.Data data3 = new PieChart.Data("VIP", ticketCounts[1]);

		// Add data to the pie chart
		pieChart.getData().addAll(data1, data2, data3);
	}

	private void addDataToLineChart() {
		// Create a series for the data
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName("Data Series");

		// Add data points to the series
		series.getData().add(new XYChart.Data<>("4 April", 15));
		series.getData().add(new XYChart.Data<>("5 April", 20));
		series.getData().add(new XYChart.Data<>("6 April", 18));
		series.getData().add(new XYChart.Data<>("7 April", 28));
		series.getData().add(new XYChart.Data<>("8 April", 14));
		series.getData().add(new XYChart.Data<>("9 April", 22));
		series.getData().add(new XYChart.Data<>("10 April", 26));
		// Add more data points as needed

		// Add the series to the line chart
		lineChart.getData().add(series);
	}

}

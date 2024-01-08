import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.util.List;

/**
 * Klasa narzędziowa do generowania i wyświetlania wykresów XY Line przy użyciu biblioteki JFreeChart.
 */
public class ChartGenerator {

    /**
     * Generuje i wyświetla wykres XY Line na podstawie dostarczonych danych.
     *
     * @param timePoints Lista punktów czasowych.
     * @param xPoints     Lista wartości na osi X.
     * @param yPoints     Lista wartości na osi Y.
     */
    public static void generateCharts(List<Double> timePoints, List<Double> xPoints, List<Double> yPoints) {
        // Tworzenie serii danych dla współrzędnych X i Y
        XYSeries seriesX = new XYSeries("X(t)");
        XYSeries seriesY = new XYSeries("Y(t)");

        // Dodawanie punktów danych do serii
        for (int i = 0; i < timePoints.size(); i++) {
            seriesX.add(timePoints.get(i), xPoints.get(i));
            seriesY.add(timePoints.get(i), yPoints.get(i));
        }

        // Tworzenie kolekcji serii danych
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesX);
        dataset.addSeries(seriesY);

        // Tworzenie wykresu XY Line
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Trajektoria",
                "Czas [s]",
                "Współrzędne",
                dataset
        );

        // Wyświetlanie wykresu w ramce JFrame
        JFrame frame = new JFrame("Wykres");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ChartPanel chartPanel = new ChartPanel(chart);
        frame.getContentPane().add(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }
}

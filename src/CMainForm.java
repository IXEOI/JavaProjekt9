import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Klasa reprezentująca główne okno aplikacji GUI do symulacji rzutu ukośnego.
 * @author Kacper Hanus
 * @author Michał Florian
 */
public class CMainForm extends JFrame {
    private JTextField initialVelocityField;
    private JTextField launchAngleField;
    private JTextField massField;
    private JTextField airResistanceField;

    private JTextArea resultTextArea;
    private ChartPanel chartPanel;

    private JButton symulujButton;
    private JButton zapiszWynikiButton;
    private JButton zapiszWykresButton;

    private boolean symulacjaWykonana = false;

    /**
     * Konstruktor klasy CMainForm, inicjalizuje komponenty interfejsu użytkownika.
     */
    public CMainForm() {
        setTitle("Rzut ukośny z GUI");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Panel dla danych wejściowych
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2));

        inputPanel.add(new JLabel("Początkowa prędkość [m/s]:"));
        initialVelocityField = new JTextField();
        inputPanel.add(initialVelocityField);

        inputPanel.add(new JLabel("Kąt rzutu [stopnie]:"));
        launchAngleField = new JTextField();
        inputPanel.add(launchAngleField);

        inputPanel.add(new JLabel("Masa ciała [kg]:"));
        massField = new JTextField();
        inputPanel.add(massField);

        inputPanel.add(new JLabel("Współczynnik oporu powietrza:"));
        airResistanceField = new JTextField();
        inputPanel.add(airResistanceField);

        symulujButton = new JButton("Symuluj ruch");
        symulujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSymulujButtonClick();
            }
        });

        inputPanel.add(symulujButton);
        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Panel wyników i wykresu
        JPanel resultChartPanel = new JPanel(new GridLayout(1, 2));

        // Panel dla wyników
        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);
        resultTextArea.setBackground(new Color(240, 240, 240));
        resultTextArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JScrollPane resultScrollPane = new JScrollPane(resultTextArea);
        resultScrollPane.setBorder(BorderFactory.createEmptyBorder());
        resultChartPanel.add(resultScrollPane);

        // Panel dla wykresu
        chartPanel = new ChartPanel(null);
        resultChartPanel.add(chartPanel);

        mainPanel.add(resultChartPanel, BorderLayout.CENTER);

        // Przyciski pod panelem wyników i wykresu
        JPanel buttonPanel = new JPanel(new FlowLayout());

        zapiszWynikiButton = new JButton("Zapisz Obliczenia");
        zapiszWynikiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSaveButtonClick();
            }
        });
        zapiszWynikiButton.setEnabled(false);
        buttonPanel.add(zapiszWynikiButton);

        zapiszWykresButton = new JButton("Zapisz Wykres");
        zapiszWykresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSaveChartButtonClick();
            }
        });
        zapiszWykresButton.setEnabled(false);
        buttonPanel.add(zapiszWykresButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Metoda obsługująca kliknięcie przycisku "Symuluj ruch".
     */
    private void onSymulujButtonClick() {
        symulacjaWykonana = false;
        zapiszWynikiButton.setEnabled(false);
        zapiszWykresButton.setEnabled(false);

        try {
            double initialVelocity = Double.parseDouble(initialVelocityField.getText().replace(",", "."));
            double launchAngle = Double.parseDouble(launchAngleField.getText().replace(",", "."));
            double mass = Double.parseDouble(massField.getText().replace(",", "."));
            double airResistanceCoefficient = Double.parseDouble(airResistanceField.getText().replace(",", "."));

            MotionSimulator simulator = new MotionSimulator(10.0, initialVelocity, launchAngle, mass, airResistanceCoefficient);
            double timeStep = 0.02;
            double totalTime = 2 * initialVelocity * Math.sin(Math.toRadians(launchAngle)) / 10.0;

            MotionData motionData = simulator.simulateMotion(timeStep, totalTime);

            JFreeChart chart = createChart(motionData.getTimePoints(), motionData.getXPoints(), motionData.getYPoints());
            chartPanel.setChart(chart);

            updateResultPanel(motionData);

            symulacjaWykonana = true;
            zapiszWynikiButton.setEnabled(true);
            zapiszWykresButton.setEnabled(true);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Błąd: Wprowadź poprawne wartości liczbowe.", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Metoda obsługująca kliknięcie przycisku "Zapisz Obliczenia".
     */
    private void onSaveButtonClick() {
        if (!symulacjaWykonana) {
            JOptionPane.showMessageDialog(this, "Najpierw wykonaj symulację przed zapisem wyników.", "Informacja", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            String filePath = "wyniki_symulacji.txt";
            List<String> data = resultTextAreaLinesToList();
            DataWriter.saveDataToFile(filePath, data);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Błąd podczas zapisywania wyników.", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Metoda obsługująca kliknięcie przycisku "Zapisz Wykres".
     */
    private void onSaveChartButtonClick() {
        if (!symulacjaWykonana) {
            JOptionPane.showMessageDialog(this, "Najpierw wykonaj symulację przed zapisem wykresu.", "Informacja", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            String filePath = "wykres.png";
            saveChartAsImage(chartPanel, filePath);
            JOptionPane.showMessageDialog(this, "Wykres został zapisany do pliku: " + filePath, "Informacja", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Błąd podczas zapisywania wykresu.", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Metoda konwertująca linie tekstu z pola wynikowego na listę.
     */
    private List<String> resultTextAreaLinesToList() {
        String[] lines = resultTextArea.getText().split("\n");
        return List.of(lines);
    }

    /**
     * Metoda zapisująca wykres do pliku graficznego.
     */
    private void saveChartAsImage(ChartPanel chartPanel, String filePath) {
        JFreeChart chart = chartPanel.getChart();
        try {
            ChartUtilities.saveChartAsPNG(new File(filePath), chart, chartPanel.getWidth(), chartPanel.getHeight());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda tworząca wykres na podstawie danych z symulacji.
     */
    private JFreeChart createChart(List<Double> timePoints, List<Double> xPoints, List<Double> yPoints) {
        XYSeries seriesY = new XYSeries("Y(t)");

        for (int i = 0; i < timePoints.size(); i++) {
            seriesY.add(timePoints.get(i), yPoints.get(i));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesY);

        return ChartFactory.createXYLineChart(
                "Tor ruchu",
                "Czas [s]",
                "Współrzędne",
                dataset
        );
    }

    /**
     * Metoda aktualizująca panel wyników na podstawie danych z symulacji.
     */
    private void updateResultPanel(MotionData motionData) {
        DecimalFormat format = new DecimalFormat("#.##");

        resultTextArea.setText("Wyniki symulacji:\n");
        resultTextArea.append("Czas [s]\tY(t) [m]\n");

        List<Double> timePoints = motionData.getTimePoints();

        List<Double> yPoints = motionData.getYPoints();

        for (int i = 0; i < timePoints.size(); i++) {
            double time = timePoints.get(i);

            double y = yPoints.get(i);

            resultTextArea.append(format.format(time)   + "\t" + format.format(y) + "\n");
        }
    }

    /**
     * Metoda uruchamiająca aplikację.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CMainForm();
            }
        });
    }
}

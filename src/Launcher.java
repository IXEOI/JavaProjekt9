import java.util.Scanner;

/**
 * Klasa Launcher służy do uruchamiania symulacji ruchu na podstawie danych wprowadzonych przez użytkownika.
 */
public class Launcher {

    /**
     * Metoda główna programu, która inicjuje symulację na podstawie danych wprowadzonych przez użytkownika.
     *
     * @param args Argumenty wiersza poleceń (nie używane).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Wprowadzanie danych przez użytkownika
        System.out.println("Podaj początkową prędkość [m/s]: ");
        double initialVelocity = scanner.nextDouble();

        System.out.println("Podaj kąt rzutu [stopnie]: ");
        double launchAngle = scanner.nextDouble();

        System.out.println("Podaj masę ciała [kg]: ");
        double mass = scanner.nextDouble();

        System.out.println("Podaj współczynnik oporu powietrza: ");
        double airResistanceCoefficient = scanner.nextDouble();

        // Tworzenie obiektu symulatora z podanymi przez użytkownika parametrami
        MotionSimulator simulator = new MotionSimulator(
                10.0, initialVelocity, launchAngle, mass, airResistanceCoefficient);

        double timeStep = 0.02; // krok czasowy [s]
        double totalTime = 2 * initialVelocity * Math.sin(Math.toRadians(launchAngle)) / 10.0; // Domyślne g

        // Symulacja ruchu
        MotionData motionData = simulator.simulateMotion(timeStep, totalTime);

        // Wywołanie ChartGenerator do wygenerowania wykresu na podstawie wyników symulacji
        ChartGenerator.generateCharts(motionData.getTimePoints(), motionData.getXPoints(), motionData.getYPoints());
    }
}

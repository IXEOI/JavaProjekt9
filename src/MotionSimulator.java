import java.util.ArrayList;
import java.util.List;

/**
 * Klasa MotionSimulator symuluje ruch ciała w dwóch wymiarach uwzględniając opór powietrza.
 */
class MotionSimulator {
    private double g; // przyspieszenie ziemskie [m/s^2]
    private double initialVelocity; // początkowa prędkość [m/s]
    private double launchAngle; // kąt rzutu [stopnie]
    private double mass; // masa ciała [kg]
    private double airResistanceCoefficient; // współczynnik oporu powietrza
    private double maxAirResistanceCoefficient; // Maksymalna wartość współczynnika oporu powietrza

    /**
     * Konstruktor klasy MotionSimulator.
     *
     * @param g                       Przyspieszenie ziemskie [m/s^2].
     * @param initialVelocity         Początkowa prędkość [m/s].
     * @param launchAngle             Kąt rzutu [stopnie].
     * @param mass                    Masa ciała [kg].
     * @param airResistanceCoefficient Współczynnik oporu powietrza.
     */
    MotionSimulator(double g, double initialVelocity, double launchAngle,
                    double mass, double airResistanceCoefficient) {
        this.g = g;
        this.initialVelocity = initialVelocity;
        this.launchAngle = Math.toRadians(launchAngle);
        this.mass = mass;
        this.airResistanceCoefficient = airResistanceCoefficient;
        this.maxAirResistanceCoefficient = 1.0; // Ustaw maksymalną wartość współczynnika oporu powietrza
    }

    /**
     * Metoda symulująca ruch ciała.
     *
     * @param timeStep  Krok czasowy [s].
     * @param totalTime Całkowity czas symulacji [s].
     * @return Dane ruchu w postaci obiektu MotionData.
     */
    MotionData simulateMotion(double timeStep, double totalTime) {
        List<Double> timePoints = new ArrayList<>();
        List<Double> xPoints = new ArrayList<>();
        List<Double> yPoints = new ArrayList<>();

        double beta = airResistanceCoefficient / maxAirResistanceCoefficient; // Przeskalowanie do zakresu 0-1

        double vx = initialVelocity * Math.cos(launchAngle);
        double vy = initialVelocity * Math.sin(launchAngle);

        double x = 0.0;
        double y = 0.0;

        for (double t = 0; t <= totalTime; t += timeStep) {
            timePoints.add(t);
            xPoints.add(x);
            yPoints.add(y);

            double[] k1 = calculateDerivatives(t, x, y, vx, vy, beta);
            double[] k2 = calculateDerivatives(t + timeStep / 2, x + timeStep / 2 * k1[0],
                    y + timeStep / 2 * k1[1], vx + timeStep / 2 * k1[2], vy + timeStep / 2 * k1[3], beta);
            double[] k3 = calculateDerivatives(t + timeStep / 2, x + timeStep / 2 * k2[0],
                    y + timeStep / 2 * k2[1], vx + timeStep / 2 * k2[2], vy + timeStep / 2 * k2[3], beta);
            double[] k4 = calculateDerivatives(t + timeStep, x + timeStep * k3[0],
                    y + timeStep * k3[1], vx + timeStep * k3[2], vy + timeStep * k3[3], beta);

            x += timeStep / 6 * (k1[0] + 2 * k2[0] + 2 * k3[0] + k4[0]);
            y += timeStep / 6 * (k1[1] + 2 * k2[1] + 2 * k3[1] + k4[1]);
            vx += timeStep / 6 * (k1[2] + 2 * k2[2] + 2 * k3[2] + k4[2]);
            vy += timeStep / 6 * (k1[3] + 2 * k2[3] + 2 * k3[3] + k4[3]);
        }

        return new MotionData(timePoints, xPoints, yPoints);
    }

    private double[] calculateDerivatives(double t, double x, double y, double vx, double vy, double beta) {
        double dxdt = vx;
        double dydt = vy;
        double dvxdt = -beta * vx; // Wpływ masy ciała
        double dvydt = -g - beta * vy; // Wpływ masy ciała

        return new double[]{dxdt, dydt, dvxdt, dvydt};
    }
}

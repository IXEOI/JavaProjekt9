import java.util.List;

/**
 * Klasa MotionData reprezentuje dane ruchu, zawierające listy punktów czasowych, współrzędnych X i Y.
 */
public class MotionData {
    private List<Double> timePoints;
    private List<Double> xPoints;
    private List<Double> yPoints;

    /**
     * Konstruktor klasy MotionData.
     *
     * @param timePoints Lista punktów czasowych.
     * @param xPoints    Lista współrzędnych X.
     * @param yPoints    Lista współrzędnych Y.
     */
    public MotionData(List<Double> timePoints, List<Double> xPoints, List<Double> yPoints) {
        this.timePoints = timePoints;

        this.yPoints = yPoints;
    }

    /**
     * Metoda zwracająca listę punktów czasowych.
     *
     * @return Lista punktów czasowych.
     */
    public List<Double> getTimePoints() {
        return timePoints;
    }

    /**
     * Metoda zwracająca listę współrzędnych X.
     *
     * @return Lista współrzędnych X.
     */
    public List<Double> getXPoints() {
        return xPoints;
    }

    /**
     * Metoda zwracająca listę współrzędnych Y.
     *
     * @return Lista współrzędnych Y.
     */
    public List<Double> getYPoints() {
        return yPoints;
    }
}

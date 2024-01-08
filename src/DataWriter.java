import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Klasa do zapisywania danych do pliku tekstowego.
 */
public class DataWriter {

    /**
     * Metoda zapisująca listę danych do pliku tekstowego.
     *
     * @param filePath Ścieżka do pliku, w którym zostaną zapisane dane.
     * @param data     Lista danych do zapisu.
     */
    public static void saveDataToFile(String filePath, List<String> data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Zapisywanie nagłówków
            writer.write("Czas [s]X(t) [m] Y(t) [m]");
            writer.newLine();

            // Zapisywanie danych
            for (String line : data) {
                writer.write(line);
                writer.newLine();
            }

            System.out.println("Dane zostały zapisane do pliku: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

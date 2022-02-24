import alhorithm.Murmur3;

import java.io.FileReader;

public class Main {

    public static void main(String[] args) {
        String data = readFromFile();
        long hash = Murmur3.hash("BSUIR");
        System.out.println(hash);
    }

    public static String readFromFile() {
        StringBuilder builder = new StringBuilder();

        try (FileReader reader = new FileReader("src/main/resources/text.txt")) {
            while (reader.ready()) {
                builder.append(reader.read());
            }
        } catch (Exception ignore) {}

        return builder.toString();
    }

}

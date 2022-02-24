import algorithm.Generator;
import algorithm.Rsa;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Generator.Keys keys = Generator.getKeys();

        String sign = Rsa.sign("BSUIR", keys.getPrivateKeys());
        System.out.println("Sigh: " + sign);

        boolean result = Rsa.verify("BSUIr", sign, keys.getPublicKeys());
        System.out.println("Is verified: " + result);
    }

    public static String readFromFile() {
        StringBuilder builder = new StringBuilder();

        try (FileReader reader = new FileReader("src/main/resources/text.txt")) {
            while (reader.ready()) {
                builder.append((char) reader.read());
            }
        } catch (Exception ignored) {}

        return builder.toString();
    }

    public static void writeInFile(String str) {
        try (FileWriter writer = new FileWriter("src/main/resources/text.txt")) {
            str.chars().forEach(c -> {
                try {
                    writer.write(c);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception ignored) {}
    }

}

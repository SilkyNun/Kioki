import algorithm.Generator;
import algorithm.Rabin;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Generator.Keys keys = Generator.getKeys();

//        String encrypt = Rabin.encrypt(readFromFile(), keys.getPublicKey());
//        writeInFile(encrypt);

        String decrypt = Rabin.decrypt(readFromFile(), keys);
        writeInFile(decrypt);
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

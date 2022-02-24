import algorithm.Generator;
import algorithm.SDes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        writeDataInFile(SDes.encrypt(readDataFromFile(), 123));
//        writeDataInFile(SDes.decrypt(readDataFromFile(), 123));
    }

    public static String readDataFromFile() throws IOException {
        FileReader reader = new FileReader("src/main/resources/m.txt");
        StringBuilder builder = new StringBuilder();

        while(reader.ready()) {
            builder.append((char) reader.read());
        }

        reader.close();
        return builder.toString();
    }

    public static void writeDataInFile(String data) throws IOException {
        FileWriter writer = new FileWriter("src/main/resources/m.txt");
        data.chars()
                .forEach(ch -> {
                    try {
                        writer.write(ch);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        writer.close();
    }
}

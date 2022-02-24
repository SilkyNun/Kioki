import algorithm.Lsb;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Main {
    private static final Path source = Path.of("src/main/resources/byron.jpg");
    private static final Path destination = Path.of("src/main/resources/byron_injected.png");

    public static void main(String[] args) throws IOException {
        Lsb.inject(readPoemFromFile(), source, destination);
        String retrieve = Lsb.retrieve(destination);
        System.out.println(retrieve);
    }

    public static String readPoemFromFile() throws IOException {
        FileReader reader = new FileReader("src/main/resources/poem.txt");
        StringBuilder builder = new StringBuilder();

        while (reader.ready()) {
            builder.append((char) reader.read());
        }

        reader.close();
        return builder.toString();
    }

}

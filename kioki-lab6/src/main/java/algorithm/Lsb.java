package algorithm;


import javax.imageio.ImageIO;
import javax.swing.text.TabExpander;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Lsb {
    private static final String END_FILLER = "1111100000111110000011111";

    public static void inject(String m, Path source, Path destination) throws IOException {
        String binaryString = toBinaryString(m);
        BufferedImage sourceImage = ImageIO.read(source.toFile());

        if (!validSizeOfMessage(binaryString, sourceImage)) {
            throw new IllegalArgumentException("Size of message is tooooo big =(");
        }

        BufferedImage modifiedImage = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), sourceImage.getType());


        List<String> splitString = splitString(binaryString);

        for (int i = 0; i < sourceImage.getWidth(); i++) {
            for (int j = 0; j < sourceImage.getHeight(); j++) {

                Color color = new Color(sourceImage.getRGB(i, j));

                if (!splitString.isEmpty()) {
                    color = modifyColor(color, splitString.get(0));
                    splitString.remove(0);
                }

                modifiedImage.setRGB(i, j, color.getRGB());
            }
        }

        ImageIO.write(modifiedImage, "png", destination.toFile());
    }

    public static String retrieve(Path source) throws IOException {
        BufferedImage sourceImage = ImageIO.read(source.toFile());
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < sourceImage.getWidth(); i++) {
            for (int j = 0; j < sourceImage.getHeight(); j++) {
                Color color = new Color(sourceImage.getRGB(i, j));
                builder.append(readColor(color));

                if (builder.toString().contains(END_FILLER)) {
                    break;
                }
            }

            if (builder.toString().contains(END_FILLER)) {
                break;
            }
        }

        return toString(builder.substring(0, builder.indexOf(END_FILLER)));
    }

    private static Color modifyColor(Color color, String str) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        int mRed  = (red & ~1) | Integer.valueOf(str.substring(0, 1), 2);
        int mGreen  = (green & ~1) | Integer.valueOf(str.substring(1, 2), 2);
        int mBlue  = (blue & ~1) | Integer.valueOf(str.substring(2, 3), 2);

        return new Color(mRed, mGreen, mBlue);
    }

    private static String readColor(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        return getLastBit(red) +
                getLastBit(green) +
                getLastBit(blue);
    }

    private static String getLastBit(int bitset) {
        if ((bitset | 1) > bitset)
            return "0";
        return "1";
    }

    private static String toBinaryString(String str) {
        return str.chars()
                .mapToObj(ch -> String.format("%8s", Integer.toBinaryString(ch)).replace(' ', '0'))
                .collect(Collectors.joining()) + END_FILLER;
    }

    private static String toString(String binaryString) {
        StringBuilder builder = new StringBuilder(binaryString);
        List<String> split = new ArrayList<>();

        while (builder.length() % 8 != 0) {
            builder.append("0");
        }

        while (builder.length() != 0) {
            split.add(builder.substring(0, 8));
            builder.delete(0, 8);
        }

        return split.stream()
                .map(str -> (char) Integer.valueOf(str, 2).intValue())
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private static List<String> splitString(String str) {
        List<String> result = new ArrayList<>();
        StringBuilder builder = new StringBuilder(str);

        //align string
        while (builder.length() % 3 != 0) {
            builder.append("0");
        }

        //split string
        while (builder.length() != 0) {
            result.add(builder.substring(0, 3));
            builder.delete(0, 3);
        }

        return result;
    }

    private static boolean validSizeOfMessage(String message, BufferedImage image) {
        return message.length() < image.getHeight() * image.getHeight() * 3;
    }

}

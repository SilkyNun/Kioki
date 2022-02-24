package denis.kioki.algorithm;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Cesar {
    public static String encrypt(String m, int k) {
        return m.chars()
                .map(c -> (c + k) % 256)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String decrypt(String c, int k) {
        int[] ints = c.chars()
                .map(ch -> (ch + 256 - k) % 256)
                .toArray();
        System.out.println(Arrays.toString(ints));
        String result = Arrays.stream(ints)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return result;
    }
}

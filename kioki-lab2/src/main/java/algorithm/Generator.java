package algorithm;

import java.util.Arrays;
import java.util.Map;

public class Generator {
    private static final int[] p10 = new int[] {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
    private static final int[] p8 = new int[] {6, 3, 7, 4, 8, 5, 10, 9};

    public static Map<String, String> genKeys(int k) {
        System.out.println(Integer.toBinaryString(k));
        System.out.println(Integer.toBinaryString((1 << 10) - 1));
        String key10 = String.format("%10s", Integer.toBinaryString(k & ((1 << 10) - 1))).replace(' ', '0');
        System.out.println(Integer.toBinaryString(k & ((1 << 10) - 1)));

        //permute using p10
        String p10Permute = permute(key10, p10);

        String left = p10Permute.substring(0, 5);
        String right = p10Permute.substring(5);

        //shifting 1 bit to left
        left =  shift(left, 1);
        right = shift(right, 1);

        //creating k1 using p8
        String k1 = permute(left + right, p8);

        //shifting 2 bits to right
        left = shift(left, 2);
        right = shift(right, 2);

        //creating k2 using p8
        String k2 = permute(left + right, p8);

        return Map.of(
                "k1", k1,
                "k2", k2
        );
    }

    private static String shift(String str, int times) {
        for (int i = 0; i < times; i++) {
            str = str.substring(1) + str.charAt(0);
        }
        return str;
    }

    static String permute(String str, int[] pattern) {
        return Arrays.stream(pattern)
                .map(i -> str.charAt(i - 1))
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}

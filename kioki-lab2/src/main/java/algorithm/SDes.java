package algorithm;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SDes {
    private final static int[] ip = new int[] {2, 6, 3, 1, 4, 8, 5, 7};
    private final static int[] fp = new int[] {4, 1, 3, 5, 7, 2, 8, 6};
    private final static int[] ep = new int[] {4, 1, 2, 3, 2, 3, 4, 1};
    private final static int[] p4 = new int[] {2, 4, 3, 1};
    private final static int[][] s1 = new int[][] {
            new int[] {1, 0, 3, 2},
            new int[] {3, 2, 1, 0},
            new int[] {0, 2, 1, 3},
            new int[] {3, 1, 3, 2},
    };
    private final static int[][] s2 = new int[][] {
            new int[] {0, 1, 2, 3},
            new int[] {2, 0, 1, 3},
            new int[] {3, 0, 1, 0},
            new int[] {2, 1, 0, 3},
    };


    public static String encrypt(String m, int k) {
        Map<String, String> keys = Generator.genKeys(k);
        List<String> list = toBinaryString(m);
        return list.stream()
                .map(s -> encryptBlock(s, keys))
                .map(SDes::fromBinaryString)
                .collect(Collectors.joining());
    }

    public static String decrypt(String m, int k) {
        Map<String, String> keys = Generator.genKeys(k);
        List<String> list = toBinaryString(m);
        return list.stream()
                .map(s -> decryptBlock(s, keys))
                .map(SDes::fromBinaryString)
                .collect(Collectors.joining());
    }

    private static List<String> toBinaryString(String str) {
        return str.chars()
                .mapToObj(ch -> String.format("%8s", Integer.toBinaryString(ch)).replace(' ', '0'))
                .toList();
    }

    public static String encryptBlock(String s, Map<String, String> keys) {
        String ipPermute = Generator.permute(s, ip);
        String afterFirstRound = doRound(ipPermute, keys.get("k1"));
        String transposed = afterFirstRound.substring(4) + afterFirstRound.substring(0, 4);
        String afterSecondRound = doRound(transposed, keys.get("k2"));
        String fpPermute = Generator.permute(afterSecondRound, fp);

        return fpPermute;
    }

    private static String decryptBlock(String s, Map<String, String> keys) {
        String ipPermute = Generator.permute(s, ip);
        String afterFirstRound = doRound(ipPermute, keys.get("k2"));
        String transposed = afterFirstRound.substring(4) + afterFirstRound.substring(0, 4);
        String afterSecondRound = doRound(transposed, keys.get("k1"));
        String fpPermute = Generator.permute(afterSecondRound, fp);

        return fpPermute;
    }

    public static String doRound(String s, String key) {
        String left = s.substring(0, 4);
        String right = s.substring(4);

        String epPermute = Generator.permute(right, ep);
        String xorResult = xorString(epPermute, key);
        String xorResultLeft = xorResult.substring(0, 4);
        String xorResultRight = xorResult.substring(4);
        String transformed = transformBySBlock(xorResultLeft, s1) + transformBySBlock(xorResultRight, s2);
        String p4Permute = Generator.permute(transformed, p4);

        String xor = xorString(p4Permute, left);
        String result = xor + right;

        return result;
    }

    private static String xorString(String a, String b) {
        return IntStream.range(0, a.length())
                .map(i -> {
                    if (a.charAt(i) == b.charAt(i)) return '0';
                    else return '1';
                })
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private static String transformBySBlock(String s, int[][] sBlock) {
        if (s.length() != 4)
            throw new IllegalArgumentException("Length of string is not 4");

        Integer row = Integer.valueOf(s.substring(0, 1) + s.substring(3, 4), 2);
        Integer column = Integer.valueOf(s.substring(1, 2) + s.substring(2, 3), 2);

        String result = String.format("%2s", Integer.toBinaryString(sBlock[row][column] & ((1 << 2) - 1))).replace(' ', '0');
        return result;
    }

    private static String fromBinaryString(String binary) {
        Integer value = Integer.valueOf(binary, 2);
        return String.valueOf((char) value.intValue());
    }
}

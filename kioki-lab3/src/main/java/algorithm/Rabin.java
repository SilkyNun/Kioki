package algorithm;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Rabin {
    private static final String TEXT_FILLER = "###";
    private static final String NUMBER_FILLER = "1415926535";
    private static final int SPLIT_LENGTH = 64;

    public static String encrypt(String m, BigInteger publicKey) {
        List<String> strings = splitString(m);

        return strings.stream()
                .map(Rabin::toBigInteger)
                .map(big -> encryptBlock(big, publicKey))
                .map(encrypted -> encrypted + NUMBER_FILLER)
                .collect(Collectors.joining());

    }

    public static String decrypt(String c, Generator.Keys keys) {

        List<String> strings = splitNumber(c);

        return strings.stream()
                .map(BigInteger::new)
                .map(big -> decryptBlock(big, keys))
                .map(Rabin::handleDecrypted)
                .collect(Collectors.joining());
    }

    private static List<String> splitString(String m) {
        List<String> result = new ArrayList<>();
        StringBuilder builder = new StringBuilder(m);

        while (builder.length() > SPLIT_LENGTH) {
            result.add(builder.substring(0, SPLIT_LENGTH) + TEXT_FILLER);
            builder.delete(0, SPLIT_LENGTH);
        }
        result.add(builder + TEXT_FILLER);

        return result;
    }

    private static String handleDecrypted(List<String> results) {
        return results.stream()
                .filter(str -> str.contains(TEXT_FILLER))
                .map(str -> str.replaceAll(TEXT_FILLER, ""))
                .findFirst()
                .orElse("null");
    }

    private static BigInteger toBigInteger(String str) {
        return new BigInteger(str.getBytes(StandardCharsets.US_ASCII));
    }

    private static List<String> splitNumber(String c) {
        return Arrays.stream(c.split(NUMBER_FILLER)).toList();
    }

    private static BigInteger encryptBlock(BigInteger m, BigInteger publicKey) {
        return m.modPow(BigInteger.TWO, publicKey);
    }

    private static List<String> decryptBlock(BigInteger c, Generator.Keys keys) {
        BigInteger n = keys.getPublicKey();
        BigInteger p = keys.getPrivateKeyP();
        BigInteger q = keys.getPrivateKeyQ();

        BigInteger mp = c.modPow(p.add(BigInteger.ONE).divide(BigInteger.valueOf(4L)), p);
        BigInteger mq = c.modPow(q.add(BigInteger.ONE).divide(BigInteger.valueOf(4L)), q);

        Map<String, BigInteger> ext = euclidExt(p, q);
        BigInteger yp = ext.get("yp");
        BigInteger yq = ext.get("yq");

        BigInteger m1 = yp.multiply(p).multiply(mq).add(yq.multiply(q).multiply(mp)).mod(n);
        BigInteger m2 = n.subtract(m1);
        BigInteger m3 = yp.multiply(p).multiply(mq).subtract(yq.multiply(q).multiply(mp)).mod(n);
        BigInteger m4 = n.subtract(m3);

        return List.of(
                new String(m1.toByteArray(), StandardCharsets.US_ASCII),
                new String(m2.toByteArray(), StandardCharsets.US_ASCII),
                new String(m3.toByteArray(), StandardCharsets.US_ASCII),
                new String(m4.toByteArray(), StandardCharsets.US_ASCII)
        );

    }

    private static Map<String, BigInteger> euclidExt(BigInteger p, BigInteger q) {
        BigInteger d0 = p;
        BigInteger d1 = q;
        BigInteger x0 = BigInteger.ONE;
        BigInteger x1 = BigInteger.ZERO;
        BigInteger y0 = BigInteger.ZERO;
        BigInteger y1 = BigInteger.ONE;

        while (d1.compareTo(BigInteger.ONE) > 0) {
            BigInteger k = d0.divide(d1);
            BigInteger d2 = d0.mod(d1);
            BigInteger x2 = x0.subtract(k.multiply(x1));
            BigInteger y2 = y0.subtract(k.multiply(y1));
            d0 = d1;
            d1 = d2;
            x0 = x1;
            x1 = x2;
            y0 = y1;
            y1 = y2;
        }

        return Map.of(
                "yp", x1,
                "yq", y1,
                "gcd", d1
        );
    }

}

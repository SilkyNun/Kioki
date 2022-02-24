package algorithm;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

public class Generator {
    private static final int KEY_LENGTH = 512;
    private static final Random random = new Random();


    @Data
    @AllArgsConstructor
    public static class Keys {
        private PublicKeys publicKeys;
        private PrivateKeys privateKeys;

        @Data
        @AllArgsConstructor
        public static class PublicKeys {
            private BigInteger publicKeyE;
            private BigInteger publicKeyR;
        }

        @Data
        @AllArgsConstructor
        public static class PrivateKeys {
            private BigInteger privateKeyD;
            private BigInteger privateKeyR;
        }
    }

    private static Keys genKeys() {
        BigInteger p;
        BigInteger q;
        do {
            p = BigInteger.probablePrime(KEY_LENGTH, random);
            q = BigInteger.probablePrime(KEY_LENGTH, random);
        } while (p.compareTo(q) == 0);

        BigInteger r = p.multiply(q);
        BigInteger eR = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        BigInteger e;
        do {
            e = BigInteger.probablePrime(KEY_LENGTH, random);
        } while (eR.gcd(e).compareTo(BigInteger.ONE) != 0);

        BigInteger d = euclidExt(eR, e);
        if (d.compareTo(BigInteger.ZERO) < 0) {
            d = d.add(eR);
        }

        return new Keys(
                new Keys.PublicKeys(e, r),
                new Keys.PrivateKeys(d, r)
        );
    }


    public static Generator.Keys getKeys() {
        Generator.Keys keys = loadKeys();
        if (keys == null) {
            Generator.Keys genKeys = Generator.genKeys();
            saveKeys(genKeys);
            return genKeys;
        }
        return keys;
    }

    private static Generator.Keys loadKeys() {
        Properties properties = new Properties();

        try (FileReader reader = new FileReader("src/main/resources/keys.properties")) {
            properties.load(reader);
        } catch (Exception ignore) {}

        if (properties.isEmpty()) {
            return null;
        }

        String publicKeyE = decodeKey(properties.getProperty("publicKeyE"));
        String publicKeyR = decodeKey(properties.getProperty("publicKeyR"));
        String privateKeyD = decodeKey(properties.getProperty("privateKeyD"));
        String privateKeyR = decodeKey(properties.getProperty("privateKeyR"));


        return new Generator.Keys(
                new Keys.PublicKeys(
                        new BigInteger(publicKeyE),
                        new BigInteger(publicKeyR)
                ),
                new Keys.PrivateKeys(
                        new BigInteger(privateKeyD),
                        new BigInteger(privateKeyR)
                )
        );
    }


    private static void saveKeys(Generator.Keys keys) {

        Properties properties = new Properties();
        properties.setProperty("publicKeyE", encodeKey(keys.getPublicKeys().getPublicKeyE()));
        properties.setProperty("publicKeyR", encodeKey(keys.getPublicKeys().getPublicKeyR()));
        properties.setProperty("privateKeyD", encodeKey(keys.getPrivateKeys().getPrivateKeyD()));
        properties.setProperty("privateKeyR", encodeKey(keys.getPrivateKeys().getPrivateKeyR()));

        try (FileWriter writer = new FileWriter("src/main/resources/keys.properties")) {
            properties.store(writer, "Keys");
        } catch (Exception ignore) {}
    }

    private static String encodeKey(BigInteger key) {
        return Base64.getEncoder()
                .encodeToString(key.toString().getBytes(StandardCharsets.US_ASCII));
    }

    private static String decodeKey(String encoded) {
        byte[] decode = Base64.getDecoder().decode(encoded);
        return new String(decode, StandardCharsets.US_ASCII);
    }

    private static BigInteger euclidExt(BigInteger a, BigInteger b) {
        BigInteger d0 = a;
        BigInteger d1 = b;
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

        return y1;
    }
}

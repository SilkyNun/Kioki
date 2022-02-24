package algorithm;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;
import java.util.Random;

public class Generator {
    private static final int LENGTH = 512;
    private static final Random random = new Random();

    @Data
    @AllArgsConstructor
    public static class Keys {
        private BigInteger publicKey;
        private BigInteger privateKeyQ;
        private BigInteger privateKeyP;
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

        String publicKey = decodeKey(properties.getProperty("publicKey"));
        String privateKeyQ = decodeKey(properties.getProperty("privateKeyQ"));
        String privateKeyP = decodeKey(properties.getProperty("privateKeyP"));


        return new Generator.Keys(
                new BigInteger(publicKey),
                new BigInteger(privateKeyQ),
                new BigInteger(privateKeyP)
        );
    }

    private static void saveKeys(Generator.Keys keys) {

        Properties properties = new Properties();
        properties.setProperty("publicKey", encodeKey(keys.getPublicKey()));
        properties.setProperty("privateKeyQ", encodeKey(keys.getPrivateKeyQ()));
        properties.setProperty("privateKeyP", encodeKey(keys.getPrivateKeyP()));

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

    private static Keys genKeys() {
        BigInteger p = genKey();
        BigInteger q = genKey();
        BigInteger n = p.multiply(q);

        return new Keys(n, q, p);
    }

    private static BigInteger genKey() {
        BigInteger key;

        do {
            key = BigInteger.probablePrime(LENGTH, random);
        } while (!key.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3)));

        return key;
    }
}

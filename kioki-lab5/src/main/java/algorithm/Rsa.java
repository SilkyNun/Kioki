package algorithm;

import java.math.BigInteger;

public class Rsa {

    public static String sign(String m, Generator.Keys.PrivateKeys keys) {
        long hash = Murmur3.hash(m);
        System.out.println("Initial hash: " + hash);

        return BigInteger.valueOf(hash)
                .modPow(keys.getPrivateKeyD(), keys.getPrivateKeyR())
                .toString();
    }

    public static boolean verify(String m, String sign, Generator.Keys.PublicKeys keys) {
        long hash = Murmur3.hash(m);
        System.out.println("Hash: " + hash);

        BigInteger signedHash = new BigInteger(sign).modPow(keys.getPublicKeyE(), keys.getPublicKeyR());
        System.out.println("Signed hash: " + signedHash);

        return hash == signedHash.longValue();
    }
}

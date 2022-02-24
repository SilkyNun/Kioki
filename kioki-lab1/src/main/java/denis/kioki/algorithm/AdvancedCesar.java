package denis.kioki.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AdvancedCesar {
    private static final int N = 256;

    public static String encrypt(String m, int ke) {
        if (!isComprise(ke, N)) {
            throw new IllegalArgumentException("Key and N is not comprised");
        }

        return m.chars()
                .map(ch -> ch * ke % N)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String decrypt(String c, int kd) {
        return encrypt(c, kd);
    }

    public static Map.Entry<Integer, Integer> getKeys() {
        for (int i = N + 1; ; i += N) {
            List<Map.Entry<Integer, Integer>> primeNumbers = findComprisedNumbers(i);
            if (!primeNumbers.isEmpty())  {
                return primeNumbers.get(0);
            }
        }
    }

    private static boolean isComprise(int a, int b) {
        int t;
        while(b != 0){
            t = a;
            a = b;
            b = t%b;
        }
        return a == 1;
    }

    private static List<Map.Entry<Integer, Integer>> findComprisedNumbers(int limit) {
        boolean[] primes = new boolean[limit + 1];
        Arrays.fill(primes, true);

        for (int i = 2; i * i < limit; i++) {
            if (primes[i]) {
                for (int j = i * i; j <= limit; j += i)
                    primes[j] = false;
            }
        }

        List<Integer> primeNumbers = new ArrayList<>();
        for (int i = 2; i <= limit; i++) {
            if (primes[i])
                primeNumbers.add(i);
        }

        List<Map.Entry<Integer, Integer>> result = new ArrayList<>();

        for (int i = 0; i < primeNumbers.size() - 1; i++) {
            for (int j = 1; j < primeNumbers.size(); j++) {
                if (primeNumbers.get(i) * primeNumbers.get(j) == limit) {
                    result.add(Map.entry(primeNumbers.get(i), primeNumbers.get(j)));
                }
            }
        }

        return result;
    }
}

package algorithm;

public class Murmur3 {

    public static long hash(String text) {
        final int seed = 21;
        final int c1 = 0xcc9e2d51;
        final int c2 = 0x1b873593;
        int curLength = text.length();
        int length = curLength;
        int h1 = seed;
        int k1 = 0;
        int currentIndex = 0;
        while (curLength >= 4)
        {
            k1 = (text.charAt(currentIndex++)
                    | text.charAt(currentIndex++) << 8
                    | text.charAt(currentIndex++) << 16
                    | text.charAt(currentIndex++) << 24);
            k1 *= c1;
            k1 = rotl32(k1, (byte) 15);
            k1 *= c2;
            h1 ^= k1;
            h1 = rotl32(h1, (byte) 13);
            h1 = h1 * 5 + 0xe6546b64;
            curLength -= 4;
        }
        if ((curLength & 3) > 0)
        {
            switch (curLength)
            {
                case 3:
                    k1 = text.charAt(currentIndex++)
                            | text.charAt(currentIndex++) << 8
                            | text.charAt(currentIndex++) << 16;
                    break;
                case 2:
                    k1 = (text.charAt(currentIndex++)
                            | text.charAt(currentIndex++) << 8);
                    break;
                case 1:
                    k1 = text.charAt(currentIndex++);
                    break;
            };
            k1 *= c1;
            k1 = rotl32(k1, (byte) 15);
            k1 *= c2;
            h1 ^= k1;
        }
        h1 ^= length;
        h1 ^= h1 >> 16;
        h1 *= 0x85ebca6b;
        h1 ^= h1 >> 13;
        h1 *= 0xc2b2ae35;
        h1 ^= h1 >> 16;
        return h1;
    }

    private static int rotl32(int x, byte r)
    {
        return (x << r) | (x >> (32 - r));
    }
}

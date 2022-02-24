package denis.kioki.algorithm;

import java.util.*;

public class RotatingGrid {
    private static final char[][] grid = new char[][] {
            new char[] {'1', '2', '3', '1'},
            new char[] {'3', '4', '4', '2'},
            new char[] {'2', '4', '4', '3'},
            new char[] {'1', '3', '2', '1'}
    };

    private static final char[][] matrix = new char[4][4];

    public static String encrypt(String m, List<Map.Entry<Integer, Integer>> indexes) {
        if (!validIndexes(indexes)) {
            throw new IllegalArgumentException("Indexes are incorrect");
        }

        m = alignWord(m, 16);
        List<String> strings = splitWordIntoBlocks(m, 16);
        List<Map.Entry<Integer, Integer>> sortedIndexes = sortIndexes(indexes);
        StringBuilder builder = new StringBuilder();

        strings.forEach(str -> {
            List<String> blocks = splitWordIntoBlocks(str, 4);
            blocks.forEach(b -> {
                fillMatrix(b, sortedIndexes);
                rotateMatrix();
            });

            builder.append(getStringFromMatrix());
        });

        return builder.toString();
    }

    public static String decrypt(String c, List<Map.Entry<Integer, Integer>> indexes) {
        if (!validIndexes(indexes)) {
            throw new IllegalArgumentException("Indexes are incorrect");
        }

        List<Map.Entry<Integer, Integer>> sortedIndexes = sortIndexes(indexes);
        List<String> strings = splitWordIntoBlocks(c, 16);
        StringBuilder builder = new StringBuilder();

        strings.forEach(str -> {
            populateMatrix(str);
            builder.append(readFromMatrix(sortedIndexes));
        });

        return builder.toString().replaceAll("#", "");
    }

    private static boolean validIndexes(List<Map.Entry<Integer, Integer>> indexes) {
       return indexes.stream()
                .map(entry -> grid[entry.getKey()][entry.getValue()])
                .distinct()
                .toList()
                .size() == indexes.size();
    }

    private static String alignWord(String m, int splitSize) {
        StringBuilder builder = new StringBuilder(m);

        while (builder.length() % splitSize != 0) {
            builder.append('#');
        }

        return builder.toString();
    }

    private static List<String> splitWordIntoBlocks(String m, int splitSize) {
        List<String> result = new ArrayList<>();
        String alignedWord = alignWord(m, splitSize);

        for (int i = 0; i < alignedWord.length(); i += splitSize) {
            result.add(alignedWord.substring(i, i + splitSize));
        }

        return result;
    }

    private static void rotateMatrix() {
        int N = 4;

        for (int i = 0; i < N / 2; i++) {
            for (int j = i; j < N - i - 1; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[N - 1 - j][i];
                matrix[N - 1 - j][i] = matrix[N - 1 - i][N - 1 - j];
                matrix[N - 1 - i][N - 1 - j] = matrix[j][N - 1 - i];
                matrix[j][N - 1 - i] = (char) temp;
            }
        }
    }

    private static void fillMatrix(String str, List<Map.Entry<Integer, Integer>> indexes) {
        for (int i = 0; i < str.length(); i++) {
            Map.Entry<Integer, Integer> currentIndex = indexes.get(i);
            matrix[currentIndex.getKey()][currentIndex.getValue()] = str.charAt(i);
        }
    }

    private static String getStringFromMatrix() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                builder.append(matrix[i][j]);
            }
        }

        return builder.toString();
    }

    private static String readFromMatrix(List<Map.Entry<Integer, Integer>> indexes) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            indexes.forEach(entry -> builder.append(matrix[entry.getKey()][entry.getValue()]));
            rotateMatrix();
        }

        return builder.toString();
    }

    private static List<Map.Entry<Integer, Integer>> sortIndexes(List<Map.Entry<Integer, Integer>> indexes) {
        return indexes.stream()
                .sorted(Comparator.comparing(entry -> grid[entry.getKey()][entry.getValue()]))
                .toList();
    }

    private static void populateMatrix(String str) {
        for (int i = 0, k = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++, k++) {
                matrix[i][j] = str.charAt(k);
            }
        }
    }
}

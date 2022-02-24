package denis.kioki.algorithm;

public class RailFence {

    public static String encrypt(String m, int k) {
        char[][] matrix = new char[k][m.length()];
        StringBuilder result = new StringBuilder();

        boolean reverse = false;
        for (int j = 0, i = 0; j < m.length(); j++) {
            matrix[i][j] = m.charAt(j);

            if (i == k - 1) {
                reverse = true;
            } else if (i == 0) {
                reverse = false;
            }

            i = reverse ? i - 1 : i + 1;
        }

//        printMatrix(matrix);

        for (int i = 0; i < k; i++) {
            for (int j = 0; j < m.length(); j++) {
                if (matrix[i][j] != 0) {
                    result.append(matrix[i][j]);
                }
            }
        }

        return result.toString();
    }

    public static String decrypt(String c, int k) {
        char[][] matrix = new char[k][c.length()];

        char[][] emptyMatrix = buildEmptyMatrix(k, c.length());
//        printMatrix(emptyMatrix);

        char[][] filledMatrix = fillMatrixWithChars(emptyMatrix, c);
//        printMatrix(filledMatrix);

        return convertMatrixToString(filledMatrix);
    }

    private static void printMatrix(char[][] m) {
        System.out.println();
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                System.out.print(m[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    private static char[][] buildEmptyMatrix(int r, int c) {
        char[][] matrix = new char[r][c];

        boolean reverse = false;
        for (int j = 0, i = 0; j < c; j++) {
            matrix[i][j] = 'X';

            if (i == r - 1) {
                reverse = true;
            } else if (i == 0) {
                reverse = false;
            }

            i = reverse ? i - 1 : i + 1;
        }

        return matrix;
    }

    private static char[][] fillMatrixWithChars(char[][] matrix, String c) {
        int counter = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] != 0) {
                    matrix[i][j] = c.charAt(counter++);
                }
            }
        }

        return matrix;
    }

    private static String convertMatrixToString(char[][] matrix) {
        StringBuilder result = new StringBuilder();

        for (int j = 0; j < matrix[0].length; j++) {
            for (int i = 0; i < matrix.length; i++) {
                if (matrix[i][j] != 0) {
                    result.append(matrix[i][j]);
                }
            }
        }

        return result.toString();
    }
}

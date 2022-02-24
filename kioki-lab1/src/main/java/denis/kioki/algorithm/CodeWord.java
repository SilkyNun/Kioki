package denis.kioki.algorithm;

import java.util.*;
import java.util.stream.Collectors;

public class CodeWord {

    public static String encrypt(String m, String key) {
        List<Integer> integers = getEncryptedIndexes(key);
        List<String> strings = splitWordIntoBlocks(m, key.length());

        String encryptedWord = strings.stream()
                .map(str -> encryptString(str, integers))
                .collect(Collectors.joining());


        return encryptedWord;
    }

    public static String decrypt(String c, String key) {
        List<Integer> integers = getEncryptedIndexes(key);
        List<String> strings = splitWordIntoBlocks(c, key.length());
        List<Integer> decryptIndexes = getDecryptIndexes(integers);

        String decryptedString = strings.stream()
                .map(str -> decryptString(str, decryptIndexes))
                .collect(Collectors.joining())
                .replaceAll("#", "");

        return decryptedString;
    }

    private static String encryptString(String str, List<Integer> indexes) {
        StringBuilder builder = new StringBuilder();

        indexes.forEach(i ->  builder.append(str.charAt(i)));

        return builder.toString();
    }

    private static String decryptString(String str, List<Integer> decryptIndexes) {
        StringBuilder builder = new StringBuilder();

        decryptIndexes.forEach(i -> builder.append(str.charAt(i)));

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

    private static List<Integer> getEncryptedIndexes(String keyword) {
        List<Map.Entry<Integer, Character>> mappedChars = new ArrayList<>();

        for (int i = 0; i < keyword.length(); i++) {
            mappedChars.add(Map.entry(i, keyword.charAt(i)));
        }

        return mappedChars.stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .toList();
    }

    private static String alignWord(String m, int splitSize) {
        StringBuilder builder = new StringBuilder(m);

        while (builder.length() % splitSize != 0) {
            builder.append('#');
        }

        return builder.toString();
    }

    private static List<Integer> getDecryptIndexes(List<Integer> encryptIndexes) {
        List<Map.Entry<Integer, Integer>> decryptIndexes = new ArrayList<>();

        encryptIndexes.forEach(i -> decryptIndexes.add(Map.entry(i, encryptIndexes.get(i))));

        return  decryptIndexes.stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .toList();
    }
}


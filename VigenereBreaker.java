import java.io.IOException;
import java.net.Inet4Address;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Stream;

class VigenereBreaker {
    private final int MAX_KEYS = 100;
    private String languageUsed = "";
    private int[] keysUsed = new int[0];

    String getLanguageUsed() {
        return languageUsed;
    }

    int[] getKeysUsed() {
        return keysUsed;
    }

    String breakVigenere(String encrypted, int klength, String language) {
        char mostCommonChar = getMostCommonChar(loadDictionary(language));
        int[] decryptionKeys = tryKeyLength(encrypted, klength, mostCommonChar);

        this.keysUsed = decryptionKeys;
        this.languageUsed = language;

        VigenereCipher vc = new VigenereCipher(decryptionKeys);
        return vc.decrypt(encrypted);
    }

    String breakVigenere(String encrypted, String language) {
        HashSet<String> dictionary = loadDictionary(language);
        Tuple<String, Tuple<Integer, int[]>> result = breakForLanguage(encrypted, dictionary);

        this.keysUsed = result.SECOND.SECOND;
        this.languageUsed = language;

        return result.FIRST;
    }

    String breakVigenere(String encrypted) {
        HashMap<String, HashSet<String>> allDictionaries = loadAllDictionaries();

        String decryptionRestul = "";
        int maxCorrectWords = 0;

        for (String dictionary: allDictionaries.keySet()) {
            Tuple<String, Tuple<Integer, int[]>> langResult = breakForLanguage(encrypted, allDictionaries.get(dictionary));
            if (maxCorrectWords < langResult.SECOND.FIRST) {
                maxCorrectWords = langResult.SECOND.FIRST;
                decryptionRestul = langResult.FIRST;

                this.keysUsed = langResult.SECOND.SECOND;
                this.languageUsed = dictionary;
            }
        }

        return decryptionRestul;
    }

    private Tuple<String, Tuple<Integer, int[]>> breakForLanguage(String encrypted, HashSet<String> dictionary) {
        String decryptionResult = "";
        char mostCommonChar = getMostCommonChar(dictionary);

        int[] keys = new int[0];
        int maxCorrectWords = 0;

        for (int i = 1; i < MAX_KEYS; i++) {
            int[] decryptionKeys = tryKeyLength(encrypted, i, mostCommonChar);
            VigenereCipher vc = new VigenereCipher(decryptionKeys);
            String decryptedMessage = vc.decrypt(encrypted);

            int correctWords = nCorrectMatches(decryptedMessage, dictionary);
            if (maxCorrectWords < correctWords) {
                maxCorrectWords = correctWords;
                keys = decryptionKeys;
                decryptionResult = decryptedMessage;
            }
        }
        return new Tuple<>(decryptionResult, new Tuple<>(maxCorrectWords, keys));
    }


    private HashSet<String> loadDictionary(String dictName) {
        HashSet<String> dict = new HashSet<>();
        try (Stream<String> stream = Files.lines( Paths.get("Dictionaries/" + dictName), StandardCharsets.ISO_8859_1)) {
            stream.forEach(s -> dict.add(s.toLowerCase()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dict;
    }

    private HashMap<String, HashSet<String>> loadAllDictionaries() {
        String[] dictionaries = {"Danish",  "Dutch",  "English",  "French",  "German",  "Italian",  "Portuguese",  "Spanish"};
        HashMap<String, HashSet<String>> allDicts = new HashMap<>();

        for (String dict: dictionaries)
            allDicts.put(dict, loadDictionary(dict));

        return allDicts;
    }

    private String sliceString(String message, int whichSlice, int totalSlices) {
        StringBuilder slice = new StringBuilder();
        for (int i = whichSlice; i < message.length(); i+= totalSlices)
            slice.append((message.charAt(i)));
        return slice.toString();
    }

    private int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        for (int i = 0; i < klength; i++) {
            CaesarCracker cc = new CaesarCracker(mostCommon);
            String currEncryptedSlice = sliceString(encrypted, i, klength);
            int sliceKey = cc.getKey(currEncryptedSlice);
            key[i] = sliceKey;
        }
        return key;
    }

    private int nCorrectMatches(String decryptedMessage, HashSet<String> dictionary) {
        int matches = 0;
        for (String word: decryptedMessage.split("\\W"))
            if (dictionary.contains(word.toLowerCase()))
                matches++;
        return matches;
    }

    private char getMostCommonChar(HashSet<String> words) {
        HashMap<Character, Integer> charFreqs = new HashMap<>();
        for(String word: words) {
            for (int i = 0; i < word.length(); i++) {
                if (charFreqs.containsKey(word.charAt(i))) {
                    charFreqs.put(word.charAt(i), charFreqs.get(word.charAt(i)) + 1);
                } else {
                    charFreqs.put(word.charAt(i), 1);
                }
            }
        }

        char mostCommonChar = 'a';
        int max = 0;
        for (char c : charFreqs.keySet()) {
            if (max < charFreqs.get(c)) {
                max = charFreqs.get(c);
                mostCommonChar = c;
            }
        }
        return mostCommonChar;
    }
}

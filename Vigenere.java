import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public class Vigenere {
    public static void main(String[] args) throws IOException {
        ArgParser argparser = new ArgParser();
        argparser.parse(args);
        CaesarCipher[] cc = new CaesarCipher[4];
        Vigenere vigenere = new Vigenere();
        String messageToProcess = vigenere.readFileAsString(argparser.getInput());

        if (argparser.getType().equals("enc")) {
            VigenereCipher vc = new VigenereCipher(argparser.getKeys());
            String encryptedMessage = vc.encrypt(messageToProcess);

            if (argparser.getOutput().equals("")) {
                System.out.println(encryptedMessage);
            } else {
                Files.write(Paths.get(argparser.getOutput()), encryptedMessage.getBytes());
            }
        } else {
            VigenereBreaker vb = new VigenereBreaker();
            String decryptedMessage = "";
            if (argparser.isKnownLanguage() && argparser.isKnownKeyLength())
                decryptedMessage = vb.breakVigenere(messageToProcess, argparser.getKlength(), argparser.getLanguage());
            else if (argparser.isKnownLanguage())
                decryptedMessage = vb.breakVigenere(messageToProcess, argparser.getLanguage());
            else
                decryptedMessage = vb.breakVigenere(messageToProcess);

            if (argparser.getOutput().equals("")) {
                System.out.println("Language: " + vb.getLanguageUsed() + "\nKeys: " + Arrays.toString(vb.getKeysUsed()));
                System.out.println(decryptedMessage);
            } else {
                String messageToWrite = "Language: " + vb.getLanguageUsed() + "\nKeys: " + Arrays.toString(vb.getKeysUsed()) + "\n\n" + decryptedMessage;
                Files.write(Paths.get(argparser.getOutput()), messageToWrite.getBytes());
            }
        }
    }

    private String readFileAsString(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }


}

public class ArgParser {

    private String type = "";
    private String input = "";
    private String output = "";

    private String language = "English";
    private boolean knownLanguage = false;

    private int[] keys;
    private boolean knownKeys = false;

    private int klength;
    private boolean knownKeyLength;

    private String helpMessage = "Vigenere -input [FILENAME] -type [enc|dec] [-keys k1,k2,...] [-lang [En|Fr|Da|Du|Ge|It|Po|Sp]]\n" +
            "\t [-klength [NUMBER]] [-output [FILENAME]] [-help]\n\n" +
            "\t -input [FILENAME]\t Path to the input file.\n" +
            "\t -output [FILENAME]\t Path to the output file. The output will be in the console if no output was given.\n" +
            "\t -type [enc|dec]\t Type of operation, there is `enc` for encryption and `dec` for decryption.\n" +
            "\t -keys [k1,k2,...]\t Keys used for encryption. It will be ignored in case of decryption.\n" +
            "\t -lang [LANGUAGE]\t The language used for encryption. It can be one of the following\n" +
            "\t\t\t\t En: English, Fr: French, Da: Danish, Du: Dutch, Ge: German, It: Italian, Po: Portuguese, Sp: Spanish\n" +
            "\t -klength [NUMBER]\t Length of keys used for encryption. Eg: let [20,4,2] be the keys used in encryption, then klength is 3.\n" +
            "\t\t\t\t It will be ignored in case of encryption.\n" +
            "\t -help [FILENAME]\t Print this help message.";


    public String getType() {
        return type;
    }

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }

    public String getLanguage() {
        return language;
    }

    boolean isKnownLanguage() {
        return knownLanguage;
    }

    public int[] getKeys() {
        return keys;
    }

    public boolean isKnownKeys() {
        return knownKeys;
    }

    int getKlength() {
        return klength;
    }

    boolean isKnownKeyLength() {
        return knownKeyLength;
    }

    public void parse(String[] args) {
        int i = 0;
        String arg;
        String flag;
        while (i < args.length && args[i].startsWith("-")) {
            arg = args[i++];
            if (arg.equals("-help")) {
                System.out.println(this.helpMessage);
                System.exit(0);
            }

            if (i == args.length) {
                String[] validArgs = {"-help", "-input", "-keys", "-klength", "-lang", "-output", "-type"};
                boolean valid = false;
                for(String a: validArgs) {
                    if (arg.equals(a)) {
                        valid = true;
                        break;
                    }
                }
                if (valid)
                    System.err.println("Error: Missing argument value for " + arg);
                else
                    System.err.println("Error: IllegalOption: " + arg);
                System.exit(1);
            }

            flag = args[i++];
            if (flag.startsWith("-")) {
                System.err.println("Error: Argument " + arg + " requires a value");
                System.exit(1);
            }
            switch (arg) {
                case "-type":
                    this.type = flag;
                    if (!this.type.equals("enc") && !this.type.equals("dec")) {
                        System.err.println("Error: -type requires either enc for encryption or dec for decryption");
                        System.exit(1);
                    }
                    break;

                case "-output":
                    this.output = flag;
                    break;

                case "-input":
                    this.input = flag;
                    break;

                case "-lang":
                    parseLanguage(flag);
                    this.knownLanguage = true;
                    break;

                case "-keys":
                    parseKeys(flag);
                    this.knownKeys = true;
                    break;

                case "-klength":
                    try {
                        this.klength = Integer.parseInt(flag);
                    } catch (NumberFormatException e) {
                        System.err.println("Error: InvalidNumber for klength: " + flag);
                        System.exit(1);
                    }
                    this.knownKeyLength = true;
                    break;

                default:
                    System.err.println("Error: IllegalOption: " + arg);
                    System.exit(1);
            }
        }

        if (this.input.equals("") || this.type.equals("")) {
            System.err.println("Error: Missing positional arguments: -input FILENAME or -type [enc|dec]");
            System.exit(1);
        } else if (this.type.equals("enc") && this.keys == null) {
            System.err.println("Error: Missing keys for encryption");
            System.exit(1);
        }
    }

    private void parseKeys(String argKeys) {
        String[] splitKeys = argKeys.split(",");
        int[] keys = new int[splitKeys.length];
        for(int i = 0; i < splitKeys.length; i++) {
            try {
                keys[i] = Integer.parseInt(splitKeys[i]);
                if (keys[i] > 26 || keys[i] < 0) {
                    String errMsg = keys[i] > 26 ? "bigger than 26": "smaller than 0";
                    System.err.println("Error: InvalidNumber: " + keys[i] + " is " + errMsg);
                    System.exit(1);
                }
            } catch (NumberFormatException e) {
                System.err.println("Error: InvalidNumber: " + e.getLocalizedMessage());
                System.exit(1);
            }
        }
        this.keys = keys;
    }

    private void parseLanguage(String argLanguage) {
        switch (argLanguage.toLowerCase()) {
            case "en":
                this.language = "English";
                break;
            case "fr":
                this.language = "French";
                break;
            case "da":
                this.language = "Danish";
                break;
            case "du":
                this.language = "Dutch";
                break;
            case "ge":
                this.language = "German";
                break;
            case "it":
                this.language = "Italian";
                break;
            case "po":
                this.language = "Portuguese";
                break;
            case "sp":
                this.language = "Spanish";
                break;
            default:
                System.err.println("Error: InvalidLanguage for " + argLanguage);
                System.exit(1);
        }
    }

}

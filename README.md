## Vigenère Cipher 🔒 - Encryption & Decryption
![Imgur](https://i.imgur.com/Ski7uDe.jpg)

The idea behind the Vigenère cipher, like all other polyalphabetic ciphers, is to disguise the plaintext **letter frequency** to interfere with a straightforward application of **frequency analysis**. For instance, if `P` is the most frequent letter in a ciphertext whose plaintext is in English, one might suspect that `P` corresponds to `E` since `E` is the most frequently used letter in English. However, by using the Vigenère cipher, `E` can be enciphered as different ciphertext letters at different points in the message, which defeats simple frequency analysis. 

### Implementation

Both encryption and decryption are implemented, here are how both works:

**Encrypting**

A key is given with the message, this key is then duplicated as many times until it's length is the same as the message. The key is then converted to its equivalent ASCII number. After that each letter in the message is shifted by the relevant ASCII number.

![Imgur](https://i.imgur.com/1w2Iepa.png)

**Decrypting**

When decrypting, each letter is considered an instance of `CaesarCracker` so we have an array of `CaesarCrackers`. Each one of these `CaesarCracker` is initialized with a different key. So for a message that is encrypted with 4 keys, we will have an array of 4 `CaesarCraker`s where each letter in the message associated with the same key is added to a `String` that will be decrypted with that key later.

![Imgur](https://i.imgur.com/V8MCRZJ.png)
After decrypting we reconstruct the message the same way the `String`s was generated.

### Requirements
* OpenJDK 8+

### Compiling & Running
```bash
$ javac Vigenere.java
$ java Vigenere
Error: Missing positional arguments: -input FILENAME or -type [enc|dec]
```

### How to use
Refer to `java Vigenere -help` for more information about the available options. 
```bash
$ java Vigenere -help
Vigenere -input [FILENAME] -type [enc|dec] [-keys k1,k2,...] [-lang [En|Fr|Da|Du|Ge|It|Po|Sp]]
         [-klength [NUMBER]] [-output [FILENAME]] [-help]

         -input [FILENAME]       Path to the input file.
         -output [FILENAME]      Path to the output file. The output will be in the console if no output was given.
         -type [enc|dec]         Type of operation, there is `enc` for encryption and `dec` for decryption.
         -keys [k1,k2,...]       Keys used for encryption. It will be ignored in case of decryption.
         -lang [LANGUAGE]        The language used for encryption. It can be one of the following
                                 En: English, Fr: French, Da: Danish, Du: Dutch, Ge: German, It: Italian, Po: Portuguese, Sp: Spanish
         -klength [NUMBER]       Length of keys used for encryption. Eg: let [20,4,2] be the keys used in encryption, then klength is 3.
                                 It will be ignored in case of encryption.
         -help [FILENAME]        Print this help message.
```

#### Encrypting
The encryption requires the `-type` to be `enc`, an input given by `-input`, and the keys to encrypt with `-keys`

Encryption Examples
```bash
$ # Output resut to the console
$ java Vigenere -input Test/athens_keyflute_decrypted.txt -type enc -keys 5,11,20,19,4

XNYGI TC. Febxrx. JYNYWX'X bhyxp.

Jynxv BOBRHP, JQFNX, DHHYY, tri MMEWGYEMSR
JYNYWX...

$ # Output to a file
$ java Vigenere -input Test/athens_keyflute_decrypted.txt -type enc -keys 5,11,20,19,4 -output Test/enc_result.txt
$ ls Test
athens_keyflute.txt athens_keyflute_decrypted.txt enc_result.txt
```
#### Decryption

Decryption can be done in three ways:
1. Known languages and keys length.
2. Known language and unknown keys length.
3. Unknown language and keys length.

*__Note:__ If the keys used to encrypt a text file is `[12, 20, 3, 13]` then keys length is `4`, therefore keys length is the length of the array used to encrypt the file.*

When decrypting, the language and keys used to decrypt the file will be the first two lines.

Decryption Examples
```bash
# The simplest but the least accurate way is the 3rd way
$ java Vigenere -input Test/athens_keyflute.txt -type dec

Language: English
Keys: [5, 11, 20, 19, 4]

SCENE II. Athens. QUINCE'S house.

Enter QUINCE, FLUTE, SNOUT, and STARVELING...
```
```bash
# Decrypting if you know the language it was encrypted by
$ java Vigenere -input Test/athens_keyflute.txt -type dec -lang En

Language: English
Keys: [5, 11, 20, 19, 4]

SCENE II. Athens. QUINCE'S house.

Enter QUINCE, FLUTE, SNOUT, and STARVELING
```
```bash
# Decrypting if you know the language it was encrypted by
$ java Vigenere -input Test/athens_keyflute.txt -type dec -lang En -klength 5

Language: English
Keys: [5, 11, 20, 19, 4]

SCENE II. Athens. QUINCE'S house.

Enter QUINCE, FLUTE, SNOUT, and STARVELING
```
```bash
# Output to a file
$ java Vigenere -input Test/athens_keyflute.txt -type dec -output Test/dec_result.txt
$ ls Test
athens_keyflute.txt athens_keyflute_decrypted.txt dec_result.txt
```

### Contributed
* Duke University 
* Coursera

### License
M.I.T

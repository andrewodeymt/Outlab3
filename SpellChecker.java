import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.System.*;

/**
 * Authors: Andrew O'Donnell and Liam Fallon
 */
public class SpellChecker {
    private static void spellCheck(Dictionary dict, String path) {
        Scanner sc = null;
        try {
            sc = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            err.println("Input file not found.");
            exit(1);
        }

        String output = "";
        while (sc.hasNextLine()) {
            String[] line = sc.nextLine().split(" ");
            for (int i = 0; i < line.length; i++) {
                String word = line[i];

                //check if word is in dict else find replacement value
                if (!wordInDict(dict, word)) {
                    word = dict.options(word);
                }
                output += word + " ";
            }
            output += "\n";
        }

        //print output to a file
        try {
            fileOutput(output);
        } catch (IOException e) {
            err.println("Invalid output file path.");
        } catch (InputMismatchException e) {
            out.print(e.getMessage()); //try to find out specific reason.
        }
    }

    /**
     * @param dict hashtable
     * @param word word to be checked
     * @return boolean variable
     */
    public static boolean wordInDict(Dictionary dict, String word) {
        if (dict.containsKey(word.hashCode())) {
            if (dict.containsValue(word))
                return true;
        }
        return false;
    }

    /**
     * @param path path of file to be stored
     */
    private static ArrayList<String> getWords(String path) {
        Scanner sc = null;
        try {
            sc = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            err.println("File containing words not found.");
            exit(1);
        }

        ArrayList<String> words = new ArrayList();
        //store dictionary in words ^
        while (sc.hasNextLine()) {
            words.add(sc.nextLine());
        }
        return words;
    }

    /**
     * @param output String to be outputted
     * @throws IOException
     */
    private static void fileOutput(String output) throws IOException {
        String path = "mydoc-checked.txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));

        writer.write(output);
        writer.close();
    }

    public static void main(String[] args) {
        try {
            //String path = "words.txt"; for all existing words
            String path = args[0];
            Dictionary dict = new Dictionary(getWords(path));

            //path = "mydoc.txt"; for all words that need to be corrected
            path = args[1];
            spellCheck(dict, path);
        } catch (ArrayIndexOutOfBoundsException e) {
            err.println("Enter file paths as command line arguments.");
        }
    }
}

import edu.princeton.cs.algs4.Queue;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.System.out;

public class Dictionary {
    private Hashtable<Integer, String> dict;

    private final ArrayList<String> options = new ArrayList();
    private final ArrayList<String> visited = new ArrayList();

    private Queue<String> vals = new Queue();
    private Queue<Integer> depths = new Queue();

    private final int OPTIONS = 7;
    private final int MAXDEPTH = 2;

    private final char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
            'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z'};

    Dictionary(ArrayList<String> words) {
        dict = new Hashtable();

        for (String str : words) {
            dict.put(str.hashCode(), str);
        }
    }

    /**
     * @param initial misspelled word
     * @return chosen word
     */
    public String options(String initial) {
        options.clear();
        visited.clear();
        vals = new Queue();
        depths = new Queue();

        vals.enqueue(initial);
        depths.enqueue(0);

        computeOptions();

        String[] output = new String[OPTIONS];

        int count = 0;

        //output contains potentially misspelled words
        for (int i = 0; count < OPTIONS && i < options.size(); i++) {
            //output shall only contain != elements
            if (options.get(i) != null) {
                output[count] = options.get(i);
                count++;
            }
        }

        String replacementWord = displayOptions(initial, output, count);
        return replacementWord;
    }

    private void computeOptions() {
        //generate possible replacements for misspelled word
        while (!vals.isEmpty() && options.size() < OPTIONS) {
            String value = vals.dequeue();
            int depth = depths.dequeue();

            //value is in the dictionary, add to options
            if (dict.containsKey(value.hashCode())) {
                if (dict.containsValue(value)) {
                    options.add(value);
                }
            }

            //process word IFF MAXDEPTH
            if (depth < MAXDEPTH)
                processWord(value, depth);
        }
    }

    /**
     * @param value
     * @param depth
     */
    private void processWord(String value, int depth) {
        for (int pos = 0; pos < value.length() + 1; pos++) {
            addLetter(value, pos, depth + 1);
            removeLetter(value, pos, depth + 1);
            replaceLetter(value, pos, depth + 1);
            swapConsecutiveLetters(value, pos, depth + 1);
            computeOptions();
        }
    }

    /**
     * @param newValue
     * @param depth
     */
    private void processNewVal(String newValue, int depth) {
        // either mark, enqueue or dequeue as necessary
        visited.add(newValue);
        vals.enqueue(newValue);
        depths.enqueue(depth);
    }

    /**
     * Takes in word, enqueues all possible replacements the modification is
     * adding a letter at a fixed position
     *
     * @param value
     */
    private void addLetter(String value, int pos, int depth) {
        if (pos < value.length() + 1) {
            for (int j = 0; j < alphabet.length; j++) {
                String newValue;
                if (pos < value.length())
                    newValue = value.substring(0, pos) + alphabet[j] + value.substring(pos);
                else
                    newValue = value + alphabet[j];
                if (!visited.contains(newValue))
                    processNewVal(newValue, depth);
            }
        }
    }

    /**
     * Takes in word, enqueues all possible replacements the modification is
     * removing the positions of a letter at a fixed position
     *
     * @param value
     */
    private void removeLetter(String value, int pos, int depth) {
        if (pos < value.length()) {
            String newValue = value.substring(0, pos) + value.substring(pos + 1);
            if (!visited.contains(newValue))
                processNewVal(newValue, depth);
        }
    }

    /**
     * Takes in word, enqueues all possible replacements the modification is
     * swapping the positions of one letter at a fixed position
     *
     * @param value
     * @param depth
     */
    private void replaceLetter(String value, int pos, int depth) {
        if (pos < value.length()) {
            for (int j = 0; j < alphabet.length; j++) {
                String newValue = value.substring(0, pos) + alphabet[j] + value.substring(pos + 1);
                if (!visited.contains(newValue))
                    processNewVal(newValue, depth);
            }
        }
    }

    /**
     * Takes in word, enqueues all possible replacements the modification is
     * swapping the positions of two consecutive letters at a fixed position
     *
     * @param value
     * @param depth
     */
    private void swapConsecutiveLetters(String value, int pos, int depth) {
        if (pos < value.length() - 1) {
            char cur = value.charAt(pos);
            char next = value.charAt(pos + 1);
            String newValue = value.substring(0, pos) + next + cur + value.substring(pos + 2);

            if (!visited.contains(newValue))
                processNewVal(newValue, depth);
        }
    }

    /**
     * @param word   word to be replaced
     * @param output options for misspelled word. Maximum value of 10
     * @param count  count of values in output
     * @return
     */
    private String displayOptions(String word, String[] output, int count) {
        out.println("Word to Replace: " + word + "\nDid you mean:");
        for (int i = 0; i < output.length; i++) {
            if (output[i] != null) {
                int index = i + 1;
                out.println(index + ". " + output[i]);
            }
        }
        out.print("0. Something else\nEnter replacement value integer: ");

        Scanner sc = new Scanner(System.in);
        int choice = 99;
        try {
            choice = sc.nextInt();
        } catch (InputMismatchException e) {
            out.println("\n** Invalid input parameter, try using int's next time **\n");
        }

        switch (choice) {
            case 0:
                out.print("Enter word: ");
                word = sc.next();
                break;
            default:
                int index = choice - 1;
                if (index < count)
                    word = output[index];
                else {
                    word = displayOptions(word, output, count);
                }
                break;
        }
        out.println();
        return word;
    }

    public boolean containsKey(Integer key) {
        return dict.containsKey(key);
    }

    public boolean containsValue(String val) {
        return dict.containsValue(val);
    }

    public void put(String val) {
        dict.put(val.hashCode(), val);
    }
}

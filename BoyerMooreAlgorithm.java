import java.util.Arrays;

public class BoyerMooreAlgorithm {

    public static void main(String[] args) {
        String text = "GEMINIEATICECREAM";
        String pattern = "ICE"; // Pattern to be searched
        final int ALPHABET_SIZE = 52;

        long startTime = System.nanoTime();

        int matchIndex = searchBoyerMoore(text, pattern, ALPHABET_SIZE); // Run the Boyer-Moore search

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime; // Calculate elapsed time in nanoseconds

        int badCharSpace = ALPHABET_SIZE * Integer.BYTES;
        int goodSuffixSpace = (pattern.length() + 1) * Integer.BYTES;
        int totalSpace = badCharSpace + goodSuffixSpace;

        // Print the required output values for assignment
        System.out.println("Pattern found at index        : " + matchIndex); // Output index where pattern is found
        System.out.println("Text size                     : " + text.length()); // Length of the text
        System.out.println("Pattern size                  : " + pattern.length()); // Length of the pattern
        System.out.println("Alphabet size                 : " + ALPHABET_SIZE); // Number of characters supported
        System.out.println("Bad character table space     : " + badCharSpace + " bytes"); // Space used by bad char table
        System.out.println("Good suffix table space       : " + goodSuffixSpace + " bytes"); // Space used by good suffix table
        System.out.println("Total space used              : " + totalSpace + " bytes"); // Total space used
        System.out.println("Elapsed time (ms)             : " + elapsedTime / 1_000); // Time it took to perform the search
    }

    // Main search function for Boyer-Moore algorithm
    public static int searchBoyerMoore(String text, String pattern, int ALPHABET_SIZE) {
        int n = text.length(); // Get the length of the text
        int m = pattern.length(); // Get the length of the pattern

        int[] badChar = buildBadCharTable(pattern, ALPHABET_SIZE); // Build the bad character table
        int[] goodSuffix = new int[m + 1]; // Array to store good suffix shift values
        int[] border = new int[m + 1]; // Help array for border positions

        preprocessGoodSuffix(pattern, goodSuffix, border);

        int s = 0;
        while (s <= n - m) { // Loop while the pattern fits within the text
            int j = m - 1; // Start comparing from the end of the pattern

            while (j >= 0 && pattern.charAt(j) == text.charAt(s + j)) {
                j--; // Move left if characters match
            }

            if (j < 0) {
                return s; // If j < 0, a full match is found
            }
            else {
                char c = text.charAt(s + j); // Mismatched character
                int index = mapCharToIndex(c); // Map character to table index
                int bcShift = j - badChar[index]; // Bad character shift amount
                int gsShift = goodSuffix[j + 1]; // Good suffix shift amount
                s += Math.max(1, Math.max(bcShift, gsShift)); // Shift by the larger of the two
            }
        }
        return -1; // Pattern not found
    }

    // Create the bad character table
    public static int[] buildBadCharTable(String pattern, int ALPHABET_SIZE) {
        int[] table = new int[ALPHABET_SIZE]; // Array for storing last positions
        Arrays.fill(table, -1); // Fill all with -1 as default (char not found)

        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i); // Get character in pattern
            int index = mapCharToIndex(c); // Map char to index
            table[index] = i; // Store the rightmost index of the character
        }
        return table;
    }

    // Preprocess pattern to create the good suffix shift table
    public static void preprocessGoodSuffix(String pattern, int[] shift, int[] border) {
        int m = pattern.length(); // Get pattern length
        int i = m; // Initialize i to end
        int j = m + 1; // Initialize j to m + 1
        border[i] = j; // Set border value at end

        while (i > 0) {
            while (j <= m && pattern.charAt(i - 1) != pattern.charAt(j - 1)) {
                if (shift[j] == 0)
                    shift[j] = j - i; // Fill shift table
                j = border[j]; // Move j to next border
            }
            i--;
            j--; // Move i and j back
            border[i] = j; // Store border value
        }

        for (int k = 0; k <= m; k++) {
            if (shift[k] == 0)
                shift[k] = j; // Fill empty shift values
            if (k == j)
                j = border[j]; // Update j for next border
        }
    }

    // Map character A-Z to 0–25 and a-z to 26–51
    public static int mapCharToIndex(char c) {
        if (c >= 'A' && c <= 'Z')
            return c - 'A'; // Uppercase letters
        if (c >= 'a' && c <= 'z')
            return c - 'a' + 26; // Lowercase letters
        return 0; // Default to 0 if character is outside A-Z, a-z
    }
}

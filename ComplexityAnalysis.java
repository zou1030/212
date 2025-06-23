import java.util.Arrays;
import java.util.Random;

public class ComplexityAnalysis {

    public static void main(String[] args) {
        int textSize = 1000;
        int patternSize = 10;

        final int ALPHABET_SIZE = 52; // A–Z and a–z

        String pattern = generateRandomPattern(patternSize); // Generate random pattern
        String baseText = generateRandomText(textSize); // Generate random text
        String text = insertPatternIntoText(baseText, pattern); // Insert pattern into random text

        long startTime = System.nanoTime(); // Start time
        int matchIndex = searchBoyerMoore(text, pattern, ALPHABET_SIZE); // Run Boyer-Moore search
        long endTime = System.nanoTime(); // End time
        long elapsedTime = endTime - startTime; // Time taken

        // Space calculation
        int badCharSpace = ALPHABET_SIZE * Integer.BYTES;
        int goodSuffixSpace = (pattern.length() + 1) * Integer.BYTES;
        int totalSpace = badCharSpace + goodSuffixSpace;

        // Output results
        System.out.println("Text size                     : " + text.length());
        System.out.println("Pattern size                  : " + pattern.length());
        System.out.println("Pattern found at index        : " + matchIndex);
        System.out.println("Elapsed time (ms)             : " + elapsedTime / 1_000); // Time it took to perform the search
        System.out.println("Bad character table space     : " + badCharSpace + " bytes");
        System.out.println("Good suffix table space       : " + goodSuffixSpace + " bytes");
        System.out.println("Total space used              : " + totalSpace + " bytes");
    }

    // Boyer-Moore Search Algorithm
    public static int searchBoyerMoore(String text, String pattern, int ALPHABET_SIZE) {
        int n = text.length();
        int m = pattern.length();

        int[] badChar = buildBadCharTable(pattern, ALPHABET_SIZE);
        int[] goodSuffix = new int[m + 1];
        int[] border = new int[m + 1];

        preprocessGoodSuffix(pattern, goodSuffix, border);

        int s = 0;
        while (s <= n - m) { // Slide the pattern until it fits within the text
            int j = m - 1;  // Start comparing from the end of the pattern

            // Move left while characters match
            while (j >= 0 && pattern.charAt(j) == text.charAt(s + j)) {
                j--;
            }

            // If j < 0, full match is found at index s
            if (j < 0) {
                return s;
            }
            else {
                int bcShift = j - badChar[mapCharToIndex(text.charAt(s + j))]; // Calculate bad character shift
                int gsShift = goodSuffix[j + 1]; // Calculate good suffix shift
                s += Math.max(1, Math.max(bcShift, gsShift)); // Shift the pattern by the larger of the two values
            }
        }
        return -1;
    }

    // Generate random text of a given length (uppercase A–Z)
    public static String generateRandomText(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            char c = (char) ('A' + rand.nextInt(26));
            sb.append(c);
        }
        return sb.toString(); // Return final text
    }

    // Generate a random pattern (uppercase A–Z)
    public static String generateRandomPattern(int length) {
        return generateRandomText(length);
    }

    // Insert the pattern into the random text at a random position
    public static String insertPatternIntoText(String text, String pattern) {
        if (text.length() < pattern.length())
            return pattern;

        Random rand = new Random();
        int insertIndex = rand.nextInt(text.length() - pattern.length());

        // Insert pattern by replacing part of text
        return text.substring(0, insertIndex) + pattern + text.substring(insertIndex + pattern.length());
    }

    // Build the bad character table
    public static int[] buildBadCharTable(String pattern, int ALPHABET_SIZE) {
        int[] table = new int[ALPHABET_SIZE];
        Arrays.fill(table, -1);
        for (int i = 0; i < pattern.length(); i++) {
            int index = mapCharToIndex(pattern.charAt(i));
            table[index] = i;
        }
        return table;
    }

    // Preprocess good suffix
    public static void preprocessGoodSuffix(String pattern, int[] shift, int[] border) {
        int m = pattern.length();
        int i = m, j = m + 1;
        border[i] = j;

        // Fill the border array using pattern matches
        while (i > 0) {
            while (j <= m && pattern.charAt(i - 1) != pattern.charAt(j - 1)) {
                if (shift[j] == 0)
                    shift[j] = j - i; // Fill shift table
                j = border[j]; // Move to next border
            }
            i--;
            j--;
            border[i] = j; // Store border index
        }

        // Final adjustment of the good suffix shift values
        for (int k = 0; k <= m; k++) {
            if (shift[k] == 0)
                shift[k] = j; // Fill remaining positions
            if (k == j)
                j = border[j]; // Update j if it matches
        }
    }

    // Map uppercase and lowercase letters to 0–51
    public static int mapCharToIndex(char c) {
        if (c >= 'A' && c <= 'Z')
            return c - 'A';
        if (c >= 'a' && c <= 'z')
            return c - 'a' + 26;
        return 0;
    }
}

import java.io.FileWriter;
import java.io.IOException;

public class KMPExperiment {

    // Performs the KMP pattern matching algorithm
    public static String findKMP(char[] text, char[] pattern) {
        int n = text.length;    // Length of text
        int m = pattern.length; // Length of pattern

        // If the pattern is empty, return immediately
        if (m == 0) {
            return "The pattern is empty: " + 0;
        }

        // Compute the failure function (LPS array)
        int[] fail = computeFailKMP(pattern);
        int j = 0; // Pointer for text
        int k = 0; // Pointer for pattern

        // Loop through the text to find the pattern
        while (j < n) {
            if (text[j] == pattern[k]) {
                // If full match is found
                if (k == m - 1) {
                    int index = j - m + 1; // Starting index of the match
                    return "Pattern found at index " + index;
                }
                j++; // Move to next character in text
                k++; // Move to next character in pattern
            } else if (k > 0) {
                // Use LPS to avoid unnecessary comparisons
                k = fail[k - 1];
            } else {
                // No match and k == 0, just move in text
                j++;
            }
        }

        // Pattern not found
        return "No match found: " + -1;
    }

    // Builds the failure function (Longest Prefix Suffix array)
    private static int[] computeFailKMP(char[] pattern) {
        int m = pattern.length;
        int[] fail = new int[m]; // LPS array
        int j = 1; // Start from second character
        int k = 0; // Length of the current prefix-suffix

        // Loop to fill the LPS array
        while (j < m) {
            if (pattern[j] == pattern[k]) {
                fail[j] = k + 1; // Store LPS length
                j++;
                k++;
            } else if (k > 0) {
                // Try shorter previous prefix
                k = fail[k - 1];
            } else {
                // No prefix found, move to next
                j++;
            }
        }

        return fail;
    }

    // Main method to test KMP performance across various input sizes
    public static void main(String[] args) throws IOException {
        int[] textSizes = {1000, 2000, 3000, 4000, 5000};      // Text lengths to test
        int[] patternSizes = {10, 20, 30, 40, 50};             // Pattern lengths to test
        int runs = 10000;                                      // Number of runs for averaging

        FileWriter writer = new FileWriter("kmp_time_results.csv"); // Output CSV file
        writer.write("Text Size,Pattern Size,Average Elapsed Time (ms)\n");

        // Test all combinations of text and pattern sizes
        for (int tSize : textSizes) {
            for (int pSize : patternSizes) {
                String text = generateRandomString(tSize);       // Generate random text
                String pattern = generateRandomString(pSize);    // Generate random pattern

                char[] textArr = text.toCharArray();
                char[] patternArr = pattern.toCharArray();




                long start = System.nanoTime();
                findKMP(textArr, patternArr);
                long end = System.nanoTime();
                long elapsed = end - start;

                writer.write(pSize + "," + tSize + "," + elapsed + "\n");

                System.out.printf("✅ PatternSize=%d, TextSize=%d, Time=%d ns\n", pSize, tSize, elapsed);
            }
        }

        writer.close();
        System.out.println("✅ CSV file 'kmp_time_results.csv' created successfully.");
    }

    // Generates a random lowercase string of the given length
    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + (int)(Math.random() * 26)));
        }
        return sb.toString();
    }
}

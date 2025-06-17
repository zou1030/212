public class KMPAlgorithm {

    /**
     * Naive pattern matching algorithm
     */
    public boolean hasSubstring(char[] text, char[] pattern) {
        int i = 0, j = 0, k = 0;
        while (i < text.length && j < pattern.length) {
            if (text[i] == pattern[j]) {
                i++;
                j++;
            } else {
                j = 0;
                k++;
                i = k;
            }
        }
        return j == pattern.length;
    }

    /**
     * Compute LPS (Longest Prefix Suffix) array
     */
    private int[] computeLPSArray(char[] pattern) {
        int[] lps = new int[pattern.length];
        int len = 0;
        for (int i = 1; i < pattern.length;) {
            if (pattern[i] == pattern[len]) {
                lps[i] = ++len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i++] = 0;
                }
            }
        }
        return lps;
    }

    /**
     * KMP algorithm: returns starting index of match or -1
     */
    public int KMP(char[] text, char[] pattern) {
        int[] lps = computeLPSArray(pattern);

        System.out.println("LPS Table:");
        for (int i = 0; i < lps.length; i++) {
            System.out.println("π(" + i + ") = " + lps[i]);
        }
        System.out.println("Total LPS Space: " + (lps.length * Integer.BYTES) + " bytes");

        int i = 0, j = 0;
        while (i < text.length) {
            if (text[i] == pattern[j]) {
                i++;
                j++;
                if (j == pattern.length) {
                    return i - j; // match found
                }
            } else {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return -1; // no match
    }

    public static void main(String[] args) {
        String text = "abcxabcdabcdabcy";
        String pattern = "abcdabcy";

        char[] textArr = text.toCharArray();
        char[] patternArr = pattern.toCharArray();

        KMPAlgorithm searcher = new KMPAlgorithm();

        // Naive Search (optional)
        boolean naiveResult = searcher.hasSubstring(textArr, patternArr);
        System.out.println("Naive Search Match: " + naiveResult);

        // KMP Search
        long startTime = System.currentTimeMillis();
        int matchIndex = searcher.KMP(textArr, patternArr);
        long endTime = System.currentTimeMillis();

        if (matchIndex != -1) {
            System.out.println("KMP Match Found at Index: " + matchIndex);
        } else {
            System.out.println("KMP Match Not Found");
        }

        System.out.println("Text Length: " + textArr.length);
        System.out.println("Pattern Length: " + patternArr.length);
        System.out.println("Elapsed Time: " + (endTime - startTime) + " ms");
    }
}

package org.practise.algorithm.leetcode.design;

/**
 * The API: int read4(char *buf) reads 4 characters at a time from a file.
 * <p>
 * The return value is the actual number of characters read. For example, it returns 3 if there is only 3 characters left in the file.
 * <p>
 * By using the read4 API, implement the function int read(char *buf, int n) that reads n characters from the file.
 * <p>
 * Note:
 * The read function may be called multiple times.
 * <p>
 * Example 1:
 * <p>
 * Given buf = "abc"
 * read("abc", 1) // returns "a"
 * read("abc", 2); // returns "bc"
 * read("abc", 1); // returns ""
 * Example 2:
 * <p>
 * Given buf = "abc"
 * read("abc", 4) // returns "abc"
 * read("abc", 1); // returns ""
 */
public class ReadNCharactersGivenRead4II {
    /* The read4 API is defined in the parent class Reader4.
      int read4(char[] buf); */

    public class Solution extends Reader4 {

        char[] previousBuffer = new char[4];
        int previousIndex = 0;
        int previousSize = 0;

        /**
         * @param buf Destination buffer
         * @param n   Maximum number of characters to read
         * @return The number of characters read
         */
        public int read(char[] buf, int n) {
            int counter = 0;
            while (counter < n) {
                if (previousIndex < previousSize) {
                    buf[counter++] = previousBuffer[previousIndex++];
                } else {
                    previousSize = read4(previousBuffer);
                    previousIndex = 0;
                    if (previousSize == 0) {
                        break;
                    }
                }
            }
            return counter;
        }
    }

    class Reader4 {
        public int read4(char[] buf) {
            return 0;
        }
    }
}

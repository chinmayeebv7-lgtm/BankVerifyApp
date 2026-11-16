package com.example.bankverifyapp;

import java.util.regex.Pattern;

public class Validator {

    // Verhoeff lookup tables (fixed values, do not change)
    private static final int[][] D = {
            {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
            {1, 2, 3, 4, 0, 6, 7, 8, 9, 5},
            {2, 3, 4, 0, 1, 7, 8, 9, 5, 6},
            {3, 4, 0, 1, 2, 8, 9, 5, 6, 7},
            {4, 0, 1, 2, 3, 9, 5, 6, 7, 8},
            {5, 9, 8, 7, 6, 0, 4, 3, 2, 1},
            {6, 5, 9, 8, 7, 1, 0, 4, 3, 2},
            {7, 6, 5, 9, 8, 2, 1, 0, 4, 3},
            {8, 7, 6, 5, 9, 3, 2, 1, 0, 4},
            {9, 8, 7, 6, 5, 4, 3, 2, 1, 0}
    };

    private static final int[][] P = {
            {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
            {1, 5, 7, 6, 2, 8, 3, 0, 9, 4},
            {5, 8, 0, 3, 7, 9, 6, 1, 4, 2},
            {8, 9, 1, 6, 0, 4, 3, 5, 2, 7},
            {9, 4, 5, 3, 1, 2, 6, 8, 7, 0},
            {4, 2, 8, 6, 5, 7, 3, 9, 0, 1},
            {2, 7, 9, 3, 8, 0, 6, 4, 1, 5},
            {7, 0, 4, 6, 9, 1, 3, 2, 5, 8}
    };

    // Compiled once and reused (faster than rebuilding it on every tap)
    private static final Pattern PAN_PATTERN =
            Pattern.compile("[A-Z]{3}[PCHFATBLJG][A-Z][0-9]{4}[A-Z]");

    public static boolean isValidAadhaar(String aadhaar) {
        // 12 digits, and the first digit cannot be 0 or 1
        if (aadhaar == null || !aadhaar.matches("[2-9][0-9]{11}")) {
            return false;
        }
        return passesVerhoeff(aadhaar);
    }

    private static boolean passesVerhoeff(String number) {
        int c = 0;
        // Walk the digits from right to left
        for (int i = 0; i < number.length(); i++) {
            int digit = number.charAt(number.length() - 1 - i) - '0';
            c = D[c][P[i % 8][digit]];
        }
        return c == 0; // valid numbers always end at 0
    }

    public static boolean isValidPan(String pan) {
        return pan != null && PAN_PATTERN.matcher(pan).matches();
    }

    // Shows only the last 4 digits: XXXX XXXX 1234
    public static String maskAadhaar(String aadhaar) {
        return "XXXX XXXX " + aadhaar.substring(8);
    }
}
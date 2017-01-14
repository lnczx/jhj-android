package com.meijialife.dingdang.utils;

/**
 * String utils
 */
public class StringUtils {
    public static boolean isNotEmpty(String string) {
        return !isEmpty(string);
    }

    public static boolean isEmpty(String string) {
        if (null == string || 0 == string.trim().length())
            return true;
        else
            return false;
    }

    public static String ensureNoNull(String string) {
        return null == string ? "" : string;
    }

    public static boolean notEquals(String string1, String string2) {
        if (null == string1) {
            return string2 != null;
        } else {
            return !string1.equals(string2);
        }
    }

    public static boolean isEquals(String string1, String string2) {
        return !notEquals(string1, string2);
    }

    public static double isSimilar(String strA, String strB) {
        String newStrA = removeSign(strA);
        String newStrB = removeSign(strB);
        if (newStrA.length() < newStrB.length()) {
            String temp = newStrA;
            newStrA = newStrB;
            newStrB = temp;
        }
        return getLCSlen(newStrA, newStrB) * 1.0 / newStrA.length();
    }

    private static String removeSign(String str) {
        StringBuffer sb = new StringBuffer();
        for (char item : str.toCharArray()) {
            if (charReg(item)) {
                sb.append(item);
            }
        }
        return sb.toString();
    }

    private static boolean charReg(char charValue) {
        return (charValue >= 0x4E00 && charValue <= 0x9FA5) || (charValue >= 'a' && charValue <= 'z') || (charValue >= 'A' && charValue <= 'Z');
    }

    private static int getLCSlen(String strA, String strB) {
        String[] strAElement = new String[strA.length()];
        for (int i = 0; i < strA.length(); i++) {
            strAElement[i] = strA.substring(i, i + 1);
        }
        String[] strBElement = new String[strB.length()];
        for (int i = 0; i < strB.length(); i++) {
            strBElement[i] = strB.substring(i, i + 1);
        }
        int m = strAElement.length;
        int n = strBElement.length;
        int[][] matrix = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (strAElement[i - 1].equals(strBElement[j - 1])) {
                    matrix[i][j] = matrix[i - 1][j - 1] + 1;
                } else {
                    matrix[i][j] = Math.max(Math.max(matrix[i][j - 1], matrix[i - 1][j]), matrix[i - 1][j - 1]);
                }
            }
        }
        return matrix[m][n];
    }
}

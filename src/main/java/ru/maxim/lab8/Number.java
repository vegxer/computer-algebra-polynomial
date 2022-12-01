package ru.maxim.lab8;

import java.util.Arrays;

public class Number {
    private Number() {
    }

    public static double parseRealNumber(final String input) {
        int index = spaces(input, 0);

        double num = 0;
        int numSign = 1;
        if (sign(input, index) == index + 1) {
            if (input.charAt(index) == '-') {
                numSign = -1;
            }
            ++index;
        }

        num = strToNumber(input, index, index = digits(input, index));
        if (index == input.length()) {
            return num * numSign;
        }

        if (index + 1 == point(input, index)) {
            ++index;
            int endIndex = digits(input, index);
            num += Math.pow(10, index - endIndex) * strToNumber(input, index, endIndex);
            index = endIndex;
        }

        index = spaces(input, index);
        if (index != input.length()) {
            throw new NumberFormatException(String.format("%s не число", input));
        }

        return num * numSign;
    }

    public static boolean isInteger(final String input) {
        if (input == null || input.length() == 0) {
            return false;
        }

        int index = spaces(input, 0);

        index = sign(input, index);

        index = digits(input, index);
        if (index < 0) {
            return false;
        }
        if (index == input.length()) {
            return true;
        }

        index = spaces(input, index);

        return index == input.length();
    }

    public static boolean isRealNumber(final String input) {
        if (input == null || input.length() == 0) {
            return false;
        }

        int index = spaces(input, 0);

        index = sign(input, index);

        index = digits(input, index);
        if (index < 0) {
            return false;
        }
        if (index == input.length()) {
            return true;
        }

        if (index + 1 == point(input, index)) {
            ++index;
            index = digits(input, index);
            if (index < 0) {
                return false;
            }
        }

        index = spaces(input, index);

        return index == input.length();
    }

    private static int spaces(final String input, final int index) {
        int i;
        for (i = index; i < input.length() && Character.isSpaceChar(input.charAt(i)); ++i) ;
        return i;
    }

    private static int sign(final String input, final int index) {
        return Arrays.asList('+', '-').contains(input.charAt(index)) ? index + 1 : index;
    }

    private static int digits(final String input, final int index) {
        int i;
        for (i = index; i < input.length() && Character.isDigit(input.charAt(i)); ++i) ;
        return i == index ? -1 : i;
    }

    private static int point(final String input, final int index) {
        return input.charAt(index) == '.' ? index + 1 : index;
    }

    private static double strToNumber(final String input, final int startIndexInclusive, final int endIndexExclusive) {
        if (endIndexExclusive < 0) {
            throw new NumberFormatException(String.format("%s не число", input));
        }

        final String substr = input.substring(startIndexInclusive, endIndexExclusive);
        double number = 0;

        for (int i = 0; i < substr.length(); ++i) {
            number += (substr.charAt(i) - 48) * (double) Math.pow(10, substr.length() - i - 1);
        }

        return number;
    }
}

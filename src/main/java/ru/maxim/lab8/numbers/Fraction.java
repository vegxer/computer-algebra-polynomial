package ru.maxim.lab8.numbers;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Fraction {
    private int numerator;
    private int denominator;

    public Fraction(int numerator, int denominator) {
        this.numerator = numerator;
        if (denominator > 0)
            this.denominator = denominator;
        else
            throw new IllegalArgumentException("Знаменатель должен быть натуральным числом");
        reduceFraction();
    }

    public static Fraction parse(final String fraction) {
        final String[] parts = fraction.replace(" ", "")
            .split("/");
        return new Fraction(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    private void reduceFraction() {
        if (numerator != 0) {
            int gcf = gcf(Math.abs(numerator), denominator);
            numerator /= gcf;
            denominator /= gcf;
        }
    }

    public static int gcf(int a, int b) {
        if (a < b)
            return gcf(a, b - a);
        else if (b < a)
            return gcf(a - b, b);
        else
            return a;
    }

    @Override
    public String toString() {
        return String.format("%d/%d", numerator, denominator);
    }

    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }
}

package ru.maxim.lab8.numbers;

import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

@EqualsAndHashCode(callSuper = true)
public class FractionNumber extends Arithmetical<Fraction> {
    public FractionNumber(@NotNull Fraction fraction) {
        super(fraction);
    }

    @Override
    public Arithmetical<Fraction> add(@NotNull Fraction number) {
        if (number.getNumerator() == 0) {
            return new FractionNumber(new Fraction(value.getNumerator(), value.getDenominator()));
        }
        if (value.getNumerator() == 0) {
            return new FractionNumber(new Fraction(number.getNumerator(), number.getDenominator()));
        }

        int denominator = value.getDenominator() * number.getDenominator() / Fraction.gcf(value.getDenominator(), number.getDenominator());
        int numerator = denominator / value.getDenominator() * value.getNumerator() + denominator / number.getDenominator() * number.getNumerator();
        return new FractionNumber(new Fraction(numerator, denominator));
    }

    @Override
    public Arithmetical<Fraction> subtract(Fraction number) {
        if (number.getNumerator() == 0) {
            return new FractionNumber(new Fraction(value.getNumerator(), value.getDenominator()));
        }
        if (value.getNumerator() == 0) {
            return new FractionNumber(new Fraction(number.getNumerator(), number.getDenominator()));
        }

        int denominator = value.getDenominator() * number.getDenominator() / Fraction.gcf(value.getDenominator(), number.getDenominator());
        int numerator = denominator / value.getDenominator() * value.getNumerator() - denominator / number.getDenominator() * number.getNumerator();
        return new FractionNumber(new Fraction(numerator, denominator));
    }

    @Override
    public Arithmetical<Fraction> divide(Fraction number) {
        if (number.getNumerator() != 0)
            return new FractionNumber(new Fraction(value.getNumerator() * number.getDenominator(), value.getDenominator() * number.getNumerator()));
        else
            throw new ArithmeticException("Division by zero");
    }

    @Override
    public Arithmetical<Fraction> multiply(Fraction number) {
        return new FractionNumber(new Fraction(value.getNumerator() * number.getNumerator(), value.getDenominator() * number.getDenominator()));
    }

    @Override
    public Arithmetical<Fraction> mod(Fraction number) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean less(Fraction number) {
        int denominator = value.getDenominator() * number.getDenominator() / Fraction.gcf(value.getDenominator(), number.getDenominator());
        return denominator / value.getDenominator() * value.getNumerator() < denominator / number.getDenominator() * number.getNumerator();
    }

    @Override
    public boolean greater(Fraction number) {
        int denominator = value.getDenominator() * number.getDenominator() / Fraction.gcf(value.getDenominator(), number.getDenominator());
        return denominator / value.getDenominator() * value.getNumerator() > denominator / number.getDenominator() * number.getNumerator();
    }

    @Override
    public Arithmetical<Fraction> abs() {
        return new FractionNumber(new Fraction(Math.abs(value.getNumerator()), value.getDenominator()));
    }

    @Override
    public Arithmetical<Fraction> getAdditionNeutralElement() {
        return new FractionNumber(new Fraction(0, 1));
    }

    @Override
    public Arithmetical<Fraction> getMultiplicationNeutralElement() {
        return new FractionNumber(new Fraction(1, 1));
    }

    @Override
    public boolean isAdditionNeutral() {
        return value.getNumerator() == 0;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

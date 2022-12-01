package ru.maxim.lab8.numbers;

import org.jetbrains.annotations.NotNull;

public class IntegerNumber extends Arithmetical<Integer> {
    public IntegerNumber(int value) {
        super(value);
    }

    @Override
    @NotNull
    public IntegerNumber add(@NotNull Integer number) {
        return new IntegerNumber(this.value + number);
    }

    @Override
    @NotNull
    public IntegerNumber subtract(@NotNull Integer number) {
        return new IntegerNumber(this.value - number);
    }

    @Override
    public boolean isAdditionNeutral() {
        return value == 0;
    }

    @Override
    @NotNull
    public IntegerNumber divide(@NotNull Integer number) {
        return new IntegerNumber(this.value / number);
    }

    @Override
    @NotNull
    public IntegerNumber multiply(@NotNull Integer number) {
        return new IntegerNumber(this.value * number);
    }

    @Override
    @NotNull
    public IntegerNumber mod(@NotNull Integer number) {
        return new IntegerNumber(this.value % number);
    }

    @Override
    public boolean less(@NotNull Integer number) {
        return this.value < number;
    }

    @Override
    public boolean greater(@NotNull Integer number) {
        return this.value > number;
    }

    @Override
    public Arithmetical<Integer> abs() {
        return new IntegerNumber(Math.abs(value));
    }

    @Override
    @NotNull
    public IntegerNumber getAdditionNeutralElement() {
        return new IntegerNumber(0);
    }

    @Override
    public Arithmetical<Integer> getMultiplicationNeutralElement() {
        return new IntegerNumber(1);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

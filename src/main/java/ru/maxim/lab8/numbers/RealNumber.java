package ru.maxim.lab8.numbers;

import org.jetbrains.annotations.NotNull;

public class RealNumber extends Arithmetical<Double> {
    public RealNumber(double value) {
        super(value);
    }

    @Override
    public RealNumber add(@NotNull Double number) {
        return new RealNumber(this.value + number);
    }

    @Override
    public RealNumber subtract(@NotNull Double number) {
        return new RealNumber(this.value - number);
    }

    @Override
    public RealNumber divide(@NotNull Double number) {
        return new RealNumber(this.value / number);
    }

    @Override
    public RealNumber multiply(@NotNull Double number) {
        return new RealNumber(this.value * number);
    }

    @Override
    public RealNumber mod(Double number) {
        throw new UnsupportedOperationException("Остаток от деления вещественных чисел нельзя найти");
    }

    @Override
    public boolean less(@NotNull Double number) {
        return this.value < number;
    }

    @Override
    public boolean greater(@NotNull Double number) {
        return this.value > number;
    }

    @Override
    public boolean isAdditionNeutral() {
        return value == 0;
    }

    @Override
    public Arithmetical<Double> abs() {
        return new RealNumber(Math.abs(value));
    }

    @Override
    @NotNull
    public RealNumber getAdditionNeutralElement() {
        return new RealNumber(0.0);
    }

    @Override
    public Arithmetical<Double> getMultiplicationNeutralElement() {
        return new RealNumber(1.0);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

package ru.maxim.lab8.numbers;

import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

@EqualsAndHashCode(callSuper = true)
public class ResidueRingNumber extends Arithmetical<Integer> {
    private final int module;

    public ResidueRingNumber(int value, int module) {
        super(value % module);
        this.module = module;
    }

    @Override
    public Arithmetical<Integer> add(@NotNull Integer number) {
        return new ResidueRingNumber((value + (number % module)) % module, module);
    }

    @Override
    public Arithmetical<Integer> subtract(@NotNull Integer number) {
        return new ResidueRingNumber((value - (number % module)) % module, module);
    }

    @Override
    public Arithmetical<Integer> divide(@NotNull Integer number) {
        return new ResidueRingNumber((value / (number % module)) % module, module);
    }

    @Override
    public Arithmetical<Integer> multiply(@NotNull Integer number) {
        return new ResidueRingNumber((value * (number % module)) % module, module);
    }

    @Override
    public Arithmetical<Integer> mod(@NotNull Integer number) {
        return new ResidueRingNumber((value % (number % module)) % module, module);
    }

    @Override
    public boolean less(@NotNull Integer number) {
        return value < (number % module);
    }

    @Override
    public boolean greater(@NotNull Integer number) {
        return value > (number % module);
    }

    @Override
    public boolean isAdditionNeutral() {
        return value == 0;
    }

    @Override
    public Arithmetical<Integer> abs() {
        return new ResidueRingNumber(Math.abs(value) % module, module);
    }

    @Override
    public Arithmetical<Integer> getAdditionNeutralElement() {
        return new ResidueRingNumber(0, module);
    }

    @Override
    public Arithmetical<Integer> getMultiplicationNeutralElement() {
        return new ResidueRingNumber(1, module);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public int getModule() {
        return this.module;
    }
}

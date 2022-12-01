package ru.maxim.lab8.numbers;

import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

@EqualsAndHashCode
public abstract class Arithmetical<T> {
    protected T value;

    protected Arithmetical(@NotNull T value) {
        this.value = value;
    }

    public abstract Arithmetical<T> add(T number);

    public Arithmetical<T> add(Arithmetical<T> arithmetical) {
        return add(arithmetical.value);
    }

    public abstract Arithmetical<T> subtract(T number);

    public Arithmetical<T> subtract(Arithmetical<T> number) {
        return subtract(number.value);
    }

    public abstract Arithmetical<T> divide(T number);

    public Arithmetical<T> divide(Arithmetical<T> number) {
        return divide(number.value);
    }

    public abstract Arithmetical<T> multiply(T number);

    public Arithmetical<T> multiply(Arithmetical<T> number) {
        return multiply(number.value);
    }

    public Arithmetical<T> pow(int degree) {
        if (degree == 0) {
            return getAdditionNeutralElement();
        }
        if (degree == 1) {
            return multiply(getMultiplicationNeutralElement());
        }

        Arithmetical<T> res = getMultiplicationNeutralElement();
        for (int i = 0; i < degree; ++i) {
            res = multiply(res);
        }
        return res;
    }

    public abstract Arithmetical<T> mod(T number);

    public Arithmetical<T> mod(Arithmetical<T> number) {
        return mod(number.value);
    }

    public abstract boolean less(T number);

    public boolean less(Arithmetical<T> number) {
        return less(number.value);
    }

    public abstract boolean greater(T number);

    public boolean greater(Arithmetical<T> number) {
        return greater(number.value);
    }

    public abstract boolean isAdditionNeutral();

    public abstract Arithmetical<T> abs();

    public abstract Arithmetical<T> getAdditionNeutralElement();

    public abstract Arithmetical<T> getMultiplicationNeutralElement();

    @Override
    public abstract String toString();

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}

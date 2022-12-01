package ru.maxim.lab8.numbers;

import java.util.List;
import java.util.stream.Collectors;

public class RCS {
    private final List<Integer> modules;
    private final List<Long> number;

    public RCS(final List<Integer> modules, final long number) {
        this.modules = modules;
        if (multiplyModules() <= number) {
            throw new IllegalArgumentException("Ошибка: число должно быть меньше произведения модулей");
        }

        if (!pairwiseSimple(modules)) {
            throw new IllegalArgumentException("Ошибка: модули должны быть попарно простые");
        }

        this.number = modules.stream()
            .map(module -> number % module)
            .collect(Collectors.toList());
    }

    public RCS(final List<Integer> modules, List<Long> number) {
        this.modules = modules;
        this.number = number;
    }

    public List<Integer> getModules() {
        return modules;
    }

    public List<Long> getNumber() {
        return number;
    }

    public long getNumberAsLong() {
        final long modulesMultiplication = multiplyModules();
        long numberAsLong = 0;

        for (int i = 0; i < this.number.size(); ++i) {
            final long Mi = modulesMultiplication / modules.get(i);
            numberAsLong = (numberAsLong + Mi * getMiAddition(Mi, modules.get(i)) * this.number.get(i)) % modulesMultiplication;
        }

        return numberAsLong;
    }

    @Override
    public String toString() {
        return String.format("(%s)", number.stream()
            .map(num -> Long.toString(num))
            .collect(Collectors.joining(", ")));
    }

    private long multiplyModules() {
        return modules.stream()
            .reduce((x, y) -> x * y)
            .orElseThrow(() -> new IllegalArgumentException("Модули некорректны"));
    }

    private long getMiAddition(final long Mi, final int module) {
        long result = 0;
        while ((Mi * result) % module != 1) {
            ++result;
        }
        return result;
    }

    private int gcd(int a, int b) {
        if (a == b) {
            return a;
        }
        if (a > b) {
            return gcd(a - b, b);
        }
        return gcd(a, b - a);
    }

    private boolean pairwiseSimple(final List<Integer> list) {
        for (int i = 0; i < list.size(); ++i) {
            for (int j = i + 1; j < list.size(); ++j) {
                if (gcd(list.get(i), list.get(j)) != 1) {
                    return false;
                }
            }
        }
        return true;
    }
}

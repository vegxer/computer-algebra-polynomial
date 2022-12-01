package ru.maxim.lab8.numbers;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class RCSNumber extends Arithmetical<RCS> {
    public RCSNumber(RCS rcs) {
        super(rcs);
    }

    @Override
    @NotNull
    public RCSNumber getAdditionNeutralElement() {
        return new RCSNumber(new RCS(new ArrayList<>(value.getModules()), 0));
    }

    @Override
    public Arithmetical<RCS> getMultiplicationNeutralElement() {
        return new RCSNumber(new RCS(new ArrayList<>(value.getModules()), 1));
    }

    @Override
    @NotNull
    public RCSNumber add(@NotNull final RCS rcs) {
        validateRCSForOperations(rcs);
        final RCS resultRCS = new RCS(new ArrayList<>(value.getModules()), new ArrayList<>(value.getNumber()));

        for (int i = 0; i < resultRCS.getModules().size(); ++i) {
            long sum = value.getNumber().get(i) + rcs.getNumber().get(i);
            if (sum < 0) {
                sum += value.getModules().get(i);
            }
            resultRCS.getNumber().set(i, sum % value.getModules().get(i));
        }

        return new RCSNumber(resultRCS);
    }

    @Override
    @NotNull
    public RCSNumber subtract(@NotNull final RCS rcs) {
        validateRCSForOperations(rcs);
        final RCS resultRCS = new RCS(new ArrayList<>(value.getModules()), new ArrayList<>(value.getNumber()));

        for (int i = 0; i < resultRCS.getModules().size(); ++i) {
            long difference = value.getNumber().get(i) - rcs.getNumber().get(i);
            if (difference < 0) {
                difference += value.getModules().get(i);
            }
            resultRCS.getNumber().set(i, difference % value.getModules().get(i));
        }

        return new RCSNumber(resultRCS);
    }

    @Override
    @NotNull
    public RCSNumber multiply(@NotNull final RCS rcs) {
        validateRCSForOperations(rcs);
        final RCS resultRCS = new RCS(new ArrayList<>(value.getModules()), new ArrayList<>(value.getNumber()));

        for (int i = 0; i < resultRCS.getModules().size(); ++i) {
            resultRCS.getNumber().set(i, (value.getNumber().get(i) * rcs.getNumber().get(i)) % value.getModules().get(i));
        }

        return new RCSNumber(resultRCS);
    }

    @Override
    @NotNull
    public RCSNumber divide(@NotNull final RCS number) {
        return null;
    }

    @Override
    @NotNull
    public RCSNumber mod(@NotNull final RCS number) {
        return null;
    }

    @Override
    public boolean less(@NotNull RCS number) {
        return false;
    }

    @Override
    public boolean greater(@NotNull RCS number) {
        return false;
    }

    @Override
    public boolean isAdditionNeutral() {
        return getAdditionNeutralElement().getValue().equals(value);
    }

    @Override
    public Arithmetical<RCS> abs() {
        return new RCSNumber(new RCS(new ArrayList<>(value.getModules()), new ArrayList<>(value.getNumber())));
    }

    private void validateRCSForOperations(final RCS rcs) {
        if (value.getModules().size() != rcs.getModules().size()) {
            throw new IllegalArgumentException("Количество модулей в двух числах должно быть одинаково");
        }
        if (!value.getModules().equals(rcs.getModules())) {
            throw new IllegalArgumentException("Модули чисел должны быть одинаковые");
        }
    }

    @Override
    public String toString() {
        return String.format("(%s)", value.getNumber()
            .stream()
            .map(Object::toString)
            .collect(Collectors.joining(", ")));
    }
}

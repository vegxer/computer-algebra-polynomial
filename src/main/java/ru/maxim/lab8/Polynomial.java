package ru.maxim.lab8;

import lombok.EqualsAndHashCode;
import lombok.val;
import org.apache.commons.numbers.complex.Complex;
import ru.maxim.lab8.numbers.Arithmetical;
import ru.maxim.lab8.numbers.RealNumber;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.*;

@EqualsAndHashCode
@SuppressWarnings("unchecked")
public class Polynomial<T extends Arithmetical> {
    private final T[] coeffs;
    private final int degree;

    public Polynomial(int degree, T fillingElement) {
        if (degree >= 0) {
            this.degree = degree;
            coeffs = Collections.nCopies(degree + 1, fillingElement)
                .toArray((T[]) Array.newInstance(fillingElement.getClass(), 0));
        } else {
            throw new IllegalArgumentException("Степень многочлена должна быть целой неотрицательной");
        }
    }

    public Polynomial(T[] coeffs) {
        if (coeffs.length == 0) {
            throw new IllegalArgumentException("Массив коэффициентов не должен быть пустой");
        }

        degree = coeffs.length - 1;
        this.coeffs = coeffs;
    }

    public static Polynomial<RealNumber> parse(String polynomial) {
        polynomial = polynomial.replace(" ", "");
        List<Pair<Integer, Double>> elements = new ArrayList<>();

        int index = 0;
        while (index < polynomial.length()) {
            Pair<Integer, Double> element = new Pair<>(0, 1.0);
            index = coefficientState(element, polynomial, index);
            elements.add(element);
        }

        if (elements.stream()
            .map(x -> x.left)
            .distinct()
            .count() != elements.size()) {
            throw new IllegalArgumentException("Неверно введён многочлен");
        }

        elements.sort(Comparator.comparing(x -> x.left));
        double[] coeffs = new double[elements.get(elements.size() - 1).left + 1];
        for (int i = 0, j = 0; i < coeffs.length; ++i) {
            if (elements.get(j).left == i) {
                coeffs[i] = elements.get(j).right;
                ++j;
            }
        }

        return new Polynomial<>(Arrays.stream(coeffs)
            .mapToObj(RealNumber::new)
            .toArray(RealNumber[]::new));
    }

    private static int coefficientState(Pair<Integer, Double> element, String polynomial, int index) {
        index = spaceState(polynomial, index);

        int startIndex = index;
        while (index < polynomial.length() && polynomial.charAt(index) != '*' && polynomial.charAt(index) != 'x') {
            if (index != startIndex && Arrays.asList('+', '-').contains(polynomial.charAt(index))) {
                break;
            }
            ++index;
        }

        final String number = polynomial.substring(startIndex, index);

        index = spaceState(polynomial, index);
        if (Number.isRealNumber(number)) {
            element.right = Number.parseRealNumber(number);
            if (index == polynomial.length() || (Character.isDigit(polynomial.charAt(index - 1)) && Arrays.asList('+', '-').contains(polynomial.charAt(index)))) {
                return index;
            } else if ((polynomial.charAt(index) == '*' || polynomial.charAt(index) == 'x')) {
                index = degreeState(element, polynomial, index);
            } else {
                throw new IllegalArgumentException("Многочлен введён неверно");
            }
        } else if (startIndex == index) {
            index = degreeState(element, polynomial, index);
        } else if (index >= polynomial.length() || polynomial.charAt(index) == 'x') {
            element.right = Number.parseRealNumber(polynomial.charAt(index - 1) + "1");
            index = degreeState(element, polynomial, index);
        } else {
            throw new IllegalArgumentException("Многочлен введён неверно");
        }

        return index;
    }

    private static int degreeState(Pair<Integer, Double> element, String polynomial, int index) {
        if (polynomial.charAt(index) == '*') {
            ++index;
        }
        if (polynomial.charAt(index) != 'x') {
            throw new IllegalArgumentException("После коэффициента ожидался 'x'");
        }
        ++index;
        if (index >= polynomial.length() || polynomial.charAt(index) != '^') {
            element.left = 1;
            return index;
        }
        ++index;

        int startIndex = index;
        while (index < polynomial.length() && (Character.isDigit(polynomial.charAt(index)) || polynomial.charAt(index) == '.')) {
            ++index;
        }

        final String number = polynomial.substring(startIndex, index);
        if (Number.isInteger(number)) {
            element.left = (int) Number.parseRealNumber(number);
            return index;
        } else {
            throw new IllegalArgumentException("В показателе степени должно быть натуральное число или 0");
        }
    }

    public Map<Complex, Complex> fft() {
        List<Complex> result = Arrays.stream(coeffs)
            .map(coeff -> Complex.ofCartesian((double) coeff.getValue(), 0))
            .collect(Collectors.toList());
        privateFft(result, false);


        Map<Complex, Complex> map = new HashMap<>();
        List<Complex> nthRoot = Complex.ofCartesian(1, 0).nthRoot(result.size());
        for (int i = 0; i < result.size(); ++i) {
            map.put(nthRoot.get(i), result.get(i));
        }

        return map;
    }

    public static List<Complex> reverseFft(List<Complex> values) {
        List<Complex> res = new ArrayList<>(values);
        privateFft(res, true);
        return res;
    }

    private static void privateFft(List<Complex> a, boolean invert) {
        int n = a.size();
        if (n == 1) {
            return;
        }

        List<Complex> a0 = fillByZeroes(n / 2);
        List<Complex> a1 = fillByZeroes(n / 2);
        for (int i = 0, j = 0; i < n; i += 2, ++j) {
            a0.set(j, a.get(i));
            a1.set(j, a.get(i + 1));
        }
        privateFft(a0, invert);
        privateFft(a1, invert);

        double ang = 2 * PI / n * (invert ? -1 : 1);
        Complex w = Complex.ofCartesian(1, 0);
        Complex wn = Complex.ofCartesian(cos(ang), sin(ang));
        for (int i = 0; i < n / 2; ++i) {
            a.set(i, a0.get(i).add(w.multiply(a1.get(i))));
            a.set(i + n / 2, a0.get(i).subtract(w.multiply(a1.get(i))));
            if (invert) {
                a.set(i, a.get(i).divide(2));
                a.set(i + n / 2, a.get(i + n / 2).divide(2));
            }
            w = w.multiply(wn);
        }
    }

    private static List<Complex> fillByZeroes(int n) {
        List<Complex> res = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            res.add(Complex.ofCartesian(0, 0));
        }
        return res;
    }

    public List<Polynomial<RealNumber>> kronecker() {
        List<Polynomial<RealNumber>> result = new ArrayList<>();
        Polynomial<RealNumber> curPolynom = new Polynomial<>((RealNumber[]) coeffs);
        boolean isEnd = false;

        while (!isEnd) {
            int n = coeffs.length - 1;
            for (int i = 1; i < Math.ceil((double) n / 2) + 1; ++i) {
                boolean isFind = false;
                int m = i;
                List<List<Integer>> divisors = new ArrayList<>();
                for (int k = 0; k < m + 1; ++k) {
                    RealNumber solve = curPolynom.value(new RealNumber(k));
                    if (solve.getValue() == 0) {
                        Polynomial<RealNumber> polynom = new Polynomial<>(new RealNumber[]{new RealNumber(-k), new RealNumber(1)});
                        result.add(polynom);
                        System.out.printf("%s / %s = ", curPolynom.toString(), polynom.toString());
                        curPolynom = curPolynom.divide(polynom);
                        System.out.printf("%s%n", curPolynom.toString());
                        isFind = true;
                        break;
                    }
                    divisors.add(getDivisorList(solve.getValue()));
                }
                if (isFind) {
                    break;
                }
                System.out.println("Делители:");
                System.out.println(divisors);
                System.out.println("Декартовое произведение:");
                List<List<Integer>> U = new ArrayList<>();
                product(divisors.toArray(new ArrayList[0]))
                    .forEach(a -> U.add(((List<Object>) a).stream()
                        .map(Integer.class::cast)
                        .collect(Collectors.toList())));
                System.out.println(U);
                for (List<Integer> yValues : U) {
                    List<Integer> xValues = new ArrayList<>();
                    for (int l = 0; l < yValues.size(); ++l) {
                        xValues.add(l);
                    }
                    Polynomial<RealNumber> lagrangePolynomial = createLagrangePolynomial(xValues, yValues);
                    if (lagrangePolynomial.coeffs.length != 1 || !coeffs[0].getAdditionNeutralElement().equals(lagrangePolynomial.coeffs[0])) {
                        Polynomial<RealNumber> remainder = curPolynom.mod(lagrangePolynomial);
                        System.out.printf("(Текущий многочлен) mod (многочлен Лагранжа) = %s%n", remainder.toString());
                        if (remainder.coeffs.length == 1 && coeffs[0].getAdditionNeutralElement().equals(remainder.coeffs[0]) && lagrangePolynomial.coeffs.length == m + 1) {
                            lagrangePolynomial = lagrangePolynomial.divide(lagrangePolynomial.coeffs[lagrangePolynomial.coeffs.length - 1]);
                            System.out.printf("%s / %s = ", curPolynom.toString(), lagrangePolynomial.toString());
                            curPolynom = curPolynom.divide(lagrangePolynomial);
                            System.out.printf("%s%n", curPolynom.toString());
                            result.add(lagrangePolynomial);
                            isFind = true;
                            break;
                        }
                    }
                }
                if (isFind) {
                    break;
                }
                isEnd = true;
            }
        }
        result.add(curPolynom);
        result.removeIf(r -> new Polynomial<>(new RealNumber[]{new RealNumber(1)}).equals(r));
        return result;
    }

    private Polynomial<RealNumber> createLagrangePolynomial(List<Integer> xValues, List<Integer> yValues) {
        Polynomial<RealNumber> resultPolynomial = new Polynomial<>(new RealNumber[]{new RealNumber(0)});
        for (int i = 0; i < yValues.size(); ++i) {
            resultPolynomial = resultPolynomial.add(createSubLagrange(xValues, i).multiply(new RealNumber(yValues.get(i))));
        }
        return (Polynomial<RealNumber>) removeRedundantZeroes((Polynomial<T>) resultPolynomial);
    }

    private Polynomial<RealNumber> createSubLagrange(List<Integer> xValues, int index) {
        Polynomial<RealNumber> resultPolynomial = new Polynomial<>(new RealNumber[]{new RealNumber(1)});
        double sum = 1;
        for (int i = 0; i < xValues.size(); ++i) {
            if (i == index) {
                continue;
            }
            sum *= xValues.get(index) - xValues.get(i);
            Polynomial<RealNumber> pol = new Polynomial<>(new RealNumber[]{new RealNumber(0), new RealNumber(1)});
            pol.coeffs[0].setValue(-((double) xValues.get(i)));
            resultPolynomial = resultPolynomial.multiply(pol);
        }
        if (sum == 0) {
            return new Polynomial<>(new RealNumber[]{new RealNumber(0)});
        }
        return resultPolynomial.divide(new RealNumber(sum));
    }

    public static <T> Iterable<List<T>> product(List<T>... lists) {
        int total = 1;
        int[] max = new int[lists.length];
        for (int i = 0; i < lists.length; i++) {
            max[i] = lists[i].size();
        }
        int[] initProduct = new int[lists.length];
        Arrays.fill(initProduct, 1);
        for (List<T> list :
            lists) {
            total *= list.size();
        }
        int finalTotal = total;
        return () -> new Iterator<List<T>>() {
            int index = -1;
            int[] presentProduct;

            @Override
            public boolean hasNext() {
                index++;
                return index < finalTotal;
            }

            @Override
            public List<T> next() {
                if (index == 0)
                    presentProduct = initProduct;
                else
                    presentProduct = generateNextProduct(presentProduct, max);
                List<T> result = new ArrayList<>();
                for (int i = 0; i < presentProduct.length; i++) {
                    result.add(lists[i].get(presentProduct[i] - 1));
                }
                return result;
            }
        };
    }

    public static int[] generateNextProduct(int[] curr, int[] max) {
        int n = curr.length - 1;
        curr[n]++;
        for (int i = n; i > 0; i--) {
            if (curr[i] > max[i]) {
                curr[i] = 1;
                curr[i - 1]++;
            }
        }
        return curr;
    }

    private static int spaceState(String polynomial, int index) {
        while (index < polynomial.length() && polynomial.charAt(index) == ' ') {
            ++index;
        }
        return index;
    }

    private List<Integer> getDivisorList(double number) {
        List<Integer> divisors = new ArrayList<>();
        for (int i = 1; i <= Math.abs(number); ++i) {
            if (number % i == 0) {
                divisors.add(i);
                divisors.add(-i);
            }
        }
        return divisors;
    }

    public T value(T number) {
        T res = coeffs[0];

        for (int i = 1; i < coeffs.length; ++i) {
            res = (T) res.add(coeffs[i].multiply(number.pow(i)));
        }

        return res;
    }

    public Polynomial<T> add(Polynomial<T> other) {
        final Polynomial<T> resPolynomial = new Polynomial<>(Math.max(degree, other.degree), (T) other.coeffs[0].getAdditionNeutralElement());
        int i;
        for (i = 0; i <= Math.min(degree, other.degree); ++i) {
            resPolynomial.coeffs[i] = (T) coeffs[i].add(other.coeffs[i]);
        }
        for (; i <= Math.max(degree, other.degree); ++i) {
            resPolynomial.coeffs[i] = degree > other.degree ? coeffs[i] : other.coeffs[i];
        }
        return resPolynomial;
    }

    public Polynomial<T> subtract(Polynomial<T> other) {
        final Polynomial<T> resPolynomial = new Polynomial<>(Math.max(degree, other.degree), (T) other.coeffs[0].getAdditionNeutralElement());
        int i;
        for (i = 0; i <= Math.min(degree, other.degree); ++i) {
            resPolynomial.coeffs[i] = (T) coeffs[i].subtract(other.coeffs[i]);
        }
        for (; i <= Math.max(degree, other.degree); ++i) {
            resPolynomial.coeffs[i] = degree > other.degree ? coeffs[i] : other.coeffs[i];
        }

        return removeRedundantZeroes(resPolynomial);
    }

    public Polynomial<T> multiply(T number) {
        final Polynomial<T> resPolynomial = new Polynomial<>(Arrays.copyOf(coeffs, coeffs.length));
        for (int i = 0; i <= resPolynomial.degree; ++i) {
            resPolynomial.coeffs[i] = (T) resPolynomial.coeffs[i].multiply(number);
        }
        return resPolynomial;
    }

    public Polynomial<T> multiply(Polynomial<T> other) {
        Polynomial<T> resPolynomial = new Polynomial<>(0, (T) other.coeffs[0].getAdditionNeutralElement());
        for (int i = 0; i <= other.degree; ++i) {
            final T[] coeffMultiplication = multiply(other.coeffs[i]).coeffs;
            resPolynomial = resPolynomial.add(
                new Polynomial<>(addZeroesToStart(coeffMultiplication, i))
            );
        }
        return resPolynomial;
    }

    public Polynomial<T> divide(Polynomial<T> other) {
        final T neutralElement = (T) coeffs[0].getAdditionNeutralElement();
        if (other.degree == 0 && neutralElement.equals(other.coeffs[0])) {
            throw new ArithmeticException("Деление на ноль");
        }

        if (coeffs.length == 1 && neutralElement.equals(coeffs[0])) {
            return new Polynomial<>(0, neutralElement);
        }

        if (degree < other.degree) {
            return new Polynomial<>(0, neutralElement);
        }

        T coefficient = (T) coeffs[degree].divide(other.coeffs[other.degree]);
        Polynomial<T> c = new Polynomial<>(degree - other.degree, neutralElement);
        c.coeffs[c.degree] = coefficient;
        return c.add(subtract(other.multiply(c)).divide(other));
    }

    public Polynomial<T> mod(Polynomial<T> other) {
        return subtract(other.multiply(divide(other)));
    }

    public boolean less(Polynomial<T> other) {
        final Polynomial<T> a = removeRedundantZeroes(this);
        final Polynomial<T> b = removeRedundantZeroes(other);
        if (a.coeffs.length < b.coeffs.length) {
            return true;
        }
        if (a.coeffs.length > b.coeffs.length) {
            return false;
        }
        return a.coeffs[a.coeffs.length - 1].less(b.coeffs[b.coeffs.length - 1]);
    }

    public boolean greater(Polynomial<T> other) {
        return !less(other) && !equals(other);
    }

    public Polynomial<T> gcd(Polynomial<T> other) {
        Polynomial<T> gcdPolynomial = privateGcd(other);
        return gcdPolynomial.divide(gcdPolynomial.coeffs[gcdPolynomial.coeffs.length - 1]);
    }

    public Polynomial<T> privateGcd(Polynomial<T> other) {
        if (removeRedundantZeroes(other).coeffs.length == 1 && coeffs[0].getAdditionNeutralElement().equals(other.coeffs[0])) {
            return this;
        }
        if (less(other)) {
            return privateGcd(other.mod(this));
        }
        return other.privateGcd(mod(other));
    }

    private T[] addZeroesToStart(T[] a, int count) {
        List<T> res = (List<T>) new ArrayList<>(Collections.nCopies(count, coeffs[0].getAdditionNeutralElement()));
        res.addAll(Arrays.stream(a)
            .collect(Collectors.toList()));

        return res.toArray((T[]) Array.newInstance(res.get(0).getClass(), 0));
    }

    private Polynomial<T> removeRedundantZeroes(Polynomial<T> polynomial) {
        final T neutralElement = (T) polynomial.coeffs[0].getAdditionNeutralElement();
        int j = polynomial.coeffs.length - 1;
        for (; j >= 0 && polynomial.coeffs[j].isAdditionNeutral(); --j) ;
        List<T> noZeroCoeffs = Arrays.stream(polynomial.coeffs)
            .collect(Collectors.toList())
            .subList(0, j + 1);
        if (noZeroCoeffs.isEmpty()) {
            noZeroCoeffs.add(neutralElement);
        }
        return new Polynomial<>(noZeroCoeffs.toArray((T[]) Array.newInstance(noZeroCoeffs.get(0).getClass(), 0)));
    }

    public Polynomial<T> divide(T number) {
        final Polynomial<T> resPolynomial = new Polynomial<>(Arrays.copyOf(coeffs, coeffs.length));
        for (int i = 0; i < resPolynomial.coeffs.length; ++i) {
            resPolynomial.coeffs[i] = (T) resPolynomial.coeffs[i].divide(number);
        }
        return resPolynomial;
    }

    @Override
    public String toString() {
        final T additionNeutralElement = (T) coeffs[0].getAdditionNeutralElement();
        final T multiplicationNeutralElement = (T) coeffs[0].getMultiplicationNeutralElement();
        if (coeffs.length == 1) {
            return coeffs[0].toString();
        }
        String polynom = "";
        if (degree > 0 && additionNeutralElement.greater(coeffs[coeffs.length - 1])) {
            polynom += "-";
        }
        boolean polyn = false;
        for (int i = 0; i < degree; ++i) {
            if (!additionNeutralElement.equals(coeffs[degree - i])) {
                final String generalPart = (degree - i > 1) ? ("^" + (degree - i)) : "";
                if (!multiplicationNeutralElement.equals(coeffs[degree - i].abs())) {
                    polynom = polynom.concat(coeffs[degree - i].abs().toString() + "*x" + generalPart);
                } else {
                    polynom = polynom.concat("x" + generalPart);
                }
                polyn = true;
            }
            if (additionNeutralElement.greater(coeffs[degree - i - 1])) {
                polynom = polynom.concat(" - ");
            } else if (!additionNeutralElement.equals(coeffs[degree - i - 1])) {
                polynom = polynom.concat(" + ");
            }
        }
        if (!additionNeutralElement.equals(coeffs[0])) {
            polynom += coeffs[0].abs().toString();
        } else if (!polyn) {
            polynom = "0";
        }
        return polynom;
    }

    public T[] getCoeffs() {
        return coeffs;
    }

    public int getDegree() {
        return degree;
    }
}

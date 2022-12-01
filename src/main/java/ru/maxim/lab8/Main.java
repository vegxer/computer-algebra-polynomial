package ru.maxim.lab8;

import lombok.val;
import lombok.var;
import org.apache.commons.numbers.complex.Complex;
import ru.maxim.lab8.numbers.*;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // x^4+x^3+3x^2+4x+1
        // x^2+x^3+x+1
        // 5
        // 1/1 4/1 3/1 1/1 1/1
        // 1/1 1/1 1/1 1/1
        final Scanner scanner = new Scanner(System.in);

        System.out.print("Введите первый многочлен: ");
        final Polynomial<RealNumber> polynomial1 = Polynomial.parse(scanner.nextLine());
        System.out.printf("Введённый многочлен: %s%n", polynomial1);

        System.out.print("Введите второй многочлен: ");
        final Polynomial<RealNumber> polynomial2 = Polynomial.parse(scanner.nextLine());
        System.out.printf("Введённый многочлен: %s%n%n", polynomial2);

        System.out.printf("Сумма многочленов: %s%n", polynomial1.add(polynomial2));
        System.out.printf("Разность многочленов: %s%n", polynomial1.subtract(polynomial2));
        System.out.printf("Произведение многочленов: %s%n", polynomial1.multiply(polynomial2));
        System.out.printf("Частное многочленов: %s%n", polynomial1.divide(polynomial2));
        System.out.printf("Остаток от деления многочленов: %s%n", polynomial1.mod(polynomial2));
        System.out.printf("НОД многочленов: %s%n", polynomial1.gcd(polynomial2));
        System.out.printf("Сравнение: %s %s %s%n", polynomial1, polynomial1.less(polynomial2) ? "<" : (polynomial2.less(polynomial1) ? ">" : "="), polynomial2);
        System.out.println("Кронекер: ");
        List<Polynomial<RealNumber>> polynomials = polynomial1.kronecker();
        System.out.print("Неприводимые многочлены: ");
        polynomials.forEach(polyn -> System.out.print(polyn.toString() + "; "));
        System.out.println("\nПрямой Фурье:");
        Map<Complex, Complex> complexes = polynomial1.fft();
        System.out.println(complexes);
        System.out.println("Обратный Фурье:");
        System.out.println(Polynomial.reverseFft(new ArrayList<>(complexes.values())));

        System.out.println("\n\nМногочлен из кольца вычетов");
        System.out.print("Введите модуль: ");
        int module = Integer.parseInt(scanner.nextLine());
        System.out.print("Введите первый многочлен: ");
        final Polynomial<ResidueRingNumber> residualPolynomial1 = new Polynomial<>(Arrays.stream(Polynomial.parse(scanner.nextLine())
                .getCoeffs())
            .map(realNumber -> new ResidueRingNumber(realNumber.getValue().intValue(), module))
            .toArray(ResidueRingNumber[]::new));
        System.out.printf("Введённый многочлен: %s%n", residualPolynomial1);

        System.out.print("Введите второй многочлен: ");
        final Polynomial<ResidueRingNumber> residualPolynomial2 = new Polynomial<>(Arrays.stream(Polynomial.parse(scanner.nextLine())
                .getCoeffs())
            .map(realNumber -> new ResidueRingNumber(realNumber.getValue().intValue(), module))
            .toArray(ResidueRingNumber[]::new));
        System.out.printf("Введённый многочлен: %s%n", residualPolynomial2);

        System.out.printf("Сумма многочленов: %s%n", residualPolynomial1.add(residualPolynomial2));
        System.out.printf("Разность многочленов: %s%n", residualPolynomial1.subtract(residualPolynomial2));
        System.out.printf("Произведение многочленов: %s%n", residualPolynomial1.multiply(residualPolynomial2));
        System.out.printf("Частное многочленов: %s%n", residualPolynomial1.divide(residualPolynomial2));
        System.out.printf("Остаток от деления многочленов: %s%n", residualPolynomial1.mod(residualPolynomial2));
        //System.out.printf("НОД многочленов: %s%n", residualPolynomial1.gcd(residualPolynomial2));
        System.out.printf("Сравнение: %s %s %s", residualPolynomial1, residualPolynomial1.less(residualPolynomial2) ? "<" : (residualPolynomial2.less(residualPolynomial1) ? ">" : "="), residualPolynomial2);


        System.out.println("\n\nМногочлен из рациональных чисел");
        System.out.print("Введите коэффициенты первого многочлена по возрастанию степеней: ");
        final Polynomial<FractionNumber> fractionPolynomial1 = new Polynomial<>(
            Arrays.stream(scanner.nextLine().split("\\s"))
                .map(x -> new FractionNumber(Fraction.parse(x)))
                .toArray(FractionNumber[]::new)
        );
        System.out.printf("Введённый многочлен: %s%n", fractionPolynomial1);
        System.out.print("Введите коэффициенты второго многочлена по возрастанию степеней: ");
        final Polynomial<FractionNumber> fractionPolynomial2 = new Polynomial<>(
            Arrays.stream(scanner.nextLine().split("\\s"))
                .map(x -> new FractionNumber(Fraction.parse(x)))
                .toArray(FractionNumber[]::new)
        );
        System.out.printf("Введённый многочлен: %s%n", fractionPolynomial2);

        System.out.printf("Сумма многочленов: %s%n", fractionPolynomial1.add(fractionPolynomial2));
        System.out.printf("Разность многочленов: %s%n", fractionPolynomial1.subtract(fractionPolynomial2));
        System.out.printf("Произведение многочленов: %s%n", fractionPolynomial1.multiply(fractionPolynomial2));
        System.out.printf("Частное многочленов: %s%n", fractionPolynomial1.divide(fractionPolynomial2));
        System.out.printf("Остаток от деления многочленов: %s%n", fractionPolynomial1.mod(fractionPolynomial2));
        System.out.printf("НОД многочленов: %s%n", fractionPolynomial1.gcd(fractionPolynomial2));
        System.out.printf("Сравнение: %s %s %s", fractionPolynomial1, fractionPolynomial1.less(fractionPolynomial2) ? "<" : (fractionPolynomial2.less(fractionPolynomial1) ? ">" : "="), fractionPolynomial2);

        /*System.out.println("\n\nДействия в СОК:");
        System.out.print("Введите модули через пробел: ");
        val modules = Arrays.stream(scanner.nextLine().split("\\s"))
            .map(Integer::parseInt)
            .collect(Collectors.toList());
        val polynomial1RCS = new Polynomial<>(Arrays.stream(polynomial1.getCoeffs())
            .map(coeff -> new RCSNumber(new RCS(modules, coeff.getValue().longValue())))
            .toArray(RCSNumber[]::new));
        System.out.printf("Первый многочлен в СОК: %s%n", polynomial1RCS);
        val polynomial2RCS = new Polynomial<>(Arrays.stream(polynomial2.getCoeffs())
            .map(coeff -> new RCSNumber(new RCS(modules, coeff.getValue().longValue())))
            .toArray(RCSNumber[]::new));
        System.out.printf("Второй многочлен в СОК: %s%n%n", polynomial2RCS);

        System.out.printf("Сумма многочленов в СОК: %s%n", polynomial1RCS.add(polynomial2RCS));
        System.out.printf("Сумма многочленов после перевода из СОК в 10СС: %s%n%n", toStringAsLongs(polynomial1RCS.add(polynomial2RCS)));
        System.out.printf("Разность многочленов в СОК: %s%n", polynomial1RCS.subtract(polynomial2RCS));
        System.out.printf("Разность многочленов после перевода из СОК в 10СС: %s%n%n", toStringAsLongs(polynomial1RCS.subtract(polynomial2RCS)));
        System.out.printf("Произведение многочленов в СОК: %s%n", polynomial1RCS.multiply(polynomial2RCS));
        System.out.printf("Произведение многочленов после перевода из СОК в 10СС: %s%n%n", toStringAsLongs(polynomial1RCS.multiply(polynomial2RCS)));*/
    }

    static String toStringAsLongs(Polynomial<RCSNumber> polynomial) {
        String polynom = "";
        var coeffs = polynomial.getCoeffs();
        if (polynomial.getDegree() > 0 && coeffs[coeffs.length - 1].getValue().getNumberAsLong() < 0) {
            polynom += "-";
        }
        boolean polyn = false;
        for (int i = 0; i < polynomial.getDegree(); ++i) {
            if (coeffs[polynomial.getDegree() - i].getValue().getNumberAsLong() != 0) {
                val generalPart = (polynomial.getDegree() - i > 1) ? ("^" + (polynomial.getDegree() - i)) : "";
                if (coeffs[polynomial.getDegree() - i].getValue().getNumberAsLong() != 1) {
                    polynom = polynom.concat(coeffs[polynomial.getDegree() - i].getValue().getNumberAsLong() + "*x" + generalPart);
                } else {
                    polynom = polynom.concat("x" + generalPart);
                }
                polyn = true;
            }
            if (coeffs[polynomial.getDegree() - i - 1].getValue().getNumberAsLong() < 0) {
                polynom = polynom.concat(" - ");
            } else if (coeffs[polynomial.getDegree() - i - 1].getValue().getNumberAsLong() > 0) {
                polynom = polynom.concat(" + ");
            }
        }
        if (coeffs[0].getValue().getNumberAsLong() != 0) {
            polynom += Long.toString(coeffs[0].getValue().getNumberAsLong());
        } else if (!polyn) {
            polynom = "0";
        }
        return polynom;
    }
}

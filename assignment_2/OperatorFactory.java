import calculable.BinaryOperator;
import calculable.DigitOperator;
import calculable.Number;
import calculable.UnaryOperator;
import ch.obermuhlner.math.big.BigDecimalMath;
import ch.obermuhlner.math.big.DefaultBigDecimalMath;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

public class OperatorFactory {

    public static UnaryOperator getFactorialOperator() {
        return new UnaryOperator("!", "", "!", new UnaryOperator.Operation() {
            static final HashMap<String, String> factorialMap = new HashMap<>() {{
                put(BigInteger.ZERO.toString(), BigInteger.ONE.toString());
                put(BigInteger.ONE.toString(), BigInteger.ONE.toString());
            }};

            @Override
            public Number run(Number a) {

                if (a.getX().compareTo(BigDecimal.valueOf(0)) < 0) {
                    throw new AssertionError("Factorial cannot be applied to negative number.");
                }

                if (!a.isIntegerValue()) {
                    throw new AssertionError("Factorial cannot be applied to number with decimal points.");
                }

                BigInteger i;

                for (i = BigInteger.ONE; i.compareTo(a.getX().toBigInteger()) <= 0; i = i.add(BigInteger.ONE)) {

                    if (factorialMap.containsKey(i.toString())) continue;

                    factorialMap.put(i.toString(), i.multiply(new BigInteger(factorialMap.get(i.subtract(BigInteger.ONE).toString()))).toString());
                }

                return new Number(new BigInteger(factorialMap.get(a.getX().toBigInteger().toString())));
            }
        }) {
            {
                setType(Type.SINGLE);
                setMode(Mode.END);
            }
        };
    }

    public static UnaryOperator getParenthesisOperator(UnaryOperator.Mode newMode) {
        return new UnaryOperator(null, "(", ")", new UnaryOperator.Operation() {
            @Override
            public Number run(Number a) {
                return a;
            }
        }) {
            {
                setType(Type.MULTI);
                setMode(newMode);
            }
        };
    }

    public static UnaryOperator getPercentageOperator() {
        return new UnaryOperator("%", "", "%", new UnaryOperator.Operation() {
            @Override
            public Number run(Number a) {
                return new Number(a.getX().multiply(BigDecimal.valueOf(0.01)));
            }
        }) {
            {
                setType(Type.SINGLE);
                setMode(Mode.END);
            }
        };
    }

    public static UnaryOperator getLnOperator() {
        return new UnaryOperator("ln", "ln(", ")", new UnaryOperator.Operation() {
            @Override
            public Number run(Number a) {
                return new Number(BigDecimalMath.log(a.getX(), DefaultBigDecimalMath.getDefaultMathContext()));
            }
        }) {{
            {
                setType(Type.MULTI);
            }
        }};
    }

    public static UnaryOperator getLog10Operator() {
        return new UnaryOperator("log", "log(", ")", new UnaryOperator.Operation() {
            @Override
            public Number run(Number a) {
                return new Number(BigDecimalMath.log10(a.getX(), DefaultBigDecimalMath.getDefaultMathContext()));
            }
        }) {{
            {
                setType(Type.MULTI);
            }
        }};
    }

    public static UnaryOperator getRootOperator(BigDecimal n) {
        return new UnaryOperator("√", "√(", ")", new UnaryOperator.Operation() {
            @Override
            public Number run(Number a) {
                return new Number(BigDecimalMath.root(a.getX(), n, DefaultBigDecimalMath.getDefaultMathContext()));
            }
        }) {{
            {
                setType(Type.MULTI);
            }
        }};
    }

    public static UnaryOperator getSquareRootOperator() {
        return getRootOperator(BigDecimal.valueOf(2));
    }

    public static BinaryOperator getPowOperator() {
        return new BinaryOperator("^", "^", new BinaryOperator.Operation() {
            @Override
            public Number run(Number a, Number b) {
                return new Number(BigDecimalMath.pow(b.getX(), a.getX(), DefaultBigDecimalMath.getDefaultMathContext()));
            }
        });
    }

    public static BinaryOperator getAddOperator() {
        return new BinaryOperator("+", "+", new BinaryOperator.Operation() {
            @Override
            public Number run(Number a, Number b) {
                return new Number(a.getX().add(b.getX()));
            }
        }) {{
            setPriority(1);
        }};
    }

    public static BinaryOperator getSubtractOperator() {
        return new BinaryOperator("-", "-", new BinaryOperator.Operation() {
            @Override
            public Number run(Number a, Number b) {
                return new Number(a.getX().subtract(b.getX()));
            }
        }) {{
            setPriority(1);
        }};
    }

    public static BinaryOperator getMultiplyOperator() {
        return new BinaryOperator("*", "*", new BinaryOperator.Operation() {
            @Override
            public Number run(Number a, Number b) {
                return new Number(a.getX().multiply(b.getX()));
            }
        }) {{
            setPriority(2);
        }};
    }

    public static BinaryOperator getDivideOperator() {
        return new BinaryOperator("/", "/", new BinaryOperator.Operation() {
            @Override
            public Number run(Number a, Number b) {
                return new Number(a.getX().divide(b.getX(), DefaultBigDecimalMath.getDefaultMathContext()));
            }
        }) {{
            setPriority(2);
        }};
    }

    public static DigitOperator getDigitOperator(int digit) {
        return new DigitOperator(digit);
    }

}

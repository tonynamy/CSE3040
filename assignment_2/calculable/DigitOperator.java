package calculable;

import java.math.BigDecimal;

public class DigitOperator extends UnaryOperator {
    protected final int digit;
    protected final int priority = Integer.MAX_VALUE;

    public DigitOperator(int digit) {
        super(String.valueOf(digit), "", String.valueOf(digit), new Operation() {

            int totalDigit(int a) {
                if (a == 0) return 0;

                return 1 + totalDigit(a / 10);
            }

            @Override
            public Number run(Number a) {
                return new Number(a.getX().multiply(BigDecimal.TEN).add(BigDecimal.valueOf(digit)));
            }
        });
        this.digit = digit;
        setType(Type.SINGLE);
        setMode(Mode.END);
    }

    public int getDigit() {
        return digit;
    }
}

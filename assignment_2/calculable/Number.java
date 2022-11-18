package calculable;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Number extends Calculable {

    protected BigDecimal x;
    protected String numberString = "";

    public Number(BigDecimal x) {
        super(null);
        this.x = x;
        if (!x.equals(BigDecimal.ZERO)) {
            this.numberString = x.toString();
        }
    }

    public Number(BigInteger x) {
        this(new BigDecimal(x));
    }

    public Number(double x) {
        this(BigDecimal.valueOf(x));
    }

    public Number(int x) {
        this(BigDecimal.valueOf(x));
    }

    public void addDigit(int digit) {
        setNumberString(numberString + digit);
    }

    public void addDecimalPoint() {

        if (numberString.contains(".")) {
            throw new AssertionError("Cannot have more than two decimal points");
        }

        setNumberString(numberString + ".");
    }

    public void removeDigit() {
        setNumberString(numberString.substring(0, numberString.length() - 1));
    }

    protected void setNumberString(String numberString) {
        this.numberString = numberString;
        if (numberString.isEmpty()) {
            x = BigDecimal.ZERO;
        } else {
            x = new BigDecimal(numberString);
        }
    }

    public BigDecimal getX() {
        return x;
    }

    public boolean isIntegerValue() {
        return x.signum() == 0 || x.scale() <= 0 || x.stripTrailingZeros().scale() <= 0;
    }

    @Override
    public String toCalculatorString() {
        return numberString.isEmpty() ? "0" : numberString;
    }
}

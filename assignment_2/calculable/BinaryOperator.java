package calculable;

public class BinaryOperator extends Operator {
    protected final String operator;
    private final Operation operation;

    public BinaryOperator(String label, String operator, Operation operation) {
        super(label);
        this.operation = operation;
        this.operator = operator;
    }


    @Override
    public String getOperator() {
        return operator;
    }

    @Override
    public Number operate(Number... operands) {
        if (operands.length != 2) {
            throw new AssertionError("Binary Operator expects exactly 2 operands");
        }

        return operation.run(operands[0], operands[1]);
    }

    @Override
    public String toCalculatorString() {
        return getOperator();
    }

    public interface Operation {
        Number run(Number a, Number b);
    }
}

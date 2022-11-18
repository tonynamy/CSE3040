package calculable;

public class UnaryOperator extends Operator {
    private final Operation operation;
    protected String prefix;
    protected String suffix;
    protected Type type;
    protected Mode mode = Mode.START;

    public UnaryOperator(String label, String prefix, String suffix, Operation operation) {
        super(label);
        this.prefix = prefix;
        this.suffix = suffix;
        this.operation = operation;
    }

    public UnaryOperator(String label, String prefix, Operation operation) {
        this(label, prefix, "", operation);
    }


    public UnaryOperator(UnaryOperator operator) {
        this(operator.label, operator.prefix, operator.suffix, operator.operation);
        setType(operator.type);
        setMode(operator.mode);
    }

    public Boolean isSame(UnaryOperator x) {
        if (label == null && x.label == null) return true;
        
        assert label != null;

        return label.equals(x.label);
    }

    @Override
    public String getLabel() {
        if (label != null) return super.getLabel();
        return getOperator();
    }

    @Override
    public String toCalculatorString() {
        return getOperator();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @Override
    public String getOperator() {
        if (type == Type.SINGLE) {
            return super.getLabel();
        } else if (type == Type.MULTI) {
            return mode == Mode.START ? prefix : suffix;
        }

        throw new AssertionError("Unsupported Type");
    }

    @Override
    public Number operate(Number... operands) {
        if (operands.length != 1) {
            throw new AssertionError("Binary Operator expects exactly 1 operands");
        }

        return operation.run(operands[0]);
    }

    public enum Type {
        MULTI, SINGLE
    }

    public enum Mode {
        START, END
    }

    public interface Operation {
        Number run(Number a);
    }
}

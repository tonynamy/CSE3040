package calculable;

public abstract class Operator extends Calculable {
    protected int priority = 0;

    public Operator(String label) {
        super(label);
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public abstract String getOperator();

    public abstract Number operate(Number... operands);
}

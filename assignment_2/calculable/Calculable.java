package calculable;

public abstract class Calculable {

    protected final String label;

    public Calculable(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public abstract String toCalculatorString();
}

import button.CalculatorButton;
import calculable.Number;
import calculable.*;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Stack;

public class Calculator_20211533 extends Frame {

    protected ArrayList<Calculable> calculableList = new ArrayList<>();
    protected int caret = 0;

    protected TextField extraResultTextField = new TextField();
    protected TextField resultTextField = new TextField();

    protected CalculatorButton.OnClickListener operatorButtonListener = new CalculatorButton.OnClickListener() {
        @Override
        public void onClicked(Operator operator) {
            addOperator(operator);
            refreshResultText();
        }
    };

    protected CalculatorButton.OnClickListener digitOperatorButtonListener = new CalculatorButton.OnClickListener() {
        @Override
        public void onClicked(Operator operator) {

            if (!(operator instanceof DigitOperator)) {
                throw new AssertionError("Should be DigitOperator");
            }

            if (!(calculableList.get(caret - 1) instanceof Number)) {
                calculableList.add(caret++, new Number(((DigitOperator) operator).getDigit()));
            } else {
                ((Number) calculableList.get(caret - 1)).addDigit(((DigitOperator) operator).getDigit());
            }

            refreshResultText();
        }
    };

    protected CalculatorButton.OnClickListener DPButtonListener = new CalculatorButton.OnClickListener() {
        @Override
        public void onClicked(Operator operator) {

            if (!(calculableList.get(caret - 1) instanceof Number)) {
                throw new AssertionError("Last calculable should be Number");
            }

            ((Number) calculableList.get(caret - 1)).addDecimalPoint();

            refreshResultText();
        }
    };

    protected CalculatorButton.OnClickListener CEButtonListener = new CalculatorButton.OnClickListener() {
        @Override
        public void onClicked(Operator operator) {

            if (calculableList.size() == 0) return;

            if (calculableList.get(caret - 1) instanceof Number) {

                ((Number) calculableList.get(caret - 1)).removeDigit();

                if (((Number) calculableList.get(caret - 1)).getX().equals(BigDecimal.ZERO)) {
                    calculableList.remove(--caret);
                }
            } else if (calculableList.get(caret - 1) instanceof UnaryOperator && ((UnaryOperator) calculableList.get(caret - 1)).getType() == UnaryOperator.Type.MULTI) {

                UnaryOperator unaryOperator = (UnaryOperator) calculableList.get(caret - 1);

                calculableList.remove(--caret);

                if (unaryOperator.getMode() == UnaryOperator.Mode.START) {
                    for (int i = caret; i < calculableList.size(); i++) {
                        if (!(calculableList.get(i) instanceof UnaryOperator)) continue;
                        if (((UnaryOperator) calculableList.get(i)).isSame(unaryOperator) && ((UnaryOperator) calculableList.get(i)).getMode() == UnaryOperator.Mode.END) {
                            calculableList.remove(i);
                            break;
                        }
                    }
                } else {
                    for (int i = caret - 1; i >= 0; i--) {
                        if (!(calculableList.get(i) instanceof UnaryOperator)) continue;
                        if (((UnaryOperator) calculableList.get(i)).isSame(unaryOperator) && ((UnaryOperator) calculableList.get(i)).getMode() == UnaryOperator.Mode.START) {
                            calculableList.remove(i);
                            caret--;
                            break;
                        }
                    }
                }

            } else {
                calculableList.remove(--caret);
            }

            if (calculableList.size() == 0) {
                calculableList.add(new Number(0));
                caret = 1;
            }


            refreshResultText();

        }
    };

    protected CalculatorButton.OnClickListener SumButtonListener = new CalculatorButton.OnClickListener() {
        @Override
        public void onClicked(Operator operator) {

            Number result = calculateResult();

            calculableList.clear();

            calculableList.add(result);
            caret = 1;

            refreshResultText();
        }
    };

    public Calculator_20211533(String str) {
        super(str);
        calculableList.add(new Number(0));
        caret = 1;
        setLayout();
    }

    public static void main(String[] args) {
        new Calculator_20211533("Calc");
    }

    public void addOperator(Operator operator) {

        // UnaryOperator 이면서 MULTI 모드에 Type 이 START 라면 뒤에 것도 붙여줘야겠지?

        if (calculableList.get(caret - 1) instanceof Number && ((Number) calculableList.get(caret - 1)).getX().equals(BigDecimal.ZERO)) {
            calculableList.remove(--caret);
        }
        calculableList.add(caret++, operator);

        if (operator instanceof UnaryOperator
                && ((UnaryOperator) operator).getType() == UnaryOperator.Type.MULTI
                && ((UnaryOperator) operator).getMode() == UnaryOperator.Mode.START) {

            calculableList.add(caret, new UnaryOperator((UnaryOperator) operator) {{
                setMode(Mode.END);
            }});
        }

    }

    protected ArrayList<Calculable> getPostfixCalculable() {

        ArrayList<Calculable> postfixCalculable = new ArrayList<>();
        Stack<Operator> operatorStack = new Stack<>();

        for (Calculable calculable : calculableList) {

            // 숫자라면 그대로
            if (calculable instanceof Number) {
                postfixCalculable.add(calculable);
                continue;
            }

            if (calculable instanceof UnaryOperator) {

                UnaryOperator unaryOperator = (UnaryOperator) calculable;

                if (unaryOperator.getType() == UnaryOperator.Type.MULTI) {

                    if (unaryOperator.getMode() == UnaryOperator.Mode.START) {
                        operatorStack.add(unaryOperator);
                        continue;

                    } else {

                        while (!operatorStack.empty()) {
                            Calculable t = operatorStack.pop();
                            if (t instanceof UnaryOperator && ((UnaryOperator) t).isSame(unaryOperator)) {
                                break;
                            }
                            postfixCalculable.add(t);
                        }

                    }

                }

            }

            if (!(calculable instanceof Operator)) {
                throw new AssertionError("Should be Operator");
            }

            Operator operator = (Operator) calculable;

            // 연산자라면 자신보다 우선순가 낮은 것을 만날 때까지 빼기
            while (!operatorStack.empty() && operatorStack.peek().getPriority() >= operator.getPriority()) {
                postfixCalculable.add(operatorStack.pop());
            }

            operatorStack.add(operator);
        }

        while (!operatorStack.empty()) {
            postfixCalculable.add(operatorStack.pop());
        }

        return postfixCalculable;
    }

    protected Number calculatePostfixCalculable(ArrayList<Calculable> postfixCalculable) {

        Stack<Number> resultStack = new Stack<>();

        for (Calculable calculable : postfixCalculable) {

            if (calculable instanceof Number) {
                resultStack.push((Number) calculable);
                continue;
            }

            if (calculable instanceof BinaryOperator) {

                resultStack.push(((BinaryOperator) calculable).operate(
                        resultStack.pop(), resultStack.pop()
                ));

            } else if (calculable instanceof UnaryOperator) {
                resultStack.push(((UnaryOperator) calculable).operate(
                        resultStack.pop()
                ));
            }

        }

        return resultStack.pop();
    }

    public Number calculateResult() {
        return calculatePostfixCalculable(getPostfixCalculable());
    }

    public String getCalculatorText() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Calculable x : calculableList) {
            stringBuilder.append(x.toCalculatorString());
        }
        return stringBuilder.toString();
    }

    public void refreshResultText() {
        resultTextField.setText(getCalculatorText());

        int textCaret = 0;

        for (Calculable x : calculableList) {
            textCaret += x.toCalculatorString().length();
        }

        resultTextField.setCaretPosition(textCaret);
        resultTextField.requestFocus();

        String resultText;

        try {
            resultText = calculateResult().toCalculatorString();
        } catch (Exception e) {
            resultText = e.getMessage();
        }

        extraResultTextField.setText(resultText);
    }

    public void setLayout() {
        this.setSize(400, 400);

        Panel mainPanel = new Panel(new GridBagLayout());

        Panel resultPanel = new Panel(new GridBagLayout());

        GridBagConstraints extraResultTextFieldConstraints = new GridBagConstraints();
        extraResultTextFieldConstraints.fill = GridBagConstraints.BOTH;
        extraResultTextFieldConstraints.gridx = 0;
        extraResultTextFieldConstraints.gridy = 0;
        extraResultTextFieldConstraints.weightx = 1;
        extraResultTextFieldConstraints.weighty = 0.1;

        GridBagConstraints resultTextFieldConstraints = new GridBagConstraints();
        resultTextFieldConstraints.fill = GridBagConstraints.BOTH;
        resultTextFieldConstraints.gridx = 0;
        resultTextFieldConstraints.gridy = 1;
        resultTextFieldConstraints.weightx = 1;
        resultTextFieldConstraints.weighty = 0.9;

        extraResultTextField.setEditable(false);
        resultTextField.setCursor(Cursor.getDefaultCursor());
        resultTextField.setEditable(false);

        resultPanel.add(extraResultTextField, extraResultTextFieldConstraints);
        resultPanel.add(resultTextField, resultTextFieldConstraints);

        GridBagConstraints resultPanelConstraints = new GridBagConstraints();
        resultPanelConstraints.fill = GridBagConstraints.BOTH;
        resultPanelConstraints.gridx = 0;
        resultPanelConstraints.gridy = 0;
        resultPanelConstraints.weightx = 1;
        resultPanelConstraints.weighty = 0.3;

        mainPanel.add(resultPanel, resultPanelConstraints);

        Panel buttonPanel = new Panel();
        buttonPanel.setLayout(new GridLayout(5, 5));

        ArrayList<CalculatorButton> calculableButtons = new ArrayList<>();

        calculableButtons.add(new CalculatorButton(OperatorFactory.getFactorialOperator(), operatorButtonListener));
        calculableButtons.add(new CalculatorButton(OperatorFactory.getParenthesisOperator(UnaryOperator.Mode.START), operatorButtonListener));
        calculableButtons.add(new CalculatorButton(OperatorFactory.getParenthesisOperator(UnaryOperator.Mode.END), operatorButtonListener));
        calculableButtons.add(new CalculatorButton(OperatorFactory.getPercentageOperator(), operatorButtonListener));
        calculableButtons.add(new CalculatorButton(new Operator("CE") {
            @Override
            public String getOperator() {
                return "CE";
            }

            @Override
            public Number operate(Number... operands) {
                return null;
            }

            @Override
            public String toCalculatorString() {
                return "CE";
            }
        }, CEButtonListener));

        calculableButtons.add(new CalculatorButton(OperatorFactory.getLnOperator(), operatorButtonListener));
        calculableButtons.add(new CalculatorButton(OperatorFactory.getDigitOperator(7), digitOperatorButtonListener));
        calculableButtons.add(new CalculatorButton(OperatorFactory.getDigitOperator(8), digitOperatorButtonListener));
        calculableButtons.add(new CalculatorButton(OperatorFactory.getDigitOperator(9), digitOperatorButtonListener));
        calculableButtons.add(new CalculatorButton(OperatorFactory.getDivideOperator(), operatorButtonListener));

        calculableButtons.add(new CalculatorButton(OperatorFactory.getLog10Operator(), operatorButtonListener));
        calculableButtons.add(new CalculatorButton(OperatorFactory.getDigitOperator(4), digitOperatorButtonListener));
        calculableButtons.add(new CalculatorButton(OperatorFactory.getDigitOperator(5), digitOperatorButtonListener));
        calculableButtons.add(new CalculatorButton(OperatorFactory.getDigitOperator(6), digitOperatorButtonListener));
        calculableButtons.add(new CalculatorButton(OperatorFactory.getMultiplyOperator(), operatorButtonListener));

        calculableButtons.add(new CalculatorButton(OperatorFactory.getSquareRootOperator(), operatorButtonListener));
        calculableButtons.add(new CalculatorButton(OperatorFactory.getDigitOperator(1), digitOperatorButtonListener));
        calculableButtons.add(new CalculatorButton(OperatorFactory.getDigitOperator(2), digitOperatorButtonListener));
        calculableButtons.add(new CalculatorButton(OperatorFactory.getDigitOperator(3), digitOperatorButtonListener));
        calculableButtons.add(new CalculatorButton(OperatorFactory.getSubtractOperator(), operatorButtonListener));

        calculableButtons.add(new CalculatorButton(OperatorFactory.getPowOperator(), operatorButtonListener));
        calculableButtons.add(new CalculatorButton(OperatorFactory.getDigitOperator(0), digitOperatorButtonListener));
        calculableButtons.add(new CalculatorButton(new Operator(".") {
            @Override
            public String getOperator() {
                return ".";
            }

            @Override
            public Number operate(Number... operands) {
                return null;
            }

            @Override
            public String toCalculatorString() {
                return ".";
            }
        }, DPButtonListener));
        calculableButtons.add(new CalculatorButton(new Operator("=") {
            @Override
            public String getOperator() {
                return "=";
            }

            @Override
            public Number operate(Number... operands) {
                return null;
            }

            @Override
            public String toCalculatorString() {
                return "=";
            }
        }, SumButtonListener));
        calculableButtons.add(new CalculatorButton(OperatorFactory.getAddOperator(), operatorButtonListener));

        for (CalculatorButton button : calculableButtons) {
            buttonPanel.add(button);
        }

        WindowDestroyer listener = new WindowDestroyer(); // window destroy button
        this.addWindowListener(listener);

        GridBagConstraints buttonPanelConstraints = new GridBagConstraints();
        buttonPanelConstraints.fill = GridBagConstraints.BOTH;
        buttonPanelConstraints.gridx = 0;
        buttonPanelConstraints.gridy = 1;
        buttonPanelConstraints.weightx = 1;
        buttonPanelConstraints.weighty = 0.7;

        mainPanel.add(buttonPanel, buttonPanelConstraints);

        this.add(mainPanel);

        this.setVisible(true);
    }

}
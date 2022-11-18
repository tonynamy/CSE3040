package button;

import calculable.Operator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorButton extends Button implements ActionListener {

    protected OnClickListener onClickListener;
    protected Operator operator;

    public CalculatorButton(Operator calculable, OnClickListener calculableButtonListener) {
        this.operator = calculable;
        this.onClickListener = calculableButtonListener;

        this.addActionListener(this);

        this.setLabel(calculable.getLabel());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        onClickListener.onClicked(operator);
    }

    public interface OnClickListener {
        public void onClicked(Operator operator);
    }
}

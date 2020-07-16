package lab3.guicalc;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

import lab3.operator.BinaryOperator;
import lab3.operator.UnaryOperator;

import java.util.Optional;

/**
 * CalculatorController - a JPanel containing a GUI for a calculator.
 */
class CalculatorController extends JPanel {
    //CHECKSTYLE:OFF
    private Set<BinaryOperator> supportedBinaryOperators;
    private Set<UnaryOperator> supportedUnaryOperators;
    private JLabel display;
    private Optional<BinaryOperator> pendingOperation = Optional.empty();
    private double storedValue = 0;
    private StringBuilder currentValue = new StringBuilder("0");
    private boolean waitingForInput = true;
    private boolean pointInserted = false;

    /**
     * Constructor for the CalculatorController class
     *
     * @param supportedUnaryOperators the unary operators that the GUI calculator supports
     * @param supportedBinaryOperators the binary operators that the GUI calculator supports
     */
    CalculatorController(Set<UnaryOperator> supportedUnaryOperators,
            Set<BinaryOperator> supportedBinaryOperators) {
        this.supportedUnaryOperators = Objects.requireNonNull(supportedUnaryOperators);
        this.supportedBinaryOperators = Objects.requireNonNull(supportedBinaryOperators);
    }

    /**
     * Sets required fields to run the GUI properly and initializes its content
     */
    void launch() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addContents();
        frame.add(this);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.pack();
    }

    /**
     * Helper function for launch that puts everything into place, that is: the display
     * and the operations the calculator supports.
     */
    private void addContents() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(display(), BorderLayout.NORTH);
        panel.add(lowerPanel(), BorderLayout.SOUTH);
        add(panel);
        this.setDisplay("0"); // Reset to zero on launch
    }

    /**
     * Creates and customizes the calculator display
     *
     * @return a {@code JPanel} with the display
     */
    private JPanel display() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Here the numbers and answers are stored and displayed
        JLabel display = new JLabel();
        this.display = display;
        panel.add(display, BorderLayout.EAST);
        return panel;
    }

    /**
     * Panel in charge of holding both the numbers and operators
     * on the lower part of the screen.
     *
     * @return a {@code Panel} that contains both the number and
     * operator buttons in an organized format.
     */
    private JPanel lowerPanel() {
        JPanel panel = new JPanel((new BorderLayout()));
        panel.add(numberPad(), BorderLayout.WEST);
        panel.add(operatorsPanel(), BorderLayout.EAST);
        panel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        return panel;
    }

    /**
     * Creates and customizes the numberPad of the calculator. It
     * arranges the buttons into the appropriate order and
     * adds them to the panel.
     *
     * @return the panel that contains the numbers
     */
    private JPanel numberPad() {
        JPanel panel = new JPanel(new GridLayout(0, 3));
        Integer[] array = {7,8,9,4,5,6,1,2,3};
        List<Integer> numbers = new ArrayList<>(Arrays.asList(array));

        for(Integer number : numbers) {
            JButton button = new JButton(String.valueOf(number));
            button.addActionListener((e) -> numberPressed(String.valueOf(number)));
            panel.add(button);
        }

        // Last line of the buttons is made up of unique ones, must be done manually
        JButton clearButton = new JButton("clr");
        clearButton.addActionListener((e) -> clearRegisters());
        panel.add(clearButton);

        JButton zeroButton = new JButton("0");
        zeroButton.addActionListener((e) -> numberPressed("0"));
        panel.add(zeroButton);

        JButton dotButton = new JButton(".");
        dotButton.addActionListener((e) -> numberPressed("."));
        panel.add(dotButton);

        return panel;
    }

    private void clearRegisters() {
        pendingOperation = Optional.empty();
        storedValue = 0;
        currentValue = new StringBuilder("0");
        waitingForInput = true;
        pointInserted = false;
        setDisplay("0");
    }

    /**
     * Creates and customizes the operatorPanel of the calculator.
     * It arranges the buttons into two columns and adds to the panel.
     *
     * @return the panel that contains the operator buttons.
     */
    private JPanel operatorsPanel() {
        JPanel operatorPanel = new JPanel(new GridLayout(0, 2));

        for (UnaryOperator operator : supportedUnaryOperators) {
            JButton button = new JButton(operator.toString());
            button.addActionListener((e) -> applyUnaryOperator(operator));
            operatorPanel.add(button);
        }

        for (BinaryOperator operator : supportedBinaryOperators) {
            JButton button = new JButton(operator.toString());
            button.addActionListener((e) -> setSelectedOperator(operator));
            operatorPanel.add(button);
        }

        JButton equalsButton = new JButton("=");
        equalsButton.addActionListener((e) -> calculate(Optional.empty()));
        operatorPanel.add(equalsButton);

        return operatorPanel;
    }

    /**
     * Updates the calculator's display
     *
     * @param value is the new value that the calculator will display
     */
    private void setDisplay(String value) {
        this.display.setText(value);
    }

    /**
     * If an binary operator is pressed, it stores it
     * until the second parameter is inserted. If there
     * is a previous operator waiting, it calculates its value
     * first.
     *
     * @param binaryOperator the binary operator to be stored.
     */
    private void setSelectedOperator(BinaryOperator binaryOperator) {
        if(pendingOperation.isPresent()) {
            calculate(Optional.of(binaryOperator));
        } else {
            storedValue = Double.valueOf(currentValue.toString());
            pendingOperation = Optional.of(binaryOperator);
            pointInserted = false;
            waitingForInput = true;
        }
    }

    /**
     * Updates the display of the calculator for every
     * number that the user presses. If an operation
     * had already been done, the value shown
     * gets restarted to the first digit received in
     * this method
     *
     * @param digit the next number added by the user
     */
    private void numberPressed(String digit) {
        if(waitingForInput) {
            storedValue = Double.valueOf(currentValue.toString());

            if(!pointInserted) {
                if (digit.equals(".")) {
                    currentValue = new StringBuilder("0" + digit);
                    pointInserted = true;
                } else {
                    currentValue = new StringBuilder(digit);
                }
            }
            waitingForInput = false;
        } else {
            if(pointInserted) {
                if (!digit.equals(".")) {
                    currentValue.append(digit);
                }
            } else {
                if (digit.equals(".")) {
                    pointInserted = true;
                }
                currentValue.append(digit);
            }
        }

        this.setDisplay(currentValue.toString());
    }

    /**
     * Calculates the result of a binary operation, updates the registers
     * and shows the result. If there is no nextOperator, no
     * pending operation will be stored.
     *
     * @param nextOperator is an {@code Optional} that comes (or not)
     *                     with an operator.
     */
    private void calculate(Optional<BinaryOperator> nextOperator) {
        if(pendingOperation.isPresent()) {
            storedValue = pendingOperation.get().apply(storedValue,
                    Double.valueOf(currentValue.toString()));
            currentValue = new StringBuilder(String.valueOf(storedValue));
        }
        pointInserted = false;
        waitingForInput = true;
        pendingOperation = nextOperator;
        setDisplay("" + storedValue);
    }

    /**
     * Calculates the result of a unary operation, updates the registers
     * and shows the result.
     *
     * @param unaryOperator the operation to be executed.
     */
    private void applyUnaryOperator(UnaryOperator unaryOperator) {
        storedValue = unaryOperator.apply(Double.valueOf(currentValue.toString()));
        currentValue = new StringBuilder(String.valueOf(storedValue));
        setDisplay("" + storedValue);
        pendingOperation = Optional.empty();
        pointInserted = false;
        waitingForInput = true;
    }

}

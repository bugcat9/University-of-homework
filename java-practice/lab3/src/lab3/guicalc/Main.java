package lab3.guicalc;

import java.util.HashSet;
import java.util.Set;

import lab3.operator.BinaryOperator;
import lab3.operator.BinaryOperatorImp;
import lab3.operator.UnaryOperator;
import lab3.operator.UnaryOperatorImp;

/**
 * Main program that runs the GUI Calculator
 */
public class Main {

    /**
     * Add BinaryOperators and UnaryOperators implements to the calculator.
     * 
     * @param args as input
     */
    public static void main(String[] args) {
        // Generating OperatorSet
       // throw new Exception("Need to be implemented！");
    	Set<BinaryOperator>binaryOperators=new HashSet<>();
    	binaryOperators.add(BinaryOperatorImp.ADDITION);binaryOperators.add(BinaryOperatorImp.DIVISION);
    	binaryOperators.add(BinaryOperatorImp.EXPONENTIATION);binaryOperators.add(BinaryOperatorImp.MULTIPLICATION);
    	binaryOperators.add(BinaryOperatorImp.SUBTRACTION);
    	
    	Set<UnaryOperator> unaryOperators=new HashSet<>();
    	unaryOperators.add(UnaryOperatorImp.ABS);
    	unaryOperators.add(UnaryOperatorImp.NEGATION);
        // Run the calculator!
        //throw new Exception("Need to be implemented！");
    	GuiCalculator c=new GuiCalculator(unaryOperators,binaryOperators);

    }
}

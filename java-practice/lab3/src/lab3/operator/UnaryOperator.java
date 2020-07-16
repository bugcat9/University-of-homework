package lab3.operator;

/** 
 * UnaryOperator - an arithmetic operator with a single operand.
 *
 */
public interface UnaryOperator extends Operator {
	/**
	 * Applies the Operator on the number given.
	 * 
	 * @param arg a number to apply the operator on
	 * @return the number outputted by the operation given input arg
	 */
	double apply(double arg);
}

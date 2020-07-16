package lab3.operator;

/**
 * Operator - an arithmetic operator.
 */
 public interface Operator {
	/**
	 * Creates a String representation of an operator.
	 * 
	 * @return a printable representation of this operator in a form suitable 
	 * for printing in arithmetic expressions (e.g., "sin", "+").
	 */
	@Override
	String toString();
}

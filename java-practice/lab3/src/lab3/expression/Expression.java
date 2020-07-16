package lab3.expression;

/**
 * Expression - An arithmetic expression.
 */
public interface Expression {
	
	/**
	 * Evaluates an arithmetic expression.
	 * 
	 * @return the value to which this expression evaluates
	 */
	double eval();
	
	/**
	 * Creates a String representation of an arithmetic expression.
	 * 
	 * @return this expression in standard form, suitable for inclusion
	 * in a program or text document (e.g., "(2 - 4 * (7 + 2))"). Note
	 * that the string can have "unnecessary" parentheses as this (toy)
	 * system does not know about operator precedence. 
	 */
	String toString();

}

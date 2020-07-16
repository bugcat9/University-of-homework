package lab3.expression;

/**
 * Class to reverse the sign of an Expression.
 * @author zhilinh
 *
 */
public final class NegationExpression implements Expression {

	private Expression operand;
	
	/**
	 * Public constructor that assigns the Expression to instance Expression.
	 * 
	 * @param operand the expression whose value will be returned with a negative sign 
	 */
	public NegationExpression(Expression operand) {
		this.operand = operand;
	}
	
	/**
	 * Method that returns the value of the Expression with an contrary sign.
	 * 
	 * @return returns the value of the Expression.
	 */
	public double eval() {
		//throw new Exception("Need to be implemented！");
		return (0.0-operand.eval());
	}
	
	/**
	 * toString method that returns the Expression with an contrary sign.	 * 
	 */	
	@Override
	public String toString() {
		//throw new Exception("Need to be implemented！");
		return this.eval()+"";
	}

}

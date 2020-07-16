package lab3.expression;

/**
 * Class to represents an expression that only contains a value (a number)
 * @author zhilinh
 *
 */
public final class NumberExpression implements Expression{
	
	private double valueExpressed;
	
	/**
	 * Public constructor that assigns the value to instance Expression. 
	 * 
	 * @param value the number that this expression represents
	 */
	public NumberExpression(double value) {
		valueExpressed = value;
	}
	
	/**
	 * toString method that returns a string of the value.
	 */
	@Override
	public String toString() {
		//throw new Exception("Need to be implemented！");
		return valueExpressed+"";
	}
	
	/**
	 * Method that returns the value as the number.
	 * 
	 *  @return returns the value.
	 */
	public double eval() {
		//throw new Exception("Need to be implemented！");
		return valueExpressed;
	}

}

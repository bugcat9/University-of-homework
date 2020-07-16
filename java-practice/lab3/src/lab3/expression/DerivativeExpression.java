package lab3.expression;

/**
 * Class to compute the derivative of an Expression. 
 * @author zhilinh
 *
 */
public class DerivativeExpression implements Expression {

	private final double deltaX = 1e-9;
	private Expression fn;
	private Variable independentVar;
	private double fnVal;
	
	/**
	* Creates an expression representing the derivative of the specified
	* function with respect to the specified variable.
	*
	* @param fn the function whose derivative this expression represents
	* @param independent the variable with respect to which we're
	* differentiating
	*/
	public DerivativeExpression(Expression fn, Variable independent) {
		//throw new Exception("Need to be implemented！");
		this.fn=fn;
		this.independentVar=independent;
		this.fnVal=independent.eval();
	}
	
	/**
	 * Method that returns the derivative of the Expression.
	 * 
	 * @return returns the value of the Expression.
	 */
	@Override
	public double eval() {
		//throw new Exception("Need to be implemented！");
		this.fnVal=fn.eval();
		independentVar.store(independentVar.eval()+this.deltaX);
		return (fn.eval()-this.fnVal)/this.deltaX;
		
	}

}

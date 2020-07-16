package lab3.expression;

/**
 * Class to find a zero of a function using Newton's method
 * @author zhilinh
 *
 */
public class NewtonsMethod {

	/**
	* Returns a zero of the specified function using Newton's method with
	* approxZero as the initial estimate.
	*
	* @param fn the function whose zero is to be found
	* @param x the independent variable of the function
	* @param approxZero initial approximation for the zero of the function
	* @param tolerance how close to zero f(the returned value) has to be
	* @return a value x for which f(x) is "close to zero" (<= tolerance)
	*/
	public double zero(Expression fn, Variable x, double approxZero, double tolerance) {
		
		// Continue the loop to find the zero when the difference between the value of
		// variable x and approxZero is larger than tolerance.
		//throw new Exception("Need to be implemented！");
		DerivativeExpression derivativeExpression=new DerivativeExpression(fn,x);
		x.store(approxZero);
		while(Math.abs(fn.eval()-tolerance)>0.001) {
			approxZero=approxZero-(fn.eval()/derivativeExpression.eval());
			x.store(approxZero);
		}
		
		return approxZero;
	}

}

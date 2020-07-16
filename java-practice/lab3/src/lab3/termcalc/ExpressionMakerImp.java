package lab3.termcalc;

import lab3.expression.AbsoluteValueExpression;
import lab3.expression.DifferenceExpression;
import lab3.expression.DivisionExpression;
import lab3.expression.ExponentiationExpression;
import lab3.expression.Expression;
import lab3.expression.NegationExpression;
import lab3.expression.NumberExpression;
import lab3.expression.ProductExpression;
import lab3.expression.SumExpression;

/**
 * Class to implement ExpressionMaker and create a new Expression
 * @author zhilinh
 *
 */
public class ExpressionMakerImp implements ExpressionMaker{

	/**
	 * Method that returns a new Expression of the sum of addend1 and addend2.
	 */
	@Override
	public Expression sumExpression(Expression addend1, Expression addend2) {
		//throw new Exception("Need to be implemented！");
		return new	SumExpression(addend1,addend2);
	}

	/**
	 * Method that returns a new Expression of the difference of op1 and op2.
	 */
	@Override
	public Expression differenceExpression(Expression op1, Expression op2) {
		//throw new Exception("Need to be implemented！");
		return  new DifferenceExpression(op1,op2);
	}

	/**
	 * Method that returns a new Expression of the product of factor1 and factor2.
	 */
	@Override
	public Expression productExpression(Expression factor1, Expression factor2) {
		//throw new Exception("Need to be implemented！");
		return new ProductExpression(factor1,factor2);
	}

	/**
	 * Method that returns a new Expression of the division of dividend and divisor.
	 */
	@Override
	public Expression divisionExpression(Expression dividend, Expression divisor) {
		//throw new Exception("Need to be implemented！");
		return new DivisionExpression(dividend,divisor);
	}

	/**
	 * Method that returns a new Expression of the exponentiation of base and exponent.
	 */
	@Override
	public Expression exponentiationExpression(Expression base, Expression exponent) {
		//throw new Exception("Need to be implemented！");
		return new ExponentiationExpression(base,exponent);
	}

	/**
	 * Method that returns a new Expression of the the negated operand.
	 */
	@Override
	public Expression negationExpression(Expression operand) {
		//throw new Exception("Need to be implemented！");
		return new NegationExpression(operand);
	}

	/**
	 * Method that returns a new Expression of the absolute value Expression of value.
	 */
	@Override
	public Expression absoluteValueExpression(Expression value) {
	//	throw new Exception("Need to be implemented！");
		return new AbsoluteValueExpression(value);
	}

	/**
	 * Method that returns a new Expression of the value.
	 */
	@Override
	public Expression numberExpression(double value) {
		//throw new Exception("Need to be implemented！");
		return new NumberExpression(value);
	}

}

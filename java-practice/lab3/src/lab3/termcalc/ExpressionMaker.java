package lab3.termcalc;

import lab3.expression.Expression;

/**
 * provides the methods required to generate each of the
 * operations that the command line calculator supports.
 */
public interface ExpressionMaker {
    /**
     * adds two expressions via the sum operator
     *
     * @param addend1 the first addend of the sum
     * @param addend2 the second addend of the sum
     * @return a new expression that contains the sum of both parameters
     */
    Expression sumExpression(Expression addend1, Expression addend2);

    /**
     * subtracts two expressions via the difference operator
     *
     * @param op1 the first operand of the subtraction
     * @param op2 the second operand of the subtraction
     * @return a new expression that contains the difference of both parameters
     */
    Expression differenceExpression(Expression op1, Expression op2);

    /**
     * multiples two expressions via the multiplication operator
     *
     * @param factor1 the first factor of the multiplication
     * @param factor2 the second factor of the multiplication
     * @return a new expression that contains the multiplication of both parameters
     */
    Expression productExpression(Expression factor1, Expression factor2);

    /**
     * divides two expressions via the division operator
     *
     * @param dividend of the division
     * @param divisor of the division
     * @return a new expression that contains the division of both parameters
     */
    Expression divisionExpression(Expression dividend, Expression divisor);

    /**
     * gives the exponentiation of two expressions via the exponentiation operator
     *
     * @param base of the exponentiation
     * @param exponent of the exponentiation
     * @return a new expression that contains the exponentiation of both parameters
     */
    Expression exponentiationExpression(Expression base, Expression exponent);

    /**
     * gives the expression but with contrary sign
     *
     * @param operand the expression whose value will be returned with a negative sign
     * @return a new expression that contains the the operand times (-1)
     */
    Expression negationExpression(Expression operand);

    /**
     * gives the absolute value of an expression
     *
     * @param value the expression that will be returned with a positive sign
     * @return a new expression that contains the the |operand|
     */
    Expression absoluteValueExpression(Expression value);

    /**
     * represents an expression that only contains a value (a number)
     *
     * @param value the number that this expression represents
     * @return a new expression that holds the value that was passed to it
     */
    Expression numberExpression(double value);
}

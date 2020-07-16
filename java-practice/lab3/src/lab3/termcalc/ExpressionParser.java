package lab3.termcalc;

import java.util.*;

import lab3.expression.Expression;

/**
 * ExpressionParser - parses String expressions and returns an Expression.
 * 
 *         Constraints: Supports custom configured operator precedence.
 *
 *         Use: The termcalc expects standard symbols for sum, subtraction, multiplication,
 *         division, exponentiation, and negation (represented also as '-'). To execute the
 *         absolute value enter: 'abs' followed by its argument.
 */
final class ExpressionParser {
    //CHECKSTYLE:OFF
    private final ExpressionMaker maker;

    ExpressionParser(ExpressionMaker maker) {
        this.maker = Objects.requireNonNull(maker);
    }

    /**
     * Receives a properly formatted expression in String form and parses it
     * using an ExpressionMaker to return a composed Expression object.
     *
     * @param str the string representation of an expression
     * @return an expression that encompasses the expression contained within the parameter
     */
    Expression eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            Expression parse() {
                nextChar();
                Expression x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | abs factor | factor `^` factor

            Expression parseExpression() {
                Expression x = parseTerm();
                while(true) {
                    if (eat('+')) {
                        x = maker.sumExpression(x, parseTerm()); // addition
                    }
                    else if (eat('-')) {
                        x = maker.differenceExpression(x, parseTerm()); // subtraction
                    } else {
                        return x;
                    }
                }
            }

            Expression parseTerm() {
                Expression x = parseFactor();
                while(true) {
                    if (eat('*')) {
                        x = maker.productExpression(x, parseFactor()); // multiplication
                    }
                    else if (eat('/')) {
                        x = maker.divisionExpression(x, parseFactor()); // division
                    }
                    else {
                        return x;
                    }
                }
            }

            Expression parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return maker.negationExpression(parseFactor()); // unary minus

                Expression x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if (isNumeric()) { // numbers
                    while (isNumeric()) nextChar();
                    x = maker.numberExpression(Double.parseDouble(str.substring(startPos, this.pos)));
                } else if (Character.isAlphabetic(ch)) { // functions
                    while (Character.isAlphabetic(ch)) nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("abs")) x = maker.absoluteValueExpression(x);
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected character: '" + (char)ch + "'");
                }

                if (eat('^')) x = maker.exponentiationExpression(x, parseFactor()); // exponentiation

                return x;
            }

            private boolean isNumeric() {
                return Character.isDigit(ch) || ch == '.';
            }
        }.parse();
    }
}

package lab3.termcalc;

import java.util.Scanner;

import lab3.expression.Expression;

/**
 * Main entry point for the command line calculator
 */
public class Main {
    /**
     * @param args program arguments
     */
    public static void main(String[] args) {
        //Create Instances(ExpressionMaker,TerminalCalculator)
     //   throw new Exception("Need to be implemented！");
    	ExpressionMaker maker=new ExpressionMakerImp();
    	TerminalCalculator tc=new TerminalCalculator(maker);
        //Use TerminalCalculator
       // throw new Exception("Need to be implemented！");
    	while(true) {
    		System.out.print("Enter an expression:	");
    		Scanner sc=new Scanner(System.in);
    		String expression=sc.next();
    		System.out.println("Result:"+tc.run(expression));
    		
    	}
    }
}

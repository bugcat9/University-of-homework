package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.Test;

import lab3.expression.DerivativeExpression;
import lab3.expression.DivisionExpression;
import lab3.expression.ExponentiationExpression;
import lab3.expression.Expression;
import lab3.expression.LogarithmicExpression;
import lab3.expression.NewtonsMethod;
import lab3.expression.NumberExpression;
import lab3.expression.ProductExpression;
import lab3.expression.TriangularCosineExpression;
import lab3.expression.TriangularSineExpression;
import lab3.expression.Variable;
import lab3.termcalc.ExpressionMaker;
import lab3.termcalc.ExpressionMakerImp;
import lab3.termcalc.TerminalCalculator;

class ExpressionTest {

	@Test//单个参数x^2 -2
	void VariableTest1() {
		Variable x=new Variable("x");
		NumberExpression num=new NumberExpression(2.0);
		ExpressionMaker maker=new ExpressionMakerImp();
		Expression expression=maker.productExpression(x, x);//变成x^2;
		expression=maker.differenceExpression(expression, num);//变成x^2 -2
		x.store(1.0);
		assertEquals(expression.eval(),-1.0,0.001);
		x.store(2.0);
		assertEquals(expression.eval(),2.0,0.001);
	}
	
	@Test//多个参数ax*x+bx+c
	void VariableTest2() {
		Variable x=new Variable("x");
		Variable a=new Variable("a");
		Variable b=new Variable("b");
		Variable c=new Variable("c");
		ExpressionMaker maker=new ExpressionMakerImp();
		NumberExpression num=new NumberExpression(2.0);
		Expression expression1=maker.exponentiationExpression(x,num);//变成x^2
		expression1=maker.productExpression(a, expression1);//变成ax^2
		Expression expression2=maker.productExpression(b, x);//变成bx
		
		Expression	expression=maker.sumExpression(expression1, expression2);//变成ax^2+bx
		expression=maker.sumExpression(expression, c);//变成ax*x+bx+c
		
		//完全平方，方便计算
		a.store(1);
		b.store(2);
		c.store(1);
		
		x.store(1);
		assertEquals(expression.eval(),4.0,0.001);
		x.store(2);
		assertEquals(expression.eval(),9.0,0.001);
	}
	
	@Test//求幂函数的导数
	void DerivativeExpressionTest1() {
		Variable x=new Variable("x");
		NumberExpression num=new NumberExpression(2.0);
		ExpressionMaker maker=new ExpressionMakerImp();
		Expression expression1=maker.productExpression(x, x);
		expression1=maker.differenceExpression(expression1, num);//x*x-2
		DerivativeExpression derivativeExpression=new DerivativeExpression(expression1,x);
		Expression expression2=maker.productExpression(x, num);//2*x
		
		Random random=new Random();
		for(int i=0;i<10;i++) {
			double d=random.nextDouble();
			x.store(d);
			//System.out.println(d.eval()+"-----------"+expression2.eval());
			assertEquals(derivativeExpression.eval(),expression2.eval(),0.001);	
		}
	}

	
	@Test//求三角函数的导数
	void DerivativeExpressionTest2() {
		Variable x=new Variable("x");
		TriangularSineExpression sin=new TriangularSineExpression(x);//sin
		TriangularCosineExpression cos=new TriangularCosineExpression(x);//cos
		DerivativeExpression derivativeExpression=new DerivativeExpression(sin,x);
		
		Random random=new Random();
		for(int i=0;i<10;i++) {
			double d=random.nextDouble();
			x.store(d);
			assertEquals(derivativeExpression.eval(),cos.eval(),0.001);	
		}
	}
	
	@Test//指数函数的导数
	void DerivativeExpressionTest3() {
		Variable x=new Variable("x");
		NumberExpression num=new NumberExpression(2.0);
		NumberExpression ln2=new NumberExpression(Math.log(2.0));
		ExponentiationExpression exponentiationExpression=new ExponentiationExpression(num,x);//成为2^x函数
		DerivativeExpression derivativeExpression=new DerivativeExpression(exponentiationExpression,x);
		ProductExpression productExpression=new ProductExpression(exponentiationExpression,ln2);//成为(2^x)*ln2
		
		Random random=new Random();
		for(int i=0;i<10;i++) {
			double d=random.nextDouble();
			x.store(d);
			assertEquals(derivativeExpression.eval(),productExpression.eval(),0.001);	
		}
	}
	
	
	@Test//对数函数的导数
	void DerivativeExpressionTest4() {
		Variable x=new Variable("x");
		NumberExpression num=new NumberExpression(2.0);	//常数2
		LogarithmicExpression logarithmicExpression=new LogarithmicExpression(num,x);//log(2)(x)以2为底
		ExponentiationExpression exponentiationExpression=new ExponentiationExpression(x,new NumberExpression(-1));//x^(-1)
		DerivativeExpression derivativeExpression=new DerivativeExpression(logarithmicExpression,x);
		NumberExpression ln2=new NumberExpression(Math.log(2));
		DivisionExpression divisionExpression=new DivisionExpression(exponentiationExpression,ln2);//变成1/(x*ln2);
		Random random=new Random();
		
		for(int i=0;i<10;i++) {
			double d=random.nextDouble();
			x.store(d);
			assertEquals(derivativeExpression.eval(),divisionExpression.eval(),0.001);	
		}
	}
	
	
	
	@Test//牛顿法求零点,求幂函数(x*x-2)的零点
	void NewtonsMethodTest1() {
		Variable x=new Variable("x");
		NumberExpression num=new NumberExpression(2.0);
		ExpressionMaker maker=new ExpressionMakerImp();
		Expression fn=maker.productExpression(x, x);
		fn=maker.differenceExpression(fn, num);//x*x-2;
		NewtonsMethod newtonsMethod=new NewtonsMethod ();
		double approxZero=1.0;double tolerance=0.001;
		assertEquals(newtonsMethod.zero(fn, x, approxZero, tolerance), 1.414,0.001); 
	}
	
	@Test//牛顿法求零点,求三角函数(sinx)的零点
	void NewtonsMethodTest2() {
		Variable x=new Variable("x");
		TriangularSineExpression sin=new TriangularSineExpression(x);//sin
		NewtonsMethod newtonsMethod=new NewtonsMethod ();
		double approxZero=1.0;double tolerance=0.001;	//从1开始，可知是越来越接近0
		assertEquals(newtonsMethod.zero(sin, x, approxZero, tolerance),0.0,0.001); 
	}
	
	
	

}

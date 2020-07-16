package Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import lab3.operator.BinaryOperatorImp;
import lab3.operator.UnaryOperatorImp;

class OperatorTest {

	@Test//加运算符的测试
	void ADDITIONTest() {
		//fail("Not yet implemented");
		BinaryOperatorImp binaryOperatorImp=BinaryOperatorImp.ADDITION;
		//正数加正数情况
		double arg1=1.0;double arg2=2.0;
		assertEquals(binaryOperatorImp.apply(arg1, arg2), 3.0,0.001);
		
		//正负相加的情况
		arg2=-2.0;
		assertEquals(binaryOperatorImp.apply(arg1, arg2), -1.0,0.001);
	}
	
	@Test//除运算符的测试
	void DIVISIONTest() {
		BinaryOperatorImp binaryOperatorImp=BinaryOperatorImp.DIVISION;
		//正数除正数情况
		double arg1=1.0;double arg2=2.0;
		assertEquals(binaryOperatorImp.apply(arg1, arg2), 0.5,0.001);
		arg1=0.0;
		assertEquals(binaryOperatorImp.apply(arg1, arg2), 0.0,0.001);
		//正数除负数
		arg1=2.0;arg2=-2.0;
		assertEquals(binaryOperatorImp.apply(arg1, arg2), -1.0,0.001);
		//正数除以0
		arg1=2.0;arg2=0.0;
		assertEquals(binaryOperatorImp.apply(arg1, arg2),Double.POSITIVE_INFINITY ,0.001);
		//负数除以0
		arg1=-2.0;arg2=0.0;
		assertEquals(binaryOperatorImp.apply(arg1, arg2),Double.NEGATIVE_INFINITY ,0.001);
		//0除以0
		arg1=0.0;arg2=0.0;
		assertEquals(binaryOperatorImp.apply(arg1, arg2),Double.NaN ,0.001);
	}
	
	@Test//幂运算符的测试
	void EXPONENTIATIONTest() {
		
		BinaryOperatorImp binaryOperatorImp=BinaryOperatorImp.EXPONENTIATION;
	
		double arg1=2.0;double arg2=10.0;
		assertEquals(binaryOperatorImp.apply(arg1, arg2), 1024.0,0.001);
		
		arg1=4.0;arg2=0.5;//开方统一是算术平方根
		assertEquals(binaryOperatorImp.apply(arg1, arg2), 2.0,0.001);
		arg2=0.0;
		assertEquals(binaryOperatorImp.apply(arg1, arg2),1.0,0.001);
		arg2=-0.5;
		assertEquals(binaryOperatorImp.apply(arg1, arg2),0.5,0.001);
		
		arg1=-4.0;arg2=0.5;
		assertEquals(binaryOperatorImp.apply(arg1, arg2),Double.NaN,0.001);
		arg1=-4.0;arg2=-0.5;
		assertEquals(binaryOperatorImp.apply(arg1, arg2),Double.NaN,0.001);
		arg1=0.0;arg2=-4;
		assertEquals(binaryOperatorImp.apply(arg1, arg2),Double.POSITIVE_INFINITY,0.001);
	}
	
	@Test//乘运算符的测试
	void MULTIPLICATIONTest() {
	
		BinaryOperatorImp binaryOperatorImp=BinaryOperatorImp.MULTIPLICATION;
		//正数乘正数情况
		double arg1=1.0;double arg2=2.0;
		assertEquals(binaryOperatorImp.apply(arg1, arg2), 2.0,0.001);
		
		//正负相乘的情况
		arg2=-2.0;
		assertEquals(binaryOperatorImp.apply(arg1, arg2), -2.0,0.001);
		//和零相乘
		arg2=0.0;
		assertEquals(binaryOperatorImp.apply(arg1, arg2), 0.0,0.001);
		
	}
	
	@Test//减法运算符的测试
	void SUBTRACTIONTest() {

		BinaryOperatorImp binaryOperatorImp=BinaryOperatorImp.SUBTRACTION;
		//正数加正数情况
		double arg1=1.0;double arg2=2.0;
		assertEquals(binaryOperatorImp.apply(arg1, arg2), -1.0,0.001);
		
		//正负相加的情况
		arg2=-2.0;
		assertEquals(binaryOperatorImp.apply(arg1, arg2), 3.0,0.001);
	}
	
	@Test//对数运算的测试
	void LOGARITHMTest() {
		BinaryOperatorImp binaryOperatorImp=BinaryOperatorImp.LOGARITHM;
		double base=2.0;double value=4.0;
		assertEquals(binaryOperatorImp.apply(base, value), 2.0,0.001);
		
		base=-2.0;value=4.0;
		assertEquals(binaryOperatorImp.apply(base, value),Double.NaN,0.001);
		
		base=2.0;value=-4.0;
		assertEquals(binaryOperatorImp.apply(base, value),Double.NaN,0.001);
		
		base=-2.0;value=-4.0;
		assertEquals(binaryOperatorImp.apply(base, value),Double.NaN,0.001);
	}
	
	@Test//绝对值测试
	void ABSTest() {
		UnaryOperatorImp unaryOperatorImp=UnaryOperatorImp.ABS;
		double arg=1.0;
		assertEquals(unaryOperatorImp.apply(arg),1.0,0.001);
		arg=-1.0;
		assertEquals(unaryOperatorImp.apply(arg),1.0,0.001);
		arg=0.0;
		assertEquals(unaryOperatorImp.apply(arg),0.0,0.001);
	}
	
	@Test//取反测试
	void NEGATIONTest() {
		UnaryOperatorImp unaryOperatorImp=UnaryOperatorImp.NEGATION;
		double arg=1.0;
		assertEquals(unaryOperatorImp.apply(arg),-1.0,0.001);
		arg=-1.0;
		assertEquals(unaryOperatorImp.apply(arg),1.0,0.001);
		arg=0.0;
		assertEquals(unaryOperatorImp.apply(arg),0.0,0.001);
	}
	
	@Test//sin函数测试
	void SinTest() {
		UnaryOperatorImp unaryOperatorImp=UnaryOperatorImp.Sin;
		double arg=Math.PI;
		assertEquals(unaryOperatorImp.apply(arg),0.0,0.001);
		
		arg=Math.PI/2;
		assertEquals(unaryOperatorImp.apply(arg),1.0,0.001);
		
		arg=-(Math.PI/2);
		assertEquals(unaryOperatorImp.apply(arg),-1.0,0.001);
	}
	
	@Test//cos函数测试
	void CosTest() {
		UnaryOperatorImp unaryOperatorImp=UnaryOperatorImp.Cos;
		double arg=Math.PI;
		assertEquals(unaryOperatorImp.apply(arg),-1.0,0.001);
		
		arg=Math.PI/2;
		assertEquals(unaryOperatorImp.apply(arg),0.0,0.001);
		
		arg=0.0;
		assertEquals(unaryOperatorImp.apply(arg),1.0,0.001);
	}
	
	@Test//tan函数测试
	void TanTest() {
		UnaryOperatorImp unaryOperatorImp=UnaryOperatorImp.Tan;
		double arg=Math.PI;
		assertEquals(unaryOperatorImp.apply(arg),0.0,0.001);
		
		arg=Math.PI/2;
		assertEquals(unaryOperatorImp.apply(arg),Double.POSITIVE_INFINITY,0.001);
		
		arg=Math.PI/4;
		assertEquals(unaryOperatorImp.apply(arg),1.0,0.001);
	}
	
	
	
}

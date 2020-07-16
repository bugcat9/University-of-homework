package lab3.expression;

public class TriangularSineExpression  implements Expression{
	
	Expression radian;
	public TriangularSineExpression(Expression radian) {
		this.radian=radian;
	}

	@Override
	public double eval() {
		// TODO Auto-generated method stub
		return Math.sin(this.radian.eval());
	}
	
	
	public String toString() {
		return this.eval()+"";
	}
}

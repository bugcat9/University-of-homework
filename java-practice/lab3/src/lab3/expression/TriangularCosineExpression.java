package lab3.expression;

public class TriangularCosineExpression {
	Expression radian;
	public TriangularCosineExpression(Expression radian) {
		this.radian=radian;
	}

	public double eval() {
		// TODO Auto-generated method stub
		return Math.cos(this.radian.eval());
	}
	
	
	public String toString() {
		return this.eval()+"";
	}
}

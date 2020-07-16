package lab3.expression;

public class LogarithmicExpression implements Expression {
	Expression base;
	Expression value;
	
	public	LogarithmicExpression(Expression base,Expression value){
		this.base=base;
		this.value=value;
	}
	
	@Override
	public String toString() {
		//throw new Exception("Need to be implemented！");
		return this.eval()+"";
	}
	
	public double eval() {
		//throw new Exception("Need to be implemented！");
		return Math.log(value.eval())/Math.log(base.eval());
	}
}

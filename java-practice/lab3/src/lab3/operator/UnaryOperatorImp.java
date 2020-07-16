package lab3.operator;

/**
 * Enum that contains implements of UnaryOperator
 * @author zhilinh
 *
 */
public enum UnaryOperatorImp implements UnaryOperator {
	ABS{
		
		/**
		 * toString method that returns the abs operator "abs".
		 */
		@Override
		public String toString() {
			return "abs";
		}
		
		/**
		 * Method that returns the absolute value of arg.
		 * 
		 * @param arg as the value.
		 * @return the absolute value.
		 */
		public double apply(double arg){
		//	throw new Exception("Need to be implemented！");
			return Math.abs(arg);
		}
	},
	NEGATION{
		
		/**
		 * toString method that returns the negation operator "��".
		 */
		@Override
		public String toString() {
			return "neg";
		}
		
		/**
		 * Method that returns the value of arg with a contrary sign.
		 * 
		 * @param arg as the value.
		 * @return the negated value.
		 */
		public double apply(double arg){
	//		throw new Exception("Need to be implemented！");
			return (0.0-arg);
		}
	},
	
	Cos{
		@Override
		public String toString() {
			return "cos";
		}
		
		public double apply(double arg){
			//		throw new Exception("Need to be implemented！");
					return Math.cos(arg);
		}
		
	},
	
	Tan{
		@Override
		public String toString() {
			return "tan";
		}
		
		public double apply(double arg){
			//		throw new Exception("Need to be implemented！");
			arg=arg%Math.PI;
			if(arg-(Math.PI/2)<0.001&&arg-(Math.PI/2)>-0.0001)
			{
				return Double.POSITIVE_INFINITY;
			}
					return Math.tan(arg);
		}
		
	},
	
	Sin{
		@Override
		public String toString() {
			return "sin";
		}
		
		public double apply(double arg){
			//		throw new Exception("Need to be implemented！");
					return Math.sin(arg);
		}
		
	},
	
}
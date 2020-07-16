package lab3.expression;

/**
 * An expression that represents a variable. Its value may be set as well as read.
 * Unlike other expressions in this assignment, variables are mutable. Using a variable
 * within a containing expression makes the expression a function of the current value of 
 * the variable.
 *
 * <p>Variables have immutable string names, which are used in their string representations. If an 
 * expression contains multiple distinct variables as subexpressions, it's important that they 
 * have different names, or the string representation of the containing expression will be 
 * misleading.
 */
public class Variable implements Expression {
	
	private String name;
	private double value = 0;
	
	/**
     * Constructs a variable with the specified name, whose initial value is zero.
     * 
     * @param name assigned to the variable.
     */	
    public Variable(String name) {
    	this.name = name;
    }

    /**
     * Method that returns the value of the variable.
     * 
     * @return returns the value.
     */
    @Override
    public double eval() {
       // throw new Exception("Need to be implemented！");
    	return value;
    }

    /**
     * toString method that returns the name and value of the variable.
     */
    @Override
    public String toString() { 
        //throw new Exception("Need to be implemented！");
    	return name+"="+value;
    }

    /**
     * Sets the value of this variable.
     *
     * @param value the new value of this variable
     */
    public void store(double value) {
    	//check the value of expression to value.
    	//throw new Exception("Need to be implemented！");
    	this.value=value;
    }

    /**
     * Returns the name of this variable
     * 
     * @return returns the name of the variable.
     */
    public String name() { 
        //throw new Exception("Need to be implemented！");
    	return name;
    }
}


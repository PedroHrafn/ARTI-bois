import aima.core.search.csp.Assignment;
import aima.core.search.csp.Variable;


public class DifferByOneConstraint extends BinaryConstraint {

	/**
	 * var1 and var2 differ by 1 (var1 = var2 + 1 or var1 = var2 - 1) and both are restricted to Integer domains 
	 * @param var1
	 * @param var2
	 */
	public DifferByOneConstraint(Variable var1, Variable var2) {
		super(var1, var2);
	}

	@Override
	public boolean isSatisfiedWith(Assignment assignment) {
		// TODO implement this
		return false;
	}

}

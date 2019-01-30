import aima.core.search.csp.Assignment;
import aima.core.search.csp.Variable;

public class DifferByOneConstraint extends BinaryConstraint {

	/**
	 * var1 and var2 differ by 1 (var1 = var2 + 1 or var1 = var2 - 1) and both are
	 * restricted to Integer domains
	 * 
	 * @param var1
	 * @param var2
	 */
	public DifferByOneConstraint(Variable var1, Variable var2) {
		super(var1, var2);
	}

	@Override
	public boolean isSatisfiedWith(Assignment assignment) {
		Integer value1 = (Integer) assignment.getAssignment(var1);
		Integer value2 = (Integer) assignment.getAssignment(var2);
		return value1 == null || value2 == null || value1 == value2 + 1 || value1 == value2 - 1;
	}

}

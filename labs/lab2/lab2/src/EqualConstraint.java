import aima.core.search.csp.Assignment;
import aima.core.search.csp.Variable;


public class EqualConstraint extends BinaryConstraint {

	public EqualConstraint(Variable var1, Variable var2) {
		super(var1, var2);
	}

	@Override
	public boolean isSatisfiedWith(Assignment assignment) {
		Object value1 = assignment.getAssignment(var1);
		Object value2 = assignment.getAssignment(var2);
		return value1 == null || value2 == null || value1.equals(value2);
	}
}

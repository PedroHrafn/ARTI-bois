import java.util.ArrayList;
import java.util.List;

import aima.core.search.csp.Constraint;
import aima.core.search.csp.Variable;

public abstract class BinaryConstraint implements Constraint {

	protected Variable var1;
	protected Variable var2;
	protected List<Variable> scope;

	public BinaryConstraint(Variable var1, Variable var2) {
		this.var1 = var1;
		this.var2 = var2;
		scope = new ArrayList<Variable>(2);
		scope.add(var1);
		scope.add(var2);
	}

	@Override
	public List<Variable> getScope() {
		return scope;
	}

}
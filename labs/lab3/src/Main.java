import aima.core.logic.propositional.inference.InferenceProcedure;
import aima.gui.fx.applications.agent.WumpusAgentApp;

public class Main extends WumpusAgentApp {

	@Override
    public InferenceProcedure getSolver() {
    	return new DPLL();
    }
	
    public static void main(String[] args) {
        launch(args);
    }

}

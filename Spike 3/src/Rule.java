import java.io.Serializable;

public class Rule implements Serializable {
	private static final long serialVersionUID = 1L;

	private RuleID ruleID;
	private CalcRule calcRules;
	private int eventsIncluded;

	public Rule() {
		ruleID = new RuleID();
	}

	public Rule(int comp, int round, CalcRule crule, int events) {
		this.ruleID = new RuleID(comp, round);
		this.calcRules = crule;
		this.eventsIncluded = events;
	}

	public RuleID getRuleID() {
		return ruleID;
	}
	
	public void setRuleID(RuleID id) {
		this.ruleID = id;
	}

	public CalcRule getCalcRules() {
		return calcRules;
	}

	public void setCalcRules(CalcRule calcRules) {
		this.calcRules = calcRules;
	}

	public int getEventsIncluded() {
		return eventsIncluded;
	}

	public void setEventsIncluded(int eventsIncluded) {
		this.eventsIncluded = eventsIncluded;
	}
	
	

}

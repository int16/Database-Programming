import java.io.Serializable;

public class Rule implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int compID, roundID;
	private String calcRules;
	private int eventsIncluded;

	public Rule() {
	}

	public int getCompID() {
		return compID;
	}

	public void setCompID(int compID) {
		this.compID = compID;
	}

	public int getRoundID() {
		return roundID;
	}

	public void setRoundID(int roundID) {
		this.roundID = roundID;
	}

	public String getCalcRules() {
		return calcRules;
	}

	public void setCalcRules(String calcRules) {
		this.calcRules = calcRules;
	}

	public int getEventsIncluded() {
		return eventsIncluded;
	}

	public void setEventsIncluded(int eventsIncluded) {
		this.eventsIncluded = eventsIncluded;
	}
	
	

}

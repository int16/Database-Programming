import java.io.Serializable;


public class RuleID implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int compID, roundID;
	
	public RuleID() {
	}

	public RuleID(int compID, int roundID) {
		this.compID = compID;
		this.roundID = roundID;
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
	
}

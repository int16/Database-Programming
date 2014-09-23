import java.sql.Date;
import java.util.Set;


public class Competition {

	private int id;
	private String compName;
	private String periodStart, periodEnd;
	private Set<?> rules;
	
	public Competition() {
	}

	public Competition(String name) {
		this.compName = name;
		java.util.Date now = new java.util.Date();
		Date date = new Date(now.getTime());
		this.periodStart = date.toString();
		this.periodEnd = date.toString();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getPeriodStart() {
		return periodStart;
	}

	public void setPeriodStart(String periodStart) {
		this.periodStart = periodStart;
	}

	public String getPeriodEnd() {
		return periodEnd;
	}

	public void setPeriodEnd(String periodEnd) {
		this.periodEnd = periodEnd;
	}

	public Set<?> getRules() {
		return rules;
	}

	public void setRules(Set<?> rules) {
		this.rules = rules;
	}

}

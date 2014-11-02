package scores;

import java.util.ArrayList;
import java.util.Date;

import com.mysql.jdbc.jdbc2.optional.SuspendableXAConnection;

public class Score {
	// is this a problem?
	private String[][][] scores;
	private int id;
	private Round round;
	private boolean claimAward = false;
	private ArrayList<Award> awards;
	private AwardType[] awardTypes;
	private int[] rangeTotals;
	private int rangeCounter;
	private int rating;
	private HandicapCalculator ratingCalculator;
	private int totalXs;
	private int total10s;
	private Date roundShotTime;
	private Archer archer;
	private String venue;
	private String eventLevel;
	private boolean dnf;
	private String comment;
	private Event event = null;

	public void initialise(Round round, AwardType[] types, boolean claimAward) {
		this.round = round;

		awards = new ArrayList<Award>();
		awardTypes = types;
		this.claimAward = claimAward;
		rangeCounter = 0;
		rating = -1; // set it to something impossible
		ratingCalculator = new HandicapCalculator(round);
		totalXs = 0;
		total10s = 0;
		dnf = false;
	}

	public void processRangeScore(String[][] rangeData) {
		if (scores == null) {
			scores = new String[round.getRangeCount()][][];
			rangeTotals = new int[round.getRangeCount()];
			for (int i = 0; i < scores.length; i++) {
				scores[i] = new String[rangeData.length][round.getRange(i).getArrowsPerEnd()];
			}
		}
		rangeTotals[rangeCounter] = 0;
		for (int i = 0; i < rangeData.length; i++) {
			for (int j = 0; j < rangeData[i].length - 1; j++) {
				scores[rangeCounter][i][j] = rangeData[i][j];
				if (rangeData[i][j].equalsIgnoreCase("x")) {
					rangeTotals[rangeCounter] += 10;
					totalXs++;

				} else if (!rangeData[i][j].equals("") && !rangeData[i][j].equalsIgnoreCase("m")) rangeTotals[rangeCounter] += Integer.parseInt(scores[rangeCounter][i][j]);
				if (rangeData[i][j].equals("10")) total10s++;

			}
		}

		if (claimAward) {
			checkAwards();
		}
		rangeCounter++;

		checkDidNotFinish(rangeData);
	}

	private void checkDidNotFinish(String[][] rangeData) {
		if (rangeCounter == round.getRangeCount()) dnf = true;
		// if the last entry is empty, we can assume the score is incomplete
		else if (rangeData[rangeData.length - 1][rangeData[rangeData.length - 1].length - 2].equals("")) dnf = true;

	}

	private void checkAwards() {
		Range r = round.getRange(rangeCounter);
		int minScoreForAllGold = (r.getArrowsPerEnd() * r.getMaxArrowScore()) - r.getArrowsPerEnd();
		int endScore;
		for (int i = 0; i < scores[rangeCounter].length; i++) {
			endScore = 0;
			for (int j = 0; j < scores[rangeCounter][i].length; j++) {
				if (scores[rangeCounter][i][j].equalsIgnoreCase("x")) endScore += 10;
				else if (!scores[rangeCounter][i][j].equals("") && !scores[rangeCounter][i][j].equalsIgnoreCase("m")) endScore += Integer.parseInt(scores[rangeCounter][i][j]);
			}
			if (endScore == (r.getArrowsPerEnd() * r.getMaxArrowScore())) {
				Award a = new Award(round, r);
				awards.add(a);
				a.setEnd(scores[rangeCounter][i]);
				a.setEndNo(i + 1);
				// stupid handbrake
				if (awardTypes[1].getDescription().indexOf("Perfect") != -1) a.setAwardType(awardTypes[1]);
				a.setRange(r);
				a.setRound(round);

			} else if (endScore >= minScoreForAllGold) {
				boolean isAllGold = true;
				for (int j = 0; j < scores[rangeCounter][i].length; j++) {
					if (!scores[rangeCounter][i][j].equalsIgnoreCase("x") && !scores[rangeCounter][i][j].equals("10") && !scores[rangeCounter][i][j].equals("9")) {
						isAllGold = false;
						break;
					}

				}
				if (isAllGold) {
					Award a = new Award(round, r);
					a.setEnd(scores[rangeCounter][i]);
					if (awardTypes[0].getDescription().indexOf("All") != -1) a.setAwardType(awardTypes[0]);
					a.setRange(r);
					a.setRound(round);
					a.setEndNo(i + 1);
					awards.add(a);
				}

			}
		}
		System.out.println("No awards " + awards.size());
	}

	public boolean hasAward() {
		if (awards.size() > 0) return true;
		return false;
	}

	public void calculateRating() {
		int roundTotal = 0;
		for (int i = 0; i < round.getRangeCount(); i++) {
			roundTotal += rangeTotals[i];
		}

		rating = (int) Math.round(ratingCalculator.getExpRatingForScore(roundTotal));
		System.out.println("Rating " + rating);
	}

	public int getRating() {
		return rating;
	}

	public int getTotalXs() {
		return totalXs;
	}

	public int getTotal10s() {
		return total10s;
	}

	public Date getRoundShotTime() {
		return roundShotTime;
	}

	public void setRoundShotTime(Date roundShotTime) {
		this.roundShotTime = roundShotTime;
	}

	public Archer getArcher() {
		return archer;
	}

	public void setArcher(Archer archer) {
		this.archer = archer;
	}

	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public String getEventLevel() {
		return eventLevel;
	}

	public void setEventLevel(String eventLevel) {
		this.eventLevel = eventLevel;
	}

	public int getRangeScore(int index) {
		return rangeTotals[index];
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Round getRound() {
		return round;
	}

	public boolean getDNF() {
		return dnf;
	}

	public int getTotal() {
		int total = 0;
		for (int i = 0; i < rangeTotals.length; i++) {
			total += rangeTotals[i];
		}
		return total;
	}
	
	public int getEndCount(int range) {
		return scores[range].length;
	}

	public String[] getEnd(int rangeno, int endno) {
		return scores[rangeno][endno];
	}

	public boolean isClaimAward() {
		return this.claimAward;
	}

	public ArrayList<Award> getAwards() {
		return awards;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean hasBeenStored() {
		if (id > 0) return true;
		return false;
	}

	public Award getAward(int no) {
		return awards.get(no);
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public void setAwards(ArrayList<Award> newAwards) {
		this.awards = newAwards;
	}

	public String toString() {
		return "Score!";
	}

}

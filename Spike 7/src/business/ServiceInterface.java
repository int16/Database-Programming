package business;

import java.sql.SQLException;
import java.util.List;

import scores.Archer;
import scores.Award;
import scores.AwardType;
import scores.Event;
import scores.Rating;
import scores.Round;
import scores.Score;
import database.MiscellaneousData;
import database.ScoringData;
import exceptions.NotEnoughOfficialRoundsException;

public class ServiceInterface {

	public AwardType[] getTypes() throws ClassNotFoundException, SQLException {
		MiscellaneousData misc = new MiscellaneousData();
		return misc.getAwardTypeForEndScores();
	}

	public String save(Round round, Score score) throws ClassNotFoundException, SQLException, NotEnoughOfficialRoundsException {
		StringBuffer sb = new StringBuffer();
		ScoringData sd = new ScoringData();
		sd.startTransaction();
		if (score.hasBeenStored()) sd.updateScore(score);
		else {
			// this gets an id for the score
			sd.storeNewScoreInTransaction(score);
			sb.append("Saved score. Total: " + score.getTotal() + " for " + score.getRound().getName() + "\n");

			if (round.getDiscipline().isHandicapped()) {
				if (score.getArcher().getRating() == null) {
					Rating r = sd.checkInitialRating(score.getArcher());// read only
					sd.storeInitialRatingInTransaction(score, r);
					sb.append("Stored initial rating " + r.getRating() + "\n");
				} else {
					// rating changes
					if (score.getArcher().checkRatingGoesUp(score)) {
						sd.storeNewRatingInTransaction(score);
						sb.append("Saved new rating. Archer has rating of " + score.getArcher().getNewRating().getRating() + "\n");
					}

				}
			}
			if (score.isClaimAward()) {
				sd.processPerfectAllGoldInTransaction(score);
				// after processPerfectAllGold, only the actual new awards are left (duplicates eliminated)
				for (int i = 0; i < score.getAwards().size(); i++) {
					Award a = score.getAward(i);
					sb.append("Saved " + a.getAwardType().getDescription() + " for " + a.getRange().getDistance() + "m, " + a.getRange().getFace().getDescription() + " face.\n");
				}

			}
		}
		sd.endTransaction();
		return sb.toString();
	}

	/**
	 * Spike 7
	 * 
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public Score selectScore(Archer archer, Event event) throws ClassNotFoundException, SQLException {
		ScoringData sd = new ScoringData();
		sd.startTransaction();
		Score score = sd.selectScore(archer.getId(), event.getId());
		sd.endTransaction();
		return score;
	}

}

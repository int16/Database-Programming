package scores;

public class HandicapCalculator 
{
    	
	private static final float PARAM_A =  0.027f;
	private static final float PARAM_B = 2.37f;
	private static final float PARAM_C = 0.004f;
	private static float PARAM_E = 2.5f;
	
	private static final int DISCIPLINE_TARGET = 1;
	private static final int DISCIPLINE_FIELD = 2;
	private static final int DISCIPLINE_INDOOR = 4;
	private Round round = null;
	
	
	public HandicapCalculator(Round round)
	{
	    this.round = round;
	    if (round.getDiscipline() == null) return;
	    int disc = round.getDiscipline().getId();
	    
	    if(disc == DISCIPLINE_INDOOR)
		PARAM_E = 4.65f;
	}
	
	private float getParam_W(int distance, float archerRating)
	{
		float param_W = (float) (distance * (double)( Math.exp(-PARAM_A * archerRating + PARAM_B + (PARAM_C*distance))));
		return param_W;
	}
	
	/*private static int getArcherRating(double distance, double param_W)
	{
		param_W /= distance;
		double exponent = Math.log(param_W);
		exponent -=  PARAM_B + PARAM_C*distance;
		exponent /= -PARAM_A;
		return (int) Math.round(exponent);
	}*/
	

	
	private float[] getExpectedScoreForArrows(int distance, float archerRating, Face face)
	{
		float[] expScores  = new float[face.getNoRings()];
		int radius = face.getRingWidth();
		float param_W = getParam_W(distance, archerRating);

		//int radiusFactor = 10;// this worked 
		int radiusFactor = face.getNoRings();
		
		for (int i = 0; i < expScores.length; i++) 
		{
		    expScores[i] = (float) (1 - Math.exp(-0.5 * Math.pow((double)((radius*radiusFactor-- + PARAM_E)/param_W), 2)));
		}
		return expScores;
	}
	
	public int getExpScoreForRating(float archerRating)
	{
	    float total = 0;
	    for (int i = 0; i < round.getRangeCount(); i++)
	    {
		Range r = round.getRange(i);
		total += getRangeScoreForRating(r.getDistance(), archerRating, r.getFace())
			* (r.getArrowsPerEnd() * round.getRange(i).getEnds());
	    }
	    //System.out.println("Float value "+total);
	    return (int)total;
	}
	
	private double getRangeScoreForRating(int distance, float archerRating, Face face)
	{
		float result = 0;
		float [] expScores = getExpectedScoreForArrows(distance, archerRating, face);
		int scoresPerRing = 1;
		
		for (int i = 1; i < face.getNoRings(); i++) 
		{
			result += (expScores[i-1] - expScores[i]) * scoresPerRing++;	
		}
		result += expScores[expScores.length-1] * scoresPerRing;
		
		return result;
	}
	
	public float getExpRatingForScore(int score)
	{
		float rating  = 0;
		
		
		int expectedScore = 0;
	    
	        while (expectedScore < score)
	        {
	            rating += 10;
	            expectedScore = getExpScoreForRating(rating);
	     
	        }
	        while (expectedScore > score)
	        {
	        	rating -= 1;
	        	expectedScore = getExpScoreForRating(rating);
	        	
	        }
	        while (expectedScore < score)
	        {
	            rating +=  0.1;
	            expectedScore = getExpScoreForRating(rating);
	            
	        }
	        if (expectedScore != score)
	        {
	            rating -=  0.1;
	        }
	       
	        //truncate
	        //int r = (int) (rating * 10);
	        //rating = (float) r/10;
	        String r = ""+rating;
	        r = r.substring(0, (r.indexOf(".")+2));
	        System.out.println("Rating "+r);
	        rating = Float.parseFloat(r);
	        
		return rating;
	}
	
//	public static void main(String[] args)
//	{
//		//Assuming the distance is 50m, large face, archer's rating 65
//		//Result should be 740
//	    Round round = new Round(24, "Townsville");
//	    Range range1 = new Range();
//	    range1.setArrowsPerEnd(6);
//	    range1.setDistance(70);
//	    range1.setEnds(6);
//	    Face face = new Face();
//	    face.setDiameter(122);
//	    face.setId(1);
//	    face.setNoRings(10);
//	    face.setRingWidth(61);
//	    range1.setFace(face);
//	    range1.setMaxArrowScore(10);
//	    Range range2 = new Range();
//	    range2.setArrowsPerEnd(6);
//	    range2.setDistance(60);
//	    range2.setEnds(6);
//	    range2.setFace(face);
//	    range2.setMaxArrowScore(10);
//	    Discipline disc = new Discipline();
//	    disc.setId(1);
//	    round.setRangeCount(2);
//	    round.setDiscipline(disc);
//	    round.addRange(0, range1);
//	    round.addRange(1, range2);
//	    
//	    HandicapCalculator hc = new HandicapCalculator(round);
//	    //583
//	    float d = hc.getExpScoreForRating(106);
//	    System.out.println("Expected score "+(d));
//	    System.out.println("Max float "+Float.MAX_VALUE);
////	    double [] ringArray = new double[10];
////	    for (int i = 0; i < ringArray.length; i++)
////	    {
////		ringArray[i] = (1220.0 /20.0) * (i+1);
////		System.out.println(ringArray[i]);
////		System.out.println((int) ringArray[i]);
////	    }
//		//d= getExpRatingForScore(30, 760, 90, "L");
//		//System.out.println("Expected rating "+d);
//	}
	
/*	double expectedScore = (double)score/(double)maxScore;
	expectedScore /= 10; //max arrow score
	double radius = getRadius(face) * expectedScore;
	double exponent = Math.log(expectedScore);
	exponent /=-0.5;
	exponent = Math.sqrt(exponent);
	radius *= RINGS_ON_FACE;
	radius += PARAM_E;
	exponent = exponent* radius;
	rating = getArcherRating(distance, exponent);
	System.out.println("Rating? "+rating);*/
	
	
}

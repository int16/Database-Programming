package scores;

import java.util.Date;




public class Archer 
{
	
	private int id;
	private String firstName;
	private String lastName;
	private Category cat;
	private Category currentCat;
	private Equipment currentEquipment = null;
	private boolean archived;
	private int finYear;
	private char gender ;
	private char initial;
	private Date joinDate;
	private int birthYear;
	private char status;
	private String club;
	private String notes;
	private String title;
	private Equipment defaultEquipment;
	private Discipline defaultDiscipline;
	private Discipline currentDiscipline;
	private Category category;
	private Rating rating = null;
	private Rating newRating = null;
	


	public Equipment getCurrentEquipment()
	{
	    return currentEquipment;
	}

	public void setCurrentEquipment(Equipment currentEquipment)
	{
	    this.currentEquipment = currentEquipment;
	}

	public Category getCat()
	{
	    return cat;
	}

	public void setCat(Category cat)
	{
	    this.cat = cat;
	}


	public boolean checkRatingGoesUp(Score s)
	{
	    int increase = (s.getRating()-this.getRating().getRating())/2 ;
	    if(increase > 0)
	    {
		increase += this.getRating().getRating();
		Rating newRating = new Rating(increase);
		newRating.setComment("Score improvement");
		newRating.setDiscipline(this.getCurrentDiscipline());
		newRating.setEquipment(this.getCurrentEquipment());
		newRating.setFromDate(s.getRoundShotTime());
		newRating.setUntilDate(Utils.getDefaultEndDate());
		//make it clear this is a new rating, not subject to change by current score
		this.setNewRating(newRating);
		return true;
	    }
	    return false;
	}

	public int getId() 
	{
		return id;
	}
	
	public void setId(int id) 
	{
		this.id = id;
	}
	
	public String getFirstName() 
	{
		return firstName;
	}
	
	public void setFirstName(String firstName) 
	{
		this.firstName = firstName;
	}
	
	public String getLastName() 
	{
		return lastName;
	}
	
	public void setLastName(String lastName) 
	{
		this.lastName = lastName;
	}

	public String toString()
	{
	    return lastName+", "+firstName;
	}
	
	public Equipment getDefaultEquipment()
	{
	    return defaultEquipment;
	}
	public void setDefaultEquipment(Equipment defaultEquipment)
	{
	    this.defaultEquipment = defaultEquipment;
	}
	public Discipline getDefaultDiscipline()
	{
	    return defaultDiscipline;
	}
	public void setDefaultDiscipline(Discipline discipline)
	{
	    this.defaultDiscipline = discipline;
	}
	
	public Discipline getCurrentDiscipline()
	{
	    return currentDiscipline;
	}
	public void setCurrentDiscipline(Discipline discipline)
	{
	    this.currentDiscipline = discipline;
	}
	public Category getCategory()
	{
	    return category;
	}
	public void setCategory(Category category)
	{
	    this.category = category;
	}
	public boolean isArchived() {
		return archived;
	}
	public void setArchived(boolean archived) {
		this.archived = archived;
	}
	public int getFinYear() {
		return finYear;
	}
	public void setFinYear(int finYear) {
		this.finYear = finYear;
	}
	public char getGender() {
		return gender;
	}
	public void setGender(char gender) {
		this.gender = gender;
	}
	public char getInitial() {
		return initial;
	}
	public void setInitial(char c) {
		this.initial = c;
	}
	public Date getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}
	public int getBirthYear() {
		return birthYear;
	}
	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public String getClub() {
		return club;
	}
	public void setClub(String club) {
		this.club = club;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public Rating getRating()
	{
	    return rating;
	}

	public void setRating(Rating rating)
	{
	    this.rating = rating;
	}

	public Rating getNewRating()
	{
	    return newRating;
	}

	public void setNewRating(Rating newRating)
	{
	    this.newRating = newRating;
	}
	
	public Category getCurrentCat()
	{
	    return currentCat;
	}

	public void setCurrentCat(Category currentCat)
	{
	    this.currentCat = currentCat;
	}

}

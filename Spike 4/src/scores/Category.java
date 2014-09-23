package scores;

import java.util.ArrayList;
import java.util.Date;

public class Category
{
    private int id;
    private char gender;
    private String name;
    private int from_age;
    private int to_age;
    private int level;
    
    public static Category[] getApplicableCategories(Category cat, Category[] cats)
    {
	if (cat.getLevel()==0)
	    return new Category[]{cat};
	//even means a junior category
	boolean even = cat.getLevel()%2 == 0;
	ArrayList<Category> tempCats = new ArrayList<Category>();
	int no = 0;
	if(even)
	{
	    
	    for (int i = 0; i < cats.length; i++)
	    {
		if (cat.gender != cats[i].gender)
		    continue;
		if(cats[i].getLevel()%2 == 0 && cats[i].getLevel() <= cat.getLevel())
		    tempCats.add(cats[i]);
	    }

	}
	else
	{
	    for (int i = 0; i < cats.length; i++)
	    {
		if (cat.gender != cats[i].gender)
		    continue;
		if(cats[i].getLevel()%2 == 1 && cats[i].getLevel() <= cat.getLevel())
		    tempCats.add(cats[i]);
		else if(cats[i].getLevel() == 0) // if the number is uneven, the member is senior and Open has to be added
		    tempCats.add(cats[i]);
	    }
	}
	Category[] result = new Category[tempCats.size()];
	for (int i = 0; i < tempCats.size(); i++)
	{
	    result[i] = tempCats.get(i);
	}
	return result;
    }
    
    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public char getGender()
    {
        return gender;
    }
    public void setGender(char gender)
    {
        this.gender = gender;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public int getFrom_age()
    {
        return from_age;
    }
    public void setFrom_age(int age)
    {
        this.from_age = age;
    }
    public int getTo_age()
    {
        return to_age;
    }
    public void setTo_age(int to_age)
    {
        this.to_age = to_age;
    }
    public int getLevel()
    {
        return level;
    }
    public void setLevel(int level)
    {
        this.level = level;
    }
    
    public String toString()
    {
	return name;
    }
}

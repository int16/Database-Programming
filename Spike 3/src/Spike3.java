import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.hql.internal.ast.QuerySyntaxException;

public class Spike3 {

	private SessionFactory factory;

	public Spike3() {
		Logger.getLogger("org.hibernate").setLevel(Level.OFF);
		Configuration config = null;
		try {
			config = new Configuration().configure();
			StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(config.getProperties());
			factory = config.buildSessionFactory(builder.build());
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		Scanner scanner = new Scanner(System.in);
		boolean running = true;
		while (running) {
			String input = scanner.nextLine().toLowerCase();
			switch (input) {
			case "quit":
				running = false;
				break;
			}
			
		}
		scanner.close();
		list();
		insertCompetition("Test");
		updateCompetition("Test", "NewTest");
		deleteCompetition("NewTest");
		factory.close();
	}

	public void insertCompetition(String name) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		Competition comp = new Competition(name);
		session.save(comp);
		t.commit();
		session.close();
	}

	public void updateCompetition(String name, String newName) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		Competition comp = (Competition) session.get(Competition.class, findCompetitionID(name));
		comp.setCompName(newName);
		session.save(comp);
		t.commit();
		session.close();
	}

	public void deleteCompetition(String name) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		Competition comp = (Competition) session.get(Competition.class, findCompetitionID(name));
		session.delete(comp);
		t.commit();
		session.close();
	}

	public void list() {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		List<?> comps = null;
		try {
			comps = session.createQuery("FROM Competition").list();
		} catch (QuerySyntaxException e) {
			e.printStackTrace();
		}
		for (Object o : comps) {
			Competition comp = (Competition) o;
			System.out.println(comp.getId() + ": " + comp.getCompName());
		}
		t.commit();
		session.close();
	}

	public int findCompetitionID(String name) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		List<?> comps = null;
		try {
			comps = session.createQuery("FROM Competition where comp_name='" + name + "'").list();
		} catch (QuerySyntaxException e) {
			e.printStackTrace();
		}
		int result = -1;
		if (comps.size() > 0) {
			Competition comp = (Competition) comps.get(0);
			result = comp.getId();
		}
		t.commit();
		session.close();
		return result;
	}

	public static void main(String[] args) {
		new Spike3().run();
	}

}

import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.hql.internal.ast.QuerySyntaxException;

public class Spike3 {

	private SessionFactory factory;

	private static String menu = "Please specify one of the following options:\n" + "[list]   - Display foreign key mapping in DataSet\n" + "[insert] - Insert DataSet table\n" + "[update] - Update a row in the archer table\n" + "[delete] - Delete a row from the archer table\n" + "[quit]   - Terminate this program\n";

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
			System.out.println(menu);
			String input = scanner.nextLine().toLowerCase();
			switch (input) {
			case "list":
				listRelations();
				break;
			case "insert":
				System.out.print("Competition or Rule? > ");
				String in = scanner.nextLine().toLowerCase();
				if (in.equals("competition")) {
					System.out.print("Enter a new competition name to insert: ");
					insertCompetition(scanner.nextLine());
					System.out.println("Inserted.");
				} else if (in.equals("rule")) {
					System.out.print("Enter the competition ID to add the rule: ");
					int comp = 0;
					while (true) {
						try {
							comp = Integer.parseInt(scanner.nextLine());
							break;
						} catch (NumberFormatException e) {
							System.out.println("Please enter a number.");
							System.out.print("> ");
						}
					}
					System.out.print("Enter the round ID: ");
					int round = 0;
					while (true) {
						try {
							round = Integer.parseInt(scanner.nextLine());
							break;
						} catch (NumberFormatException e) {
							System.out.println("Please enter a number.");
							System.out.print("> ");
						}
					}
					System.out.print("Enter a rule (ALL, MAX or AVG): ");
					CalcRule rule = null;
					while (true) {
						try {
							rule = CalcRule.valueOf(scanner.nextLine().toUpperCase());
							break;
						} catch (IllegalArgumentException e) {
							System.out.println("Please enter either ALL, MAX or AVG.");
							System.out.print("> ");
						}
					}
					System.out.print("Enter the number of events included: ");
					int events = 0;
					while (true) {
						try {
							events = Integer.parseInt(scanner.nextLine());
							break;
						} catch (NumberFormatException e) {
							System.out.println("Please enter a number.");
							System.out.print("> ");
						}
					}
					if (insertRule(comp, round, rule, events)) System.out.println("Inserted.");
					else System.out.println("Nothing was inserted.");
				} else {
					System.out.println("Please enter either 'Competition' or 'Rule'.");
				}
				break;
			case "update":
				System.out.print("Competition or Rule? > ");
				in = scanner.nextLine().toLowerCase();
				if (in.equals("competition")) {
					System.out.print("Enter the competition name to update: ");
					String old = scanner.nextLine();
					System.out.print("Enter a new competition name: ");
					String newn = scanner.nextLine();
					if (updateCompetition(old, newn)) {
						System.out.println("Updated.");
					}
				} else if (in.equals("rule")) {
					System.out.print("Enter the competition ID of the rule to update: ");
					int cid = 0;
					while (true) {
						try {
							cid = Integer.parseInt(scanner.nextLine());
							break;
						} catch (NumberFormatException e) {
							System.out.println("Please enter a number.");
							System.out.print("> ");
						}
					}
					System.out.print("Enter the round ID of the rule to update: ");
					int rid = 0;
					while (true) {
						try {
							rid = Integer.parseInt(scanner.nextLine());
							break;
						} catch (NumberFormatException e) {
							System.out.println("Please enter a number.");
							System.out.print("> ");
						}
					}
					System.out.print("Enter a new rule (ALL, MAX or AVG): ");
					CalcRule rule = null;
					while (true) {
						try {
							rule = CalcRule.valueOf(scanner.nextLine().toUpperCase());
							break;
						} catch (IllegalArgumentException e) {
							System.out.println("Please enter either ALL, MAX or AVG.");
							System.out.print("> ");
						}
					}

					System.out.print("Enter the number of events included: ");
					int number = 0;
					while (true) {
						try {
							number = Integer.parseInt(scanner.nextLine());
							break;
						} catch (NumberFormatException e) {
							System.out.println("Please enter a number.");
							System.out.print("> ");
						}
					}
					if (updateRule(cid, rid, rule, number)) System.out.println("Updated.");
					else System.out.println("Nothing was updated.");
				} else {
					System.out.println("Please enter either 'Competition' or 'Rule'.");
				}
				break;
			case "delete":
				System.out.print("Competition or Rule? > ");
				in = scanner.nextLine().toLowerCase();
				if (in.equals("competition")) {
					System.out.print("Enter the competition name to delete: ");
					String name = scanner.nextLine();
					if (deleteCompetition(name)) System.out.println("Deleted.");
					else System.out.println("Nothing was deleted.");
				} else if (in.equals("rule")) {
					System.out.print("Enter the competition ID of the rule to delete: ");
					int cid = 0;
					while (true) {
						try {
							cid = Integer.parseInt(scanner.nextLine());
							break;
						} catch (NumberFormatException e) {
							System.out.println("Please enter a number.");
							System.out.print("> ");
						}
					}
					System.out.print("Enter the round ID of the rule to delete: ");
					int rid = 0;
					while (true) {
						try {
							rid = Integer.parseInt(scanner.nextLine());
							break;
						} catch (NumberFormatException e) {
							System.out.println("Please enter a number.");
							System.out.print("> ");
						}
					}
					deleteRule(cid, rid);
					System.out.println("Deleted.");
				} else {
					System.out.println("Please enter either 'Competition' or 'Rule'.");
				}
				break;
			case "quit":
				running = false;
				break;
			}
			System.out.println();
		}
		scanner.close();
		factory.close();
	}

	public void listRelations() {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		List<?> comps = session.createQuery("FROM Competition").list();
		for (Object o : comps) {
			Competition comp = (Competition) o;
			String result = comp.getCompName() + " (" + comp.getId() + "): ";
			Set<?> rules = comp.getRules();
			for (Object ro : rules) {
				Rule rule = (Rule) ro;
				result += rule.getRuleID().getRoundID() + " ";
			}
			System.out.println(result);
		}
		t.commit();
		session.close();
	}

	public void insertCompetition(String name) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		Competition comp = new Competition(name);
		session.save(comp);
		t.commit();
		session.close();
	}

	private boolean insertRule(int comp, int round, CalcRule calcrule, int events) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		Rule rule = new Rule(comp, round, calcrule, events);
		session.save(rule);
		try {
			t.commit();
		} catch (ConstraintViolationException e) {
			System.err.println("Rule already exists.");
			System.err.flush();
			return false;
		}
		session.close();
		return true;
	}

	public boolean updateCompetition(String name, String newName) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		Competition comp = (Competition) session.get(Competition.class, findCompetitionID(name));
		comp.setCompName(newName);
		session.save(comp);
		t.commit();
		session.close();
		return true;
	}

	public boolean updateRule(int ruleID, int roundID, CalcRule newRule, int newEventAmount) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		Rule rule = (Rule) session.get(Rule.class, new RuleID(ruleID, roundID));
		if (rule == null) {
			System.err.println("Rule doesn't exist.");
			System.err.flush();
			return false;
		}
		rule.setCalcRules(newRule);
		rule.setEventsIncluded(newEventAmount);
		session.save(rule);
		t.commit();
		session.close();
		return true;
	}

	public boolean deleteCompetition(String name) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		Competition comp = (Competition) session.get(Competition.class, findCompetitionID(name));
		session.delete(comp);
		try {
			t.commit();
		} catch (ConstraintViolationException e) {
			System.err.println("Cannot delete competition '" + name + "'; it has dependant rules.");
			System.err.flush();
			return false;
		}
		session.close();
		return true;
	}

	public void deleteRule(int compID, int roundID) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		Rule rule = (Rule) session.get(Rule.class, new RuleID(compID, roundID));
		session.delete(rule);
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

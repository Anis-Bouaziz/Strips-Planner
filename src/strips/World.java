package strips;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class World {
	Pattern pNames = Pattern.compile("(!?[A-Z][a-zA-Z_]*)\\(*([a-zA-Z0-9_,]*)\\)*", 0);
	Pattern pInit = Pattern.compile("init:", 0);
	Pattern pGoal = Pattern.compile("goal( state)?:", 0);
	Pattern pAction = Pattern.compile("actions:", 0);
	Pattern pPreconditions = Pattern.compile("PRE:", 0);
	Pattern pAdd = Pattern.compile("ADD:", 0);
	Pattern pDel = Pattern.compile("DEL:", 0);
	String parse = "init";
	public ArrayList<Condition> state = new ArrayList<>();
	public Condition goal;
	public Set<String> literals = new HashSet<String>();
	public ArrayList<Action> actions = new ArrayList<Action>();
	Action a;

	public boolean conditionSatisfied(Condition c) {
		return state.contains(c);
	}

	public void addLiteral(String l) {
		this.literals.add(l);
	}

	public void addAction(Action a) {
		this.actions.add(a);
	}

	public boolean goalSatisfied(ArrayList<Condition> curr_state) {

		for (Condition c : curr_state) {
			if (c.name.equals(this.goal.name) && c.params.size() == this.goal.params.size()) {
				if (c.params.isEmpty())
					return true;
				for (int i = 0; i < c.params.size(); i++) {
					if (c.params.get(i).equals(this.goal.params.get(i)) == false)
						return false;
				}
				return true;
			}
		}
		return false;
	}

	public boolean stateSatisfied(ArrayList<Condition> curr_state) {
		for (Condition c : curr_state) {

			if (!this.state.contains(c))
				return false;
		}
		return true;

	}

	public void printState() {
		for (Condition c : this.state) {
			System.out.println(c.toString());
		}
	}

	public void displayWorld() {

		System.out.println("Initial State: ");
		printState();
		System.out.println("Goal:");
		System.out.println(goal.toString());
		System.out.println("Actions :");
		for (Action a : actions) {
			System.out.println(a.toString());
		}
		System.out.println("Literals:");
		literals.forEach(s -> System.out.print(s + " "));
	}

	public void initLine(String line) throws Exception {
		Matcher m = pInit.matcher(line);
		if (m == null)
			throw new Exception("Init line not found , please write init :, then init conditions");
		Matcher m2 = pNames.matcher(line);
		if (m2 == null)
			throw new Exception("init line not found , please write init: then init condiditons ");
		m2.results().forEach(r -> {

			state.add(generateCondition(r.group()));
		});
		for (Condition c : state) {
			if (c.params != null)
				for (String p : c.params)
					addLiteral(p);
		}

		parse = "goal";
	}

	public void goalLine(String line) throws Exception {

		Matcher m = pGoal.matcher(line);
		if (m == null)
			throw new Exception("Goal line not found , please write goal :, then goal condition");

		if (m.lookingAt()) {
			ArrayList<String> tmp = new ArrayList<String>();
			String g = line.split(m.group())[1].strip();
			if (g.endsWith(")")) {
				String name = g.substring(0, g.indexOf("("));
				String[] lits = g.substring(g.indexOf("(") + 1, g.indexOf(")")).split(",");

				for (String l : lits) {

					tmp.add(l);
				}

				this.goal = new Condition(name, tmp);
			} else
				this.goal = new Condition(g, tmp);
		}

		parse = "actions";
	}

	public Condition generateCondition(String s) {
		s = s.strip();
		ArrayList<String> tmp = new ArrayList<String>();
		if (s.endsWith(")")) {

			String name = s.substring(0, s.indexOf("("));

			String[] lits = s.substring(s.indexOf("(") + 1, s.indexOf(")")).split(",");

			for (String l : lits) {

				if (a != null && a.params.contains(l.strip()) == false) {
					addLiteral(l);
				}
				tmp.add(l);
			}

			return new Condition(name, tmp);

		} else {
			return new Condition(s, tmp);
		}
	}

	public boolean is_valid(ArrayList<Condition> pre, ArrayList<Condition> curr_state) {

		List<Condition> res = new ArrayList<Condition>(pre);

		for (Condition s : curr_state) {
			for (Condition c : pre) {

				if (c.name.equals(s.name))

					res.remove(c);
			}

		}

		if (res.isEmpty())
			return true;
		return false;
	}

	public boolean is_valid2(ArrayList<Condition> g, Action a) {
		ArrayList<String> Gnames = (ArrayList<String>) g.stream().map(x -> x.name).collect(Collectors.toList());
		ArrayList<String> ADDnames = (ArrayList<String>) a.add.stream().map(x -> x.name).collect(Collectors.toList());
		boolean flag1 = false;
		for (String s : Gnames)
			if (ADDnames.contains(s))
				flag1 = true;
		return flag1;

	}

	public boolean possibleActions(ArrayList<Condition> pre, ArrayList<Condition> curr_state) {

		for (Condition c : pre) {
			if (!curr_state.contains(c)) {
				return false;
			}

		}
		return true;
	}

	public boolean possibleActions2(Action a, ArrayList<Condition> curr_state) {
		boolean flag1 = false;
		boolean flag2 = true;
		for (Condition c : a.add) {
			if (curr_state.contains(c)) {
				flag1 = true;
			}

		}
		for (Condition c : a.del) {
			if (curr_state.contains(c)) {
				flag2 = false;
			}

		}
		return flag1 && flag2;
	}

	public ArrayList<Condition> updateState(Action a, ArrayList<Condition> s) {

		ArrayList<Condition> res = new ArrayList<Condition>(s);
		for (Condition c : a.del)
			if (res.contains(c))
				res.remove(c);

		res.addAll(a.add);
		return res;
	}

	ArrayList<Condition> updateGoal(Action a, ArrayList<Condition> s) {

		Set<Condition> res = new HashSet<Condition>(s);
		for (Condition c : a.add)
			if (res.contains(c))
				res.remove(c);

		res.addAll(a.del);

		return (ArrayList<Condition>) res.stream().collect(Collectors.toList());

	
	}

	public World(String file) throws Exception {

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {

				if ((line.strip().equals("")) || (line.strip().startsWith("//")))
					continue;
				// init
				if (parse.equals("init"))
					initLine(line);
				else if (parse.equals("goal"))
					goalLine(line);
				else if (parse.equals("actions")) {
					Matcher m = pAction.matcher(line);
					if (m == null)
						throw new Exception("Actions line not found , please write action :, then actions");
					parse = "action_declaration";

				} else if (parse.equals("action_declaration")) {

					Matcher m = pNames.matcher(line);
					if (m == null)
						throw new Exception(
								"Action declaration line not found , please write action_declaration(params):");
					// System.out.println(m.results().findAny().get().group());
					String action = m.results().findAny().get().group().strip();
					// System.out.println(action);
					String name = action.substring(0, action.indexOf("("));
					if (action.endsWith(")")) {

						String[] lits = action.substring(action.indexOf("(") + 1, action.indexOf(")")).split(",");
						ArrayList<String> tmp = new ArrayList<String>();
						for (String l : lits) {

							tmp.add(l.strip());
						}

						a = new Action(name, tmp, new ArrayList<Condition>(), new ArrayList<Condition>(),
								new ArrayList<Condition>());
					} else
						a = new Action(name, null, new ArrayList<Condition>(), new ArrayList<Condition>(),
								new ArrayList<Condition>());
					parse = "PRE";
				} else if (parse.equals("PRE")) {
					Matcher m = pPreconditions.matcher(line);
					if (m == null)
						throw new Exception("Preconditions line not found , please write PRE: then preconditions ");
					line = line.replace("PRE:", "");
					Matcher m2 = pNames.matcher(line);
					if (m2 == null)
						throw new Exception("Preconditions line not found , please write PRE: then preconditions ");
					m2.results().forEach(r -> {
						// System.out.println(r.group());
						a.preconditions.add(generateCondition(r.group()));
					});
					parse = "ADD";
				} else if (parse.equals("ADD")) {
					line = line.replace("ADD:", "");
					Matcher m = pAdd.matcher(line);
					if (m == null)
						throw new Exception("ADD line not found , please write ADD: then ADD conditions ");
					Matcher m2 = pNames.matcher(line);
					if (m2 == null)
						throw new Exception("ADD line not found , please write ADD: then ADD conditions ");
					m2.results().forEach(r -> {
						a.add.add(generateCondition(r.group()));
					});
					parse = "DEL";
				} else if (parse.equals("DEL")) {
					line = line.replace("DEL:", "");
					Matcher m = pDel.matcher(line);
					if (m == null)
						throw new Exception("DEL line not found , please write DEL: then DEL conditions ");
					Matcher m2 = pNames.matcher(line);
					if (m2 == null)
						throw new Exception("DEL line not found , please write DEL: then DEL conditions ");
					m2.results().forEach(r -> {
						a.del.add(generateCondition(r.group()));
					});
					// Add this action to the world
					this.addAction(a);
					parse = "action_declaration";
				}

			}

		}
	}
}

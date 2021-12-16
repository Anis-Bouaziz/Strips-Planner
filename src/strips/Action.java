package strips;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Action {
	public String name;
	public ArrayList<String> params;
	public ArrayList<Condition> preconditions;
	public ArrayList<Condition> add;
	public ArrayList<Condition> del;

	public Action(String name, ArrayList<String> params, ArrayList<Condition> preconditions, ArrayList<Condition> add,
			ArrayList<Condition> del) {
		super();
		this.name = name;
		this.params = params;
		this.preconditions = preconditions;
		this.add = add;
		this.del = del;
	}

	ArrayList<Map<String, String>> generateAllPossibilities(ArrayList<Map<String, String>> results, String l,
			Set<String> literals) {
		ArrayList<Map<String, String>> f = new ArrayList<Map<String, String>>(results);
		results.forEach(m -> {

			for (String p : literals) {
				Map<String, String> tmp = new HashMap<String, String>(m);

				if (!tmp.keySet().contains(l) && !tmp.values().contains(p)) {
					f.add(tmp);
					tmp.put(l, p);
				}
			}

		});
		f.removeAll(results);
		return f;
	}

	public ArrayList<Action> generateActualAction(World w) {
		Action f = this;
		ArrayList<Map<String, String>> LM = new ArrayList<Map<String, String>>();

		ArrayList<Action> finalActions = new ArrayList<Action>();

		LM = generateMap(w.literals);

		for (Map<String, String> m : LM) {

			ArrayList<String> actualparams = new ArrayList<String>();
			for (String p : this.params) {
				if (m.get(p) != null && !actualparams.contains(m.get(p))) {
					actualparams.add(m.get(p));

				}

			}

			ArrayList<Condition> actualPre = new ArrayList<Condition>();
			ArrayList<Condition> actualAdd = new ArrayList<Condition>();
			ArrayList<Condition> actualDel = new ArrayList<Condition>();

			for (Condition p : this.preconditions) {

				actualPre.add(p.generateActualCondition(this, m));

			}
			for (Condition p : this.add) {
				actualAdd.add(p.generateActualCondition(this, m));

			}
			for (Condition p : this.del) {
				actualDel.add(p.generateActualCondition(this, m));

			}

			f = new Action(this.name, actualparams, actualPre, actualAdd, actualDel);

			finalActions.add(f);

		}

		return finalActions;

	}

	ArrayList<Map<String, String>> generateMap(Set<String> literals) {
		Set<String> vars = new HashSet<String>();
		ArrayList<Condition> all = new ArrayList<Condition>(this.preconditions);
		all.addAll(this.add);
		all.addAll(this.del);
		all.forEach(c -> c.params.forEach(p -> vars.add(p)));
		ArrayList<Map<String, String>> results = new ArrayList<Map<String, String>>();
		Map<String, String> result = new HashMap<String, String>();
		results.add(result);

		if (vars.isEmpty())
			return results;
		else {
			for (String l : vars) {

				results = generateAllPossibilities(results, l, literals);
			}
			results.remove(result);

			return results;
		}
	}

	@Override
	public String toString() {

		String pre = "";
		String adds = "";
		String dels = "";

		for (Condition s : preconditions) {
			pre = pre + s.toString() + ",";
		}
		for (Condition s : add) {
			adds = adds + s.toString() + ",";
		}
		for (Condition s : del) {
			dels = dels + s.toString() + ",";
		}
		return "Action [name=" + name + ",  params=" + params.toString() + " \n PRE=" + preconditions + "\nADD=" + adds
				+ "\nDEL=" + dels + "]";
	}

}

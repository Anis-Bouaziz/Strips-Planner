package strips;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

	ArrayList<Map<String, String>> generateLiteralsMap(ArrayList<Condition> curr_state, Set<String> literals) {
		ArrayList<Condition> all = new ArrayList<Condition>(this.preconditions);
		all.addAll(this.add);
		all.addAll(this.del);
		Set<String> left = new HashSet<String>();
		ArrayList<String> possibleLiterals = new ArrayList<String>(literals.stream().collect(Collectors.toList()));
		Map<String, String> result = new HashMap<String, String>();
		for (Condition c : curr_state) {
			for (String p : c.params) {
				for (Condition pre : this.preconditions) {
					if (pre.name.equals(c.name)) {
						
						for (String prep : pre.params) {
							left.add(prep);
							if (prep.equals(p)) {
								result.put(prep, p);
								//left.remove(prep);
								possibleLiterals.remove(p);
							}
							if (!result.keySet().contains(prep) && !result.values().contains(p)) {
								result.put(prep, p);
								
								possibleLiterals.remove(p);
							}
							
								if(result.keySet().contains(prep)) left.remove(prep);
						}
					}
				}
			}
		}

		ArrayList<Map<String, String>> results = new ArrayList<Map<String, String>>();
		results.add(result);

		if (left.isEmpty())
			return results;
		else {
			for (String l : left) {

				results = generateAllPossibilities(results, l, possibleLiterals);
			}
			results.remove(result);

			return results;
		}

	}

	ArrayList<Map<String, String>> generateAllPossibilities(ArrayList<Map<String, String>> results, String l,
			ArrayList<String> possibleLiterals) {
		ArrayList<Map<String, String>> f = new ArrayList<Map<String, String>>(results);
		results.forEach(m -> {

			for (String p : possibleLiterals) {
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

	public ArrayList<Action> generateActualAction(World w,ArrayList<Condition> curr_state, Set<String> literals) {
		Action f = this;
		ArrayList<Map<String, String>> LM=new ArrayList<Map<String, String>>();
		
		ArrayList<Action> finalActions = new ArrayList<Action>();
		ArrayList<Condition> state= new ArrayList<Condition>(curr_state);
		
		
	

	LM = generateLiteralsMap(state, literals);

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
ArrayList<Action> generate(ArrayList<Action> finalActions,Action a1,ArrayList<Condition>state, World w) {
	Action a=new Action(a1.name,a1.params,a1.preconditions,a1.add,a1.del);
	for (String s:w.literals) {
		for (String p :a.params) {
			
			p=s;
		for (Condition c :a.add) {
			for (String k:c.params) if(k.equals(p)) k=s;
}
		for (Condition c :a.preconditions) {
			for (String k:c.params) if(k.equals(p)) k=s;
}
		for (Condition c :a.del) {
			for (String k:c.params) if(k.equals(p)) k=s;
}
		
		if(w.possibleActions(a.preconditions, state)) {
			 
			 finalActions.add(a);
			
		 
	}
		}
		
		
	}
	return finalActions;
	
	
	
}


	@Override
	public String toString() {
		String p = "";
		String pre = "";
		String adds = "";
		String dels = "";
		if (params != null)
			for (String s : params) {
				p = p + s + ",";
			}

		for (Condition s : preconditions) {
			pre = pre + s.toString() + ",";
		}
		for (Condition s : add) {
			adds = adds + s.toString() + ",";
		}
		for (Condition s : del) {
			dels = dels + s.toString() + ",";
		}
		return "Action [name=" + name + ",  params=" + p + " \n preconditions=" + preconditions + " \nadd=" + adds
				+ "\n del=" + dels + "]";
	}

}

package strips;

import java.util.ArrayList;

import java.util.Map;
import java.util.Objects;

public class Condition {

	@Override
	public int hashCode() {
		return Objects.hash(name, params);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Condition other = (Condition) obj;
		if (other.name.equals(this.name) == false)
			return false;
		else {
			if (this.params != null) {
				if ((other.params.size() != this.params.size()))
					return false;
				else
					for (int i = 0; i < this.params.size(); i++)
						if (this.params.get(i).equals(other.params.get(i)) == false)
							return false;
			}
		}

		return true;
	}

	public String name;
	public ArrayList<String> params;
	public ArrayList<String> args;

	public Condition(String name, ArrayList<String> params) {
		super();
		this.name = name;
		this.params = params;

	}

	public Condition generateActualCondition(Action a, Map<String, String> m) {
		args = new ArrayList<String>();

		for (String p : this.params) {
			if (a.params.contains(p))
				args.add(m.get(p));
			else
				args.add(p);
		}
		return new Condition(this.name, args);

	}

	@Override
	public String toString() {
		String res = "Condition [NAME=" + name + ", LITERALS=" + params.toString();

		return res;
	}

}
